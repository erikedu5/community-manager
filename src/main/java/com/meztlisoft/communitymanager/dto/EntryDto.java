package com.meztlisoft.communitymanager.dto;

import lombok.Data;

@Data
public class EntryDto {

    private Long id;
    private String concept;
    private String cost;
    private Boolean isCooperation;
    private Long cooperationId;

}
