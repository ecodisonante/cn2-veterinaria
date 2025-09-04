package com.veterinaria.service;

import com.veterinaria.domain.Especie;
import com.veterinaria.repository.Db;
import com.veterinaria.repository.EspecieRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EspecieService {
    private static final EspecieRepository repo = new EspecieRepository();

    public Especie create(Especie req) throws SQLException {
        validate(req);
        try (Connection c = Db.open()) {
            c.setAutoCommit(false);
            long id = repo.insert(c, req);
            c.commit();
            return repo.findById(c, id);
        }
    }

    public Especie getById(long id) throws SQLException {
        try (Connection c = Db.open()) {
            return repo.findById(c, id);
        }
    }

    public List<Especie> getAll() throws SQLException {
        try (Connection c = Db.open()) {
            return repo.findAll(c);
        }
    }

    public Especie update(long id, Especie req) throws SQLException {
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

    private void validate(Especie e) {
        if (e == null || e.getNombre() == null || e.getNombre().isBlank())
            throw new IllegalArgumentException("Nombre de especie es requerido");
    }
}
