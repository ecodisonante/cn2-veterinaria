package com.veterinaria.mascota.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MascotaRequestDTO(
        Integer id,
        Integer clienteId,
        String nombre,
        Integer especieId,
        Integer razaId,
        String fechaNacimiento, // ISO yyyy-MM-dd
        Integer sexoId,
        Integer estadoId,
        String fotoUrl,
        String otraEspecie,
        String otraRaza
        ) {
}

/*
 
Ejemplo request json:

{
    "clienteId": 1,
    "nombre": "Boby",
    "especieId": 1,
    "razaId": 1,
    "fechaNacimiento": "2022-01-01",
    "sexoId": 1,
    "estadoId": 1,
    "fotoUrl": "https://example.com/foto.jpg",
    "otraEspecie": "",
    "otraRaza": ""
}

 */

