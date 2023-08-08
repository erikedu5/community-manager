package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.PaymentEntity;
import java.util.List;
import java.util.Optional;
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

}
