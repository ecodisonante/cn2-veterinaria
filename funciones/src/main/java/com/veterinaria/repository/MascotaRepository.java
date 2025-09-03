package com.veterinaria.repository;

import com.veterinaria.domain.Mascota;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class MascotaRepository {

    public long insert(Connection conn, Mascota m) throws SQLException {

        String query = """
                INSERT INTO MASCOTA(
                    nombre,
                    especie_id,
                    raza_id,
                    fecha_nacimiento,
                    cliente_id,
                    sexo_id,
                    estado_id,
                    foto_url,
                    otra_especie,
                    otra_raza,
                    fecha_creacion)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        // Asignar fecha de creacion
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        m.setFechaCreacion(Timestamp.from(now.toInstant()));

        try (PreparedStatement ps = conn.prepareStatement(query, new String[] { "ID" })) {

            ps.setString(1, m.getNombre());
            ps.setInt(2, m.getEspecieId());
            ps.setInt(3, m.getRazaId());
            if (m.getFechaNacimiento() != null)
                ps.setDate(4, m.getFechaNacimiento());
            else
                ps.setNull(4, Types.DATE);
            ps.setObject(5, m.getClienteId());
            ps.setInt(6, m.getSexoId());
            ps.setInt(7, m.getEstadoId());
            ps.setString(8, m.getFotoUrl());
            ps.setString(9, m.getOtraEspecie());
            ps.setString(10, m.getOtraRaza());
            ps.setObject(11, m.getFechaCreacion());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getLong(1);
                throw new SQLException("No se pudo obtener el ID generado");
            }
        }
    }

    public Mascota findById(Connection conn, Long id) throws SQLException {

        String query = """
                SELECT
                    id,
                    nombre,
                    especie_id,
                    raza_id,
                    fecha_nacimiento,
                    cliente_id,
                    sexo_id,
                    estado_id,
                    foto_url,
                    otra_especie,
                    otra_raza,
                    fecha_creacion
                FROM MASCOTA WHERE id=?
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

    public List<Mascota> findAll(Connection conn) throws SQLException {

        String query = """
                SELECT
                    id,
                    nombre,
                    especie_id,
                    raza_id,
                    fecha_nacimiento,
                    cliente_id,
                    sexo_id,
                    estado_id,
                    foto_url,
                    otra_especie,
                    otra_raza,
                    fecha_creacion
                FROM MASCOTA ORDER BY id
                """;

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            try (ResultSet rs = ps.executeQuery()) {
                List<Mascota> out = new ArrayList<>();
                while (rs.next())
                    out.add(map(rs));
                return out;
            }
        }
    }

    public Mascota update(Connection conn, long id, Mascota m) throws SQLException {

        String query = """
                UPDATE MASCOTA SET
                    nombre=?,
                    especie_id=?,
                    raza_id=?,
                    fecha_nacimiento=?,
                    cliente_id=?,
                    sexo_id=?,
                    estado_id=?,
                    foto_url=?,
                    otra_especie=?,
                    otra_raza=?
                WHERE id=?
                """;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, m.getNombre());
            ps.setInt(2, m.getEspecieId());
            ps.setInt(3, m.getRazaId());
            if (m.getFechaNacimiento() != null)
                ps.setDate(4, m.getFechaNacimiento());
            else
                ps.setNull(4, Types.DATE);
            ps.setObject(5, m.getClienteId());
            ps.setInt(6, m.getSexoId());
            ps.setInt(7, m.getEstadoId());
            ps.setString(8, m.getFotoUrl());
            ps.setString(9, m.getOtraEspecie());
            ps.setString(10, m.getOtraRaza());
            ps.setLong(11, id);

            int rows = ps.executeUpdate();
            return rows > 0 ? m : null;
        }
    }

    public void delete(Connection conn, long id) throws SQLException {

        String query = "DELETE FROM MASCOTA WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Mascota map(ResultSet rs) throws SQLException {
        Mascota m = new Mascota();
        m.setId(rs.getLong("id"));
        m.setNombre(rs.getString("nombre"));
        m.setEspecieId(rs.getInt("especie_id"));
        m.setRazaId(rs.getInt("raza_id"));
        Date f = rs.getDate("fecha_nacimiento");
        m.setFechaNacimiento(f != null ? f : null);
        m.setClienteId(rs.getLong("cliente_id"));
        m.setSexoId(rs.getInt("sexo_id"));
        m.setEstadoId(rs.getInt("estado_id"));
        m.setFotoUrl(rs.getString("foto_url"));
        m.setOtraEspecie(rs.getString("otra_especie"));
        m.setOtraRaza(rs.getString("otra_raza"));
        m.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return m;
    }
}
