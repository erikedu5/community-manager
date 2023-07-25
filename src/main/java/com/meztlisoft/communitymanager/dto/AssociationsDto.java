package com.meztlisoft.communitymanager.dto;

import com.meztlisoft.communitymanager.entity.RoleEntity;
import lombok.Data;
import java.util.Map;

@Data
public class AssociationsDto {

    private Map<Long, RoleEntity> asociation;

}
