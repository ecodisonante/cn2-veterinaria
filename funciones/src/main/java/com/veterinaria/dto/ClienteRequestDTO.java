package com.veterinaria.dto;

public record ClienteRequestDTO(
        String rut,
        String nombre,
        String email,
        String telefono,
        String direccion,
        Long estadoId
) {}
