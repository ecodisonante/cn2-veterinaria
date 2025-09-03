package com.veterinaria.dto;

public record ClienteRequestDTO(
        String rut,
        String nombreCompleto,
        String email,
        String telefono,
        String direccion,
        Integer estadoId
) {}
