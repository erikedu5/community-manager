package com.meztlisoft.communitymanager.dto;

import com.meztlisoft.communitymanager.entity.RoleEntity;
import lombok.Data;

@Data
public class AssociatedDto {

    private Long retinueId;
    private String retinueName;
    private boolean isMember;
    private RoleEntity role;
    private Long citizenId;
    private String citizenName;

}
