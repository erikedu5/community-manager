package com.meztlisoft.communitymanager.dto;

import com.meztlisoft.communitymanager.entity.CitizenEntity;
import com.meztlisoft.communitymanager.entity.RetinueEntity;
import com.meztlisoft.communitymanager.entity.RoleEntity;
import lombok.Data;

@Data
public class AdministratorResponse {

    private Long administratorId;
    private RoleEntity role;
    private CitizenEntity citizen;
    private RetinueEntity retinue;
}
