package com.veterinaria.cliente.domain;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class Cliente {
    private Integer id;
    private String rut;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String direccion;
    private Integer estadoId;
    private Timestamp fechaCreacion;
}
