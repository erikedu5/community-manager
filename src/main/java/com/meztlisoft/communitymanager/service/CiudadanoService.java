package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.CiudadanoDto;
import com.meztlisoft.communitymanager.dto.filters.CiudadanoFilters;
import org.springframework.data.domain.Page;

public interface CiudadanoService {

    CiudadanoDto create(CiudadanoDto ciudadano, String token);

    Page<CiudadanoDto> getAll(CiudadanoFilters ciudadanoFilters);

    CiudadanoDto getById(long id);

    ActionStatusResponse update(long id, CiudadanoDto ciudadanoDto, String token);

    ActionStatusResponse delete(long id, String token);
}
