package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AddPaymentDto;
import com.meztlisoft.communitymanager.dto.PaymentDto;
import com.meztlisoft.communitymanager.dto.filters.PaymentFilters;
import com.meztlisoft.communitymanager.entity.AdministratorEntity;
import com.meztlisoft.communitymanager.entity.AssociatedEntity;
import com.meztlisoft.communitymanager.entity.CooperationEntity;
import com.meztlisoft.communitymanager.entity.CreditEntity;
import com.meztlisoft.communitymanager.entity.PaymentEntity;
import com.meztlisoft.communitymanager.entity.specification.PaymentSpecification;
import com.meztlisoft.communitymanager.repository.AdministratorRepository;
import com.meztlisoft.communitymanager.repository.AssociationRepository;
import com.meztlisoft.communitymanager.repository.CooperationRepository;
import com.meztlisoft.communitymanager.repository.CreditRepository;
import com.meztlisoft.communitymanager.repository.PaymentRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AssociationRepository associationRepository;
    private final AdministratorRepository administratorRepository;
    private final CreditRepository creditRepository;
    private final JwtService jwtService;
    private final CooperationRepository cooperationRepository;

    @Override
    public Page<PaymentDto> getPaymentInfo(long cooperationId, PaymentFilters paymentFilters) {
        this.verifyAssociated(cooperationRepository.findById(cooperationId).orElseThrow());
        Pageable paging = PageRequest.of(paymentFilters.getPage(), paymentFilters.getSize());
        Specification<PaymentEntity> paymentSpecification = PaymentSpecification.getFilteredPayment(paymentFilters, cooperationId);
        Page<PaymentEntity> payments = paymentRepository.findAll(paymentSpecification, paging);
        List<PaymentDto> paymentDtos = new ArrayList<>();
        payments.forEach(payment -> {
            PaymentDto dto = new PaymentDto();
            dto.setCitizenName(payment.getAssociated().getCitizen().getName());
            dto.setCitizenId(payment.getAssociated().getCitizen().getId());
            dto.setCitizenDescription(payment.getAssociated().getCitizen().getDescription());
            dto.setAssociatedId(payment.getAssociated().getId());
            dto.setPayment(payment.getPayment());
            dto.setVolunteer(payment.isVolunteer());
            dto.setComplete(payment.isComplete());
            paymentDtos.add(dto);
        });
        return new PageImpl<>(paymentDtos, payments.getPageable(), payments.getTotalElements());

    }

    @Override
    public void createInitialPaymentsAssociated(CooperationEntity cooperation) {
        List<AssociatedEntity> associatedEntities = associationRepository.findAllByRetinueId(cooperation.getRetinue().getId());
        this.saveAssociateds(associatedEntities, cooperation);
    }

    public void verifyAssociated(CooperationEntity cooperation) {
        List<PaymentEntity> paymentEntities = paymentRepository.findAllByCooperationId(cooperation.getId());
        List<Long> ids = new ArrayList<>();
        paymentEntities.forEach(payment -> ids.add(payment.getAssociated().getId()));
        List<AssociatedEntity> associatedEntities = associationRepository.findNotIn(ids, cooperation.getRetinue().getId());
        this.saveAssociateds(associatedEntities, cooperation);
    }

    private void saveAssociateds(List<AssociatedEntity> associatedEntities, CooperationEntity cooperation) {
        List<PaymentEntity> payments = new ArrayList<>();
        associatedEntities.forEach(associated -> {
            PaymentEntity payment = new PaymentEntity();
            payment.setPayment(0L);
            payment.setAssociated(associated);
            payment.setCooperation(cooperation);
            payment.setPaymentDate(LocalDateTime.now());
            payments.add(payment);
        });
        paymentRepository.saveAll(payments);
    }

    @Override
    public ActionStatusResponse addPayment(AddPaymentDto addPaymentDto, String token) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        actionStatusResponse.setId(addPaymentDto.getCooperationId());
        try {
            Claims claims = jwtService.decodeToken(token);
            long citizenId = Long.parseLong(claims.get("ciudadano_id").toString());
            AdministratorEntity admin = administratorRepository.findByCitizenIdAndActive(citizenId, true);
            CooperationEntity cooperation = cooperationRepository.findById(addPaymentDto.getCooperationId()).orElseThrow();

            Long cooperationBase = cooperation.getBaseCooperation();
            if (!admin.getCitizen().isNative()) {
                cooperationBase = cooperation.getNotNativeCooperation();
            }

            PaymentEntity payment = paymentRepository
                    .findByCooperationIdAndAssociatedId(addPaymentDto.getCooperationId(),
                        addPaymentDto.getAssociatedId()).orElse(new PaymentEntity());

            if (Objects.isNull(payment.getCooperation())) {
                payment.setCooperation(cooperation);
            }

            if (Objects.isNull(payment.getAssociated())) {
                payment.setAssociated(associationRepository.findById(addPaymentDto.getAssociatedId()).orElseThrow());
            }

            Long paymentValue = Objects.nonNull(payment.getPayment())? payment.getPayment(): 0;
            payment.setPayment(paymentValue + addPaymentDto.getPayment());

            if (payment.getPayment() >= cooperationBase) {
                payment.setPaymentDate(LocalDateTime.now());
                payment.setComplete(true);
            }
            paymentRepository.save(payment);

            CreditEntity creditEntity = new CreditEntity();
            creditEntity.setCredit(addPaymentDto.getPayment());
            creditEntity.setPayment(payment);
            creditEntity.setDatePayment(LocalDateTime.now());
            creditEntity.setAdministrator(admin);
            creditRepository.save(creditEntity);

            actionStatusResponse.setDescription("Pago completo");
            actionStatusResponse.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            actionStatusResponse.setDescription("Error en el pago ");
            actionStatusResponse.setStatus(HttpStatus.NOT_FOUND);
            Map<HttpStatus, String> errors = new HashMap<>();
            errors.put(HttpStatus.NOT_FOUND, e.getMessage());
            actionStatusResponse.setErrors(errors);
        }
        return actionStatusResponse;
    }

    @Override
    public ActionStatusResponse makeVolunteer(AddPaymentDto addPaymentDto) {
        ActionStatusResponse actionStatusResponse = new ActionStatusResponse();
        actionStatusResponse.setId(addPaymentDto.getAssociatedId());
        try {
            PaymentEntity payment = paymentRepository
                    .findByCooperationIdAndAssociatedId(addPaymentDto.getCooperationId(),
                            addPaymentDto.getAssociatedId()).orElse(new PaymentEntity());

            payment.setVolunteer(true);
            payment.setComplete(true);

            if (Objects.isNull(payment.getAssociated())) {
                payment.setAssociated(associationRepository.findById(addPaymentDto.getAssociatedId()).orElseThrow());
            }

            if (Objects.isNull(payment.getCooperation())) {
                payment.setCooperation(cooperationRepository.findById(addPaymentDto.getCooperationId()).orElseThrow());
            }

            paymentRepository.save(payment);

            actionStatusResponse.setDescription("Actualización completa");
            actionStatusResponse.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            actionStatusResponse.setDescription("Error al hacer voluntario al ciudadano");
            actionStatusResponse.setStatus(HttpStatus.NOT_FOUND);
            Map<HttpStatus, String> errors = new HashMap<>();
            errors.put(HttpStatus.NOT_FOUND, e.getMessage());
            actionStatusResponse.setErrors(errors);
        }
        return actionStatusResponse;
    }
}
