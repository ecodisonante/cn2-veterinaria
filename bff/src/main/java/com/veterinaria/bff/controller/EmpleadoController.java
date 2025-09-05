package com.veterinaria.bff.controller;

import com.veterinaria.bff.dto.EmpleadoRequestDTO;
import com.veterinaria.bff.dto.EmpleadoResponseDTO;
import com.veterinaria.bff.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoResponseDTO>> getAllEmpleados() {
        return ResponseEntity.ok(empleadoService.getAllEmpleados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> getEmpleadoById(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.getEmpleadoById(id));
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponseDTO> createEmpleado(@RequestBody EmpleadoRequestDTO empleadoRequestDTO) {
        return new ResponseEntity<>(empleadoService.createEmpleado(empleadoRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> updateEmpleado(@PathVariable Long id, @RequestBody EmpleadoRequestDTO empleadoRequestDTO) {
        return ResponseEntity.ok(empleadoService.updateEmpleado(id, empleadoRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Long id) {
        empleadoService.deleteEmpleado(id);
        return ResponseEntity.noContent().build();
    }
}
