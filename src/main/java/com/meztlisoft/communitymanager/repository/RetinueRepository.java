package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.RetinueEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RetinueRepository extends JpaRepository<RetinueEntity, Long>, JpaSpecificationExecutor<RetinueEntity> {

    Optional<RetinueEntity> findByIdAndActive(Long retinueId, boolean active);

    @Query("FROM RetinueEntity r where r.id > 0 and r.active = :active")
    Page<RetinueEntity> findAllActive(boolean active, Pageable page);

    @Query("FROM RetinueEntity r where r.id > 0 and r.active = :active")
    Page<RetinueEntity> findAllActive(boolean active);
}
