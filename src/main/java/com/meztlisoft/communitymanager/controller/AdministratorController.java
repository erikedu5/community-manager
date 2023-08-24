package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AdministratorDto;
import com.meztlisoft.communitymanager.dto.AdministratorResponse;
import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import com.meztlisoft.communitymanager.service.AdministratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/administrator")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorService administratorService;

    @PostMapping
    public ResponseEntity<AdministratorResponse> create(@RequestBody AdministratorDto request,
                                                        @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(administratorService.create(request, token));
    }

    @PostMapping("/all")
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<Page<AdministratorResponse>> getAll(@RequestBody AdminFilters adminFilters) {
        return ResponseEntity.ok(administratorService.getAll(adminFilters));
    }


    @GetMapping("/{id}")
    public ResponseEntity<AdministratorResponse> getById(@PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(administratorService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> update(@PathVariable(name = "id") final long id,
                                                       @RequestBody AdministratorDto administratorDto,
                                                       @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(administratorService.update(id, administratorDto, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> delete(@PathVariable(name = "id") final long id,
                                                       @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(administratorService.delete(id, token));
    }
}
