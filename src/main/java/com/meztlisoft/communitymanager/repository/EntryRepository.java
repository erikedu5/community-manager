package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.EntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntryRepository extends JpaRepository<EntryEntity, Long>, JpaSpecificationExecutor<EntryEntity> {

    @Query("FROM EntryEntity ee WHERE ee.cooperation.id = :cooperationId")
    Optional<EntryEntity> existByCooperationId(Long cooperationId);

}
