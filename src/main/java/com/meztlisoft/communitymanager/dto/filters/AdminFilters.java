package com.meztlisoft.communitymanager.dto.filters;

import com.meztlisoft.communitymanager.dto.enums.Role;
import lombok.Data;

@Data
public class AdminFilters {

    private String nombreComitiva;

    private String nombreCiudadano;

    private Role role;

    private int page;

    private int size;
}
