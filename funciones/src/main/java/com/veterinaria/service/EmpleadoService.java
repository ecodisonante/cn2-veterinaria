package com.veterinaria.service;

import com.veterinaria.domain.Empleado;
import com.veterinaria.dto.EmpleadoRequestDTO;
import com.veterinaria.dto.EmpleadoResponseDTO;
import com.veterinaria.mapper.EmpleadoMapper;
import com.veterinaria.repository.Db;
import com.veterinaria.repository.EmpleadoRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EmpleadoService {
    private static final EmpleadoRepository repo = new EmpleadoRepository();

    public EmpleadoResponseDTO create(EmpleadoRequestDTO req) throws SQLException {
        validate(req);

        try (Connection c = Db.open()) {
            c.setAutoCommit(false);
            Empleado empleado = EmpleadoMapper.toEntity(req);

            long newId = repo.insert(c, empleado);
            empleado.setId(newId);

            c.commit();

            // El objeto 'empleado' ya tiene la fecha de creación asignada por el
            // repositorio
            return EmpleadoMapper.toResponse(repo.findById(c, newId));
        }
    }

    public EmpleadoResponseDTO getById(long id) throws SQLException {
        try (Connection c = Db.open()) {
            var result = repo.findById(c, id);
            if (result == null) {
                return null;
            }
            return EmpleadoMapper.toResponse(result);
        }
    }

    public List<EmpleadoResponseDTO> getAll() throws SQLException {
        try (Connection c = Db.open()) {
            List<Empleado> empleados = repo.findAll(c);
            return empleados.stream()
                    .map(EmpleadoMapper::toResponse)
                    .toList();
        }
    }

    public EmpleadoResponseDTO update(long id, EmpleadoRequestDTO req) throws SQLException {
        validate(req);

        try (Connection c = Db.open()) {
            c.setAutoCommit(false);

            Empleado empleado = EmpleadoMapper.toEntity(req);
            empleado.setId(id);

            repo.update(c, id, empleado);

            c.commit();

            return EmpleadoMapper.toResponse(repo.findById(c, id));
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = Db.open()) {
            c.setAutoCommit(false);
            repo.delete(c, id);
            c.commit();
        }
    }

    private void validate(EmpleadoRequestDTO req) {
        if (req == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (req.rut() == null || req.rut().isBlank()) {
            throw new IllegalArgumentException("RUT es requerido");
        }
        if (req.nombreCompleto() == null || req.nombreCompleto().isBlank()) {
            throw new IllegalArgumentException("Nombre completo es requerido");
        }
    }
}
