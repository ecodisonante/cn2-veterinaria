package com.veterinaria.cliente.dto;

public record ClienteRequestDTO(
        Integer id,
        String rut,
        String nombreCompleto,
        String email,
        String telefono,
        String direccion,
        Integer estadoId
) {}
