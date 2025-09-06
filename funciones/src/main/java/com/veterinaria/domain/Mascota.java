package com.veterinaria.domain;

import java.sql.Date;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class Mascota {
    private Long id;
    private Long clienteId;
    private String nombre;
    private Long especieId;
    private Long razaId;
    private Date fechaNacimiento;
    private Long sexoId;
    private Long estadoId;
    private String fotoUrl;
    private String otraEspecie;
    private String otraRaza;
    private Timestamp fechaCreacion;

}
