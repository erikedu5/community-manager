package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.RetinueDto;
import io.jsonwebtoken.Claims;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails, Long id, List<RetinueDto> retinues);

    boolean isTokenValid(String token, UserDetails userDetails);

    Claims decodeToken(String token);
}
