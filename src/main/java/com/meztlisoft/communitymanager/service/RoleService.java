package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.entity.RoleEntity;

import java.util.List;

public interface RoleService {
    List<RoleEntity> getAll();

    RoleEntity getById(long id);
}
