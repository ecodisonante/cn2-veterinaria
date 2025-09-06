package com.veterinaria.domain;

import lombok.Data;

@Data
public class Raza {
    private Long id;
    private Long especieId;
    private String nombre;
}
