package com.veterinaria.dto;

import java.sql.Timestamp;

public record CitaResponseDTO(
                Long id,
                Long clienteId,
                Long mascotaId,
                Long veterinarioId,
                Timestamp fechaHora,
                Integer estadoId,
                String motivo,
                Timestamp fechaCreacion) {
}
