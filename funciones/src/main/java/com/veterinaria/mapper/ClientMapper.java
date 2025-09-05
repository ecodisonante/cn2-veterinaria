package com.veterinaria.mapper;

import com.veterinaria.domain.Client;
import com.veterinaria.dto.ClienteRequestDTO;
import com.veterinaria.dto.ClientResponseDTO;

import java.time.format.DateTimeFormatter;

public final class ClientMapper {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ClientMapper() {
    }

    public static Client toEntity(ClienteRequestDTO d) {
        Client c = new Client();
        c.setRut(d.rut());
        c.setNombreCompleto(d.nombre());
        c.setEmail(d.email());
        c.setTelefono(d.telefono());
        c.setDireccion(d.direccion());
        c.setEstadoId(d.estadoId());
        return c;
    }

    public static ClientResponseDTO toResponse(Client cli) {

        String fechaCreacion = "";
        if (cli.getFechaCreacion() != null)
            fechaCreacion = cli.getFechaCreacion().toLocalDateTime().format(dtf);

        return new ClientResponseDTO(
                cli.getId(),
                cli.getRut(),
                cli.getNombreCompleto(),
                cli.getEmail(),
                cli.getTelefono(),
                cli.getDireccion(),
                cli.getEstadoId(),
                fechaCreacion);
    }
}
