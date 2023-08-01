package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.CitizenDto;
import com.meztlisoft.communitymanager.dto.filters.CitizenFilters;
import com.meztlisoft.communitymanager.service.CitizenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/citizen")
@RequiredArgsConstructor
public class CitizenController {

    private final CitizenService citizenService;

    @PostMapping
    public ResponseEntity<CitizenDto> create(@RequestBody CitizenDto citizenDto,
                                             @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(citizenService.create(citizenDto, token));
    }

    @PostMapping("/all")
    public ResponseEntity<Page<CitizenDto>> getAll(@RequestBody CitizenFilters citizenFilters,
                                                   @RequestHeader(value = "retinueId") Long retinueId) {
        return ResponseEntity.ok(citizenService.getAll(citizenFilters, retinueId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitizenDto> getById(@PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(citizenService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> update(@PathVariable(name = "id") final long id,
                                                       @RequestBody CitizenDto citizenDto,
                                                       @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(citizenService.update(id, citizenDto, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> delete(@PathVariable(name = "id") final long id,
                                                       @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(citizenService.delete(id, token));
    }
}
