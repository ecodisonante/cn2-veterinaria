package com.veterinaria.domain;

import java.sql.Date;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class Empleado {
    private Long id;
    private String rut;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private Integer rolId;
    private Integer estadoId;
    private Date fechaIngreso;
    private Timestamp fechaCreacion;
}
