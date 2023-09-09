package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.entity.UnitBenefitEntity;
import com.meztlisoft.communitymanager.repository.UnitBenefitRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UnitBenefitServiceImpl implements UnitBenefitService {

    private final UnitBenefitRepository unitBenefitRepository;

    @Override
    public List<UnitBenefitEntity> getAll() {
        return unitBenefitRepository.findAll();
    }

    @Override
    public UnitBenefitEntity getById(long id) {
        return unitBenefitRepository.findById(id).orElseThrow();
    }
}
