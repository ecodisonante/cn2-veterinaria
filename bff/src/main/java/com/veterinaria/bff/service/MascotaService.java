package com.veterinaria.bff.service;

import com.veterinaria.bff.dto.MascotaRequestDTO;
import com.veterinaria.bff.dto.MascotaResponseDTO;

import java.util.List;

public interface MascotaService {

    List<MascotaResponseDTO> getAllMascotas();

    MascotaResponseDTO getMascotaById(Long id);

    List<MascotaResponseDTO> getMascotasByClienteId(Long clienteId);

    MascotaResponseDTO createMascota(MascotaRequestDTO mascotaRequestDTO);

    MascotaResponseDTO updateMascota(Long id, MascotaRequestDTO mascotaRequestDTO);

    void deleteMascota(Long id);
}
