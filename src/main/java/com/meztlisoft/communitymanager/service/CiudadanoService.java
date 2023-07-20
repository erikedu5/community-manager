package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.CiudadanoDto;
import com.meztlisoft.communitymanager.dto.filters.CiudadanoFilters;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CiudadanoService {

    CiudadanoDto create(CiudadanoDto ciudadano, String token);

    Page<CiudadanoDto> getAll(CiudadanoFilters ciudadanoFilters);
}
