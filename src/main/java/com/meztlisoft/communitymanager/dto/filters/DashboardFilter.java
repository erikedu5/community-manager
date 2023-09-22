package com.meztlisoft.communitymanager.dto.filters;

import lombok.Data;

@Data
public class DashboardFilter {

    private String citizenName;
    private Long citizenId;
    public int page;
    public int size;
}
