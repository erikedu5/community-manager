package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AdministradorDto;
import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import com.meztlisoft.communitymanager.service.AdministradorService;
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
@RequestMapping("/administrador")
@RequiredArgsConstructor
public class AdministradorController {

    private final AuthenticationService authenticationService;
    private final AdministradorService administradorService;

    @PostMapping
    public ResponseEntity<AdministradorDto> create(@RequestBody AdministradorDto request,
                                               @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(authenticationService.create(request, token));
    }

    @PostMapping("/all")
    public ResponseEntity<Page<AdministradorDto>> getAll(@RequestBody AdminFilters adminFilters) {
        return ResponseEntity.ok(administradorService.getAll(adminFilters));
    }


    @GetMapping("/{id}")
    public ResponseEntity<AdministradorDto> getById(@PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(administradorService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> update(@PathVariable(name = "id") final long id,
                                                       @RequestBody AdministradorDto administradorDto,
                                                       @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(authenticationService.update(id, administradorDto, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> delete(@PathVariable(name = "id") final long id,
                                                       @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(authenticationService.delete(id, token));
    }
}
