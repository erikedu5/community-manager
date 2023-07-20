package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.AdministradorDto;
import com.meztlisoft.communitymanager.dto.JwtAuthenticationResponse;
import com.meztlisoft.communitymanager.dto.SignInRequest;

public interface AuthenticationService {

    JwtAuthenticationResponse signin(SignInRequest request);

    AdministradorDto create(AdministradorDto request, String token);
}
