package com.meztlisoft.communitymanager.dto.filters;

import com.meztlisoft.communitymanager.entity.RoleEntity;
import lombok.Data;

@Data
public class AdminFilters {

    private String retinueName;

    private String citizenName;

    private RoleEntity role;

    private int page;

    private int size;
}
