package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AdministratorDto;
import com.meztlisoft.communitymanager.dto.AdministratorResponse;
import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import org.springframework.data.domain.Page;

public interface AdministratorService {

    AdministratorResponse create(AdministratorDto request, String token);

    Page<AdministratorResponse> getAll(AdminFilters adminFilters);

    AdministratorResponse getById(long id);

    ActionStatusResponse update(long id, AdministratorDto administratorDto, String token);

    ActionStatusResponse delete(long id, String token);
}
