package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.EntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends JpaRepository<EntryEntity, Long> {
}
