package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.RetinueDto;
import com.meztlisoft.communitymanager.dto.filters.RetinueFilters;
import com.meztlisoft.communitymanager.entity.RetinueEntity;
import com.meztlisoft.communitymanager.entity.specification.RetinueSpecification;
import com.meztlisoft.communitymanager.repository.RetinueRepository;
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
public class RetinueServiceImpl implements RetinueService {

    private final RetinueRepository retinueRepository;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    public RetinueDto create(RetinueDto retinueDto, String token) {

        Claims claims = jwtService.decodeToken(token);
        RetinueEntity retinueEntity = objectMapper.convertValue(retinueDto, RetinueEntity.class);
        retinueEntity.setActive(true);
        retinueEntity.setCreationDate(LocalDateTime.now());
        retinueEntity.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));

        return objectMapper.convertValue(retinueRepository.save(retinueEntity), RetinueDto.class);
    }

    @Override
    public Page<RetinueDto> getAll(RetinueFilters retinueFilters) {
        Pageable paging = PageRequest.of(retinueFilters.getPage(), retinueFilters.getSize());
        Specification<RetinueEntity> specification = RetinueSpecification.getFilteredRetinue(retinueFilters);

        Page<RetinueEntity> retinues = retinueRepository.findAll(specification, paging);

        List<RetinueDto> dtos = new ArrayList<>();
        for (RetinueEntity retinue : retinues) {
            dtos.add(objectMapper.convertValue(retinue, RetinueDto.class));
        }
        return new PageImpl<>(dtos, retinues.getPageable(), retinues.getTotalElements());

    }

    @Override
    public RetinueDto getById(long id) {
        RetinueEntity retinueEntity = retinueRepository.findByIdAndActive(id, true).orElse(new RetinueEntity());
        return objectMapper.convertValue(retinueEntity, RetinueDto.class);
    }

    @Override
    public ActionStatusResponse update(long id, RetinueDto retinueDto, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        Claims claims = jwtService.decodeToken(token);
        try {
            RetinueEntity retinue = retinueRepository.findByIdAndActive(id, true).orElseThrow();
            if (StringUtils.isNotBlank(retinueDto.getName())) {
                retinue.setName(retinueDto.getName());
            }
            retinue.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            retinue.setUpdateDate(LocalDateTime.now());
            RetinueEntity saved = retinueRepository.save(retinue);
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
    public ActionStatusResponse delete(long id, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        Claims claims = jwtService.decodeToken(token);
        try {
            RetinueEntity retinue = retinueRepository.findByIdAndActive(id, true).orElseThrow();
            retinue.setActive(false);
            retinue.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            retinue.setUpdateDate(LocalDateTime.now());
            retinueRepository.save(retinue);
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
