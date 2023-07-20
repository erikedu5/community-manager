package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.CiudadanoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CiudadanoRepository extends JpaRepository<CiudadanoEntity, Long>, JpaSpecificationExecutor<CiudadanoEntity> {

}
