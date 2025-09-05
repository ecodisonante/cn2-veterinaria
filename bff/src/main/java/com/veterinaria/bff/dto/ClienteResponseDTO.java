package com.veterinaria.bff.dto;

public record ClienteResponseDTO(
        Long id,
        String rut,
        String nombre,
        String email,
        String telefono,
        String direccion,
        Long estadoId,
        String fechaCreacion
) {
}
