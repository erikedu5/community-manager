package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.CooperationDto;
import com.meztlisoft.communitymanager.dto.filters.CooperationFilters;
import org.springframework.data.domain.Page;

public interface CooperationService {
    CooperationDto create(CooperationDto cooperationDto, String token, Long retinueId);

    Page<CooperationDto> getAll(CooperationFilters cooperationFilters, Long retinueId);

    CooperationDto getById(long id);
}
