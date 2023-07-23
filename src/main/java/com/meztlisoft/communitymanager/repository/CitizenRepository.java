package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.CitizenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitizenRepository extends JpaRepository<CitizenEntity, Long>, JpaSpecificationExecutor<CitizenEntity> {

    Optional<CitizenEntity> findByIdAndActive(Long citizenId, boolean active);
}
