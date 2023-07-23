package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AdministratorDto;
import com.meztlisoft.communitymanager.dto.JwtAuthenticationResponse;
import com.meztlisoft.communitymanager.dto.SignInRequest;

public interface AuthenticationService {

    JwtAuthenticationResponse signin(SignInRequest request);

    AdministratorDto create(AdministratorDto request, String token);

    ActionStatusResponse update(long id, AdministratorDto administratorDto, String token);

    ActionStatusResponse delete(long id, String token);
}
