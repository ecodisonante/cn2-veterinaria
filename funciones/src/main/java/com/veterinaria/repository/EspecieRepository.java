package com.veterinaria.repository;

import com.veterinaria.domain.Especie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecieRepository {

    public long insert(Connection conn, Especie e) throws SQLException {
        String q = """
                INSERT INTO ESPECIE (NOMBRE)
                VALUES (?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(q, new String[] { "ID" })) {
            ps.setString(1, e.getNombre());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
                throw new SQLException("No se pudo obtener el ID generado");
            }
        }
    }

    public Especie findById(Connection conn, long id) throws SQLException {
        String q = "SELECT ID, NOMBRE FROM ESPECIE WHERE ID=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    public List<Especie> findAll(Connection conn) throws SQLException {
        String q = "SELECT ID, NOMBRE FROM ESPECIE ORDER BY ID";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Especie> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public Especie update(Connection conn, long id, Especie e) throws SQLException {
        String q = "UPDATE ESPECIE SET NOMBRE=? WHERE ID=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, e.getNombre());
            ps.setLong(2, id);
            int rows = ps.executeUpdate();
            return rows > 0 ? e : null;
        }
    }

    public void delete(Connection conn, long id) throws SQLException {
        String q = "DELETE FROM ESPECIE WHERE ID=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Especie map(ResultSet rs) throws SQLException {
        Especie e = new Especie();
        e.setId(rs.getLong("ID"));
        e.setNombre(rs.getString("NOMBRE"));
        return e;
    }
}
