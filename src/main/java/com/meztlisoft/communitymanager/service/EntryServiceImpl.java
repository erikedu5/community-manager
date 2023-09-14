package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.EntryDto;
import com.meztlisoft.communitymanager.dto.SummaryDto;
import com.meztlisoft.communitymanager.dto.filters.EntryFilters;
import com.meztlisoft.communitymanager.entity.CooperationEntity;
import com.meztlisoft.communitymanager.entity.EntryEntity;
import com.meztlisoft.communitymanager.entity.specification.EntrySpecification;
import com.meztlisoft.communitymanager.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;
    private final EntryCustomRepository entryCustomRepository;
    private final AdministratorRepository administratorRepository;
    private final CooperationRepository cooperationRepository;
    private final PaymentRepository paymentRepository;
    private final JwtService jwtService;
    private final RetinueRepository retinueRepository;
    private final ObjectMapper objectMapper;

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
