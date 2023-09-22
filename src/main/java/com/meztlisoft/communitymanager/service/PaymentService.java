package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AddPaymentDto;
import com.meztlisoft.communitymanager.dto.PaymentDto;
import com.meztlisoft.communitymanager.dto.filters.PaymentFilters;
import com.meztlisoft.communitymanager.entity.CooperationEntity;
import org.springframework.data.domain.Page;
import java.io.File;

public interface PaymentService {

    Page<PaymentDto> getPaymentInfo(long cooperationId, PaymentFilters paymentFilters);

    void verifyAssociated(CooperationEntity cooperation);

    ActionStatusResponse addPayment(AddPaymentDto addPaymentDto, String token);

    ActionStatusResponse makeVolunteer(AddPaymentDto addPaymentDto);

    Long getSummaryByCooperationId(long id);

    File createReceipt(Long associatedId, Long cooperationId, String token) ;

    File generateReport(Long cooperationId);
}
