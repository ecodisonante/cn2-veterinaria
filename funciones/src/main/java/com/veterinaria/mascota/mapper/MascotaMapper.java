package com.veterinaria.mascota.mapper;

import com.veterinaria.mascota.domain.Mascota;
import com.veterinaria.mascota.dto.MascotaRequestDTO;
import com.veterinaria.mascota.dto.MascotaResponseDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

public final class MascotaMapper {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private MascotaMapper() {
    }

    public static Mascota toEntity(MascotaRequestDTO d) {
        Mascota m = new Mascota();
        m.setId(d.id());
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

    public static MascotaResponseDTO toDTO(Mascota m) {

        String fechaNacimiento = "";
        if (m.getFechaNacimiento() != null) {
            LocalDate ld = m.getFechaNacimiento().toLocalDate();
            fechaNacimiento = ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        return new MascotaResponseDTO(
                m.getId(),
                m.getNombre(),
                m.getEspecieId(),
                m.getRazaId(),
                fechaNacimiento,
                m.getClienteId(),
                m.getSexoId(),
                m.getEstadoId(),
                m.getFotoUrl(),
                m.getOtraEspecie(),
                m.getOtraRaza(),
                m.getFechaCreacion().toLocalDateTime().format(dtf));
    }
}
