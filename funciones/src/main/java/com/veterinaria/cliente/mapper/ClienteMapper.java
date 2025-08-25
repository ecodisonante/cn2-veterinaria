package com.veterinaria.cliente.mapper;

import com.veterinaria.cliente.domain.Cliente;
import com.veterinaria.cliente.dto.ClienteRequestDTO;
import com.veterinaria.cliente.dto.ClienteResponseDTO;

import java.time.format.DateTimeFormatter;

public final class ClienteMapper {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ClienteMapper() {}

    public static Cliente toEntity(ClienteRequestDTO d) {
        Cliente c = new Cliente();
        c.setId(d.id());
        c.setRut(d.rut());
        c.setNombreCompleto(d.nombreCompleto());
        c.setEmail(d.email());
        c.setTelefono(d.telefono());
        c.setDireccion(d.direccion());
        c.setEstadoId(d.estadoId());
        return c;
    }

    public static ClienteResponseDTO toDTO(Cliente c) {
        String fechaCreacion = c.getFechaCreacion() != null
                ? c.getFechaCreacion().toLocalDateTime().format(dtf)
                : null;
        return new ClienteResponseDTO(
                c.getId(),
                c.getRut(),
                c.getNombreCompleto(),
                c.getEmail(),
                c.getTelefono(),
                c.getDireccion(),
                c.getEstadoId(),
                fechaCreacion
        );
    }
}
