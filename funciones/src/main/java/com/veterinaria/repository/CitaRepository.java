package com.veterinaria.repository;

import com.veterinaria.domain.Cita;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class CitaRepository {

    public long insert(Connection conn, Cita cita) throws SQLException {
        String query = """
                INSERT INTO CITA(
                    cliente_id,
                    mascota_id,
                    veterinario_id,
                    fecha_hora,
                    estado_id,
                    motivo,
                    fecha_creacion)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        cita.setFechaCreacion(Timestamp.from(now.toInstant()));

        try (PreparedStatement ps = conn.prepareStatement(query, new String[] { "ID" })) {
            ps.setLong(1, cita.getClienteId());
            ps.setLong(2, cita.getMascotaId());
            ps.setLong(3, cita.getVeterinarioId());
            ps.setTimestamp(4, cita.getFechaHora());
            ps.setInt(5, cita.getEstadoId());
            ps.setString(6, cita.getMotivo());
            ps.setTimestamp(7, cita.getFechaCreacion());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getLong(1);
                throw new SQLException("No se pudo obtener el ID generado");
            }
        }
    }

    public Cita findById(Connection conn, Long id) throws SQLException {
        String query = "SELECT * FROM CITA WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return map(rs);
                return null;
            }
        }
    }

    public Cita update(Connection conn, long id, Cita cita) throws SQLException {
        String query = """
                UPDATE CITA SET
                    cliente_id=?,
                    mascota_id=?,
                    veterinario_id=?,
                    fecha_hora=?,
                    estado_id=?,
                    motivo=?
                WHERE id=?
                """;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, cita.getClienteId());
            ps.setLong(2, cita.getMascotaId());
            ps.setLong(3, cita.getVeterinarioId());
            ps.setTimestamp(4, cita.getFechaHora());
            ps.setInt(5, cita.getEstadoId());
            ps.setString(6, cita.getMotivo());
            ps.setLong(7, id);

            int rows = ps.executeUpdate();
            return rows > 0 ? cita : null;
        }
    }

    public void delete(Connection conn, long id) throws SQLException {
        String query = "DELETE FROM CITA WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Cita map(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        cita.setId(rs.getLong("id"));
        cita.setClienteId(rs.getLong("cliente_id"));
        cita.setMascotaId(rs.getLong("mascota_id"));
        cita.setVeterinarioId(rs.getLong("veterinario_id"));
        cita.setFechaHora(rs.getTimestamp("fecha_hora"));
        cita.setEstadoId(rs.getInt("estado_id"));
        cita.setMotivo(rs.getString("motivo"));
        cita.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return cita;
    }
}
