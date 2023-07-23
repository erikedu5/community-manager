package com.meztlisoft.communitymanager.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RangeDates {
    private LocalDate upperLimit;
    private LocalDate lowerLimit;
}
