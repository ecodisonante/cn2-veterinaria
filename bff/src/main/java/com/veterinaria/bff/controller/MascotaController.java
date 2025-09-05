package com.veterinaria.bff.controller;

import com.veterinaria.bff.dto.MascotaRequestDTO;
import com.veterinaria.bff.dto.MascotaResponseDTO;
import com.veterinaria.bff.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    @Autowired
    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @GetMapping
    public ResponseEntity<List<MascotaResponseDTO>> getMascotas(@RequestParam(required = false) Long clienteId) {
        if (clienteId == null) {
            log.info("Obteniendo todas las mascotas...");
            try {
                return ResponseEntity.ok(mascotaService.getAllMascotas());
            } catch (Exception e) {
                log.error("Error al obtener mascotas: {}", e.getMessage(), e);
                return ResponseEntity.internalServerError().build();
            }
        } else {
            log.info("Obteniendo mascotas por cliente ID: {}", clienteId);
            try {
                return ResponseEntity.ok(mascotaService.getMascotasByClienteId(clienteId));
            } catch (Exception e) {
                log.error("Error al obtener mascotas por cliente ID {}: {}", clienteId, e.getMessage(), e);
                return ResponseEntity.internalServerError().build();
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> getMascotaById(@PathVariable Long id) {
        log.info("Obteniendo mascota con ID: {}", id);
        try {
            return ResponseEntity.ok(mascotaService.getMascotaById(id));
        } catch (Exception e) {
            log.error("Error al obtener mascota con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<MascotaResponseDTO> createMascota(@RequestBody MascotaRequestDTO mascotaRequestDTO) {
        log.info("Creando mascota: {}", mascotaRequestDTO);
        try {
            return new ResponseEntity<>(mascotaService.createMascota(mascotaRequestDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear mascota: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> updateMascota(@PathVariable Long id, @RequestBody MascotaRequestDTO mascotaRequestDTO) {
        log.info("Actualizando mascota con ID: {}", id);
        try {
            return ResponseEntity.ok(mascotaService.updateMascota(id, mascotaRequestDTO));
        } catch (Exception e) {
            log.error("Error al actualizar mascota con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMascota(@PathVariable Long id) {
        log.info("Eliminando mascota con ID: {}", id);
        try {
            mascotaService.deleteMascota(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar mascota con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
