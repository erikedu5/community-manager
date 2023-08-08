package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.*;
import com.meztlisoft.communitymanager.dto.filters.AssociatedFilters;
import java.util.List;
import org.springframework.data.domain.Page;

public interface AssociationService {
    Page<AssociatedDto> getCitizenAssociated(long idRetinue, AssociatedFilters associatedFilters);

    ActionStatusResponse create(long id, List<AssociationsDto> associationsDto, String token);

    ActionStatusResponse addCitizenToAssociation(long idCitizen, List<AssociationsDto> associationsDto, String token);

    Page<AssociatedDto> getRetinueAssociatedByCitizenId(long idCitizen, AssociatedFilters associatedFilters);

}
