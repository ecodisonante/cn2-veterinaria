package com.veterinaria.bff.dto;

import java.sql.Date;
import java.sql.Timestamp;

public record EmpleadoResponseDTO(
        Long id,
        String rut,
        String nombreCompleto,
        String email,
        String telefono,
        Integer rolId,
        Integer estadoId,
        Date fechaIngreso,
        Timestamp fechaCreacion) {
}
