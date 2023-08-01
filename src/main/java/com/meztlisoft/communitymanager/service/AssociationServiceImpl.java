package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.*;
import com.meztlisoft.communitymanager.dto.filters.AssociatedFilters;
import com.meztlisoft.communitymanager.entity.*;
import com.meztlisoft.communitymanager.entity.specification.AssociationSpecification;
import com.meztlisoft.communitymanager.repository.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AssociationServiceImpl implements  AssociationService {

    private final AdministratorRepository administratorRepository;
    private final AssociationRepository associationRepository;
    private final CitizenRepository citizenRepository;
    private final RetinueRepository retinueRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    public ActionStatusResponse create(long id, List<AssociationsDto> associationsDto, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        actionStatusResponse.setId(id);
        Claims claims = jwtService.decodeToken(token);
        Map<HttpStatus, String> errors = new HashMap<>();
        try {
            List<AssociatedEntity> associatedEntities = new ArrayList<>();
            RetinueEntity retinue = retinueRepository.findByIdAndActive(id, true).orElseThrow();
            List<AdministratorEntity> admins = new ArrayList<>();
            for (AssociationsDto association : associationsDto) {
                try {
                    CitizenEntity citizen = citizenRepository.findByIdAndActive(association.getCitizenId(), true).orElseThrow();
                    AssociatedEntity associatedEntity = new AssociatedEntity();
                    associatedEntity.setActive(true);
                    associatedEntity.setCreationDate(LocalDateTime.now());
                    associatedEntity.setCitizen(citizen);
                    associatedEntity.setRetinue(retinue);
                    associatedEntity.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
                    associatedEntities.add(associatedEntity);
                    if (Objects.nonNull(association.getRoleId())) {
                        AdministratorEntity administrator = new AdministratorEntity();
                        administrator.setRole(roleRepository.findById(association.getRoleId()).orElseThrow());
                        administrator.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
                        administrator.setActive(true);
                        administrator.setCreationDate(LocalDateTime.now());
                        administrator.setCitizen(citizen);
                        administrator.setRetinue(retinue);
                        admins.add(administrator);
                    }
                } catch (Exception ex) {
                    errors.put(HttpStatus.PRECONDITION_FAILED, "No se encontró el cuidadano con el id " + association.getCitizenId());
                }
            }
            associationRepository.saveAll(associatedEntities);
            if (admins.size() > 0) {
                administratorRepository.saveAll(admins);
            }
            if (errors.size() > 0) {
                actionStatusResponse.setStatus(HttpStatus.PARTIAL_CONTENT);
                actionStatusResponse.setDescription("Associacion creada con errores");
            } else {
                actionStatusResponse.setStatus(HttpStatus.OK);
                actionStatusResponse.setDescription("Associacion completa");
            }
        } catch (Exception ex) {
            errors.put(HttpStatus.BAD_REQUEST, "no se encontró la associación con id " + id);
            actionStatusResponse.setStatus(HttpStatus.PRECONDITION_REQUIRED);
            actionStatusResponse.setDescription("Associacion no creada");
        }
        actionStatusResponse.setErrors(errors);
        return actionStatusResponse;
    }

    @Override
    public Page<CitizenDto> getCitizenAssociated(long id, AssociatedFilters associatedFilters) {
        Pageable paging = PageRequest.of(associatedFilters.getPage(), associatedFilters.getSize());
        Specification<AssociatedEntity> specification = AssociationSpecification.getFilterAssociation(associatedFilters);

        Page<AssociatedEntity> associatedEntity = associationRepository.findAllByRetinueId(id, paging, specification);

        List<CitizenDto> citizenDtos = new ArrayList<>();
        for (AssociatedEntity associated: associatedEntity) {
            citizenDtos.add(objectMapper.convertValue(associated.getCitizen(), CitizenDto.class));
        }
       return new PageImpl<>(citizenDtos, associatedEntity.getPageable(), associatedEntity.getTotalElements());
    }
}
