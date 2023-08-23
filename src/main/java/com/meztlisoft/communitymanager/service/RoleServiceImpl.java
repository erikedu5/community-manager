package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.entity.RoleEntity;
import com.meztlisoft.communitymanager.repository.RoleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleEntity> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public RoleEntity getById(long id) {
        return roleRepository.findById(id).orElseThrow();
    }
}
