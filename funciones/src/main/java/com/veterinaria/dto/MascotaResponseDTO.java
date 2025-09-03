package com.veterinaria.dto;

public record MascotaResponseDTO(
        Long id,
        Long clienteId,
        String nombre,
        Integer especieId,
        Integer razaId,
        String fechaNacimiento, // ISO
        Integer sexoId,
        Integer estadoId,
        String fotoUrl,
        String otraEspecie,
        String otraRaza,
        String fechaCreacion) {
}
