package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.CooperationDto;
import com.meztlisoft.communitymanager.dto.SummaryDto;
import com.meztlisoft.communitymanager.dto.filters.CooperationFilters;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CooperationService {
    CooperationDto create(CooperationDto cooperationDto, String token, Long retinueId);

    Page<CooperationDto> getAll(CooperationFilters cooperationFilters, Long retinueId);

    CooperationDto getById(long id);

    List<CooperationDto> getCatalog(Long retinueId, boolean inRange);

    SummaryDto calculateSummaryById(long id);
}
