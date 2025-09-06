package com.veterinaria.bff.dto;

public record MascotaRequestDTO(
        Long clienteId,
        String nombre,
        Long especieId,
        Long razaId,
        String fechaNacimiento, // ISO yyyy-MM-dd
        Long sexoId,
        Long estadoId,
        String fotoUrl,
        String otraEspecie,
        String otraRaza) {
}
