package com.veterinaria.dto;

public record ClientResponseDTO(
        Long id,
        String rut,
        String nombre,
        String email,
        String telefono,
        String direccion,
        Long estadoId,
        String fechaCreacion
) {}
