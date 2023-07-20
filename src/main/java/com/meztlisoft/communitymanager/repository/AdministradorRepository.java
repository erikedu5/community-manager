package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.dto.enums.Role;
import com.meztlisoft.communitymanager.entity.AdministradorEntity;
import com.meztlisoft.communitymanager.entity.ComitivaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<AdministradorEntity, Long>, JpaSpecificationExecutor<AdministradorEntity> {

    @Query(value = "select * from administradores where user_name = ?", nativeQuery = true)
    Optional<AdministradorEntity> findByUserName(String username);

    /**
     * Filtra mayor a 0 por que el indicador 0 es el administrador del sistema.
     *
     * @return List de zadministradoreEntity
     */
    @Query(value = "select * from administradores where id > 0", nativeQuery = true)
    List<AdministradorEntity> findAll();

    boolean existsByRoleAndComitiva(Role role, ComitivaEntity comitivaId);
}
