package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.CiudadanoDto;
import com.meztlisoft.communitymanager.dto.filters.CiudadanoFilters;
import com.meztlisoft.communitymanager.entity.CiudadanoEntity;
import com.meztlisoft.communitymanager.entity.specification.CiudadanoSpecification;
import com.meztlisoft.communitymanager.repository.CiudadanoRepository;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
public class CiudadanoServiceImpl implements  CiudadanoService {

    private final CiudadanoRepository ciudadanoRepository;

    private final ObjectMapper objectMapper;

    private final JwtService jwtService;

    @Override
    public CiudadanoDto create(CiudadanoDto ciudadano, String token) {
        Claims claims = jwtService.decodeToken(token);
        CiudadanoEntity ciudadanoEntity = objectMapper.convertValue(ciudadano, CiudadanoEntity.class);
        ciudadanoEntity.setActivo(true);
        ciudadanoEntity.setFechaCreacion(LocalDateTime.now());
        ciudadanoEntity.setUsuarioEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
        CiudadanoEntity ciudadanoEntitySaved = ciudadanoRepository.save(ciudadanoEntity);
        return objectMapper.convertValue(ciudadanoEntitySaved, CiudadanoDto.class);
    }

    @Override
    public Page<CiudadanoDto> getAll(CiudadanoFilters ciudadanoFilters) {
        Pageable paging = PageRequest.of(ciudadanoFilters.getPage(), ciudadanoFilters.getSize());
        Specification<CiudadanoEntity> specification = CiudadanoSpecification.getFilteredCiudadano(ciudadanoFilters);
        Page<CiudadanoEntity> ciudadanos = ciudadanoRepository.findAll(specification, paging);
        List<CiudadanoDto> dtos = new ArrayList<>();
        for (CiudadanoEntity ciudadano : ciudadanos) {
            dtos.add(objectMapper.convertValue(ciudadano, CiudadanoDto.class));
        }
        return new PageImpl<>(dtos, ciudadanos.getPageable(), ciudadanos.getTotalElements());
    }

    @Override
    public CiudadanoDto getById(long id) {
        CiudadanoEntity ciudadanoEntity = ciudadanoRepository.findById(id).orElse(new CiudadanoEntity());
        return objectMapper.convertValue(ciudadanoEntity, CiudadanoDto.class);
    }

    @Override
    public ActionStatusResponse update(long id, CiudadanoDto ciudadanoDto, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        Claims claims = jwtService.decodeToken(token);
        try {
            CiudadanoEntity ciudadano = ciudadanoRepository.findById(id).orElseThrow();

            if (StringUtils.isNotBlank(ciudadanoDto.getNombre())) {
                ciudadano.setNombre(ciudadanoDto.getNombre());
            }

            if (StringUtils.isNotBlank(ciudadanoDto.getCurp())) {
                ciudadano.setCurp(ciudadanoDto.getCurp());
            }

            if (StringUtils.isNotBlank(ciudadanoDto.getDireccion())) {
                ciudadano.setDireccion(ciudadanoDto.getDireccion());
            }

            if (StringUtils.isNotBlank(ciudadanoDto.getDescripcion())) {
                ciudadano.setDescripcion(ciudadanoDto.getDescripcion());
            }

            if (Objects.nonNull(ciudadanoDto.getFechaNacimiento())) {
                ciudadano.setFechaNacimiento(ciudadanoDto.getFechaNacimiento());
            }

            ciudadano.setUsuarioEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            ciudadano.setFechaActualizacion(LocalDateTime.now());
            CiudadanoEntity saved = ciudadanoRepository.save(ciudadano);

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
            CiudadanoEntity ciudadano = ciudadanoRepository.findById(id).orElseThrow();
            ciudadano.setActivo(false);
            ciudadano.setUsuarioEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            ciudadano.setFechaActualizacion(LocalDateTime.now());
            ciudadanoRepository.save(ciudadano);
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
