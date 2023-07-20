package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.AdministradorDto;
import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdministradorService {

    UserDetailsService userDetailsService();

    Page<AdministradorDto> getAll(AdminFilters adminFilters);
}
