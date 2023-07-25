package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.CooperationDto;
import com.meztlisoft.communitymanager.dto.filters.CooperationFilters;
import org.springframework.data.domain.Page;

public interface CooperationService {
    CooperationDto create(CooperationDto cooperationDto, String token);

    Page<CooperationDto> getAll(CooperationFilters cooperationFilters);

    CooperationDto getById(long id);

    ActionStatusResponse update(long id, CooperationDto cooperationDto, String token);

    ActionStatusResponse delete(long id, String token);
}
