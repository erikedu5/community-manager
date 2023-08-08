package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.AssociatedEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationRepository extends JpaRepository<AssociatedEntity, Long>, JpaSpecificationExecutor<AssociatedEntity> {

    @Query("from AssociatedEntity a where a.retinue.id = :id")
    List<AssociatedEntity> findAllByRetinueId(long id);

    @Query(value = "Select COUNT(*) > 0 from asociados where comitiva_id = :id and ciudadano_id = :citizenId ", nativeQuery = true)
    Boolean existByRetinueAndCitizen(Long id, long citizenId);

    @Query("FROM AssociatedEntity a WHERE a.retinue.id = :retinueId and a.id not in :ids")
    List<AssociatedEntity> findNotIn(List<Long> ids, Long retinueId);
}