package com.meztlisoft.communitymanager.entity.specification;

import com.meztlisoft.communitymanager.dto.filters.CiudadanoFilters;
import com.meztlisoft.communitymanager.entity.CiudadanoEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class CiudadanoSpecification {

    public static Specification<CiudadanoEntity> getFilteredCiudadano(CiudadanoFilters params) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNoneBlank(params.getNombre())) {
                predicates.add(criteriaBuilder.like(root.get("nombre"), params.getNombre()));
            }

            if (StringUtils.isNoneBlank(params.getDescripcion())) {
                predicates.add(criteriaBuilder.like(root.get("descripcion"), params.getDescripcion()));
            }

            if (StringUtils.isNoneBlank(params.getCurp())) {
                predicates.add(criteriaBuilder.like(root.get("curp"), params.getCurp()));
            }

            if (Objects.nonNull(params.getActivo())) {
                predicates.add(criteriaBuilder.equal(root.get("activo"), params.getActivo()));
            }

            if (Objects.nonNull(params.getRango_fechas_nacimiento())
                    && Objects.nonNull(params.getRango_fechas_nacimiento().getLimiteSuperior())
                    && Objects.nonNull(params.getRango_fechas_nacimiento().getLimiteSuperior())) {

                predicates.add(criteriaBuilder.between(root.get("fecha_nacimiento"),
                        params.getRango_fechas_nacimiento().getLimiteInferior(),
                        params.getRango_fechas_nacimiento().getLimiteSuperior()));
            }

            predicates.add(criteriaBuilder.gt(root.get("id"), 0));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
