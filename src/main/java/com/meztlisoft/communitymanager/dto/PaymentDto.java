package com.meztlisoft.communitymanager.dto;

import lombok.Data;

@Data
public class PaymentDto {

    private String citizenName;
    private Long citizenId;
    private String citizenCurp;
    private String citizenDescription;
    private Long associatedId;
    private Long payment;
    private boolean complete;
    private boolean volunteer;
    private Long benefit;
    private boolean isNative;
    private Long total;

}
