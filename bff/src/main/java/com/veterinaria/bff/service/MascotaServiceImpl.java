package com.veterinaria.bff.service;

import com.veterinaria.bff.dto.MascotaRequestDTO;
import com.veterinaria.bff.dto.MascotaResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class MascotaServiceImpl implements MascotaService {

    private final RestTemplate restTemplate;

    @Value("${azure.functions.url}")
    private String functionsUrl;

    public MascotaServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<MascotaResponseDTO> getAllMascotas() {
        String url = functionsUrl + "/api/mascota";
        MascotaResponseDTO[] response = restTemplate.getForObject(url, MascotaResponseDTO[].class);
        return Arrays.asList(response);
    }

    @Override
    public MascotaResponseDTO getMascotaById(Long id) {
        String url = functionsUrl + "/api/mascota/" + id;
        return restTemplate.getForObject(url, MascotaResponseDTO.class);
    }

    @Override
    public List<MascotaResponseDTO> getMascotasByClienteId(Long clienteId) {
        String url = functionsUrl + "/api/mascota?clienteId=" + clienteId;
        MascotaResponseDTO[] response = restTemplate.getForObject(url, MascotaResponseDTO[].class);
        return Arrays.asList(response);
    }

    @Override
    public MascotaResponseDTO createMascota(MascotaRequestDTO mascotaRequestDTO) {
        String url = functionsUrl + "/api/mascota";
        return restTemplate.postForObject(url, mascotaRequestDTO, MascotaResponseDTO.class);
    }

    @Override
    public MascotaResponseDTO updateMascota(Long id, MascotaRequestDTO mascotaRequestDTO) {
        String url = functionsUrl + "/api/mascota/" + id;
        restTemplate.put(url, mascotaRequestDTO);
        return getMascotaById(id);
    }

    @Override
    public void deleteMascota(Long id) {
        String url = functionsUrl + "/api/mascota/" + id;
        restTemplate.delete(url);
    }
}
