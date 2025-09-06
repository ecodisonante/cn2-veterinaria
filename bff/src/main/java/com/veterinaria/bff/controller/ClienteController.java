package com.veterinaria.bff.controller;

import com.veterinaria.bff.dto.ClienteRequestDTO;
import com.veterinaria.bff.dto.ClienteResponseDTO;
import com.veterinaria.bff.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientes() {
        log.info("Obteniendo todos los clientes...");
        try {
            return ResponseEntity.ok(clienteService.getAllClientes());
        } catch (Exception e) {
            log.error("Error al obtener clientes: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getClienteById(@PathVariable Long id) {
        log.info("Obteniendo cliente con ID: {}", id);
        try {
            return ResponseEntity.ok(clienteService.getClienteById(id));
        } catch (Exception e) {
            log.error("Error al obtener cliente con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> createCliente(@RequestBody ClienteRequestDTO clienteRequestDTO) {
        log.info("Creando cliente: {}", clienteRequestDTO);
        try {
            return new ResponseEntity<>(clienteService.createCliente(clienteRequestDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear cliente: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> updateCliente(@PathVariable Long id, @RequestBody ClienteRequestDTO clienteRequestDTO) {
        log.info("Actualizando cliente con ID: {}", id);
        try {
            return ResponseEntity.ok(clienteService.updateCliente(id, clienteRequestDTO));
        } catch (Exception e) {
            log.error("Error al actualizar cliente con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        log.info("Eliminando cliente con ID: {}", id);
        try {
            clienteService.deleteCliente(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar cliente con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
