package com.veterinaria.bff.dto;

import java.util.Map;

import lombok.Data;

@Data
public class GraphQLRequest {

    private String query;
    private Map<String, Object> variables;
    private String operationName;

}
