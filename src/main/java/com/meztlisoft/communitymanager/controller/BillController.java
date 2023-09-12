package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.BillDto;
import com.meztlisoft.communitymanager.dto.SummaryDto;
import com.meztlisoft.communitymanager.dto.filters.BillFilters;
import com.meztlisoft.communitymanager.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping("/all")
    public ResponseEntity<Page<BillDto>> getAll(@RequestBody BillFilters billFilters,
                                                @RequestHeader(name = "retinueId") Long retinueId) {
        return ResponseEntity.ok(billService.getAll(billFilters, retinueId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadEvidence(@PathVariable(name = "id") final long id) {
        Resource file = billService.downloadEvidence(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryDto> getSummary(@RequestHeader(name = "retinueId") Long retinueId) {
        return ResponseEntity.ok(billService.getSummary(retinueId));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> uploadEvidence(@PathVariable(name = "id") final long id,
                                                               @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(billService.uploadFile(file, id));
    }

    @PostMapping
    public ResponseEntity<BillDto> createBill(@RequestBody BillDto billDto,
                                        @RequestHeader(name = "retinueId") Long retinueId,
                                              @RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(billService.createBill(retinueId, billDto, token));
    }
}
