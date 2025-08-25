package com.veterinaria.mascota.dto;

public record MascotaResponseDTO(
        Integer id,
        String nombre,
        Integer especieId,
        Integer razaId,
        String fechaNacimiento, // ISO
        Integer clienteId,
        Integer sexoId,
        Integer estadoId,
        String fotoUrl,
        String otraEspecie,
        String otraRaza,
        String fechaCreacion) {
}
