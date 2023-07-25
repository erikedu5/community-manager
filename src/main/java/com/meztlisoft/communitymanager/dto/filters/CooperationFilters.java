package com.meztlisoft.communitymanager.dto.filters;

import java.util.Date;
import lombok.Data;

@Data
public class CooperationFilters {

    private Long retinueId;
    private String concept;
    private Date limitDate;
    private Date startDate;
    private int page;
    private int size;
}
