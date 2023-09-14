package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long>, JpaSpecificationExecutor<BillEntity> {

}
