package com.meztlisoft.communitymanager.dto;

import com.meztlisoft.communitymanager.dto.enums.Role;
import com.meztlisoft.communitymanager.entity.CitizenEntity;
import com.meztlisoft.communitymanager.entity.RetinueEntity;
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
    private Role role;
    private Long citizenId;
    private Long retinueId;

    private CitizenEntity citizen;
    private RetinueEntity retinue;

}
