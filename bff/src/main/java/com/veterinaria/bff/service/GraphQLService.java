package com.veterinaria.bff.service;

import com.veterinaria.bff.dto.GraphQLRequest;

public interface GraphQLService {
    Object executeQuery(GraphQLRequest graphQLRequest);
}
