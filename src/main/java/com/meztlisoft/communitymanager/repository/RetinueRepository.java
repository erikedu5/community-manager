package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.RetinueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetinueRepository extends JpaRepository<RetinueEntity, Long>, JpaSpecificationExecutor<RetinueEntity> {

    Optional<RetinueEntity> findByIdAndActive(Long retinueId, boolean active);
}
