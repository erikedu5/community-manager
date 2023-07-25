package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AdministratorDto;
import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import org.springframework.data.domain.Page;

public interface AdministratorService {

    AdministratorDto create(AdministratorDto request, String token);

    Page<AdministratorDto> getAll(AdminFilters adminFilters);

    AdministratorDto getById(long id);

    ActionStatusResponse update(long id, AdministratorDto administratorDto, String token);

    ActionStatusResponse delete(long id, String token);
}
