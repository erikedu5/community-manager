package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.AdministradorDto;
import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import com.meztlisoft.communitymanager.service.AdministradorService;
import com.meztlisoft.communitymanager.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
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
}
