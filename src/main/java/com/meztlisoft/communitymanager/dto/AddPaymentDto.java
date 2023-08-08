package com.meztlisoft.communitymanager.dto;

import lombok.Data;

@Data
public class AddPaymentDto {

    private Long cooperationId;
    private Long AssociatedId;
    private Long payment;
}
