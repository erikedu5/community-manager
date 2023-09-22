package com.meztlisoft.communitymanager.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DashboardDto {

    private String citizenName;

    private Long citizenId;

    private String cooperationConcept;

    private Long cooperationId;

    private String retinueName;

    private Long payment;

    private boolean completeCooperation;

    private Long paymentId;

    private LocalDate startDateCooperation;

}
