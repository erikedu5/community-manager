package com.meztlisoft.communitymanager.dto.filters;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EntryFilters {

    public String concept;
    public LocalDate date;
    public int page;
    public int size;
}
