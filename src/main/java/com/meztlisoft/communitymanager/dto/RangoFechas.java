package com.meztlisoft.communitymanager.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RangoFechas {
    private LocalDate limiteSuperior;
    private LocalDate limiteInferior;
}
