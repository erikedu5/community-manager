package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.AdministratorEntity;
import com.meztlisoft.communitymanager.entity.RetinueEntity;
import com.meztlisoft.communitymanager.entity.RoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<AdministratorEntity, Long>, JpaSpecificationExecutor<AdministratorEntity> {

    boolean existsByRoleAndRetinue(RoleEntity role, RetinueEntity retinue);

    Optional<AdministratorEntity> findByIdAndActive(long id, boolean active);
}
