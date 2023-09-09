package com.meztlisoft.communitymanager.dto.filters;

import lombok.Data;

@Data
public class PaymentFilters {

    private int size;
    private int page;
    private String citizenName;
    private String citizenDescription;
    private Long credit;
    private Boolean complete = null;
    private Boolean volunteer = null;

}
