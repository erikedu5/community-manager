package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.RetinueDto;
import com.meztlisoft.communitymanager.dto.filters.RetinueFilters;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RetinueService {

    RetinueDto create(RetinueDto retinueDto, String token);

    Page<RetinueDto> getAll(RetinueFilters retinueFilters);

    ActionStatusResponse update(long id, RetinueDto retinueDto, String token);

    ActionStatusResponse delete(long id, String token);

    RetinueDto getById(long id);

    List<RetinueDto> getCatalog();
}
