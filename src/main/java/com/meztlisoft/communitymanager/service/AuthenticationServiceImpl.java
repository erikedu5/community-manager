package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AdministradorDto;
import com.meztlisoft.communitymanager.dto.JwtAuthenticationResponse;
import com.meztlisoft.communitymanager.dto.SignInRequest;
import com.meztlisoft.communitymanager.entity.AdministradorEntity;
import com.meztlisoft.communitymanager.entity.CiudadanoEntity;
import com.meztlisoft.communitymanager.entity.ComitivaEntity;
import com.meztlisoft.communitymanager.repository.AdministradorRepository;
import com.meztlisoft.communitymanager.repository.CiudadanoRepository;
import com.meztlisoft.communitymanager.repository.ComitivaRepository;
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

    private final AdministradorRepository administradorRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final CiudadanoRepository ciudadanoRepository;

    private final ComitivaRepository comitivaRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        var ciudadano = administradorRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(ciudadano, ciudadano.getId());
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }


    @Override
    public AdministradorDto create(AdministradorDto request, String token) {

        ComitivaEntity comitiva = comitivaRepository.findById(request.getComitivaId())
                .orElseThrow();

        if (administradorRepository.existsByRoleAndComitiva(request.getRole(), comitiva)) {
            throw new HttpServerErrorException(HttpStatus.PRECONDITION_FAILED,
                    "Este rol ya esta ocupado por otro cuidadano");
        }

        Claims claims = jwtService.decodeToken(token);
        CiudadanoEntity ciudadano = ciudadanoRepository.findById(request.getCiudadanoId())
                .orElseThrow();

        var administrador = AdministradorEntity.builder().userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword())).fechaCreacion(LocalDateTime.now())
                .ciudadano(ciudadano)
                .comitiva(comitiva)
                .usuarioEditor(Long.parseLong(claims.get("ciudadano_id").toString()))
                .activo(true).role(request.getRole()).build();

        var administradorSaved = administradorRepository.save(administrador);

        AdministradorDto administradorDto = new AdministradorDto();
        administradorDto.setPassword(null);
        administradorDto.setRole(administradorSaved.getRole());
        administradorDto.setCiudadano(ciudadano);
        administradorDto.setComitiva(comitiva);
        administradorDto.setUserName(administradorSaved.getUsername());

        return administradorDto;
    }

    @Override
    public ActionStatusResponse update(long id, AdministradorDto administradorDto, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        Claims claims = jwtService.decodeToken(token);
        try {
            AdministradorEntity administrador = administradorRepository.findById(id).orElseThrow();

            if (StringUtils.isNotBlank(administradorDto.getUserName())) {
                administrador.setUserName(administradorDto.getUserName());
            }

            if (StringUtils.isNotBlank(administradorDto.getPassword())) {
                administrador.setPassword(passwordEncoder.encode(administradorDto.getPassword()));
            }

            if (Objects.nonNull(administradorDto.getRole())) {
                administrador.setRole(administradorDto.getRole());
            }

            administrador.setUsuarioEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            administrador.setFechaActualizacion(LocalDateTime.now());
            AdministradorEntity saved = administradorRepository.save(administrador);

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
            AdministradorEntity administrador = administradorRepository.findById(id).orElseThrow();
            administrador.setActivo(false);
            administrador.setUsuarioEditor(Long.parseLong(claims.get("ciudadano_id").toString()));
            administrador.setFechaActualizacion(LocalDateTime.now());
            administradorRepository.save(administrador);
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
