package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.ComitivaDto;
import com.meztlisoft.communitymanager.dto.filters.ComitivaFilters;
import com.meztlisoft.communitymanager.service.ComitivaService;
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
@RequestMapping("/comitiva")
@RequiredArgsConstructor
public class ComitivaController {

    private final ComitivaService comitivaService;

    @PostMapping
    public ResponseEntity<ComitivaDto> createComitiva(@RequestBody ComitivaDto comitivaDto,
                                               @RequestHeader(value = "Authorization") final String token) {
        return ResponseEntity.ok(comitivaService.create(comitivaDto, token));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ComitivaDto>> getAll(@RequestBody ComitivaFilters comitivaFilters) {
        return ResponseEntity.ok(comitivaService.getAll(comitivaFilters));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComitivaDto> getById(@PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(comitivaService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> update(@RequestBody ComitivaDto comitivaDto,
                                                       @RequestHeader(value = "Authorization") final String token,
                                                       @PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(comitivaService.update(id, comitivaDto, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> delete(@RequestHeader(value = "Authorization") final String token,
                                                       @PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(comitivaService.delete(id, token));
    }

}
