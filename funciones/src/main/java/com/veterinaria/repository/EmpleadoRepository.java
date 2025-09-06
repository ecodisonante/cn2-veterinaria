package com.veterinaria.repository;

import com.veterinaria.domain.Empleado;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoRepository {

    public long insert(Connection conn, Empleado empleado) throws SQLException {
        String query = """
                INSERT INTO EMPLEADO(
                    rut,
                    nombre_completo,
                    email,
                    telefono,
                    rol_id,
                    estado_id,
                    fecha_ingreso,
                    fecha_creacion)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        empleado.setFechaCreacion(Timestamp.from(now.toInstant()));

        try (PreparedStatement ps = conn.prepareStatement(query, new String[] { "ID" })) {
            ps.setString(1, empleado.getRut());
            ps.setString(2, empleado.getNombreCompleto());
            ps.setString(3, empleado.getEmail());
            ps.setString(4, empleado.getTelefono());
            ps.setInt(5, empleado.getRolId());
            ps.setInt(6, empleado.getEstadoId());
            ps.setDate(7, empleado.getFechaIngreso());
            ps.setTimestamp(8, empleado.getFechaCreacion());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getLong(1);
                throw new SQLException("No se pudo obtener el ID generado");
            }
        }
    }

    public Empleado findById(Connection conn, Long id) throws SQLException {
        String query = """
                SELECT
                    id,
                    rut,
                    nombre_completo,
                    email,
                    telefono,
                    rol_id,
                    estado_id,
                    fecha_ingreso,
                    fecha_creacion
                FROM EMPLEADO WHERE id=?
                """;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return map(rs);
                return null;
            }
        }
    }

    public List<Empleado> findAll(Connection conn) throws SQLException {
        String query = """
                SELECT
                    id,
                    rut,
                    nombre_completo,
                    email,
                    telefono,
                    rol_id,
                    estado_id,
                    fecha_ingreso,
                    fecha_creacion
                FROM EMPLEADO ORDER BY id
                """;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Empleado> out = new ArrayList<>();
                while (rs.next())
                    out.add(map(rs));
                return out;
            }
        }
    }

    public Empleado update(Connection conn, long id, Empleado empleado) throws SQLException {
        String query = """
                UPDATE EMPLEADO SET
                    rut=?,
                    nombre_completo=?,
                    email=?,
                    telefono=?,
                    rol_id=?,
                    estado_id=?,
                    fecha_ingreso=?
                WHERE id=?
                """;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, empleado.getRut());
            ps.setString(2, empleado.getNombreCompleto());
            ps.setString(3, empleado.getEmail());
            ps.setString(4, empleado.getTelefono());
            ps.setInt(5, empleado.getRolId());
            ps.setInt(6, empleado.getEstadoId());
            ps.setDate(7, empleado.getFechaIngreso());
            ps.setLong(8, id);

            int rows = ps.executeUpdate();
            return rows > 0 ? empleado : null;
        }
    }

    public void delete(Connection conn, long id) throws SQLException {
        String query = "DELETE FROM EMPLEADO WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Empleado map(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado();
        empleado.setId(rs.getLong("id"));
        empleado.setRut(rs.getString("rut"));
        empleado.setNombreCompleto(rs.getString("nombre_completo"));
        empleado.setEmail(rs.getString("email"));
        empleado.setTelefono(rs.getString("telefono"));
        empleado.setRolId(rs.getInt("rol_id"));
        empleado.setEstadoId(rs.getInt("estado_id"));
        empleado.setFechaIngreso(rs.getDate("fecha_ingreso"));
        empleado.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return empleado;
    }
}
