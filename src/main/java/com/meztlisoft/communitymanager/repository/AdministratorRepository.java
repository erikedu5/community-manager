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


    @Query("FROM AdministratorEntity a WHERE a.id > 0 and a.active = true")
    List<AdministratorEntity> findAllActive();

    boolean existsByRoleAndRetinue(RoleEntity role, RetinueEntity retinue);

    Optional<AdministratorEntity> findByIdAndActive(long id, boolean active);

    List<AdministratorEntity> findByCitizenAndActive(CitizenEntity citizen, boolean active);

    @Query("FROM AdministratorEntity a where a.citizen.id = :citizenId and a.retinue.id = :retinueId")
    AdministratorEntity findRoleByCitizenIdAndRetinueId(Long citizenId, Long retinueId);

    @Query("FROM AdministratorEntity a WHERE a.citizen.id = :citizenId and a.active= :active")
    AdministratorEntity findByCitizenIdAndActive(long citizenId, boolean active);
}
