package com.veterinaria.bff.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.bff.dto.CitaRequestDTO;
import com.veterinaria.bff.dto.CitaResponseDTO;
import com.veterinaria.bff.util.Json;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonRequest;
        try {
            jsonRequest = Json.write(citaRequestDTO);
        } catch (JsonProcessingException e) {
            log.error("Error al convertir el objeto a JSON", e);
            return null;
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<CitaResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                CitaResponseDTO.class);
        return response.getBody();
    }

    @Override
    public CitaResponseDTO updateCita(Long id, CitaRequestDTO citaRequestDTO) {
        String url = functionsUrl + "/api/cita/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonRequest;
        try {
            jsonRequest = Json.write(citaRequestDTO);
        } catch (JsonProcessingException e) {
            log.error("Error al convertir el objeto a JSON", e);
            return null;
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequest, headers);
        ResponseEntity<CitaResponseDTO> responseEntity = restTemplate.exchange(
            url, 
            HttpMethod.PUT, 
            requestEntity, 
            CitaResponseDTO.class);
        return responseEntity.getBody();
    }

    @Override
    public void deleteCita(Long id) {
        String url = functionsUrl + "/api/cita/" + id;
        restTemplate.delete(url);
    }
}
