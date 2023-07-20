package com.meztlisoft.communitymanager.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CiudadanoDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private String curp;
    private String direccion;
    private LocalDate fechaNacimiento;
}
