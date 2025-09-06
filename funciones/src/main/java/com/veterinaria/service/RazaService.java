package com.veterinaria.service;

import com.veterinaria.domain.Raza;
import com.veterinaria.repository.Db;
import com.veterinaria.repository.RazaRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RazaService {
    private static final RazaRepository repo = new RazaRepository();

    public Raza create(Raza req) throws SQLException {
        validate(req);
        try (Connection c = Db.open()) {
            c.setAutoCommit(false);
            long id = repo.insert(c, req);
            c.commit();
            return repo.findById(c, id);
        }
    }

    public Raza getById(long id) throws SQLException {
        try (Connection c = Db.open()) {
            return repo.findById(c, id);
        }
    }

    public List<Raza> getAll() throws SQLException {
        try (Connection c = Db.open()) {
            return repo.findAll(c);
        }
    }

    public List<Raza> getByEspecie(long especieId) throws SQLException {
        try (Connection c = Db.open()) {
            return repo.findByEspecie(c, especieId);
        }
    }

    public Raza update(long id, Raza req) throws SQLException {
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

    private void validate(Raza r) {
        if (r == null)
            throw new IllegalArgumentException("La raza no puede ser nula");
        if (r.getEspecieId() == null)
            throw new IllegalArgumentException("EspecieId es requerido");
        if (r.getNombre() == null || r.getNombre().isBlank())
            throw new IllegalArgumentException("Nombre de raza es requerido");
    }
}
