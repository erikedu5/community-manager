package com.meztlisoft.communitymanager.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BillDto {

    private Long id;
    private String concept;
    private Long cost;
    private LocalDate date;
    private String evidencePack;
}
