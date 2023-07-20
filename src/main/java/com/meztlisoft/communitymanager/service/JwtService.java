package com.meztlisoft.communitymanager.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails, Long id);

    boolean isTokenValid(String token, UserDetails userDetails);

    Claims decodeToken(String token);
}
