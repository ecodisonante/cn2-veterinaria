package com.veterinaria.repository;

import com.veterinaria.domain.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    public long insert(Connection conn, Client cli) throws SQLException {

        String query = """
                INSERT INTO CLIENTE(
                    rut,
                    nombre_completo,
                    email,
                    telefono,
                    direccion,
                    estado_id,
                    fecha_creacion
                ) VALUES (?,?,?,?,?,?,?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(query, new String[] { "ID" })) {
            ps.setString(1, cli.getRut());
            ps.setString(2, cli.getNombreCompleto());
            ps.setString(3, cli.getEmail());
            ps.setString(4, cli.getTelefono());
            ps.setString(5, cli.getDireccion());
            ps.setLong(6, cli.getEstadoId());
            ps.setObject(7, cli.getFechaCreacion());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getLong(1);
                throw new SQLException("No se pudo obtener el ID generado");
            }
        }
    }

    public Client findById(Connection conn, Long id) throws SQLException {

        String query = """
                SELECT
                    id,
                    rut,
                    nombre_completo,
                    email,
                    telefono,
                    direccion,
                    estado_id,
                    fecha_creacion
                FROM CLIENTE WHERE id=?
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

    public List<Client> findAll(Connection conn) throws SQLException {
        String query = """
                SELECT
                    id,
                    rut,
                    nombre_completo,
                    email,
                    telefono,
                    direccion,
                    estado_id,
                    fecha_creacion
                FROM CLIENTE ORDER BY id
                """;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {

                List<Client> out = new ArrayList<>();

                while (rs.next())
                    out.add(map(rs));

                return out;
            }
        }
    }

    public Client update(Connection conn, Client cli) throws SQLException {

        String query = """
                UPDATE CLIENTE SET
                    rut=?,
                    nombre_completo=?,
                    email=?,
                    telefono=?,
                    direccion=?,
                    estado_id=?
                WHERE id=?
                """;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, cli.getRut());
            ps.setString(2, cli.getNombreCompleto());
            ps.setString(3, cli.getEmail());
            ps.setString(4, cli.getTelefono());
            ps.setString(5, cli.getDireccion());
            ps.setLong(6, cli.getEstadoId());
            ps.setLong(7, cli.getId());

            int rows = ps.executeUpdate();
            return rows > 0 ? cli : null;
        }
    }

    public int delete(Connection conn, Long id) throws SQLException {

        String query = "DELETE FROM CLIENTE WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            return ps.executeUpdate();
        }
    }

    private Client map(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setId(rs.getLong("id"));
        c.setRut(rs.getString("rut"));
        c.setNombreCompleto(rs.getString("nombre_completo"));
        c.setEmail(rs.getString("email"));
        c.setTelefono(rs.getString("telefono"));
        c.setDireccion(rs.getString("direccion"));
        c.setEstadoId(rs.getLong("estado_id"));
        c.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return c;
    }
}
