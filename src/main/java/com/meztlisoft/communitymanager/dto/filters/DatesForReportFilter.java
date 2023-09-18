package com.meztlisoft.communitymanager.dto.filters;

import java.time.LocalDate;
import lombok.Data;

@Data
public class DatesForReportFilter {

    private LocalDate dateTo;
    private LocalDate dateFrom;

}
