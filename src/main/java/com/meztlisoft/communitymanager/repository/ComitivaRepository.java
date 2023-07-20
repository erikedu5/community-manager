package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.ComitivaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ComitivaRepository  extends JpaRepository<ComitivaEntity, Long>, JpaSpecificationExecutor<ComitivaEntity> {

}
