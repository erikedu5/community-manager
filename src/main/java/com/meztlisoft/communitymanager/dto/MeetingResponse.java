package com.meztlisoft.communitymanager.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class MeetingResponse {

    private String concept;
    private LocalDateTime meetingDate;
    private Long id;
    private List<MeetingAttendanceDto> attendance;

}
