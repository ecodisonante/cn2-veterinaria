package com.veterinaria.mascota.repository;

import com.veterinaria.mascota.domain.Mascota;
import com.veterinaria.mascota.config.DataSourceProvider;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MascotaRepository {

    public Mascota insert(Mascota m) throws Exception {
        try (Connection c = DataSourceProvider.get().getConnection();
                PreparedStatement ps = c.prepareStatement("""
                        INSERT INTO MASCOTA( \
                            nombre, \
                            especie_id, \
                            raza_id, \
                            fecha_nacimiento, \
                            cliente_id, \
                            sexo_id, \
                            estado_id, \
                            foto_url, \
                            otra_especie, \
                            otra_raza) \
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""",
                        Statement.RETURN_GENERATED_KEYS)) {

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
            ps.executeUpdate();
            return m;
        }
    }

    public Mascota findById(Integer id) throws Exception {
        try (Connection c = DataSourceProvider.get().getConnection();
                PreparedStatement ps = c.prepareStatement("""
                        SELECT \
                            id, \
                            nombre, \
                            especie_id, \
                            raza_id, \
                            fecha_nacimiento, \
                            cliente_id, \
                            sexo_id, \
                            estado_id, \
                            foto_url, \
                            otra_especie, \
                            otra_raza \
                        FROM MASCOTA WHERE id=?""")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return map(rs);
                return null;
            }
        }
    }

    public List<Mascota> findAll() throws Exception {
        try (Connection c = DataSourceProvider.get().getConnection();
                PreparedStatement ps = c.prepareStatement("""
                        SELECT \
                            id, \
                            nombre, \
                            especie_id, \
                            raza_id, \
                            fecha_nacimiento, \
                            cliente_id, \
                            sexo_id, \
                            estado_id, \
                            foto_url, \
                            otra_especie, \
                            otra_raza \
                        FROM MASCOTA ORDER BY id""");

                ResultSet rs = ps.executeQuery()) {
            List<Mascota> out = new ArrayList<>();
            while (rs.next())
                out.add(map(rs));
            return out;
        }
    }

    public Mascota update(Mascota m) throws Exception {
        try (Connection c = DataSourceProvider.get().getConnection();
                PreparedStatement ps = c.prepareStatement("""
                        UPDATE MASCOTA SET \
                            nombre=?, \
                            especie_id=?, \
                            raza_id=?, \
                            fecha_nacimiento=?, \
                            cliente_id=?, \
                            sexo_id=?, \
                            estado_id=?, \
                            foto_url=?, \
                            otra_especie=?, \
                            otra_raza=? \
                        WHERE id=?""")) {
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
            ps.setInt(11, m.getId());
            int rows = ps.executeUpdate();
            return rows > 0 ? m : null;
        }
    }

    public int delete(Integer id) throws Exception {
        try (Connection c = DataSourceProvider.get().getConnection();
                PreparedStatement ps = c.prepareStatement("DELETE FROM MASCOTA WHERE id=?")) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows;
        }
    }

    private Mascota map(ResultSet rs) throws Exception {
        Mascota m = new Mascota();
        m.setId(rs.getInt("id"));
        m.setNombre(rs.getString("nombre"));
        m.setEspecieId(rs.getInt("especie_id"));
        m.setRazaId(rs.getInt("raza_id"));
        Date f = rs.getDate("fecha_nacimiento");
        m.setFechaNacimiento(f != null ? f : null);
        m.setClienteId((Integer) rs.getObject("cliente_id"));
        m.setSexoId(rs.getInt("sexo_id"));
        m.setEstadoId(rs.getInt("estado_id"));
        m.setFotoUrl(rs.getString("foto_url"));
        m.setOtraEspecie(rs.getString("otra_especie"));
        m.setOtraRaza(rs.getString("otra_raza"));
        return m;
    }
}

