package com.veterinaria.service;

import com.veterinaria.domain.Sexo;
import com.veterinaria.repository.Db;
import com.veterinaria.repository.SexoRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SexoService {
    private static final SexoRepository repo = new SexoRepository();

    public Sexo create(Sexo req) throws SQLException {
        validate(req);
        try (Connection c = Db.open()) {
            c.setAutoCommit(false);
            long id = repo.insert(c, req);
            c.commit();
            return repo.findById(c, id);
        }
    }

    public Sexo getById(long id) throws SQLException {
        try (Connection c = Db.open()) {
            return repo.findById(c, id);
        }
    }

    public List<Sexo> getAll() throws SQLException {
        try (Connection c = Db.open()) {
            return repo.findAll(c);
        }
    }

    public Sexo update(long id, Sexo req) throws SQLException {
        validate(req);
        try (Connection c = Db.open()) {
            c.setAutoCommit(false);
            repo.update(c, id, req);
            c.commit();
            return repo.findById(c, id);
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = Db.open()) {
            c.setAutoCommit(false);
            repo.delete(c, id);
            c.commit();
        }
    }

    private void validate(Sexo s) {
        if (s == null || s.getNombre() == null || s.getNombre().isBlank())
            throw new IllegalArgumentException("Nombre de sexo es requerido");
    }
}
