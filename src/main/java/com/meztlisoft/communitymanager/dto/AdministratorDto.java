package com.meztlisoft.communitymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorDto {

    private Long citizenId;
    private Long retinueId;
    private Long roleId;

}
