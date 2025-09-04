package com.veterinaria.repository;

import com.veterinaria.domain.Raza;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RazaRepository {

    public long insert(Connection conn, Raza r) throws SQLException {
        String q = """
                INSERT INTO RAZA (ESPECIE_ID, NOMBRE)
                VALUES (?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(q, new String[] { "ID" })) {
            ps.setLong(1, r.getEspecieId());
            ps.setString(2, r.getNombre());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
                throw new SQLException("No se pudo obtener el ID generado");
            }
        }
    }

    public Raza findById(Connection conn, long id) throws SQLException {
        String q = "SELECT ID, ESPECIE_ID, NOMBRE FROM RAZA WHERE ID=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    public List<Raza> findAll(Connection conn) throws SQLException {
        String q = "SELECT ID, ESPECIE_ID, NOMBRE FROM RAZA ORDER BY ID";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Raza> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public List<Raza> findByEspecie(Connection conn, long especieId) throws SQLException {
        String q = "SELECT ID, ESPECIE_ID, NOMBRE FROM RAZA WHERE ESPECIE_ID=? ORDER BY NOMBRE";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setLong(1, especieId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Raza> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public Raza update(Connection conn, long id, Raza r) throws SQLException {
        String q = "UPDATE RAZA SET ESPECIE_ID=?, NOMBRE=? WHERE ID=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setLong(1, r.getEspecieId());
            ps.setString(2, r.getNombre());
            ps.setLong(3, id);
            int rows = ps.executeUpdate();
            return rows > 0 ? r : null;
        }
    }

    public void delete(Connection conn, long id) throws SQLException {
        String q = "DELETE FROM RAZA WHERE ID=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Raza map(ResultSet rs) throws SQLException {
        Raza r = new Raza();
        r.setId(rs.getLong("ID"));
        r.setEspecieId(rs.getLong("ESPECIE_ID"));
        r.setNombre(rs.getString("NOMBRE"));
        return r;
    }
}
