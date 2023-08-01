package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.RetinueEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetinueRepository extends JpaRepository<RetinueEntity, Long>, JpaSpecificationExecutor<RetinueEntity> {

    Optional<RetinueEntity> findByIdAndActive(Long retinueId, boolean active);

    @Query(value = "SELECT * FROM comitivas where id > 0 and activo = ?", nativeQuery = true)
    Page<RetinueEntity> findAllActive(boolean active, Pageable page);
}
