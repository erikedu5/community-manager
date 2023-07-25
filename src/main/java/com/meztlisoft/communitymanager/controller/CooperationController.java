package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.CooperationDto;
import com.meztlisoft.communitymanager.dto.filters.CooperationFilters;
import com.meztlisoft.communitymanager.service.CooperationService;
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

@RestController
@RequestMapping("/cooperation")
@RequiredArgsConstructor
public class CooperationController {

    private final CooperationService cooperationService;

    @PostMapping
    public ResponseEntity<CooperationDto> create(@RequestBody CooperationDto cooperationDto,
                                                     @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(cooperationService.create(cooperationDto, token));
    }

    @PostMapping("/all")
    public ResponseEntity<Page<CooperationDto>> getAll(@RequestBody CooperationFilters cooperationFilters) {
        return ResponseEntity.ok(cooperationService.getAll(cooperationFilters));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CooperationDto> getById(@PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(cooperationService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> update(@RequestBody CooperationDto cooperationDto,
                                                       @RequestHeader(value = "Authorization") final String token,
                                                       @PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(cooperationService.update(id, cooperationDto, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> delete(@RequestHeader(value = "Authorization") final String token,
                                                       @PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(cooperationService.delete(id, token));
    }
}
