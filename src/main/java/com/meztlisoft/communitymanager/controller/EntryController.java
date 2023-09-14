package com.meztlisoft.communitymanager.controller;


import com.meztlisoft.communitymanager.dto.EntryDto;
import com.meztlisoft.communitymanager.dto.SummaryDto;
import com.meztlisoft.communitymanager.dto.filters.EntryFilters;
import com.meztlisoft.communitymanager.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entry")
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;

    @PostMapping("/all")
    public ResponseEntity<Page<EntryDto>> getAll(@RequestBody EntryFilters entryFilters,
                                                 @RequestHeader(name = "retinueId") Long retinueId) {
        return ResponseEntity.ok(entryService.findAll(entryFilters, retinueId));
    }

    @PostMapping("/summary")
    public ResponseEntity<SummaryDto> getSummary(@RequestBody EntryFilters entryFilters,
                                             @RequestHeader(name = "retinueId") Long retinueId) {
        return ResponseEntity.ok(entryService.getSummary(entryFilters, retinueId));
    }

    @PostMapping()
    public ResponseEntity<EntryDto> createByCooperation(@RequestBody EntryDto entryDto,
                                                        @RequestHeader(name = "Authorization") String token,
                                                        @RequestHeader(name = "retinueId") Long retinueId) {
        return ResponseEntity.ok(entryService.save(entryDto, token, retinueId));
    }
}
