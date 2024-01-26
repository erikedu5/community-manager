package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.EntryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CompoundSelection;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class EntryCustomRepository {

    private final EntityManager em;

    public Long getSummary(Specification<EntryEntity> spec) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<EntryEntity> from = query.from(EntryEntity.class);

        Predicate predicate = null;

        if ( spec != null )
            predicate = spec.toPredicate(from, query, criteriaBuilder);

        CompoundSelection<Long> construct = criteriaBuilder.construct(
                Long.class,
                criteriaBuilder.sum(from.get("cost"))
        );

        CriteriaQuery<Long> select = query.select(construct);

        if ( predicate != null )
            select.where(predicate);
        try {
            return em.createQuery(select).getSingleResult();
        } catch (Exception e) {
            log.warn("error to get sumary " + e.getMessage());
        }
        return 0L;
    }
}
