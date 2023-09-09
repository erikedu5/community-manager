package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.entity.UnitBenefitEntity;

import java.util.List;

public interface UnitBenefitService {
    List<UnitBenefitEntity> getAll();

    UnitBenefitEntity getById(long id);
}
