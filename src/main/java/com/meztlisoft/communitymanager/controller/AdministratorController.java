package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AdministratorDto;
import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import com.meztlisoft.communitymanager.service.AdministratorService;
import com.meztlisoft.communitymanager.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
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

    private final AuthenticationService authenticationService;
    private final AdministratorService administratorService;

    @PostMapping
    public ResponseEntity<AdministratorDto> create(@RequestBody AdministratorDto request,
                                                   @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(authenticationService.create(request, token));
    }

    @PostMapping("/all")
    public ResponseEntity<Page<AdministratorDto>> getAll(@RequestBody AdminFilters adminFilters) {
        return ResponseEntity.ok(administratorService.getAll(adminFilters));
    }


    @GetMapping("/{id}")
    public ResponseEntity<AdministratorDto> getById(@PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(administratorService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> update(@PathVariable(name = "id") final long id,
                                                       @RequestBody AdministratorDto administratorDto,
                                                       @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(authenticationService.update(id, administratorDto, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> delete(@PathVariable(name = "id") final long id,
                                                       @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(authenticationService.delete(id, token));
    }
}
