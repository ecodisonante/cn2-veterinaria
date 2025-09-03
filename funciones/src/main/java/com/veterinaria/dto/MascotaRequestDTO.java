package com.veterinaria.dto;

public record MascotaRequestDTO(
                Long id,
                Long clienteId,
                String nombre,
                Integer especieId,
                Integer razaId,
                String fechaNacimiento, // ISO yyyy-MM-dd
                Integer sexoId,
                Integer estadoId,
                String fotoUrl,
                String otraEspecie,
                String otraRaza) {
}
