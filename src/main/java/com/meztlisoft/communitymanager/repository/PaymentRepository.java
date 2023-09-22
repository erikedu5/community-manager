package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.PaymentEntity;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long>, JpaSpecificationExecutor<PaymentEntity> {

    @Query("FROM PaymentEntity p WHERE p.cooperation.id= :cooperationId")
    List<PaymentEntity> findAllByCooperationId(long cooperationId);

    @Query("FROM PaymentEntity p WHERE p.cooperation.id = :cooperationId and p.associated.id = :associatedId")
    Optional<PaymentEntity> findByCooperationIdAndAssociatedId(Long cooperationId, Long associatedId);

    @Query("SELECT SUM(pe.payment) FROM PaymentEntity pe WHERE pe.cooperation.id = :id")
    Long getSummaryByCooperationId(long id);

    @Query("FROM PaymentEntity pe WHERE pe.associated.id = :associatedId and pe.cooperation.id = :cooperationId")
    Optional<PaymentEntity> findByAssociatedIdAndCooperationId(Long associatedId, Long cooperationId);

    @Query("FROM PaymentEntity p WHERE p.cooperation.id= :cooperationId and p.complete = false ORDER BY p.associated.citizen.name DESC")
    List<PaymentEntity> findAllByCooperationIdIncomplete(Long cooperationId);

    @Query("FROM PaymentEntity p WHERE p.associated.citizen.id =:citizenId")
    Page<PaymentEntity> findAllByCitizenId(Long citizenId, Pageable page);


    @Query("FROM PaymentEntity p WHERE p.associated.citizen.id =:citizenId and p.cooperation.id =:cooperationId")
    Optional<PaymentEntity> findByCitizenIdAAndCooperationId(Long citizenId, Long cooperationId);
}
