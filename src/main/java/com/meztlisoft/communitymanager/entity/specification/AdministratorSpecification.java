package com.meztlisoft.communitymanager.entity.specification;

import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import com.meztlisoft.communitymanager.entity.AdministratorEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class AdministratorSpecification {

    public static Specification<AdministratorEntity> getFilteredAdministrator(AdminFilters params) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getRole() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), params.getRole()));
            }

            if (params.getRetinueName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("retinue").get("name")), "%" + params.getRetinueName().toUpperCase() + "%"));
            }

            if (params.getCitizenName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("citizen").get("name")), "%" + params.getCitizenName().toUpperCase() + "%"));
            }

            predicates.add(criteriaBuilder.gt(root.get("id"), 0));
            predicates.add(criteriaBuilder.equal(root.get("active"), true));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
