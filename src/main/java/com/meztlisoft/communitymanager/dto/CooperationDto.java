package com.meztlisoft.communitymanager.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CooperationDto {
    private Long retinueId;
    private Long id;
    private String concept;
    private LocalDate limitDate;
    private LocalDate startDate;
    private Long baseCooperation;
    private Long notNativeCooperation;
}
