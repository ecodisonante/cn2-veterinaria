package com.veterinaria.bff.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.bff.dto.ClienteRequestDTO;
import com.veterinaria.bff.dto.ClienteResponseDTO;
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
public class ClienteServiceImpl implements ClienteService {

    private final RestTemplate restTemplate;

    @Value("${azure.functions.url}")
    private String functionsUrl;

    public ClienteServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<ClienteResponseDTO> getAllClientes() {
        String url = functionsUrl + "/api/cliente";
        ClienteResponseDTO[] response = restTemplate.getForObject(url, ClienteResponseDTO[].class);
        return Arrays.asList(response);
    }

    @Override
    public ClienteResponseDTO getClienteById(Long id) {
        String url = functionsUrl + "/api/cliente/" + id;
        return restTemplate.getForObject(url, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO createCliente(ClienteRequestDTO clienteRequestDTO) {
        String url = functionsUrl + "/api/cliente";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonRequest;
        try {
            jsonRequest = Json.write(clienteRequestDTO);
        } catch (JsonProcessingException e) {
            log.error("Error al convertir el objeto a JSON", e);
            return null;
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<ClienteResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                ClienteResponseDTO.class);
        return response.getBody();
    }

    @Override
    public ClienteResponseDTO updateCliente(Long id, ClienteRequestDTO clienteRequestDTO) {
        String url = functionsUrl + "/api/cliente/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonRequest;
        try {
            jsonRequest = Json.write(clienteRequestDTO);
        } catch (JsonProcessingException e) {
            log.error("Error al convertir el objeto a JSON", e);
            return null;
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<ClienteResponseDTO> response = restTemplate.exchange(
            url, 
            HttpMethod.PUT, 
            requestEntity, 
            ClienteResponseDTO.class);
        return response.getBody();   
    }

    @Override
    public void deleteCliente(Long id) {
        String url = functionsUrl + "/api/cliente/" + id;
        restTemplate.delete(url);
    }
}
