package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.CooperationDto;
import com.meztlisoft.communitymanager.dto.filters.CooperationFilters;
import com.meztlisoft.communitymanager.entity.*;
import com.meztlisoft.communitymanager.entity.specification.CooperationSpecification;
import com.meztlisoft.communitymanager.repository.CooperationRepository;
import com.meztlisoft.communitymanager.repository.RetinueRepository;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CooperationServiceImpl implements CooperationService {

    private final CooperationRepository cooperationRepository;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final RetinueRepository retinueRepository;
    private final PaymentService paymentService;

    @Override
    public CooperationDto create(CooperationDto cooperationDto, String token) {
        Claims claims = jwtService.decodeToken(token);
        CooperationEntity cooperationEntity = objectMapper.convertValue(cooperationDto, CooperationEntity.class);
        cooperationEntity.setRetinue(retinueRepository.findByIdAndActive(cooperationDto.getRetinueId(), true).orElseThrow());
        cooperationEntity.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
        cooperationEntity.setCreationDate(LocalDateTime.now());
        cooperationEntity.setNotNativeCooperation(cooperationDto.getNotNativeCooperation());
        cooperationEntity.setBaseCooperation(cooperationDto.getBaseCooperation());
        cooperationEntity.setActive(true);
        CooperationEntity saved = cooperationRepository.save(cooperationEntity);
        CooperationDto savedDto = objectMapper.convertValue(saved, CooperationDto.class);
        savedDto.setRetinueId(saved.getRetinue().getId());
        paymentService.createInitialPaymentsAssociated(cooperationEntity);
        return savedDto;
    }

    @Override
    public Page<CooperationDto> getAll(CooperationFilters cooperationFilters) {
        Pageable paging = PageRequest.of(cooperationFilters.getPage(), cooperationFilters.getSize());
        Specification<CooperationEntity> specification = CooperationSpecification.getCooperationFilters(cooperationFilters);
        Page<CooperationEntity> cooperationEntities = cooperationRepository.findAll(specification, paging);

        List<CooperationDto> dtos = new ArrayList<>();
        cooperationEntities.stream().forEach(cooperation -> dtos.add(objectMapper.convertValue(cooperation, CooperationDto.class)));

        return new PageImpl<>(dtos, cooperationEntities.getPageable(), cooperationEntities.getTotalElements());
    }

    @Override
    public CooperationDto getById(long id) {
        CooperationEntity cooperation = cooperationRepository.findByIdAndActive(id, true).orElse(new CooperationEntity());
        CooperationDto cooperationDto = objectMapper.convertValue(cooperation, CooperationDto.class);
        cooperationDto.setRetinueId(cooperation.getRetinue().getId());
        return cooperationDto;
    }

    @Override
    @Deprecated
    public ActionStatusResponse update(long id, CooperationDto cooperationDto, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        Claims claims = jwtService.decodeToken(token);
        try {
            CooperationEntity cooperation = cooperationRepository.findByIdAndActive(id, true).orElseThrow();

            if (Objects.nonNull(cooperationDto.getConcept())) {
                cooperation.setConcept(cooperationDto.getConcept());
            }

            if (Objects.nonNull(cooperationDto.getBaseCooperation())) {
                cooperation.setBaseCooperation(cooperationDto.getBaseCooperation());
            }

            if (Objects.nonNull(cooperationDto.getLimitDate())) {
                cooperation.setLimitDate(cooperationDto.getLimitDate());
            }

            if (Objects.nonNull(cooperationDto.getStartDate())) {
                cooperation.setStartDate(cooperationDto.getStartDate());
            }

            if (Objects.nonNull(cooperationDto.getRetinueId())) {
                cooperation.setRetinue(retinueRepository.findByIdAndActive(cooperationDto.getRetinueId(), true).orElseThrow());
            }

            cooperation.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            cooperation.setUpdateDate(LocalDateTime.now());

            CooperationEntity saved = cooperationRepository.save(cooperation);

            actionStatusResponse.setId(saved.getId());
            actionStatusResponse.setStatus(HttpStatus.OK);
            actionStatusResponse.setDescription("Actualizado correctamente");
        } catch (Exception ex) {
            Map<HttpStatus, String> errors = new HashMap<>();
            errors.put(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            actionStatusResponse.setErrors(errors);
        }
        return actionStatusResponse;
    }

    @Override
    @Deprecated
    public ActionStatusResponse delete(long id, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        Claims claims = jwtService.decodeToken(token);
        try {
            CooperationEntity cooperation = cooperationRepository.findByIdAndActive(id, true).orElseThrow();
            cooperation.setActive(false);
            cooperation.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            cooperation.setUpdateDate(LocalDateTime.now());
            cooperationRepository.save(cooperation);
            actionStatusResponse.setId(id);
            actionStatusResponse.setStatus(HttpStatus.OK);
            actionStatusResponse.setDescription("Borrado correctamente");
        } catch (Exception ex) {
            Map<HttpStatus, String> errors = new HashMap<>();
            errors.put(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            actionStatusResponse.setErrors(errors);
        }
        return actionStatusResponse;
    }
}
