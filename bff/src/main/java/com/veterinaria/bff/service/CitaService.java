package com.veterinaria.bff.service;

import com.veterinaria.bff.dto.CitaRequestDTO;
import com.veterinaria.bff.dto.CitaResponseDTO;

public interface CitaService {

    CitaResponseDTO createCita(CitaRequestDTO citaRequestDTO);

    CitaResponseDTO updateCita(Long id, CitaRequestDTO citaRequestDTO);

    void deleteCita(Long id);
}
