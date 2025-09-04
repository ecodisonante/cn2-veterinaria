package com.veterinaria.domain;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class Client {
    private Long id;
    private String rut;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String direccion;
    private Long estadoId;
    private Timestamp fechaCreacion;
}
