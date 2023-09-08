package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.*;
import com.meztlisoft.communitymanager.dto.filters.AssociatedFilters;
import com.meztlisoft.communitymanager.dto.filters.RetinueFilters;
import com.meztlisoft.communitymanager.service.AssociationService;
import com.meztlisoft.communitymanager.service.RetinueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/retinue")
@RequiredArgsConstructor
public class RetinueController {

    private final RetinueService retinueService;

    private final AssociationService associationService;

    @PostMapping
    public ResponseEntity<RetinueDto> createComitiva(@RequestBody RetinueDto retinueDto,
                                                     @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(retinueService.create(retinueDto, token));
    }

    @PostMapping("/all")
    public ResponseEntity<Page<RetinueDto>> getAll(@RequestBody RetinueFilters retinueFilters) {
        return ResponseEntity.ok(retinueService.getAll(retinueFilters));
    }

    @GetMapping("/catalog")
    public ResponseEntity<List<RetinueDto>> getCatalog() {
        return ResponseEntity.ok(retinueService.getCatalog());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RetinueDto> getById(@PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(retinueService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> update(@RequestBody RetinueDto retinueDto,
                                                       @RequestHeader(value = "Authorization") final String token,
                                                       @PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(retinueService.update(id, retinueDto, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> delete(@RequestHeader(value = "Authorization") final String token,
                                                       @PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(retinueService.delete(id, token));
    }

    @PostMapping("/associate/{id}")
    public ResponseEntity<ActionStatusResponse> generateAssociations(@PathVariable(name = "id") final long id,
                                                                     @RequestBody AssociationWrapper associationsDto,
                                                                     @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(associationService.create(id, associationsDto.getDetail(), token));
    }

    @PostMapping("/getAssociate/{id}")
    public ResponseEntity<Page<AssociatedDto>> getAssociatedCitizen(@PathVariable(name = "id") final long id,
                                                                    @RequestBody AssociatedFilters associatedFilters) {
        return ResponseEntity.ok(associationService.getCitizenAssociated(id, associatedFilters));
    }

}
