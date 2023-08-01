package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AssociationsDto;
import com.meztlisoft.communitymanager.dto.CitizenDto;
import com.meztlisoft.communitymanager.dto.filters.AssociatedFilters;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AssociationService {
    Page<CitizenDto> getCitizenAssociated(long id, AssociatedFilters associatedFilters);

    ActionStatusResponse create(long id, List<AssociationsDto> associationsDto, String token);
}
