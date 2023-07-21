package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.ComitivaDto;
import com.meztlisoft.communitymanager.dto.filters.ComitivaFilters;
import com.meztlisoft.communitymanager.entity.ComitivaEntity;
import com.meztlisoft.communitymanager.entity.specification.ComitivaSpecification;
import com.meztlisoft.communitymanager.repository.ComitivaRepository;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    @Override
    public ComitivaDto getById(long id) {
        ComitivaEntity comitivaEntity = comitivaRepository.findById(id).orElse(new ComitivaEntity());
        return objectMapper.convertValue(comitivaEntity, ComitivaDto.class);
    }

    @Override
    public ActionStatusResponse update(long id, ComitivaDto comitivaDto, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        Claims claims = jwtService.decodeToken(token);
        try {
            ComitivaEntity comitiva = comitivaRepository.findById(id).orElseThrow();
            if (StringUtils.isNotBlank(comitivaDto.getNombre())) {
                comitiva.setNombre(comitivaDto.getNombre());
            }
            comitiva.setUsuarioEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            comitiva.setFechaActualizacion(LocalDateTime.now());
            ComitivaEntity saved = comitivaRepository.save(comitiva);
            actionStatusResponse.setId(id);
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
    public ActionStatusResponse delete(long id, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        Claims claims = jwtService.decodeToken(token);
        try {
            ComitivaEntity comitiva = comitivaRepository.findById(id).orElseThrow();
            comitiva.setActivo(false);
            comitiva.setUsuarioEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            comitiva.setFechaActualizacion(LocalDateTime.now());
            comitivaRepository.save(comitiva);
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
