package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ComitivaDto;
import com.meztlisoft.communitymanager.dto.filters.ComitivaFilters;
import org.springframework.data.domain.Page;

public interface ComitivaService {

    ComitivaDto create(ComitivaDto comitivaDto, String token);

    Page<ComitivaDto> getAll(ComitivaFilters comitivaFilters);
}
