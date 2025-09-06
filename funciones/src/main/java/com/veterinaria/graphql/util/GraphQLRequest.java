package com.veterinaria.graphql.util;

import java.util.Map;

import lombok.Data;

@Data
public class GraphQLRequest {

    private String query;
    private Map<String, Object> variables;
    private String operationName;

}
