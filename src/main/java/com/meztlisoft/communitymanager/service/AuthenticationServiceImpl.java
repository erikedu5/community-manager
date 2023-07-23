package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AdministratorDto;
import com.meztlisoft.communitymanager.dto.JwtAuthenticationResponse;
import com.meztlisoft.communitymanager.dto.SignInRequest;
import com.meztlisoft.communitymanager.entity.AdministratorEntity;
import com.meztlisoft.communitymanager.entity.CitizenEntity;
import com.meztlisoft.communitymanager.entity.RetinueEntity;
import com.meztlisoft.communitymanager.repository.AdministratorRepository;
import com.meztlisoft.communitymanager.repository.CitizenRepository;
import com.meztlisoft.communitymanager.repository.RetinueRepository;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AdministratorRepository administratorRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final CitizenRepository citizenRepository;

    private final RetinueRepository retinueRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        var citizen = administratorRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(citizen, citizen.getId());
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }


    @Override
    public AdministratorDto create(AdministratorDto request, String token) {

        RetinueEntity retinue = retinueRepository.findByIdAndActive(request.getRetinueId(), true)
                .orElseThrow();

        if (administratorRepository.existsByRoleAndRetinue(request.getRole(), retinue)) {
            throw new HttpServerErrorException(HttpStatus.PRECONDITION_FAILED,
                    "Este rol ya esta ocupado por otro cuidadano");
        }

        Claims claims = jwtService.decodeToken(token);
        CitizenEntity citizen = citizenRepository.findByIdAndActive(request.getCitizenId(), true)
                .orElseThrow();

        var administrator = AdministratorEntity.builder().userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword())).creationDate(LocalDateTime.now())
                .citizen(citizen)
                .retinue(retinue)
                .userEditor(Long.parseLong(claims.get("ciudadano_id").toString()))
                .active(true).role(request.getRole()).build();

        var administratorSaved = administratorRepository.save(administrator);

        AdministratorDto administratorDto = new AdministratorDto();
        administratorDto.setPassword(null);
        administratorDto.setRole(administratorSaved.getRole());
        administratorDto.setCitizen(citizen);
        administratorDto.setRetinue(retinue);
        administratorDto.setUserName(administratorSaved.getUsername());

        return administratorDto;
    }

    @Override
    public ActionStatusResponse update(long id, AdministratorDto administratorDto, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        Claims claims = jwtService.decodeToken(token);
        try {
            AdministratorEntity administrator = administratorRepository.findByIdAndActive(id, true).orElseThrow();

            if (StringUtils.isNotBlank(administratorDto.getUserName())) {
                administrator.setUserName(administratorDto.getUserName());
            }

            if (StringUtils.isNotBlank(administratorDto.getPassword())) {
                administrator.setPassword(passwordEncoder.encode(administratorDto.getPassword()));
            }

            if (Objects.nonNull(administratorDto.getRole())) {
                administrator.setRole(administratorDto.getRole());
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
