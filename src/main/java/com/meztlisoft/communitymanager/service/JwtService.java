package com.meztlisoft.communitymanager.service;

import io.jsonwebtoken.Claims;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails, Long id, Map<Long, String> retinues);

    boolean isTokenValid(String token, UserDetails userDetails);

    Claims decodeToken(String token);
}
