package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.CitizenDto;
import com.meztlisoft.communitymanager.dto.filters.CitizenFilters;
import com.meztlisoft.communitymanager.entity.CitizenEntity;
import com.meztlisoft.communitymanager.entity.specification.CitizenSpecification;
import com.meztlisoft.communitymanager.repository.AssociationRepository;
import com.meztlisoft.communitymanager.repository.CitizenRepository;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Log4j2
public class CitizenServiceImpl implements CitizenService {

    private final CitizenRepository citizenRepository;

    private final ObjectMapper objectMapper;

    private final JwtService jwtService;

    private final AssociationRepository associationRepository;

    @Override
    public CitizenDto create(CitizenDto citizen, String token) {
        Claims claims = jwtService.decodeToken(token);
        CitizenEntity citizenEntity = objectMapper.convertValue(citizen, CitizenEntity.class);
        citizenEntity.setActive(true);
        citizenEntity.setCreationDate(LocalDateTime.now());
        citizenEntity.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
        CitizenEntity citizenEntitySaved = citizenRepository.save(citizenEntity);
        return objectMapper.convertValue(citizenEntitySaved, CitizenDto.class);
    }

    @Override
    public Page<CitizenDto> getAll(CitizenFilters citizenFilters, Long retinueId) {
        Pageable paging = PageRequest.of(citizenFilters.getPage(), citizenFilters.getSize());

        List<Long> citizenIds = new ArrayList<>();
        boolean isAdmin = true;
        if (!retinueId.equals(0L)) {
            isAdmin = false;
            associationRepository.findAllByRetinueId(retinueId).forEach(associated -> citizenIds.add(associated.getCitizen().getId()));
        }

        Specification<CitizenEntity> specification = CitizenSpecification.getFilteredCitizen(citizenFilters, citizenIds, isAdmin);
        Page<CitizenEntity> citizens = citizenRepository.findAll(specification, paging);

        List<CitizenDto> dtos = new ArrayList<>();
        for (CitizenEntity citizen : citizens) {
            dtos.add(objectMapper.convertValue(citizen, CitizenDto.class));
        }
        return new PageImpl<>(dtos, citizens.getPageable(), citizens.getTotalElements());
    }

    @Override
    public CitizenDto getById(long id) {
        CitizenEntity citizenEntity = citizenRepository.findByIdAndActive(id, true).orElse(new CitizenEntity());
        return objectMapper.convertValue(citizenEntity, CitizenDto.class);
    }

    @Override
    public ActionStatusResponse update(long id, CitizenDto citizenDto, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        Claims claims = jwtService.decodeToken(token);
        try {
            CitizenEntity citizen = citizenRepository.findByIdAndActive(id, true).orElseThrow();

            if (StringUtils.isNotBlank(citizenDto.getName())) {
                citizen.setName(citizenDto.getName());
            }

            if (StringUtils.isNotBlank(citizenDto.getCurp())) {
                citizen.setCurp(citizenDto.getCurp());
            }

            if (StringUtils.isNotBlank(citizenDto.getAddress())) {
                citizen.setAddress(citizenDto.getAddress());
            }

            if (StringUtils.isNotBlank(citizenDto.getDescription())) {
                citizen.setDescription(citizenDto.getDescription());
            }

            if (Objects.nonNull(citizenDto.getBirthday())) {
                citizen.setBirthday(citizenDto.getBirthday());
            }

            citizen.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            citizen.setUpdateDate(LocalDateTime.now());
            CitizenEntity saved = citizenRepository.save(citizen);

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
            CitizenEntity citizen = citizenRepository.findByIdAndActive(id, true).orElseThrow();
            citizen.setActive(false);
            citizen.setCurp(citizen.getCurp().concat("-deleted"));
            citizen.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            citizen.setUpdateDate(LocalDateTime.now());
            citizenRepository.save(citizen);
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
