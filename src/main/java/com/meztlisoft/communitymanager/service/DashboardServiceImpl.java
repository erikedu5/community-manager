package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.DashboardDto;
import com.meztlisoft.communitymanager.dto.filters.DashboardFilter;
import com.meztlisoft.communitymanager.entity.CitizenEntity;
import com.meztlisoft.communitymanager.entity.PaymentEntity;
import com.meztlisoft.communitymanager.repository.CitizenRepository;
import com.meztlisoft.communitymanager.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class DashboardServiceImpl implements DashboardService {

    private final CitizenRepository citizenRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Page<DashboardDto> findDashboard(DashboardFilter dashboardFilter) {
        List<CitizenEntity> citizenEntities = new ArrayList<>();
        if (Objects.nonNull(dashboardFilter.getCitizenId())) {
            CitizenEntity citizen = new CitizenEntity();
            citizen.setId(dashboardFilter.getCitizenId());
            citizenEntities.add(citizen);
        }
        if (Objects.nonNull(dashboardFilter.getCitizenName())) {
            citizenEntities.addAll(citizenRepository.findLikeName( "%" + dashboardFilter.getCitizenName().toLowerCase() + "%"));
        }
        List<DashboardDto> dashboardDtos = new ArrayList<>();
        Pageable paging = PageRequest.of(dashboardFilter.getPage(), dashboardFilter.getSize());
        Page<PaymentEntity> paymentEntitiesPage;
        if (!citizenEntities.isEmpty()) {
            List<Long> citizenIds = new ArrayList<>();
            for (CitizenEntity citizen: citizenEntities) {
                citizenIds.add(citizen.getId());
            }
            paymentEntitiesPage = paymentRepository.findAllByCitizenId(citizenIds, paging);

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
