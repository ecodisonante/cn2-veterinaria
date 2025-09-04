package com.veterinaria.mapper;

import com.veterinaria.domain.Cita;
import com.veterinaria.dto.CitaRequestDTO;
import com.veterinaria.dto.CitaResponseDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class CitaMapper {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private CitaMapper() {
    }

    public static Cita toEntity(CitaRequestDTO dto) {
        Cita cita = new Cita();
        cita.setClienteId(dto.clienteId());
        cita.setMascotaId(dto.mascotaId());
        cita.setVeterinarioId(dto.veterinarioId());
        if (dto.fechaHora() != null && !dto.fechaHora().isEmpty()) {
            LocalDateTime ldt = LocalDateTime.parse(dto.fechaHora(), dtf);
            cita.setFechaHora(Timestamp.valueOf(ldt));
        }
        cita.setEstadoId(dto.estadoId());
        cita.setMotivo(dto.motivo());
        return cita;
    }

    public static CitaResponseDTO toResponse(Cita cita) {
        return new CitaResponseDTO(
                cita.getId(),
                cita.getClienteId(),
                cita.getMascotaId(),
                cita.getVeterinarioId(),
                cita.getFechaHora(),
                cita.getEstadoId(),
                cita.getMotivo(),
                cita.getFechaCreacion());
    }
}
