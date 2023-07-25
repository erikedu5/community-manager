package com.meztlisoft.communitymanager.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDto {

    private String userName;
    private String password;
    private Long citizenId;
}
