package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.*;
import com.meztlisoft.communitymanager.dto.filters.BillFilters;
import com.meztlisoft.communitymanager.dto.filters.DatesForReportFilter;
import com.meztlisoft.communitymanager.dto.filters.EntryFilters;
import com.meztlisoft.communitymanager.entity.*;
import com.meztlisoft.communitymanager.entity.specification.BillSpecification;
import com.meztlisoft.communitymanager.entity.specification.EntrySpecification;
import com.meztlisoft.communitymanager.repository.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;
    private final EntryCustomRepository entryCustomRepository;
    private final BillRepository billRepository;
    private final BillCustomRepository billCustomRepository;
    private final AdministratorRepository administratorRepository;
    private final CooperationRepository cooperationRepository;
    private final PaymentRepository paymentRepository;
    private final JwtService jwtService;
    private final RetinueRepository retinueRepository;
    private final ObjectMapper objectMapper;

    @Value("${files.path.community}")
    private String pathCommunity;

    @Override
    public Page<EntryDto> findAll(EntryFilters entryFilters, Long retinueId) {
        Pageable page = PageRequest.of(entryFilters.getPage(), entryFilters.getSize());
        Specification<EntryEntity> specification = EntrySpecification.getFilteredEntry(entryFilters, retinueId);
        Page<EntryEntity> entryEntities = entryRepository.findAll(specification, page);
        List<EntryDto> entryDtoList = new ArrayList<>();
        entryEntities.forEach(entry -> entryDtoList.add(objectMapper.convertValue(entry, EntryDto.class)));
        return new PageImpl<>(entryDtoList, entryEntities.getPageable(), entryEntities.getTotalElements());
    }

    @Override
    public EntryDto save(EntryDto entryDto, String token, Long retinueId) {
        EntryEntity entry = null;
        if (entryDto.getIsCooperation() && Objects.nonNull(entryDto.getCooperationId())) {
            entry = this.createEntryFromCooperation(entryDto.getCooperationId());
        } else if (!entryDto.getIsCooperation()) {
            entry = this.creationEntryFromForm(entryDto);
        }
        if (Objects.isNull(entry)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't create entry");
        }
        Claims claims = jwtService.decodeToken(token);
        long citizenId = Long.parseLong(claims.get("ciudadano_id").toString());
        entry.setAdministrator(administratorRepository.findByCitizenIdAndActive(citizenId, true));
        entry.setRetinue(retinueRepository.findByIdAndActive(retinueId, true).orElseThrow());
        entry.setDate(entryDto.getDate());
        if (Objects.nonNull(entry.getCooperation())) {
            entry.setConcept(entry.getConcept() + " de " + entry.getRetinue().getName());
        }
        entry.setCreationDate(LocalDateTime.now());
        EntryEntity entrySaved =entryRepository.save(entry);
        return objectMapper.convertValue(entrySaved, EntryDto.class);
    }

    @Override
    public SummaryDto getSummary(EntryFilters entryFilters, Long retinueId) {
        Specification<EntryEntity> specification = EntrySpecification.getFilteredEntry(entryFilters, retinueId);
        Long summary = entryCustomRepository.getSummary(specification);
        SummaryDto summaryDto = new SummaryDto();
        summaryDto.setSummary(summary);
        summaryDto.setId(retinueId);
        return summaryDto;
    }

    @Override
    public File getReport(DatesForReportFilter datesForReportFilter, Long retinueId) {
        try {
            RetinueEntity retinue = retinueRepository.findByIdAndActive(retinueId, true).orElseThrow();

            EntryFilters entryFilters = new EntryFilters();
            entryFilters.setDateFrom(datesForReportFilter.getDateFrom());
            entryFilters.setDateTo(datesForReportFilter.getDateTo());
            Specification<EntryEntity> specification = EntrySpecification.getFilteredEntry(entryFilters, retinueId);
            List<EntryEntity> entryLst = entryRepository.findAll(specification);
            Long summaryEntry = entryCustomRepository.getSummary(specification);

            BillFilters billFilters = new BillFilters();
            billFilters.setDateFrom(datesForReportFilter.getDateFrom());
            billFilters.setDateTo(datesForReportFilter.getDateTo());
            Specification<BillEntity> billSpecification = BillSpecification.getFilteredBill(billFilters, retinueId);
            List<BillEntity> billLst = billRepository.findAll(billSpecification);
            Long summaryBill = billCustomRepository.getSummary(billSpecification);

            Map<String, Object> empParams = new HashMap<>();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            empParams.put("date", LocalDate.now().format(formatters));
            empParams.put("retinueName", retinue.getName());

            empParams.put("totalEntries", summaryEntry);
            empParams.put("totalBills", summaryBill);
            empParams.put("totalFinal", summaryEntry - summaryBill);

            JRBeanCollectionDataSource entries = new JRBeanCollectionDataSource(this.parseEntryDto(entryLst));
            JRBeanCollectionDataSource bills = new JRBeanCollectionDataSource(this.parseBillsDto(billLst));

            empParams.put("entries", entries);
            empParams.put("bills", bills);

            JasperPrint empReport = JasperFillManager.fillReport(
                    JasperCompileManager.compileReport(
                            ResourceUtils.getFile(pathCommunity + "corteCaja.jrxml").getAbsolutePath()),
                    empParams,
                    new JREmptyDataSource());

            File receipt = new File("corteCaja.pdf");
            FileUtils.writeByteArrayToFile(receipt, JasperExportManager.exportReportToPdf(empReport));
            return receipt;
        } catch (JRException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<BillReportDto> parseBillsDto(List<BillEntity> billEntities) {
        List<BillReportDto> dtos = new ArrayList<>();
        billEntities.forEach(billEntity -> {
            BillReportDto dto = objectMapper.convertValue(billEntity, BillReportDto.class);
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dto.setDate(billEntity.getDate().format(formatters));
            dtos.add(dto);
        });
        return dtos;
    }

    private List<EntryReportDto> parseEntryDto(List<EntryEntity> entryLst) {
        List<EntryReportDto> dtos = new ArrayList<>();
        entryLst.forEach(entry -> {
            EntryReportDto reportDto = new EntryReportDto();
            reportDto.setId(entry.getId());
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            reportDto.setDate(entry.getDate().format(formatters));
            reportDto.setCost(entry.getCost());
            reportDto.setConcept(entry.getConcept());
            reportDto.setIsCooperation(Objects.nonNull(entry.getCooperation()) ? "Si": "No");
            dtos.add(reportDto);
        });
        return dtos;
    }

    private EntryEntity creationEntryFromForm(EntryDto entryDto) {
        EntryEntity entry = new EntryEntity();
        entry.setConcept(entryDto.getConcept());
        entry.setCost(entryDto.getCost());
        return entry;
    }

    private EntryEntity createEntryFromCooperation(Long cooperationId) {
        Optional<EntryEntity> entryExistent = entryRepository.existByCooperationId(cooperationId);
        if (entryExistent.isPresent()) {
            throw new HttpServerErrorException(HttpStatus.PRECONDITION_FAILED, "No se puede generar entrada de una cooperación ya creada");
        }
        CooperationEntity cooperation = cooperationRepository.findById(cooperationId).orElseThrow();
        if (cooperation.getLimitDate().isAfter(LocalDate.now())) {
            throw new HttpClientErrorException(HttpStatus.PRECONDITION_REQUIRED, "No se puede generar entrada de una cooperación aun en curso");
        }
        Long payments = paymentRepository.getSummaryByCooperationId(cooperationId);
        EntryEntity entry = new EntryEntity();
        entry.setConcept("Cooperacion ".concat(cooperation.getConcept()));
        entry.setCost(payments);
        entry.setCooperation(cooperation);
        return entry;
    }
}
