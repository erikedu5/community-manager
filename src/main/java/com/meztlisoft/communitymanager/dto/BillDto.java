package com.meztlisoft.communitymanager.dto;

import lombok.Data;

@Data
public class BillDto {

    private Long id;
    private String concept;
    private Long cost;
    private String evidencePack;
}
