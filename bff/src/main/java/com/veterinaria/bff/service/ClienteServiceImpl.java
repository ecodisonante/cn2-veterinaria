package com.veterinaria.bff.service;

import com.veterinaria.bff.dto.ClienteRequestDTO;
import com.veterinaria.bff.dto.ClienteResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
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
        return restTemplate.postForObject(url, clienteRequestDTO, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO updateCliente(Long id, ClienteRequestDTO clienteRequestDTO) {
        String url = functionsUrl + "/api/cliente/" + id;
        restTemplate.put(url, clienteRequestDTO);
        return getClienteById(id);
    }

    @Override
    public void deleteCliente(Long id) {
        String url = functionsUrl + "/api/cliente/" + id;
        restTemplate.delete(url);
    }
}
