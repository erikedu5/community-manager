package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.AdministratorEntity;
import com.meztlisoft.communitymanager.entity.CitizenEntity;
import com.meztlisoft.communitymanager.entity.RetinueEntity;
import com.meztlisoft.communitymanager.entity.RoleEntity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<AdministratorEntity, Long>, JpaSpecificationExecutor<AdministratorEntity> {


    @Query(value = "SELECT * FROM administradores WHERE id > 0 and activo = true", nativeQuery = true)
    List<AdministratorEntity> findAllActive();

    boolean existsByRoleAndRetinue(RoleEntity role, RetinueEntity retinue);

    Optional<AdministratorEntity> findByIdAndActive(long id, boolean active);

    List<AdministratorEntity> findByCitizenAndActive(CitizenEntity citizen, boolean active);

    @Query(value = "SELECT * FROM administradores where ciudadano_id = :citizenId and comitiva_id = :retinueId", nativeQuery = true)
    AdministratorEntity findRoleByCitizenIdAndRetinueId(Long citizenId, Long retinueId);
}
