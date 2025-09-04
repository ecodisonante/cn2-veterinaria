package com.veterinaria.repository;

import com.veterinaria.domain.MascotaEstado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MascotaEstadoRepository {

    public MascotaEstado findById(Connection conn, int estadoId) throws SQLException {
        String q = "SELECT ID, NOMBRE FROM MASCOTA_ESTADO WHERE ID=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, estadoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    public List<MascotaEstado> findAll(Connection conn) throws SQLException {
        String q = "SELECT ID, NOMBRE FROM MASCOTA_ESTADO ORDER BY ID";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<MascotaEstado> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    private MascotaEstado map(ResultSet rs) throws SQLException {
        MascotaEstado e = new MascotaEstado();
        e.setId(rs.getInt("ID"));
        e.setNombre(rs.getString("NOMBRE"));
        return e;
    }
}

