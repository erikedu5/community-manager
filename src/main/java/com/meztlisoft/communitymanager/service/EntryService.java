package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.EntryDto;
import com.meztlisoft.communitymanager.dto.filters.EntryFilters;
import org.springframework.data.domain.Page;

public interface EntryService {
    Page<EntryDto> findAll(EntryFilters entryFilters);

    EntryDto save(EntryDto entryDto, String token, Long retinueId);
}
