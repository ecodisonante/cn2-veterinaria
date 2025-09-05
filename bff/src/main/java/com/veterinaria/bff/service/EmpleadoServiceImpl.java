package com.veterinaria.bff.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.bff.dto.EmpleadoRequestDTO;
import com.veterinaria.bff.dto.EmpleadoResponseDTO;
import com.veterinaria.bff.util.Json;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EmpleadoServiceImpl implements EmpleadoService {

    private final RestTemplate restTemplate;

    @Value("${azure.functions.url}")
    private String functionsUrl;

    public EmpleadoServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<EmpleadoResponseDTO> getAllEmpleados() {
        String url = functionsUrl + "/api/empleado";
        EmpleadoResponseDTO[] response = restTemplate.getForObject(url, EmpleadoResponseDTO[].class);
        return Arrays.asList(response);
    }

    @Override
    public EmpleadoResponseDTO getEmpleadoById(Long id) {
        String url = functionsUrl + "/api/empleado/" + id;
        return restTemplate.getForObject(url, EmpleadoResponseDTO.class);
    }

    @Override
    public EmpleadoResponseDTO createEmpleado(EmpleadoRequestDTO empleadoRequestDTO) {
        String url = functionsUrl + "/api/empleado";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonRequest;
        try {
            jsonRequest = Json.write(empleadoRequestDTO);
        } catch (JsonProcessingException e) {
            log.error("Error al convertir el objeto a JSON", e);
            return null;
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<EmpleadoResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                EmpleadoResponseDTO.class);
        return response.getBody();
    }

    @Override
    public EmpleadoResponseDTO updateEmpleado(Long id, EmpleadoRequestDTO empleadoRequestDTO) {
        String url = functionsUrl + "/api/empleado/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonRequest;
        try {
            jsonRequest = Json.write(empleadoRequestDTO);
        } catch (JsonProcessingException e) {
            log.error("Error al convertir el objeto a JSON", e);
            return null;
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<EmpleadoResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                EmpleadoResponseDTO.class);
        return response.getBody();
    }

    @Override
    public void deleteEmpleado(Long id) {
        String url = functionsUrl + "/api/empleado/" + id;
        restTemplate.delete(url);
    }
}
