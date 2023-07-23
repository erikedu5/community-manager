package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.AssociatedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationRepository extends JpaRepository<AssociatedEntity, Long>, JpaSpecificationExecutor<AssociatedEntity> {

    @Query(value = "Select * from asociados where comitiva_id = ?", nativeQuery = true)
    Page<AssociatedEntity> findAllByRetinueId(long id, Pageable page, Specification<AssociatedEntity> specification);
}
