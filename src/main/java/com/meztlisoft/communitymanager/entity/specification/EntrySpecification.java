package com.meztlisoft.communitymanager.entity.specification;

import com.meztlisoft.communitymanager.dto.filters.EntryFilters;
import com.meztlisoft.communitymanager.entity.EntryEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

public class EntrySpecification {

    public static Specification<EntryEntity> getFilteredEntry(EntryFilters params, Long retinueId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(params.getConcept())) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("concept")), params.getConcept()));
            }

            if (Objects.nonNull(params.getDateTo())
                    && Objects.nonNull(params.getDateFrom())) {
                predicates.add(criteriaBuilder.between(root.get("date"),
                        params.getDateTo(),
                        params.getDateFrom()));
            }

            predicates.add(criteriaBuilder.equal(root.get("retinue").get("id"), retinueId));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
