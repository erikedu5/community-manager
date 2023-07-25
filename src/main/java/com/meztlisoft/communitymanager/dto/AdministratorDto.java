package com.meztlisoft.communitymanager.dto;

import com.meztlisoft.communitymanager.entity.CitizenEntity;
import com.meztlisoft.communitymanager.entity.RetinueEntity;
import com.meztlisoft.communitymanager.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorDto {

    private String userName;
    private String password;
    private RoleEntity role;
    private Long citizenId;
    private Long retinueId;
    private Long roleId;

    private CitizenEntity citizen;
    private RetinueEntity retinue;

}
