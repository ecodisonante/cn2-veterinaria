package com.veterinaria.dto;

public record ClientResponseDTO(
        Long id,
        String rut,
        String nombreCompleto,
        String email,
        String telefono,
        String direccion,
        Integer estadoId,
        String fechaCreacion
) {}
