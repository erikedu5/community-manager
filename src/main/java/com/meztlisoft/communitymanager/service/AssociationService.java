package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.*;
import com.meztlisoft.communitymanager.dto.filters.AssociatedFilters;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AssociationService {
    Page<AssociatedDto> getCitizenAssociated(long idRetinue, AssociatedFilters associatedFilters);

    ActionStatusResponse create(long id, List<AssociationsDto> associationsDto, String token);

    ActionStatusResponse addCitizenToAssociation(long idCitizen, List<AssociationsDto> associationsDto, String token);

    Page<AssociatedDto> getRetinueAssociatedByCitizenId(long idCitizen, AssociatedFilters associatedFilters);

}
