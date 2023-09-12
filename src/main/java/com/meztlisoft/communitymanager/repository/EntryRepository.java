package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.EntryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntryRepository extends JpaRepository<EntryEntity, Long> {

    @Query("FROM EntryEntity ee WHERE ee.retinue.id = :retinueId")
    Page<EntryEntity> findAllByRetinueId(Long retinueId, Pageable page);

    @Query("FROM EntryEntity ee WHERE ee.cooperation.id = :cooperationId")
    Optional<EntryEntity> existByCooperationId(Long cooperationId);

    @Query("FROM EntryEntity ee WHERE ee.retinue.id = :retinueId and lower(ee.concept) like lower(concat('%', :concept, '%'))")
    Page<EntryEntity> findAllByConceptAndRetinueId(String concept, Long retinueId, Pageable page);

    @Query("SELECT SUM(ee.cost) FROM EntryEntity ee WHERE ee.retinue.id = :retinueId")
    Long getSummary(Long retinueId);
}
