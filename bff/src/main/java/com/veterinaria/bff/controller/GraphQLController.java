package com.veterinaria.bff.controller;

import com.veterinaria.bff.dto.GraphQLRequest;
import com.veterinaria.bff.service.GraphQLService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/graphql")
public class GraphQLController {

    private final GraphQLService graphQLService;

    @Autowired
    public GraphQLController(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    @PostMapping
    public ResponseEntity<Object> executeQuery(@RequestBody GraphQLRequest graphQLRequest) {
        log.info("Procesando peticion GraphQL: {}", graphQLRequest.getQuery());
        try {
            Object result = graphQLService.executeQuery(graphQLRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error al procesar la peticion GraphQL: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
