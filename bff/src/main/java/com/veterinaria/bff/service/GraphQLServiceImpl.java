package com.veterinaria.bff.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.bff.dto.GraphQLRequest;
import com.veterinaria.bff.dto.MascotaResponseDTO;
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
public class GraphQLServiceImpl implements GraphQLService {

    private final RestTemplate restTemplate;

    @Value("${azure.functions.url}")
    private String azureFunctionsUrl;

    public GraphQLServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Object executeQuery(GraphQLRequest graphQLRequest) {
        String url = azureFunctionsUrl + "/api/graphql";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonRequest;
        try {
            jsonRequest = Json.write(graphQLRequest);
        } catch (JsonProcessingException e) {
            log.error("Error al convertir el objeto a JSON", e);
            return null;
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                Object.class);
        return response.getBody();
    }
}
