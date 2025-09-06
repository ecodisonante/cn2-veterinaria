package com.veterinaria.bff.controller;

import com.veterinaria.bff.dto.CitaRequestDTO;
import com.veterinaria.bff.dto.CitaResponseDTO;
import com.veterinaria.bff.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    @Autowired
    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public ResponseEntity<CitaResponseDTO> createCita(@RequestBody CitaRequestDTO citaRequestDTO) {
        return new ResponseEntity<>(citaService.createCita(citaRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> updateCita(@PathVariable Long id, @RequestBody CitaRequestDTO citaRequestDTO) {
        return ResponseEntity.ok(citaService.updateCita(id, citaRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        citaService.deleteCita(id);
        return ResponseEntity.noContent().build();
    }
}
