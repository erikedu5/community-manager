package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.CiudadanoDto;
import com.meztlisoft.communitymanager.dto.filters.CiudadanoFilters;
import com.meztlisoft.communitymanager.service.CiudadanoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ciudadano")
@RequiredArgsConstructor
public class CiudadanoController {

    private final CiudadanoService ciudadanoService;

    @PostMapping
    public ResponseEntity<CiudadanoDto> create(@RequestBody CiudadanoDto ciudadanoDto,
                                               @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(ciudadanoService.create(ciudadanoDto, token));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<CiudadanoDto>> getAll(@RequestBody CiudadanoFilters ciudadanoFilters) {
        return ResponseEntity.ok(ciudadanoService.getAll(ciudadanoFilters));
    }
}
