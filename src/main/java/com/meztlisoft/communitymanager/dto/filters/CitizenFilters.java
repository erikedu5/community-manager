package com.meztlisoft.communitymanager.dto.filters;

import com.meztlisoft.communitymanager.dto.RangeDates;
import lombok.Data;

@Data
public class CitizenFilters {

    private String name;
    private String description;
    private String curp;
    private String address;
    private String userName;
    private RangeDates rangeBirthdays;
    private int page;
    private int size;
    private boolean onlyAdmins;

    public CitizenFilters() {
        this.onlyAdmins = false;
    }
}
