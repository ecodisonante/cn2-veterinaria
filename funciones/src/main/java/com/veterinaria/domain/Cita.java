package com.veterinaria.domain;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class Cita {
    private Long id;
    private Long clienteId;
    private Long mascotaId;
    private Long veterinarioId;
    private Timestamp fechaHora;
    private Integer estadoId;
    private String motivo;
    private Timestamp fechaCreacion;
}
