package com.veterinaria.bff.dto;

public record CitaRequestDTO(
        Long clienteId,
        Long mascotaId,
        Long veterinarioId,
        String fechaHora, // yyyy-MM-dd HH:mm:ss
        Integer estadoId,
        String motivo) {
}
