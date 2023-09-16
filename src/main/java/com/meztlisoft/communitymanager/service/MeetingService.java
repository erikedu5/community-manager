package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.MeetingDto;
import com.meztlisoft.communitymanager.dto.MeetingAttendanceDto;
import com.meztlisoft.communitymanager.dto.MeetingResponse;

import java.io.File;
import java.util.List;

public interface MeetingService {
    MeetingResponse findById(long id);

    ActionStatusResponse save(MeetingDto wrapper, String token, Long retinueId);

    ActionStatusResponse check(Long id, MeetingAttendanceDto meetingAttendanceDto);

    File meetingReport(Long meetingId);

    List<MeetingResponse> getAll(Long retinueId);

}
