package com.meztlisoft.communitymanager.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MeetingDto {

    private String concept;

    private LocalDateTime dateMeeting;

    private String notes;

}