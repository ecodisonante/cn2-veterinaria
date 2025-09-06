package com.veterinaria.dto;

public record EmpleadoRequestDTO(
                String rut,
                String nombreCompleto,
                String email,
                String telefono,
                Integer rolId,
                Integer estadoId,
                String fechaIngreso) {
}
