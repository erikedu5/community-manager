package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.CiudadanoDto;
import com.meztlisoft.communitymanager.dto.filters.CiudadanoFilters;
import com.meztlisoft.communitymanager.entity.CiudadanoEntity;
import com.meztlisoft.communitymanager.entity.specification.CiudadanoSpecification;
import com.meztlisoft.communitymanager.repository.CiudadanoRepository;
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
}
