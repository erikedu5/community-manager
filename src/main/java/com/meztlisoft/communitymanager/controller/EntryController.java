package com.meztlisoft.communitymanager.controller;


import com.meztlisoft.communitymanager.dto.EntryDto;
import com.meztlisoft.communitymanager.dto.SummaryDto;
import com.meztlisoft.communitymanager.dto.filters.DatesForReportFilter;
import com.meztlisoft.communitymanager.dto.filters.EntryFilters;
import com.meztlisoft.communitymanager.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.MalformedURLException;

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


    @PostMapping("/report")
    private ResponseEntity<Resource> getReport(@RequestBody DatesForReportFilter datesForReportFilter,
                                               @RequestHeader(name = "retinueId") Long retinueId)
            throws MalformedURLException {
        File file = entryService.getReport(datesForReportFilter, retinueId);
        Resource resource = new UrlResource(file.toURI());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }
}
