package com.meztlisoft.communitymanager.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitizenDto {

    private Long id;
    private String name;
    private String description;
    private String curp;
    private String address;
    private LocalDate birthday;
    private boolean isNative;
    private boolean isMarried;
}
