package com.meztlisoft.communitymanager.service;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;
import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.AddPaymentDto;
import com.meztlisoft.communitymanager.dto.DebtorsDto;
import com.meztlisoft.communitymanager.dto.PaymentDto;
import com.meztlisoft.communitymanager.dto.filters.PaymentFilters;
import com.meztlisoft.communitymanager.entity.AdministratorEntity;
import com.meztlisoft.communitymanager.entity.AssociatedEntity;
import com.meztlisoft.communitymanager.entity.CitizenEntity;
import com.meztlisoft.communitymanager.entity.CooperationEntity;
import com.meztlisoft.communitymanager.entity.CreditEntity;
import com.meztlisoft.communitymanager.entity.PaymentEntity;
import com.meztlisoft.communitymanager.entity.specification.PaymentSpecification;
import com.meztlisoft.communitymanager.repository.AdministratorRepository;
import com.meztlisoft.communitymanager.repository.AssociationRepository;
import com.meztlisoft.communitymanager.repository.CitizenRepository;
import com.meztlisoft.communitymanager.repository.CooperationRepository;
import com.meztlisoft.communitymanager.repository.CreditRepository;
import com.meztlisoft.communitymanager.repository.PaymentRepository;
import io.jsonwebtoken.Claims;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AssociationRepository associationRepository;
    private final AdministratorRepository administratorRepository;
    private final CreditRepository creditRepository;
    private final JwtService jwtService;
    private final CooperationRepository cooperationRepository;
    private final CitizenRepository citizenRepository;

    @Value("${files.path.community}")
    private String pathCommunity;

    @Override
    public Page<PaymentDto> getPaymentInfo(long cooperationId, PaymentFilters paymentFilters) {
        this.verifyAssociated(cooperationRepository.findById(cooperationId).orElseThrow());
        Pageable paging = PageRequest.of(paymentFilters.getPage(), paymentFilters.getSize(), Sort.by("associated.citizen.curp"));
        Specification<PaymentEntity> paymentSpecification = PaymentSpecification.getFilteredPayment(paymentFilters, cooperationId);
        Page<PaymentEntity> payments = paymentRepository.findAll(paymentSpecification, paging);
        List<PaymentDto> paymentDtos = new ArrayList<>();
        payments.forEach(payment -> {
            PaymentDto dto = new PaymentDto();
            dto.setCitizenName(payment.getAssociated().getCitizen().getName());
            dto.setCitizenId(payment.getAssociated().getCitizen().getId());
            dto.setNative(payment.getAssociated().getCitizen().isNative());
            dto.setCitizenDescription(payment.getAssociated().getCitizen().getDescription());
            dto.setCitizenCurp(payment.getAssociated().getCitizen().getCurp());
            dto.setBenefit(payment.getAssociated().getBenefit());
            dto.setAssociatedId(payment.getAssociated().getId());
            dto.setPayment(payment.getPayment());
            Long total;
            if (payment.getAssociated().getCitizen().isNative()) {
                total = payment.getCooperation().getBaseCooperation();
            } else {
                total = payment.getCooperation().getNotNativeCooperation();
            }
            if (payment.getCooperation().isByUnity()) {
                total = total * payment.getAssociated().getBenefit();
            }
            dto.setTotal(total);
            dto.setVolunteer(payment.isVolunteer());
            dto.setComplete(payment.isComplete());
            paymentDtos.add(dto);
        });
        return new PageImpl<>(paymentDtos, payments.getPageable(), payments.getTotalElements());

    }

    @Override
    public void verifyAssociated(CooperationEntity cooperation) {
        List<PaymentEntity> paymentEntities = paymentRepository.findAllByCooperationId(cooperation.getId());
        List<Long> ids = new ArrayList<>();
        paymentEntities.forEach(payment -> ids.add(payment.getAssociated().getId()));
        List<AssociatedEntity> associatedEntities;
        if (ids.isEmpty()) {
            associatedEntities = associationRepository.findAllByRetinueIdAAndActiveTrue(cooperation.getRetinue().getId());
        } else {
            associatedEntities = associationRepository.findNotIn(ids, cooperation.getRetinue().getId());
        }
        this.saveAssociateds(associatedEntities, cooperation);
    }

    private void saveAssociateds(List<AssociatedEntity> associatedEntities, CooperationEntity cooperation) {
        List<PaymentEntity> payments = new ArrayList<>();
        associatedEntities.forEach(associated -> {
            Period period = associated.getCitizen().getBirthday().until(LocalDate.now());
            if (period.getYears() >= 18 || associated.getCitizen().isMarried()) {
                PaymentEntity payment = paymentRepository
                        .findByCitizenIdAAndCooperationId(associated.getCitizen().getId(), cooperation.getId())
                        .orElse(new PaymentEntity());
                payment.setPayment(Objects.isNull(payment.getPayment()) ? 0L: payment.getPayment());
                payment.setAssociated(associated);
                payment.setCooperation(cooperation);
                payment.setPaymentDate(LocalDateTime.now());
                if (Objects.isNull(payment.getId())) {
                    payments.add(payment);
                } else {
                    paymentRepository.save(payment);
                }
            }
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
            CooperationEntity cooperation = cooperationRepository.findById(addPaymentDto.getCooperationId()).orElseThrow();
            AdministratorEntity admin;
            if (citizenId != 0) {
                admin = administratorRepository.findRoleByCitizenIdAndRetinueId(citizenId, true, cooperation.getRetinue().getId()).orElseThrow();
            } else {
                admin = administratorRepository.findById(citizenId).orElseThrow();
            }
            AssociatedEntity association = associationRepository.findById(addPaymentDto.getAssociatedId()).orElseThrow();

            Long cooperationBase = cooperation.getBaseCooperation();
            if (!association.getCitizen().isNative()) {
                cooperationBase = cooperation.getNotNativeCooperation();
            }

            if (cooperation.isByUnity()) {
                cooperationBase = cooperationBase * association.getBenefit();
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

    @Override
    public Long getSummaryByCooperationId(long id) {
        return paymentRepository.getSummaryByCooperationId(id);
    }

    @Override
    public File createReceipt(Long associatedId, Long cooperationId, String token) {
        try {
            PaymentEntity payment = paymentRepository.findByAssociatedIdAndCooperationId(associatedId, cooperationId).orElseThrow();
            Claims claims = jwtService.decodeToken(token);
            long citizenId = Long.parseLong(claims.get("ciudadano_id").toString());
            CitizenEntity citizenAdmin = citizenRepository.findById(citizenId).orElseThrow();

            Map<String, Object> empParams = new HashMap<>();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            empParams.put("date", payment.getPaymentDate().toLocalDate().format(formatters));
            empParams.put("retinueName", payment.getCooperation().getRetinue().getName());
            empParams.put("citizenName", payment.getAssociated().getCitizen().getName());
            ULocale locale = new ULocale("es"); // Cambia "es" al idioma deseado
            RuleBasedNumberFormat formatter = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
            Long cost = payment.getPayment();
            empParams.put("cooperationLetter", formatter.format(cost) + " pesos 00/100 M.N.");
            empParams.put("cooperationCost", cost);
                    empParams.put("cooperationConcept", payment.getCooperation().getConcept());
            empParams.put("id", "PAY-" + payment.getId() + "-COP-" + payment.getCooperation().getId() + "-RET-" + payment.getCooperation().getRetinue().getId());
            empParams.put("adminName", citizenAdmin.getName());

        JasperPrint empReport = JasperFillManager.fillReport(
                JasperCompileManager.compileReport(
                        ResourceUtils.getFile(pathCommunity + "recibo.jrxml").getAbsolutePath()),
                empParams,
                new JREmptyDataSource());

            File receipt = new File("recibo.pdf");
            FileUtils.writeByteArrayToFile(receipt, JasperExportManager.exportReportToPdf(empReport));
            return receipt;
        } catch (JRException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File generateReport(Long cooperationId) {
        try {
            List<PaymentEntity> payments = paymentRepository.findAllByCooperationIdIncomplete(cooperationId);
            CooperationEntity cooperation = cooperationRepository.findById(cooperationId).orElseThrow();

            Map<String, Object> empParams = new HashMap<>();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            empParams.put("date", LocalDate.now().format(formatters));
            empParams.put("retinueName", cooperation.getRetinue().getName());
            empParams.put("cooperationConcept", cooperation.getConcept());

            JRBeanCollectionDataSource citizens = new JRBeanCollectionDataSource(this.parseDebtors(payments));

            empParams.put("citizens", citizens);

            JasperPrint empReport = JasperFillManager.fillReport(
                    JasperCompileManager.compileReport(
                            ResourceUtils.getFile(pathCommunity + "deudores.jrxml").getAbsolutePath()),
                    empParams,
                    new JREmptyDataSource());

            File receipt = new File("deudores.pdf");
            FileUtils.writeByteArrayToFile(receipt, JasperExportManager.exportReportToPdf(empReport));
            return receipt;
        } catch (JRException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DebtorsDto> parseDebtors(List<PaymentEntity> payments) {
        List<DebtorsDto> debtors = new ArrayList<>();
        payments.forEach(paymentEntity -> {
            DebtorsDto debtor = new DebtorsDto();
            debtor.setName(paymentEntity.getAssociated().getCitizen().getName());
            debtor.setDescription(paymentEntity.getAssociated().getCitizen().getDescription());
            debtor.setBenefit(paymentEntity.getAssociated().getBenefit().toString());
            debtor.setAbonado(paymentEntity.getPayment());
            Long baseCooperation = paymentEntity.getAssociated().getCitizen().isNative() ? paymentEntity.getCooperation().getBaseCooperation(): paymentEntity.getCooperation().getNotNativeCooperation();
            Long accumulateCooperation = baseCooperation * paymentEntity.getAssociated().getBenefit();
            debtor.setTotal(accumulateCooperation);
            debtors.add(debtor);
        });
        return debtors;
    }
}
