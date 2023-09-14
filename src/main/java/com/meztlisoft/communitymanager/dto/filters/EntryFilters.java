package com.meztlisoft.communitymanager.dto.filters;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EntryFilters {

    public String concept;
    public LocalDate dateTo;
    public LocalDate dateFrom;
    public int page;
    public int size;
}
