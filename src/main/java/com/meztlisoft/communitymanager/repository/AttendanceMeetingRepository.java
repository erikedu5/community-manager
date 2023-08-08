package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.AttendanceMeetingEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceMeetingRepository extends JpaRepository<AttendanceMeetingEntity, Long> {

    @Query(value = "FROM AttendanceMeetingEntity am WHERE am.meeting.id = :id")
    List<AttendanceMeetingEntity> findByMeetingId(long id);
}