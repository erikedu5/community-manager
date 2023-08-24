package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AdministratorDto;
import com.meztlisoft.communitymanager.dto.AdministratorResponse;
import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import com.meztlisoft.communitymanager.entity.AdministratorEntity;
import com.meztlisoft.communitymanager.entity.CitizenEntity;
import com.meztlisoft.communitymanager.entity.RetinueEntity;
import com.meztlisoft.communitymanager.entity.RoleEntity;
import com.meztlisoft.communitymanager.entity.specification.AdministratorSpecification;
import com.meztlisoft.communitymanager.repository.AdministratorRepository;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdministratorServiceImpl implements AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final JwtService jwtService;
    private final RetinueRepository retinueRepository;
    private final CitizenRepository citizenRepository;
    private final RoleRepository roleRepository;

    @Override
    public AdministratorResponse create(AdministratorDto request, String token) {
        RetinueEntity retinue = retinueRepository.findByIdAndActive(request.getRetinueId(), true)
                .orElseThrow();

        RoleEntity role = roleRepository.findById(request.getRoleId())
                .orElseThrow();

        if (administratorRepository.existsByRoleAndRetinue(role, retinue)) {
            throw new HttpServerErrorException(HttpStatus.PRECONDITION_FAILED,
                    "Este rol ya esta ocupado por otro cuidadano");
        }

        Claims claims = jwtService.decodeToken(token);
        CitizenEntity citizen = citizenRepository.findByIdAndActive(request.getCitizenId(), true)
                .orElseThrow();

        AdministratorEntity administrator = new AdministratorEntity();
        administrator.setRetinue(retinue);
        administrator.setCreationDate(LocalDateTime.now());
        administrator.setRole(roleRepository.findById(request.getRoleId()).orElseThrow());
        administrator.setCitizen(citizen);
        administrator.setActive(true);
        administrator.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
        var administratorSaved = administratorRepository.save(administrator);

        AdministratorResponse administratorResponse = new AdministratorResponse();
        administratorResponse.setRole(role);
        administratorResponse.setCitizen(citizen);
        administratorResponse.setRetinue(retinue);
        administratorResponse.setAdministratorId(administratorSaved.getId());

        return administratorResponse;
    }

    @Override
    public Page<AdministratorResponse> getAll(AdminFilters adminFilters) {
        Pageable paging = PageRequest.of(adminFilters.getPage(), adminFilters.getSize());
        Specification<AdministratorEntity> specification = AdministratorSpecification.getFilteredAdministrator(adminFilters);
        Page<AdministratorEntity> administrators = administratorRepository.findAll(specification, paging);
        List<AdministratorResponse> dtos = new ArrayList<>();
        administrators.stream().forEach(admin -> {
            AdministratorResponse administratorResponse = new AdministratorResponse();
            administratorResponse.setRole(admin.getRole());
            administratorResponse.setCitizen(admin.getCitizen());
            administratorResponse.setRetinue(admin.getRetinue());
            administratorResponse.setAdministratorId(admin.getId());
            dtos.add(administratorResponse);
        });

        return new PageImpl<>(dtos, administrators.getPageable(), administrators.getTotalElements());
    }

    @Override
    public AdministratorResponse getById(long id) {
        AdministratorEntity administratorEntity = administratorRepository.findByIdAndActive(id, true).orElse(new AdministratorEntity());
        AdministratorResponse administratorResponse = new AdministratorResponse();
        administratorResponse.setRole(administratorEntity.getRole());
        administratorResponse.setCitizen(administratorEntity.getCitizen());
        administratorResponse.setRetinue(administratorEntity.getRetinue());
        administratorResponse.setAdministratorId(administratorEntity.getId());
        return administratorResponse;
    }

    @Override
    public ActionStatusResponse update(long id, AdministratorDto administratorDto, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        Claims claims = jwtService.decodeToken(token);
        try {
            AdministratorEntity administrator = administratorRepository.findByIdAndActive(id, true).orElseThrow();

            if (Objects.nonNull(administratorDto.getRoleId())) {
                RoleEntity role = roleRepository.findById(administratorDto.getRoleId()).orElseThrow();
                administrator.setRole(role);
            }

            administrator.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            administrator.setUpdateDate(LocalDateTime.now());
            AdministratorEntity saved = administratorRepository.save(administrator);

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
            AdministratorEntity administrator = administratorRepository.findByIdAndActive(id, true).orElseThrow();
            administrator.setActive(false);
            administrator.setUserEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            administrator.setUpdateDate(LocalDateTime.now());
            administratorRepository.save(administrator);
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
