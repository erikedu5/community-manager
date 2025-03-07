package com.meztlisoft.communitymanager.dto;

import java.time.LocalDate;

import lombok.*;

@Data
public class CooperationDto {

    private String retinueName;
    private Long id;
    private String concept;
    private LocalDate limitDate;
    private LocalDate startDate;
    private Long baseCooperation;
    private Long notNativeCooperation;
    private boolean byUnity;
    private boolean elderly;
    private boolean editable;
    private boolean isComplete;
}
