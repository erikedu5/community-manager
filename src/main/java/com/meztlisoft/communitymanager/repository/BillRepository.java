package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.BillEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long> {

    @Query(value = "FROM BillEntity b WHERE b.retinue.id = :retinueId")
    Page<BillEntity> findAllByRetinueId(Long retinueId, Pageable page);

    @Query("FROM BillEntity be WHERE be.retinue.id = :retinueId and lower(be.concept) like lower(concat('%', :concept, '%'))")
    Page<BillEntity> findAllByConceptAndRetinueId(String concept, Long retinueId, Pageable page);

    @Query("SELECT SUM(be.cost) FROM BillEntity be WHERE be.retinue.id = :retinueId")
    Long getSummary(Long retinueId);
}
