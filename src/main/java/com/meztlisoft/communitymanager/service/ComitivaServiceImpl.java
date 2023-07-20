package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.ComitivaDto;
import com.meztlisoft.communitymanager.dto.filters.ComitivaFilters;
import com.meztlisoft.communitymanager.entity.ComitivaEntity;
import com.meztlisoft.communitymanager.entity.specification.ComitivaSpecification;
import com.meztlisoft.communitymanager.repository.ComitivaRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComitivaServiceImpl implements ComitivaService {

    private final ComitivaRepository comitivaRepository;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    public ComitivaDto create(ComitivaDto comitivaDto, String token) {

        Claims claims = jwtService.decodeToken(token);
        ComitivaEntity comitivaEntity = objectMapper.convertValue(comitivaDto, ComitivaEntity.class);
        comitivaEntity.setActivo(true);
        comitivaEntity.setFechaCreacion(LocalDateTime.now());
        comitivaEntity.setUsuarioEditor(Long.parseLong(claims.get("ciudadano_id").toString()));

        return objectMapper.convertValue(comitivaRepository.save(comitivaEntity), ComitivaDto.class);
    }

    @Override
    public Page<ComitivaDto> getAll(ComitivaFilters comitivaFilters) {
        Pageable paging = PageRequest.of(comitivaFilters.getPage(), comitivaFilters.getSize());
        Specification<ComitivaEntity> specification = ComitivaSpecification.getFilteredComitiva(comitivaFilters);

        Page<ComitivaEntity> comitivas = comitivaRepository.findAll(specification, paging);

        List<ComitivaDto> dtos = new ArrayList<>();
        for (ComitivaEntity comitiva : comitivas) {
            dtos.add(objectMapper.convertValue(comitiva, ComitivaDto.class));
        }
        return new PageImpl<>(dtos, comitivas.getPageable(), comitivas.getTotalElements());

    }
}
