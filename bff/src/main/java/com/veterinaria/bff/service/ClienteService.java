package com.veterinaria.bff.service;

import com.veterinaria.bff.dto.ClienteRequestDTO;
import com.veterinaria.bff.dto.ClienteResponseDTO;

import java.util.List;

public interface ClienteService {

    List<ClienteResponseDTO> getAllClientes();

    ClienteResponseDTO getClienteById(Long id);

    ClienteResponseDTO createCliente(ClienteRequestDTO clienteRequestDTO);

    ClienteResponseDTO updateCliente(Long id, ClienteRequestDTO clienteRequestDTO);

    void deleteCliente(Long id);
}
