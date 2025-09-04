package com.veterinaria.service;

import com.veterinaria.domain.MascotaEstado;
import com.veterinaria.repository.Db;
import com.veterinaria.repository.MascotaEstadoRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MascotaEstadoService {
    private static final MascotaEstadoRepository repo = new MascotaEstadoRepository();

    public MascotaEstado getById(int estadoId) throws SQLException {
        try (Connection c = Db.open()) {
            return repo.findById(c, estadoId);
        }
    }

    public List<MascotaEstado> getAll() throws SQLException {
        try (Connection c = Db.open()) {
            return repo.findAll(c);
        }
    }
}
