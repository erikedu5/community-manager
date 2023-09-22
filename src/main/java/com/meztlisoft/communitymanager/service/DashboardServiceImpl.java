package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.DashboardDto;
import com.meztlisoft.communitymanager.dto.filters.DashboardFilter;
import com.meztlisoft.communitymanager.entity.CitizenEntity;
import com.meztlisoft.communitymanager.entity.PaymentEntity;
import com.meztlisoft.communitymanager.repository.CitizenRepository;
import com.meztlisoft.communitymanager.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CitizenRepository citizenRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Page<DashboardDto> findDashboard(DashboardFilter dashboardFilter) {
        CitizenEntity citizen = new CitizenEntity();
        citizen.setId(dashboardFilter.getCitizenId());
        if (Objects.nonNull(dashboardFilter.getCitizenName())) {
            citizen = citizenRepository.findLikeName( "%" + dashboardFilter.getCitizenName().toLowerCase() + "%").orElse(new CitizenEntity());
        }
        List<DashboardDto> dashboardDtos = new ArrayList<>();
        Pageable paging = PageRequest.of(dashboardFilter.getPage(), dashboardFilter.getSize());
        Page<PaymentEntity> paymentEntitiesPage;
        if (Objects.nonNull(citizen.getId())) {
            paymentEntitiesPage = paymentRepository.findAllByCitizenId(citizen.getId(), paging);
        } else {
            paymentEntitiesPage = paymentRepository.findAll(paging);
        }

        paymentEntitiesPage.forEach(payment -> {
            DashboardDto dto = new DashboardDto();
            dto.setPayment(payment.getPayment());
            dto.setCitizenId(payment.getAssociated().getCitizen().getId());
            dto.setCitizenName(payment.getAssociated().getCitizen().getName());
            dto.setCompleteCooperation(payment.isComplete());
            dto.setCooperationConcept(payment.getCooperation().getConcept());
            dto.setCooperationId(payment.getCooperation().getId());
            dto.setRetinueName(payment.getCooperation().getRetinue().getName());
            dto.setPaymentId(payment.getId());
            dto.setStartDateCooperation(payment.getCooperation().getStartDate());
            dashboardDtos.add(dto);
        });

        return new PageImpl<>(dashboardDtos, paymentEntitiesPage.getPageable(), paymentEntitiesPage.getTotalElements());
    }
}
