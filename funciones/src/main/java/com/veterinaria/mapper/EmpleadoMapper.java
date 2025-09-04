package com.veterinaria.mapper;

import com.veterinaria.domain.Empleado;
import com.veterinaria.dto.EmpleadoRequestDTO;
import com.veterinaria.dto.EmpleadoResponseDTO;

import java.sql.Date;

public final class EmpleadoMapper {

    private EmpleadoMapper() {
    }

    public static Empleado toEntity(EmpleadoRequestDTO dto) {
        Empleado empleado = new Empleado();
        empleado.setRut(dto.rut());
        empleado.setNombreCompleto(dto.nombreCompleto());
        empleado.setEmail(dto.email());
        empleado.setTelefono(dto.telefono());
        empleado.setRolId(dto.rolId());
        empleado.setEstadoId(dto.estadoId());
        if (dto.fechaIngreso() != null && !dto.fechaIngreso().isEmpty()) {
            empleado.setFechaIngreso(Date.valueOf(dto.fechaIngreso()));
        }
        return empleado;
    }

    public static EmpleadoResponseDTO toResponse(Empleado empleado) {
        return new EmpleadoResponseDTO(
                empleado.getId(),
                empleado.getRut(),
                empleado.getNombreCompleto(),
                empleado.getEmail(),
                empleado.getTelefono(),
                empleado.getRolId(),
                empleado.getEstadoId(),
                empleado.getFechaIngreso(),
                empleado.getFechaCreacion());
    }
}
