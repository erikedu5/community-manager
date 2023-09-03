package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.*;
import com.meztlisoft.communitymanager.entity.UserEntity;
import com.meztlisoft.communitymanager.repository.AdministratorRepository;
import com.meztlisoft.communitymanager.repository.CitizenRepository;
import com.meztlisoft.communitymanager.repository.RetinueRepository;
import com.meztlisoft.communitymanager.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final CitizenRepository citizenRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AdministratorRepository administratorRepository;

    private final RetinueRepository retinueRepository;

    private final ObjectMapper objectMapper;

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
            UserEntity citizen = userRepository.findByUserName(request.getUserName())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
            List<RetinueDto> retinues = new ArrayList<>();
            if (citizen.getCitizen().getId().equals(0L)) {
                retinueRepository.findAll()
                   .forEach(ret -> retinues.add(objectMapper.convertValue(ret, RetinueDto.class)));
            } else {
                administratorRepository.findByCitizenAndActive(citizen.getCitizen(), true)
                        .forEach(ret -> retinues.add(objectMapper.convertValue(ret.getRetinue(), RetinueDto.class)));
            }
            String jwt = jwtService.generateToken(citizen, citizen.getCitizen().getId(), retinues);
            return JwtAuthenticationResponse.builder().token(jwt).build();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public UserDto create(UserDto request) {
        try {
            UserEntity user = new UserEntity();
            user.setUserName(request.getUserName());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setCitizen(citizenRepository.findByIdAndActive(request.getCitizenId(), true).orElseThrow());
            user.setActive(true);
            UserEntity saved = userRepository.save(user);

            UserDto userDto = new UserDto();
            userDto.setUserName(saved.getUsername());
            userDto.setCitizenId(saved.getCitizen().getId());
            userDto.setPassword(null);
            return userDto;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return new UserDto();
    }

    @Override
    public ActionStatusResponse change_password(long id, UserDto userDto) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        try {
            UserEntity user = userRepository.findById(id).orElseThrow();

            if (StringUtils.isNotBlank(userDto.getPassword())) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }

            UserEntity saved = userRepository.save(user);

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
    public ActionStatusResponse delete(long id) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        try {
            UserEntity user = userRepository.findById(id).orElseThrow();
            user.setActive(false);
            userRepository.save(user);
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
