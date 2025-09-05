package com.veterinaria.graphql.config;

import graphql.GraphQL;
import graphql.schema.idl.*;
import graphql.schema.DataFetcher;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.veterinaria.service.*; // MascotaService, ClienteService, EspecieService, RazaService, SexoService, MascotaEstadoService
import com.veterinaria.domain.*; // Mascota, etc.
import com.veterinaria.dto.ClientResponseDTO;
import com.veterinaria.dto.MascotaResponseDTO;

public class GraphQLProvider {
  private final GraphQL graphQL;

  public GraphQLProvider(
      MascotaService mascotaSrv,
      ClienteService clienteSrv,
      EspecieService especieSrv,
      RazaService razaSrv,
      SexoService sexoSrv,
      MascotaEstadoService estadoSrv) {

    // Cargar schema
    InputStream s = getClass().getResourceAsStream("/schema.graphqls");
    var typeRegistry = new SchemaParser().parse(new InputStreamReader(s, StandardCharsets.UTF_8));

    // Query resolvers

    // -- MASCOTA --
    DataFetcher<?> mascotaById = env -> {
      Long id = Long.valueOf(env.getArgument("id"));
      return mascotaSrv.getById(id); // devuelve com.veterinaria.domain.Mascota
    };

    // Field resolvers de Mascota
    DataFetcher<?> duenoFetcher = env -> {
      MascotaResponseDTO m = env.getSource();
      return clienteSrv.getById(m.clienteId());
    };

    // mascota.especie
    DataFetcher<?> especieFetcher = env -> {
      MascotaResponseDTO m = env.getSource();
      if (m.especieId() == null)
        return null;
      var loader = env.<Long, Especie>getDataLoader("especieLoader");
      return loader.load(m.especieId());
    };

    // mascota.raza
    DataFetcher<?> razaFetcher = env -> {
      MascotaResponseDTO m = env.getSource();
      return m.razaId() == null ? null : razaSrv.getById(m.razaId());
    };

    // mascota.sexo
    DataFetcher<?> sexoFetcher = env -> {
      MascotaResponseDTO m = env.getSource();
      return sexoSrv.getById(m.sexoId());
    };

    // mascota.estado
    DataFetcher<?> estadoFetcher = env -> {
      MascotaResponseDTO m = env.getSource();
      return estadoSrv.getById(m.estadoId());
    };

    // -- CLIENTE --
    DataFetcher<?> clienteById = env -> {
      Long id = Long.valueOf(env.getArgument("id"));
      return clienteSrv.getById(id);
    };

    DataFetcher<?> clienteMascotas = env -> {
      ClientResponseDTO c = env.getSource();
      return mascotaSrv.getByCliente(c.id());
    };

    // Wiring
    var wiring = RuntimeWiring.newRuntimeWiring()
        .type("Query", b -> b
            .dataFetcher("mascota", mascotaById)
            .dataFetcher("cliente", clienteById))
        .type("Mascota", b -> b
            .dataFetcher("dueno", duenoFetcher)
            .dataFetcher("especie", especieFetcher)
            .dataFetcher("raza", razaFetcher)
            .dataFetcher("sexo", sexoFetcher)
            .dataFetcher("estado", estadoFetcher))
        .type("Cliente", b -> b
            .dataFetcher("mascotas", clienteMascotas))
        .build();

    var schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
    this.graphQL = GraphQL.newGraphQL(schema).build();
  }

  public GraphQL graphQL() {
    return graphQL;
  }
}
