package com.veterinaria.bff.service;

import com.veterinaria.bff.dto.EmpleadoRequestDTO;
import com.veterinaria.bff.dto.EmpleadoResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
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
        return restTemplate.postForObject(url, empleadoRequestDTO, EmpleadoResponseDTO.class);
    }

    @Override
    public EmpleadoResponseDTO updateEmpleado(Long id, EmpleadoRequestDTO empleadoRequestDTO) {
        String url = functionsUrl + "/api/empleado/" + id;
        restTemplate.put(url, empleadoRequestDTO);
        return getEmpleadoById(id);
    }

    @Override
    public void deleteEmpleado(Long id) {
        String url = functionsUrl + "/api/empleado/" + id;
        restTemplate.delete(url);
    }
}
