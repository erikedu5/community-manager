package com.meztlisoft.communitymanager.dto;

import com.meztlisoft.communitymanager.dto.enums.Role;
import com.meztlisoft.communitymanager.entity.CiudadanoEntity;
import com.meztlisoft.communitymanager.entity.ComitivaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministradorDto {

    private String userName;
    private String password;
    private Role role;
    private Long ciudadanoId;
    private Long comitivaId;

    private CiudadanoEntity ciudadano;
    private ComitivaEntity comitiva;

}
