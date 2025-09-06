package com.veterinaria.mapper;

import com.veterinaria.domain.Mascota;
import com.veterinaria.dto.MascotaRequestDTO;
import com.veterinaria.dto.MascotaResponseDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

public final class MascotaMapper {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private MascotaMapper() {
    }

    public static Mascota toEntity(MascotaRequestDTO d) {
        Mascota m = new Mascota();
        m.setNombre(d.nombre());
        m.setEspecieId(d.especieId());
        m.setRazaId(d.razaId());
        m.setFechaNacimiento(Date.valueOf(d.fechaNacimiento()));
        m.setClienteId(d.clienteId());
        m.setSexoId(d.sexoId());
        m.setEstadoId(d.estadoId());
        m.setFotoUrl(d.fotoUrl());
        m.setOtraEspecie(d.otraEspecie());
        m.setOtraRaza(d.otraRaza());
        return m;
    }

    public static MascotaResponseDTO toResponse(Mascota m) {

        String fechaNacimiento = "";
        String fechaCreacion = "";

        if (m.getFechaNacimiento() != null) {
            LocalDate ld = m.getFechaNacimiento().toLocalDate();
            fechaNacimiento = ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        if (m.getFechaCreacion() != null) {
            fechaCreacion = m.getFechaCreacion().toLocalDateTime().format(dtf);
        }

        return new MascotaResponseDTO(
                m.getId(),
                m.getClienteId(),
                m.getNombre(),
                m.getEspecieId(),
                m.getRazaId(),
                fechaNacimiento,
                m.getSexoId(),
                m.getEstadoId(),
                m.getFotoUrl(),
                m.getOtraEspecie(),
                m.getOtraRaza(),
                fechaCreacion);
    }
}
