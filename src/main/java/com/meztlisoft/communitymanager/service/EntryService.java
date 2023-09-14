package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.EntryDto;
import com.meztlisoft.communitymanager.dto.SummaryDto;
import com.meztlisoft.communitymanager.dto.filters.EntryFilters;
import org.springframework.data.domain.Page;

public interface EntryService {
    Page<EntryDto> findAll(EntryFilters entryFilters, Long retinueId);

    EntryDto save(EntryDto entryDto, String token, Long retinueId);

    SummaryDto getSummary(EntryFilters entryFilters, Long retinueId);
}
