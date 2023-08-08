package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {
}