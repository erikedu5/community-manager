package com.meztlisoft.communitymanager.dto.filters;

import com.meztlisoft.communitymanager.dto.RangeDates;
import lombok.Data;

@Data
public class CitizenFilters {

    private String name;
    private String description;
    private String curp;
    private String address;
    private RangeDates rangeBirthdays;
    private int page;
    private int size;

}
