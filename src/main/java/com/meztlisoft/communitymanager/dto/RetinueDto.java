package com.meztlisoft.communitymanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RetinueDto {

    private Long id;
    private String name;
    private Long unitBenefit;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String unitBenefitName;
    private Boolean active;
}
