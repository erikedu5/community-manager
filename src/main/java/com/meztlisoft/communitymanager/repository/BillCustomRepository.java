package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.BillEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CompoundSelection;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BillCustomRepository {

    private final EntityManager em;

    public Long getSummary(Specification<BillEntity> spec) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<BillEntity> from = query.from(BillEntity.class);

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

        return em.createQuery(select).getSingleResult();
    }

}
