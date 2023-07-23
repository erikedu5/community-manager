package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.AdministratorDto;
import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdministratorService {

    UserDetailsService userDetailsService();

    Page<AdministratorDto> getAll(AdminFilters adminFilters);

    AdministratorDto getById(long id);

}
