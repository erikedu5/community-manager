package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import lombok.RequiredArgsConstructor;
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

    private final ObjectMapper objectMapper;

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

        ComitivaEntity comitiva = comitivaRepository.findById(request.getComitivaId()).get();
        if (administradorRepository.existsByRoleAndComitiva(request.getRole(), comitiva)) {
            throw new HttpServerErrorException(HttpStatus.PRECONDITION_FAILED,
                    "Este rol ya esta ocupado por otro cuidadano");
        }

        Claims claims = jwtService.decodeToken(token);
        CiudadanoEntity ciudadano = ciudadanoRepository.findById(request.getCiudadanoId()).get();

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
}
