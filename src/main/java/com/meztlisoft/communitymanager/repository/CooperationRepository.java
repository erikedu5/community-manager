package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.CooperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CooperationRepository extends JpaRepository<CooperationEntity, Long>, JpaSpecificationExecutor<CooperationEntity> {


    Optional<CooperationEntity> findByIdAndActive(Long cooperationId, boolean active);

    @Query("FROM CooperationEntity c WHERE c.retinue.id = :retinueId")
    List<CooperationEntity> findByRetinueId(Long retinueId);
}
