package com.veterinaria.service;

import com.veterinaria.domain.Mascota;
import com.veterinaria.dto.MascotaRequestDTO;
import com.veterinaria.dto.MascotaResponseDTO;
import com.veterinaria.events.CrudAction;
import com.veterinaria.events.EventGridPublisherFactory;
import com.veterinaria.mapper.MascotaMapper;
import com.veterinaria.repository.Db;
import com.veterinaria.repository.MascotaRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class MascotaService {
    private static final MascotaRepository repo = new MascotaRepository();
    private static final String ENTITY = "Mascota";

    public MascotaResponseDTO create(MascotaRequestDTO req) throws SQLException {
        validate(req);

        try (Connection c = Db.open()) {
            c.setAutoCommit(false);
            Mascota m = MascotaMapper.toEntity(req);

            m.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            m.setId(repo.insert(c, m));

            c.commit();

            var response = MascotaMapper.toResponse(m);

            // Enviar notificacion
            EventGridPublisherFactory.publishCrud(ENTITY, CrudAction.CREATED, String.valueOf(response.id()), response);

            return response;
        }
    }

    public MascotaResponseDTO getById(long id) throws SQLException {
        try (Connection c = Db.open()) {
            var result = repo.findById(c, id);
            if (result == null)
                return null;

            return MascotaMapper.toResponse(repo.findById(c, id));
        }
    }

    public List<MascotaResponseDTO> getAll() throws SQLException {
        try (Connection c = Db.open()) {
            List<Mascota> mascotas = repo.findAll(c);

            return mascotas.stream()
                    .map(MascotaMapper::toResponse)
                    .toList();
        }
    }

    public List<MascotaResponseDTO> getByCliente(long clienteId) throws SQLException {
        try (Connection c = Db.open()) {
            List<Mascota> mascotas = repo.findByCliente(c, clienteId);

            return mascotas.stream()
                    .map(MascotaMapper::toResponse)
                    .toList();
        }
    }

    public MascotaResponseDTO update(long id, MascotaRequestDTO req) throws SQLException {
        validate(req);

        try (Connection c = Db.open()) {
            c.setAutoCommit(false);

            Mascota m = MascotaMapper.toEntity(req);
            m.setId(id);

            repo.update(c, id, m);

            c.commit();
            var response = MascotaMapper.toResponse(repo.findById(c, id));

            // Enviar notificacion
            EventGridPublisherFactory.publishCrud(ENTITY, CrudAction.UPDATED, String.valueOf(response.id()), response);

            return response;
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = Db.open()) {
            c.setAutoCommit(false);

            var deleted = MascotaMapper.toResponse(repo.findById(c, id));

            repo.delete(c, id);
            c.commit();

            // Enviar notificacion
            EventGridPublisherFactory.publishCrud(ENTITY, CrudAction.DELETED, String.valueOf(deleted.id()), deleted);
        }
    }

    private void validate(MascotaRequestDTO req) {
        if (req == null || req.nombre() == null || req.nombre().isBlank())
            throw new IllegalArgumentException("Nombre es erquerido");
        if (req.clienteId() == null)
            throw new IllegalArgumentException("ClienteId es requerido");
    }
}
