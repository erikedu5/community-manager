package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.*;
import com.meztlisoft.communitymanager.entity.AssociatedEntity;
import com.meztlisoft.communitymanager.entity.AttendanceMeetingEntity;
import com.meztlisoft.communitymanager.entity.MeetingEntity;
import com.meztlisoft.communitymanager.repository.AssociationRepository;
import com.meztlisoft.communitymanager.repository.AttendanceMeetingRepository;
import com.meztlisoft.communitymanager.repository.MeetingRepository;
import com.meztlisoft.communitymanager.repository.RetinueRepository;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

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
        return convertMeetingToMeetingResponse(meeting);
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
            MeetingEntity meeting = meetingRepository.findById(id).orElseThrow();
            attendanceMeeting.setMeeting(meeting);
            attendanceMeeting.setAttendance(meetingAttendanceDto.getCheck());
            attendanceMeeting.setAssociated(associationRepository.findByRetinueIdAndCitizenIdAndActive(meeting.getRetinue().getId(), meetingAttendanceDto.getAssociatedId()).orElseThrow());
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

    @Override
    public File meetingReport(Long meetingId) {
        try {
            MeetingEntity meeting = meetingRepository.findById(meetingId).orElseThrow();
            List<AssociatedEntity> associated = associationRepository.findAllByRetinueIdAAndActiveTrue(meeting.getRetinue().getId());

            Map<String, Object> empParams = new HashMap<>();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            empParams.put("meetingDate", meeting.getMeetingDate().format(formatters));
            empParams.put("retinueName", meeting.getRetinue().getName());
            empParams.put("meetConcept", meeting.getDescription());

            JRBeanCollectionDataSource attendances = new JRBeanCollectionDataSource(this.generateDto(associated, meeting));

            empParams.put("attendances", attendances);

            JasperPrint empReport = JasperFillManager.fillReport(
                    JasperCompileManager.compileReport(
                            ResourceUtils.getFile("classpath:reports/asistencia.jrxml").getAbsolutePath()),
                    empParams,
                    new JREmptyDataSource());

            File receipt = new File("asistencia.pdf");
            FileUtils.writeByteArrayToFile(receipt, JasperExportManager.exportReportToPdf(empReport));
            return receipt;
        } catch (JRException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MeetingAttendanceReportDto> generateDto(List<AssociatedEntity> associatedEntities, MeetingEntity meeting) {
        List<MeetingAttendanceReportDto> attendanceReportDtos = new ArrayList<>();
        associatedEntities.forEach(associated -> {
            MeetingAttendanceReportDto meetingAttendance = new MeetingAttendanceReportDto();
            meetingAttendance.setName(associated.getCitizen().getName());
            meetingAttendance.setDescription(associated.getCitizen().getDescription());
            Optional<AttendanceMeetingEntity> attendence = attendanceMeetingRepository.findByAssociatedIdAndMeetingId(associated.getId(), meeting.getId());
            if (attendence.isPresent()) {
                meetingAttendance.setCheck(attendence.get().isAttendance()? "Si": "No");
            } else {
                meetingAttendance.setCheck("No");
            }
            meetingAttendance.setId(associated.getCitizen().getId());
            attendanceReportDtos.add(meetingAttendance);
        });
        return attendanceReportDtos;
    }

    @Override
    @Transactional
    public List<MeetingResponse> getAll(Long retinueId) {
        List<MeetingEntity> meets = meetingRepository.findByRetinueId(retinueId);
        List<MeetingResponse> dto = new ArrayList<>();
        meets.forEach(meeting -> dto.add(convertMeetingToMeetingResponse(meeting)));
        return dto;
    }

    private MeetingResponse convertMeetingToMeetingResponse(MeetingEntity meeting) {
        MeetingResponse response = new MeetingResponse();
        response.setConcept(meeting.getDescription());
        response.setMeetingDate(meeting.getMeetingDate());
        response.setId(meeting.getId());
        List<MeetingAttendanceDto> attendanceDtos = new ArrayList<>();
        List<AttendanceMeetingEntity> attendancesMeeting = attendanceMeetingRepository.findByMeetingId(meeting.getId());
        attendancesMeeting.forEach(attendanceMeetingEntity -> {
            MeetingAttendanceDto attendanceMeeting = new MeetingAttendanceDto();
            attendanceMeeting.setAssociatedId(attendanceMeetingEntity.getAssociated().getId());
            attendanceMeeting.setCitizenName(attendanceMeetingEntity.getAssociated().getCitizen().getName());
            attendanceMeeting.setCitizenId(attendanceMeetingEntity.getAssociated().getCitizen().getId());
            attendanceMeeting.setCheck(attendanceMeetingEntity.isAttendance());
            attendanceDtos.add(attendanceMeeting);
        });
        response.setAttendance(attendanceDtos);
        return response;
    }
}

