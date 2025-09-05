package com.veterinaria.bff.service;

import com.veterinaria.bff.dto.EmpleadoRequestDTO;
import com.veterinaria.bff.dto.EmpleadoResponseDTO;

import java.util.List;

public interface EmpleadoService {

    List<EmpleadoResponseDTO> getAllEmpleados();

    EmpleadoResponseDTO getEmpleadoById(Long id);

    EmpleadoResponseDTO createEmpleado(EmpleadoRequestDTO empleadoRequestDTO);

    EmpleadoResponseDTO updateEmpleado(Long id, EmpleadoRequestDTO empleadoRequestDTO);

    void deleteEmpleado(Long id);
}
