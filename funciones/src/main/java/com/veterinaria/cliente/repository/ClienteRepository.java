package com.veterinaria.cliente.repository;

import com.veterinaria.cliente.domain.Cliente;
import com.veterinaria.mascota.config.DataSourceProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    public Cliente insert(Cliente c) throws Exception {
        try (Connection conn = DataSourceProvider.get().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     """
                     INSERT INTO CLIENTE(
                         rut,
                         nombre_completo,
                         email,
                         telefono,
                         direccion,
                         estado_id
                     ) VALUES (?,?,?,?,?,?)
                     """,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getRut());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getDireccion());
            ps.setInt(6, c.getEstadoId());
            ps.executeUpdate();
            return c;
        }
    }

    public Cliente findById(Integer id) throws Exception {
        try (Connection conn = DataSourceProvider.get().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     """
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
                     """)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    public List<Cliente> findAll() throws Exception {
        try (Connection conn = DataSourceProvider.get().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     """
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
                     """)) {
            List<Cliente> out = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
            return out;
        }
    }

    public Cliente update(Cliente c) throws Exception {
        try (Connection conn = DataSourceProvider.get().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     """
                     UPDATE CLIENTE SET
                         rut=?,
                         nombre_completo=?,
                         email=?,
                         telefono=?,
                         direccion=?,
                         estado_id=?
                     WHERE id=?
                     """)) {
            ps.setString(1, c.getRut());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getDireccion());
            ps.setInt(6, c.getEstadoId());
            ps.setInt(7, c.getId());
            int rows = ps.executeUpdate();
            return rows > 0 ? c : null;
        }
    }

    public int delete(Integer id) throws Exception {
        try (Connection conn = DataSourceProvider.get().getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM CLIENTE WHERE id=?")) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows;
        }
    }

    private Cliente map(ResultSet rs) throws Exception {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id"));
        c.setRut(rs.getString("rut"));
        c.setNombreCompleto(rs.getString("nombre_completo"));
        c.setEmail(rs.getString("email"));
        c.setTelefono(rs.getString("telefono"));
        c.setDireccion(rs.getString("direccion"));
        c.setEstadoId(rs.getInt("estado_id"));
        c.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return c;
    }
}
