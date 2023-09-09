package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.UnitBenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitBenefitRepository extends JpaRepository<UnitBenefitEntity, Long> {
}
