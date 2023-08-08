package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AddPaymentDto;
import com.meztlisoft.communitymanager.dto.PaymentDto;
import com.meztlisoft.communitymanager.dto.filters.PaymentFilters;
import com.meztlisoft.communitymanager.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
