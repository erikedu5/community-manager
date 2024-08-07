package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.CitizenEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitizenRepository extends JpaRepository<CitizenEntity, Long>, JpaSpecificationExecutor<CitizenEntity> {

    Optional<CitizenEntity> findByIdAndActive(Long citizenId, boolean active);

    @Query("FROM CitizenEntity c WHERE c.id > 0 and c.active = :active")
    Page<CitizenEntity> findAllActive(boolean active, Pageable paging);

    @Query("FROM CitizenEntity c WHERE  LOWER(c.name) like :citizenName")
    List<CitizenEntity> findLikeName(String citizenName);
}
