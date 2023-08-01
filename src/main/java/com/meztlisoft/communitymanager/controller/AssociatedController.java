package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AssociationsDto;
import com.meztlisoft.communitymanager.dto.CitizenDto;
import com.meztlisoft.communitymanager.dto.filters.AssociatedFilters;
import com.meztlisoft.communitymanager.service.AssociationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/associated")
@RequiredArgsConstructor
public class AssociatedController {

    private final AssociationService associationService;

    @PostMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> generateAssociations(@PathVariable(name = "id") final long id,
                                                                     @RequestBody List<AssociationsDto> associationsDto,
                                                                     @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(associationService.create(id, associationsDto, token));
    }

    @PostMapping("/all/{id}")
    public ResponseEntity<Page<CitizenDto>> getAssociatedCitizen(@PathVariable(name = "id") final long id,
                                                                 @RequestBody AssociatedFilters associatedFilters) {
        return ResponseEntity.ok(associationService.getCitizenAssociated(id, associatedFilters));
    }
}
