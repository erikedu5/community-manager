package com.meztlisoft.communitymanager.dto;

import lombok.Data;

@Data
public class MeetingAttendanceDto {

    private Boolean check;
    private Long associatedId;
    private String citizenName;
}
