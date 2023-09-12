package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.CooperationDto;
import com.meztlisoft.communitymanager.dto.SummaryDto;
import com.meztlisoft.communitymanager.dto.filters.CooperationFilters;
import com.meztlisoft.communitymanager.service.CooperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cooperation")
@RequiredArgsConstructor
public class CooperationController {

    private final CooperationService cooperationService;

    @PostMapping
    public ResponseEntity<CooperationDto> create(@RequestBody CooperationDto cooperationDto,
                                                 @RequestHeader(value = "retinueId") final Long retinueId,
                                                 @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(cooperationService.create(cooperationDto, token, retinueId));
    }

    @PostMapping("/all")
    public ResponseEntity<Page<CooperationDto>> getAll(@RequestBody CooperationFilters cooperationFilters,
                                                       @RequestHeader(value = "retinueId") final Long retinueId) {
        return ResponseEntity.ok(cooperationService.getAll(cooperationFilters, retinueId));
    }

    @GetMapping("/catalog")
    public ResponseEntity<List<CooperationDto>> getCatalog(
            @RequestHeader(value = "retinueId") final Long retinueId) {
        return ResponseEntity.ok(cooperationService.getCatalog(retinueId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CooperationDto> getById(@PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(cooperationService.getById(id));
    }

    @GetMapping("/amount/{id}")
    public ResponseEntity<SummaryDto> getSummaryByCooperationId(@PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(cooperationService.calculateSummaryById(id));
    }

}
