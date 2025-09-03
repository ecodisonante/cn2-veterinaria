package com.veterinaria.domain;

import java.sql.Date;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class Mascota {
    private Long id;
    private Long clienteId;
    private String nombre;
    private Integer especieId;
    private Integer razaId;
    private Date fechaNacimiento;
    private Integer sexoId;
    private Integer estadoId;
    private String fotoUrl;
    private String otraEspecie;
    private String otraRaza;
    private Timestamp fechaCreacion;

}
