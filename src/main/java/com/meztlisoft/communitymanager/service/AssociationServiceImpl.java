package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AssociatedDto;
import com.meztlisoft.communitymanager.dto.AssociationsDto;
import com.meztlisoft.communitymanager.dto.filters.AssociatedFilters;
import com.meztlisoft.communitymanager.entity.AdministratorEntity;
import com.meztlisoft.communitymanager.entity.AssociatedEntity;
import com.meztlisoft.communitymanager.entity.CitizenEntity;
import com.meztlisoft.communitymanager.entity.RetinueEntity;
import com.meztlisoft.communitymanager.repository.AdministratorRepository;
import com.meztlisoft.communitymanager.repository.AssociationRepository;
import com.meztlisoft.communitymanager.repository.CitizenRepository;
import com.meztlisoft.communitymanager.repository.RetinueRepository;
import com.meztlisoft.communitymanager.repository.RoleRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociationServiceImpl implements  AssociationService {

    private final AdministratorRepository administratorRepository;
    private final AssociationRepository associationRepository;
    private final CitizenRepository citizenRepository;
    private final RetinueRepository retinueRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    private List<AssociatedEntity> associatedEntities;
    private List<AdministratorEntity> admins;

    @Override
    public ActionStatusResponse create(long idRetinue, List<AssociationsDto> associationsDto, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        actionStatusResponse.setId(idRetinue);
        Claims claims = jwtService.decodeToken(token);
        Map<HttpStatus, String> errors = new HashMap<>();
        try {
            associatedEntities = new ArrayList<>();
            admins = new ArrayList<>();
            RetinueEntity retinue = retinueRepository.findByIdAndActive(idRetinue, true).orElseThrow();
            for (AssociationsDto association : associationsDto) {
                try {
                    CitizenEntity citizen = citizenRepository.findByIdAndActive(association.getCitizenId(), true).orElseThrow();
                    Long userEditor = Long.parseLong(claims.get("ciudadano_id").toString());
                    this.addAssociationAndAdmin(citizen, retinue, association, userEditor);
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
            errors.put(HttpStatus.BAD_REQUEST, "no se encontró la associación con id " + idRetinue);
            actionStatusResponse.setStatus(HttpStatus.PRECONDITION_REQUIRED);
            actionStatusResponse.setDescription("Associacion no creada");
        }
        actionStatusResponse.setErrors(errors);
        return actionStatusResponse;
    }

    @Override
    public ActionStatusResponse addCitizenToAssociation(long idCitizen, List<AssociationsDto> associationsDto, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        actionStatusResponse.setId(idCitizen);
        Claims claims = jwtService.decodeToken(token);
        Map<HttpStatus, String> errors = new HashMap<>();
        try {
            associatedEntities = new ArrayList<>();
            admins = new ArrayList<>();
            CitizenEntity citizen = citizenRepository.findByIdAndActive(idCitizen, true).orElseThrow();
            for (AssociationsDto association : associationsDto) {
                try {
                    RetinueEntity retinue = retinueRepository.findByIdAndActive(association.getRetinueId(), true).orElseThrow();
                    Long userEditor = Long.parseLong(claims.get("ciudadano_id").toString());
                    this.addAssociationAndAdmin(citizen, retinue, association, userEditor);
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
            errors.put(HttpStatus.BAD_REQUEST, "no se encontró la associación con id " + idCitizen);
            actionStatusResponse.setStatus(HttpStatus.PRECONDITION_REQUIRED);
            actionStatusResponse.setDescription("Associacion no creada");
        }
        actionStatusResponse.setErrors(errors);
        return actionStatusResponse;
    }

    @Override
    public Page<AssociatedDto> getRetinueAssociatedByCitizenId(long citizenId, AssociatedFilters associatedFilters) {
        Pageable paging = PageRequest.of(associatedFilters.getPage(), associatedFilters.getSize());
        Page<RetinueEntity> retinues = retinueRepository.findAllActive(true, paging);
        CitizenEntity citizen = citizenRepository.findByIdAndActive(citizenId, true).orElseThrow();
        List<AssociatedDto> associatedDtos = new ArrayList<>();
        retinues.forEach(retinue -> associatedDtos.add(this.convertAssociatedDto(retinue, citizen)));
        return new PageImpl<>(associatedDtos, retinues.getPageable(), retinues.getTotalElements());
    }

    @Override
    public Page<AssociatedDto> getCitizenAssociated(long id, AssociatedFilters associatedFilters) {
        Pageable paging = PageRequest.of(associatedFilters.getPage(), associatedFilters.getSize());
        Page<CitizenEntity> citizens = citizenRepository.findAllActive(true, paging);
        RetinueEntity  retinue = retinueRepository.findByIdAndActive(id, true).orElseThrow();
        List<AssociatedDto> associatedDtos = new ArrayList<>();
        citizens.forEach(citizen -> associatedDtos.add(this.convertAssociatedDto(retinue, citizen)));
        return new PageImpl<>(associatedDtos, citizens.getPageable(), citizens.getTotalElements());
    }

    private AssociatedDto convertAssociatedDto(RetinueEntity retinue, CitizenEntity citizen) {
        AssociatedDto dto = new AssociatedDto();
        dto.setRetinueId(retinue.getId());
        dto.setRetinueName(retinue.getName());
        dto.setMember(associationRepository.existByRetinueAndCitizen(retinue.getId(), citizen.getId()));
        AdministratorEntity administrator = administratorRepository.findRoleByCitizenIdAndRetinueId(citizen.getId(), retinue.getId());
        dto.setRole(Objects.nonNull(administrator) ?administrator.getRole(): null);
        dto.setCitizenName(citizen.getName());
        dto.setCitizenId(citizen.getId());
        return dto;
    }

    private void addAssociationAndAdmin(CitizenEntity citizen, RetinueEntity retinue, AssociationsDto association, Long userEditor){
        AssociatedEntity associatedEntity = new AssociatedEntity();
        associatedEntity.setActive(true);
        associatedEntity.setCreationDate(LocalDateTime.now());
        associatedEntity.setCitizen(citizen);
        associatedEntity.setRetinue(retinue);
        associatedEntity.setBenefit(associatedEntity.getBenefit());
        associatedEntity.setUserEditor(userEditor);
        associatedEntities.add(associatedEntity);
        if (Objects.nonNull(association.getRoleId())) {
            AdministratorEntity administrator = new AdministratorEntity();
            administrator.setRole(roleRepository.findById(association.getRoleId()).orElseThrow());
            administrator.setUserEditor(userEditor);
            administrator.setActive(true);
            administrator.setCreationDate(LocalDateTime.now());
            administrator.setCitizen(citizen);
            administrator.setRetinue(retinue);
            admins.add(administrator);
        }
    }

}
