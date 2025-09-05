package com.veterinaria;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.veterinaria.graphql.util.GraphQLRequest;
import com.veterinaria.graphql.config.GraphQLProvider;
import com.veterinaria.graphql.dataloaders.DataLoaderRegistryFactory;

import graphql.ExecutionInput;

import java.util.Map;
import java.util.Optional;

import com.veterinaria.service.*; // instancias simples para el ejemplo

public class GraphQLFunction {

  private final GraphQLProvider provider;
  private final DataLoaderRegistryFactory registryFactory;

  public GraphQLFunction() {
    // O usa inyección propia si ya la tienes
    var mascotaSrv = new MascotaService();
    var clienteSrv = new ClienteService();
    var especieSrv = new EspecieService();
    var razaSrv = new RazaService();
    var sexoSrv = new SexoService();
    var estadoSrv = new MascotaEstadoService();

    this.provider = new GraphQLProvider(mascotaSrv, clienteSrv, especieSrv, razaSrv, sexoSrv, estadoSrv);
    this.registryFactory = new DataLoaderRegistryFactory(especieSrv); // mínimo viable (solo especie)
  }

  @FunctionName("GraphQL")
  public HttpResponseMessage run(
      @HttpTrigger(name = "req", methods = { HttpMethod.POST },
                   authLevel = AuthorizationLevel.ANONYMOUS,
                   route = "graphql")
      HttpRequestMessage<GraphQLRequest> req,
      final ExecutionContext ctx) {

    try {
      var body = Optional.ofNullable(req.getBody()).orElseGet(GraphQLRequest::new);
      var vars = body.getVariables() == null ? Map.<String,Object>of() : body.getVariables();

      var exec = ExecutionInput.newExecutionInput()
          .query(body.getQuery())
          .operationName(body.getOperationName())
          .variables(vars)
          .dataLoaderRegistry(registryFactory.create()) // <-- vincula DataLoader
          .build();

      var result = provider.graphQL().execute(exec);

      return req.createResponseBuilder(HttpStatus.OK)
        .header("Content-Type", "application/json")
        .body(result.toSpecification())
        .build();

    } catch (Exception e) {
      ctx.getLogger().severe("GraphQL error: " + e.getMessage());
      return req.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", e.getMessage()))
        .build();
    }
  }
}
