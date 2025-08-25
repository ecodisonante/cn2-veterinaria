package com.veterinaria.cliente.dto;

public record ClienteResponseDTO(
        Integer id,
        String rut,
        String nombreCompleto,
        String email,
        String telefono,
        String direccion,
        Integer estadoId,
        String fechaCreacion
) {}
