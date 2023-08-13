package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.EntryDto;
import com.meztlisoft.communitymanager.dto.filters.EntryFilters;
import com.meztlisoft.communitymanager.entity.CooperationEntity;
import com.meztlisoft.communitymanager.entity.EntryEntity;
import com.meztlisoft.communitymanager.entity.PaymentEntity;
import com.meztlisoft.communitymanager.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;
    private final AdministratorRepository administratorRepository;
    private final CooperationRepository cooperationRepository;
    private final PaymentRepository paymentRepository;
    private final JwtService jwtService;
    private final RetinueRepository retinueRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Page<EntryDto> findAll(EntryFilters entryFilters) {
        Pageable page = PageRequest.of(entryFilters.getPage(), entryFilters.getSize());
        Page<EntryEntity> entryEntities = entryRepository.findAll(page);
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
        Long citizenId = Long.parseLong(claims.get("ciudadano_id").toString());

        entry.setAdministrator(administratorRepository.findByCitizenIdAndActive(citizenId, true));
        entry.setRetinue(retinueRepository.findByIdAndActive(retinueId, true).orElseThrow());
        entry.setCreationDate(LocalDateTime.now());
        EntryEntity entrySaved =entryRepository.save(entry);
        return objectMapper.convertValue(entrySaved, EntryDto.class);
    }

    private EntryEntity creationEntryFromForm(EntryDto entryDto) {
        EntryEntity entry = new EntryEntity();
        entry.setConcept(entryDto.getConcept());
        entry.setCost(entryDto.getCost());
        return entry;
    }

    private EntryEntity createEntryFromCooperation(Long cooperationId) {
        CooperationEntity cooperation = cooperationRepository.findById(cooperationId).orElseThrow();
        AtomicReference<Long> cost = new AtomicReference<>(0L);
        List<PaymentEntity> payments = paymentRepository.findAllByCooperationId(cooperationId);
        payments.forEach(paymentEntity -> cost.updateAndGet(v -> v + paymentEntity.getPayment()));
        EntryEntity entry = new EntryEntity();
        entry.setConcept("Cooperacion ".concat(cooperation.getConcept()));
        entry.setCost(cost.toString());
        return entry;
    }
}
