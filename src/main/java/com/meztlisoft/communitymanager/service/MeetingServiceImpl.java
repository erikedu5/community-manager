package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.MeetingDto;
import com.meztlisoft.communitymanager.dto.MeetingAttendanceDto;
import com.meztlisoft.communitymanager.dto.MeetingResponse;
import com.meztlisoft.communitymanager.entity.AttendanceMeetingEntity;
import com.meztlisoft.communitymanager.entity.MeetingEntity;
import com.meztlisoft.communitymanager.repository.AssociationRepository;
import com.meztlisoft.communitymanager.repository.AttendanceMeetingRepository;
import com.meztlisoft.communitymanager.repository.MeetingRepository;
import com.meztlisoft.communitymanager.repository.RetinueRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final RetinueRepository retinueRepository;
    private final AttendanceMeetingRepository attendanceMeetingRepository;
    private final AssociationRepository associationRepository;

    @Override
    public MeetingResponse findById(long id) {
        MeetingEntity meeting = meetingRepository.findById(id).orElseThrow();
        MeetingResponse response = new MeetingResponse();
        response.setConcept(meeting.getDescription());
        response.setMeetingDate(meeting.getMeetingDate());
        response.setId(meeting.getId());
        List<MeetingAttendanceDto> dto = new ArrayList<>();
        List<AttendanceMeetingEntity> attendancesMeeting = attendanceMeetingRepository.findByMeetingId(id);
        attendancesMeeting.forEach(attendanceMeetingEntity -> {
            MeetingAttendanceDto attendanceMeeting = new MeetingAttendanceDto();
            attendanceMeeting.setAssociatedId(attendanceMeetingEntity.getAssociated().getId());
            attendanceMeeting.setCitizenName(attendanceMeetingEntity.getAssociated().getCitizen().getName());
            attendanceMeeting.setCheck(attendanceMeetingEntity.isAttendance());
            dto.add(attendanceMeeting);
        });
        response.setAttendance(dto);
        return response;
    }

    @Override
    public ActionStatusResponse save(MeetingDto meetingDto, String token, Long retinueId) {
        ActionStatusResponse response = new ActionStatusResponse();
        try {
            MeetingEntity meeting = new MeetingEntity();
            meeting.setDescription(meetingDto.getConcept());
            meeting.setMeetingDate(meetingDto.getDateMeeting());
            meeting.setRetinue(retinueRepository.findByIdAndActive(retinueId, true).orElseThrow());
            MeetingEntity actual = meetingRepository.save(meeting);
            response.setId(actual.getId());
            response.setStatus(HttpStatus.CREATED);
            response.setDescription("junta creada correctamente");
        } catch (Exception exception) {
            Map<HttpStatus, String> errors = new HashMap<>();
            errors.put(HttpStatus.BAD_GATEWAY, exception.getMessage());
            response.setErrors(errors);
        }
        return response;
    }

    @Override
    public ActionStatusResponse check(Long id, MeetingAttendanceDto meetingAttendanceDto) {
        ActionStatusResponse response = new ActionStatusResponse();
        try {
            AttendanceMeetingEntity attendanceMeeting = new AttendanceMeetingEntity();
            attendanceMeeting.setMeeting(meetingRepository.findById(id).orElseThrow());
            attendanceMeeting.setAttendance(meetingAttendanceDto.getCheck());
            attendanceMeeting.setAssociated(associationRepository.findById(meetingAttendanceDto.getAssociatedId()).orElseThrow());
            AttendanceMeetingEntity actual = attendanceMeetingRepository.save(attendanceMeeting);
            response.setId(actual.getId());
            response.setStatus(HttpStatus.CREATED);
            response.setDescription("asistencia anotada correctamente");
        } catch (Exception exception) {
            Map<HttpStatus, String> errors = new HashMap<>();
            errors.put(HttpStatus.BAD_GATEWAY, exception.getMessage());
            response.setErrors(errors);
        }
        return response;
    }
}

