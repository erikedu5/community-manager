package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.JwtAuthenticationResponse;
import com.meztlisoft.communitymanager.dto.SignInRequest;
import com.meztlisoft.communitymanager.dto.UserDto;

public interface AuthenticationService {

    JwtAuthenticationResponse signIn(SignInRequest request);

    UserDto create(UserDto request);

    ActionStatusResponse change_password(long id, UserDto userDto);

    ActionStatusResponse delete(long id);

    UserDto getLoginName(long citizenId);
}
