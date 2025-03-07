package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.AssociatedEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationRepository extends JpaRepository<AssociatedEntity, Long>, JpaSpecificationExecutor<AssociatedEntity> {

    @Query("from AssociatedEntity a where a.retinue.id = :id and a.active = true")
    List<AssociatedEntity> findAllByRetinueId(long id);

    @Query("from AssociatedEntity a where a.retinue.id = :id and a.active = true order by a.citizen.curp asc")
    List<AssociatedEntity> findAllByRetinueIdAAndActiveTrue(long id);

    @Query("FROM AssociatedEntity a WHERE a.retinue.id = :retinueId and a.id not in :ids and a.active = true")
    List<AssociatedEntity> findNotIn(List<Long> ids, Long retinueId);

    @Query("FROM AssociatedEntity a WHERE a.retinue.id= :id and a.citizen.id= :citizenId")
    Optional<AssociatedEntity> findByRetinueIdAndCitizenId(Long id, Long citizenId);


    @Query("FROM AssociatedEntity a WHERE a.retinue.id= :id and a.citizen.id= :citizenId and a.active= true")
    Optional<AssociatedEntity> findByRetinueIdAndCitizenIdAndActive(Long id, Long citizenId);

    @Query("FROM AssociatedEntity a WHERE a.citizen.id = :citizenId")
    List<AssociatedEntity> findAllByCitizenId(long citizenId);
}