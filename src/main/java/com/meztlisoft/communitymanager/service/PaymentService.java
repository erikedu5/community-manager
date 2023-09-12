package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AddPaymentDto;
import com.meztlisoft.communitymanager.dto.PaymentDto;
import com.meztlisoft.communitymanager.dto.filters.PaymentFilters;
import com.meztlisoft.communitymanager.entity.CooperationEntity;
import org.springframework.data.domain.Page;

public interface PaymentService {
    void createInitialPaymentsAssociated(CooperationEntity cooperation);

    Page<PaymentDto> getPaymentInfo(long cooperationId, PaymentFilters paymentFilters);

    ActionStatusResponse addPayment(AddPaymentDto addPaymentDto, String token);

    ActionStatusResponse makeVolunteer(AddPaymentDto addPaymentDto);

    Long getSummaryByCooperationId(long id);
}
