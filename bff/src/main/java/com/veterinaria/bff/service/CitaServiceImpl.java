package com.veterinaria.bff.service;

import com.veterinaria.bff.dto.CitaRequestDTO;
import com.veterinaria.bff.dto.CitaResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CitaServiceImpl implements CitaService {

    private final RestTemplate restTemplate;

    @Value("${azure.functions.url}")
    private String functionsUrl;

    public CitaServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public CitaResponseDTO createCita(CitaRequestDTO citaRequestDTO) {
        String url = functionsUrl + "/api/cita";
        return restTemplate.postForObject(url, citaRequestDTO, CitaResponseDTO.class);
    }

    @Override
    public CitaResponseDTO updateCita(Long id, CitaRequestDTO citaRequestDTO) {
        String url = functionsUrl + "/api/cita/" + id;
        HttpEntity<CitaRequestDTO> requestEntity = new HttpEntity<>(citaRequestDTO);
        ResponseEntity<CitaResponseDTO> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, CitaResponseDTO.class);
        return responseEntity.getBody();
    }

    @Override
    public void deleteCita(Long id) {
        String url = functionsUrl + "/api/cita/" + id;
        restTemplate.delete(url);
    }
}
