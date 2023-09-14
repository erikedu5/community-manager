package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.BillDto;
import com.meztlisoft.communitymanager.dto.SummaryDto;
import com.meztlisoft.communitymanager.dto.filters.BillFilters;
import com.meztlisoft.communitymanager.entity.BillEntity;
import com.meztlisoft.communitymanager.entity.specification.BillSpecification;
import com.meztlisoft.communitymanager.repository.AdministratorRepository;
import com.meztlisoft.communitymanager.repository.BillCustomRepository;
import com.meztlisoft.communitymanager.repository.BillRepository;
import com.meztlisoft.communitymanager.repository.RetinueRepository;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final BillCustomRepository billCustomRepository;
    private final ObjectMapper objectMapper;
    private final RetinueRepository retinueRepository;
    private final JwtService jwtService;
    private final AdministratorRepository administratorRepository;
    private final Path root = Paths.get("uploads");


    @PostConstruct
    public void init() {
        try {
            if(!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }
    @Override
    public Page<BillDto> getAll(BillFilters billFilters,  Long retinueId) {
        Pageable page = PageRequest.of(billFilters.getPage(), billFilters.getSize());
        Specification<BillEntity> specification = BillSpecification.getFilteredBill(billFilters, retinueId);
        Page<BillEntity> entities = billRepository.findAll(specification, page);
        List<BillDto> billDtoList = new ArrayList<>();
        entities.forEach(entity -> billDtoList.add(objectMapper.convertValue(entity, BillDto.class)));
        return new PageImpl<>(billDtoList, entities.getPageable(), entities.getTotalElements());

    }

    @Override
    public Resource downloadEvidence(Long id) {
        BillEntity bill = billRepository.findById(id).orElseThrow();
        if (StringUtils.isBlank(bill.getEvidencePack())) {
            throw new HttpServerErrorException(HttpStatus.PRECONDITION_FAILED, "No existe evidencia para esta cuenta");
        }
        try {
            Path file = root.resolve(bill.getEvidencePack());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public ActionStatusResponse uploadFile(MultipartFile file, Long id) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        try {
            BillEntity bill = billRepository.findById(id).orElseThrow();
            if (StringUtils.isBlank(file.getOriginalFilename())) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "El nombre del archivo está en blanco");
            }
            if (!Files.exists(root.resolve(bill.getRetinue().getName()))) {
                Files.createDirectories(Paths.get(root + "/" + bill.getRetinue().getName()));
            }
            String[] extension = file.getOriginalFilename().split("\\.");
            String evidenceName = bill.getRetinue().getName() + "/evidence-" + bill.getId() + "." + extension[extension.length - 1];
            Files.copy(file.getInputStream(), this.root.resolve(evidenceName));
            bill.setEvidencePack(evidenceName);
            billRepository.save(bill);
            actionStatusResponse.setId(bill.getId());
            actionStatusResponse.setStatus(HttpStatus.ACCEPTED);
            actionStatusResponse.setDescription("Evicencia subida correctamente");
        } catch (Exception e) {
            Map<HttpStatus, String> errors = new HashMap<>();
            if (e instanceof FileAlreadyExistsException) {
                errors.put(HttpStatus.BAD_REQUEST, "A file of that name already exists.");
            } else {
                errors.put(HttpStatus.INTERNAL_SERVER_ERROR ,e.getMessage());
            }
            actionStatusResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            actionStatusResponse.setDescription("Fallo al subir evidencia");
            actionStatusResponse.setErrors(errors);
        }
        return actionStatusResponse;
    }

    @Override
    public BillDto createBill(Long retinueId, BillDto billDto, String token) {
        BillEntity bill = new BillEntity();
        bill.setConcept(billDto.getConcept());
        bill.setCost(billDto.getCost());
        bill.setDate(billDto.getDate());
        bill.setCreationDate(LocalDateTime.now());
        bill.setRetinue(retinueRepository.findByIdAndActive(retinueId, true).orElseThrow());
        try {
            if (!Files.exists(root.resolve(bill.getRetinue().getName()))) {
                Files.createDirectories(Paths.get(bill.getRetinue().getName()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Claims claims = jwtService.decodeToken(token);
        bill.setAdministrator(administratorRepository.findByCitizenIdAndActive(Long.parseLong(claims.get("ciudadano_id").toString()), true));
        BillEntity saved = billRepository.save(bill);
        return objectMapper.convertValue(saved, BillDto.class);
    }

    @Override
    public SummaryDto getSummary(BillFilters billFilters, Long retinueId) {
        SummaryDto summaryDto = new SummaryDto();
        Specification<BillEntity> specification = BillSpecification.getFilteredBill(billFilters, retinueId);
        summaryDto.setSummary(billCustomRepository.getSummary(specification));
        summaryDto.setId(retinueId);
        return summaryDto;
    }

}
