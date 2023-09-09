package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.CooperationDto;
import com.meztlisoft.communitymanager.dto.filters.CooperationFilters;
import com.meztlisoft.communitymanager.entity.CooperationEntity;
import com.meztlisoft.communitymanager.entity.specification.CooperationSpecification;
import com.meztlisoft.communitymanager.repository.CooperationRepository;
import com.meztlisoft.communitymanager.repository.RetinueRepository;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public CooperationDto create(CooperationDto cooperationDto, String token, Long retinueId) {
        Claims claims = jwtService.decodeToken(token);
        CooperationEntity cooperationEntity = objectMapper.convertValue(cooperationDto, CooperationEntity.class);
        cooperationEntity.setRetinue(retinueRepository.findByIdAndActive(retinueId, true).orElseThrow());
        cooperationEntity.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
        cooperationEntity.setCreationDate(LocalDateTime.now());
        cooperationEntity.setByUnity(cooperationEntity.isByUnity());
        cooperationEntity.setNotNativeCooperation(cooperationDto.getNotNativeCooperation());
        cooperationEntity.setBaseCooperation(cooperationDto.getBaseCooperation());
        cooperationEntity.setActive(true);
        CooperationEntity saved = cooperationRepository.save(cooperationEntity);
        CooperationDto savedDto = objectMapper.convertValue(saved, CooperationDto.class);
        paymentService.createInitialPaymentsAssociated(cooperationEntity);
        return savedDto;
    }

    @Override
    public Page<CooperationDto> getAll(CooperationFilters cooperationFilters, Long retinueId) {
        Pageable paging = PageRequest.of(cooperationFilters.getPage(), cooperationFilters.getSize());
        Specification<CooperationEntity> specification = CooperationSpecification.getCooperationFilters(cooperationFilters, retinueId);
        Page<CooperationEntity> cooperationEntities = cooperationRepository.findAll(specification, paging);

        List<CooperationDto> dtos = new ArrayList<>();
        cooperationEntities.stream().forEach(cooperation -> {
            CooperationDto dto = objectMapper.convertValue(cooperation, CooperationDto.class);
            dto.setRetinueName(cooperation.getRetinue().getName());
            dtos.add(dto);
        });

        return new PageImpl<>(dtos, cooperationEntities.getPageable(), cooperationEntities.getTotalElements());
    }

    @Override
    public CooperationDto getById(long id) {
        CooperationEntity cooperation = cooperationRepository.findByIdAndActive(id, true).orElse(new CooperationEntity());
        CooperationDto cooperationDto = objectMapper.convertValue(cooperation, CooperationDto.class);
        cooperationDto.setRetinueName(cooperation.getRetinue().getName());
        return cooperationDto;
    }

    @Override
    public List<CooperationDto> getCatalog(Long retinueId) {
        List<CooperationEntity> cooperations = cooperationRepository.findByRetinueId(retinueId);
        List<CooperationDto> dtos = new ArrayList<>();
        cooperations.forEach(cooperation -> dtos.add(objectMapper.convertValue(cooperation, CooperationDto.class)));
        return dtos;
    }
}
