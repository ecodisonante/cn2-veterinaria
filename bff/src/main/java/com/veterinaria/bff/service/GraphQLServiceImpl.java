package com.veterinaria.bff.service;

import com.veterinaria.bff.dto.GraphQLRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GraphQLServiceImpl implements GraphQLService {

    private final RestTemplate restTemplate;

    @Value("${azure.functions.url}")
    private String azureFunctionsUrl;

    public GraphQLServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Object executeQuery(GraphQLRequest graphQLRequest) {
        String graphqlUrl = azureFunctionsUrl + "/api/graphql";
        return restTemplate.postForObject(graphqlUrl, graphQLRequest, Object.class);
    }
}
