package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {

    @Query("FROM MeetingEntity me WHERE me.retinue.id = :retinueId")
    List<MeetingEntity> findByRetinueId(Long retinueId);
}