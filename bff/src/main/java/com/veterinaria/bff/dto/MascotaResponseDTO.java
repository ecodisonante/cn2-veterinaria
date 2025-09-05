package com.veterinaria.bff.dto;

public record MascotaResponseDTO(
        Long id,
        Long clienteId,
        String nombre,
        Long especieId,
        Long razaId,
        String fechaNacimiento, // ISO
        Long sexoId,
        Long estadoId,
        String fotoUrl,
        String otraEspecie,
        String otraRaza,
        String fechaCreacion) {
}
