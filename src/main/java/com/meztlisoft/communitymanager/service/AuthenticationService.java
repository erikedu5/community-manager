package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.JwtAuthenticationResponse;
import com.meztlisoft.communitymanager.dto.SignInRequest;
import com.meztlisoft.communitymanager.dto.UserDto;

public interface AuthenticationService {

    JwtAuthenticationResponse signin(SignInRequest request);

    UserDto create(UserDto request);

    ActionStatusResponse update(long id, UserDto userDto);

    ActionStatusResponse delete(long id);
}
