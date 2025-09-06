package com.veterinaria.service;

import com.veterinaria.domain.Cita;
import com.veterinaria.dto.CitaRequestDTO;
import com.veterinaria.dto.CitaResponseDTO;
import com.veterinaria.mapper.CitaMapper;
import com.veterinaria.repository.CitaRepository;
import com.veterinaria.repository.Db;

import java.sql.Connection;
import java.sql.SQLException;

public class CitaService {
    private static final CitaRepository repo = new CitaRepository();

    public CitaResponseDTO create(CitaRequestDTO req) throws SQLException {
        validate(req);

        try (Connection c = Db.open()) {
            c.setAutoCommit(false);
            Cita cita = CitaMapper.toEntity(req);

            long newId = repo.insert(c, cita);
            cita.setId(newId);

            c.commit();

            return CitaMapper.toResponse(repo.findById(c, newId));
        }
    }

    public CitaResponseDTO update(long id, CitaRequestDTO req) throws SQLException {
        validate(req);

        try (Connection c = Db.open()) {
            c.setAutoCommit(false);

            Cita cita = CitaMapper.toEntity(req);
            cita.setId(id);

            repo.update(c, id, cita);

            c.commit();

            return CitaMapper.toResponse(repo.findById(c, id));
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = Db.open()) {
            c.setAutoCommit(false);
            repo.delete(c, id);
            c.commit();
        }
    }

    private void validate(CitaRequestDTO req) {
        if (req == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (req.clienteId() == null) {
            throw new IllegalArgumentException("ClienteId es requerido");
        }
        if (req.mascotaId() == null) {
            throw new IllegalArgumentException("MascotaId es requerido");
        }
        if (req.fechaHora() == null || req.fechaHora().isBlank()) {
            throw new IllegalArgumentException("Fecha y hora son requeridas");
        }
    }
}
