package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.dto.enums.Role;
import com.meztlisoft.communitymanager.entity.AdministratorEntity;
import com.meztlisoft.communitymanager.entity.RetinueEntity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<AdministratorEntity, Long>, JpaSpecificationExecutor<AdministratorEntity> {

    @Query(value = "select * from administradores where user_name = ?", nativeQuery = true)
    Optional<AdministratorEntity> findByUserName(String username);

    boolean existsByRoleAndRetinue(Role role, RetinueEntity retinue);

    Optional<AdministratorEntity> findByIdAndActive(long id, boolean active);
}
