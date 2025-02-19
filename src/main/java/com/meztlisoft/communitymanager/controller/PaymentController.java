package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AddPaymentDto;
import com.meztlisoft.communitymanager.dto.PaymentDto;
import com.meztlisoft.communitymanager.dto.filters.PaymentFilters;
import com.meztlisoft.communitymanager.service.PaymentService;
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
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{cooperationId}")
    private ResponseEntity<Page<PaymentDto>> getPaymentDto(@PathVariable(name = "cooperationId") final long cooperationId,
                                                           @RequestBody PaymentFilters paymentFilters) {
        return ResponseEntity.ok(paymentService.getPaymentInfo(cooperationId, paymentFilters));
    }

    @PostMapping("/add")
    private ResponseEntity<ActionStatusResponse> addPayment(@RequestBody AddPaymentDto addPaymentDto,
                                                            @RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok(paymentService.addPayment(addPaymentDto, token));
    }

    @PostMapping("/volunteer")
    private ResponseEntity<ActionStatusResponse> makeVolunteer(@RequestBody AddPaymentDto addPaymentDto) {
        return ResponseEntity.ok(paymentService.makeVolunteer(addPaymentDto));
    }
    
    @GetMapping("/receipt/{associatedId}/{cooperationId}")
    private ResponseEntity<Resource> getReceipt(@PathVariable(name = "associatedId") final Long associatedId,
                                                @PathVariable(name = "cooperationId") final Long cooperationId,
                                                @RequestHeader(name = "Authorization") String token) throws MalformedURLException {
        File file = paymentService.createReceipt(associatedId, cooperationId, token);
        Resource resource = new UrlResource(file.toURI());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @GetMapping("/report/{cooperationId}")
    private ResponseEntity<Resource> getReport(@PathVariable(name = "cooperationId") final Long cooperationId) throws MalformedURLException {
        File file = paymentService.generateReport(cooperationId);
        Resource resource = new UrlResource(file.toURI());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @GetMapping("/report/{cooperationId}/all")
    private ResponseEntity<Resource> getReportAll(@PathVariable(name = "cooperationId") final Long cooperationId) throws MalformedURLException {
        File file = paymentService.generateReportCooperations(cooperationId);
        Resource resource = new UrlResource(file.toURI());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }
}
