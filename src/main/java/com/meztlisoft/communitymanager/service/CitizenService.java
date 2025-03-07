package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.CitizenDto;
import com.meztlisoft.communitymanager.dto.filters.CitizenFilters;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CitizenService {

    CitizenDto create(CitizenDto citizen, String token);

    Page<CitizenDto> getAll(CitizenFilters citizenFilters, Long retinueId);

    CitizenDto getById(long id);

    ActionStatusResponse update(long id, CitizenDto citizenDto, String token);

    ActionStatusResponse delete(long id, String token);

    List<CitizenDto> getCatalog();
}
