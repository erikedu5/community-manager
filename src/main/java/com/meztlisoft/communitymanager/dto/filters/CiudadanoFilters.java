package com.meztlisoft.communitymanager.dto.filters;

import java.time.LocalDate;
import java.util.List;

import com.meztlisoft.communitymanager.dto.RangoFechas;
import lombok.Data;

@Data
public class CiudadanoFilters {

    private String nombre;
    private String descripcion;
    private String curp;
    private String direccion;
    private Boolean activo;
    private RangoFechas rango_fechas_nacimiento;
    private int page;
    private int size;

}
