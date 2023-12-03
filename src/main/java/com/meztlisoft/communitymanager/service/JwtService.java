package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.RetinueDto;
import com.meztlisoft.communitymanager.entity.AdministratorEntity;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails, Long id, List<RetinueDto> retinues, Optional<AdministratorEntity> admin);

    boolean isTokenValid(String token, UserDetails userDetails);

    Claims decodeToken(String token);
}
