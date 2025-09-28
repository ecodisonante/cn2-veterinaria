package com.veterinaria.service;

import com.veterinaria.domain.Client;
import com.veterinaria.dto.ClienteRequestDTO;
import com.veterinaria.dto.ClientResponseDTO;
import com.veterinaria.events.CrudAction;
import com.veterinaria.events.EventGridPublisherFactory;
import com.veterinaria.mapper.ClientMapper;
import com.veterinaria.repository.ClienteRepository;
import com.veterinaria.repository.Db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class ClienteService {
    private static final ClienteRepository repo = new ClienteRepository();
    private static final String ENTITY = "Cliente";

    public ClientResponseDTO create(ClienteRequestDTO req) throws SQLException {
        validate(req);

        try (Connection conn = Db.open()) {
            conn.setAutoCommit(false);

            Client cli = ClientMapper.toEntity(req);

            cli.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            cli.setId(repo.insert(conn, cli));

            conn.commit();

            var response = ClientMapper.toResponse(cli);

            EventGridPublisherFactory.publishCrud(ENTITY, CrudAction.CREATED, String.valueOf(response.id()), response);

            return response;
        }
    }

    public ClientResponseDTO getById(Long id) throws SQLException {
        try (Connection conn = Db.open()) {
            return ClientMapper.toResponse(repo.findById(conn, id));
        }
    }

    public List<ClientResponseDTO> getAll() throws SQLException {
        try (Connection conn = Db.open()) {
            List<Client> result = repo.findAll(conn);

            return result.stream()
                    .map(ClientMapper::toResponse)
                    .toList();
        }
    }

    public ClientResponseDTO update(long id, ClienteRequestDTO req) throws SQLException {
        validate(req);

        try (Connection conn = Db.open()) {
            conn.setAutoCommit(false);

            Client cli = ClientMapper.toEntity(req);
            cli.setId(id);

            repo.update(conn, cli);
            conn.commit();

            var response = ClientMapper.toResponse(repo.findById(conn, id));

            EventGridPublisherFactory.publishCrud(ENTITY, CrudAction.UPDATED, String.valueOf(response.id()), response);

            return response;
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection conn = Db.open()) {
            conn.setAutoCommit(false);

            var deleted = ClientMapper.toResponse(repo.findById(conn, id));
            repo.delete(conn, id);

            conn.commit();

            if(deleted != null) {
                EventGridPublisherFactory.publishCrud(ENTITY, CrudAction.DELETED, String.valueOf(deleted.id()), deleted);
            }
        }
    }

    private void validate(ClienteRequestDTO req) {
        if (req == null || req.nombre() == null || req.nombre().isBlank())
            throw new IllegalArgumentException("nombreCompleto requerido");
        if (req.email() == null || req.email().isBlank())
            throw new IllegalArgumentException("email requerido");
        if (req.rut() == null || req.rut().isBlank())
            throw new IllegalArgumentException("rut requerido");
    }
}
