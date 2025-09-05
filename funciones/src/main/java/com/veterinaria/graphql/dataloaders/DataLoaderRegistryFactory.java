package com.veterinaria.graphql.dataloaders;

import org.dataloader.*;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.veterinaria.service.EspecieService;

import com.veterinaria.domain.Especie;

public class DataLoaderRegistryFactory {
    private final EspecieService especieService;

    public DataLoaderRegistryFactory(EspecieService especieService) {
        this.especieService = especieService;
    }

    public DataLoaderRegistry create() {
        DataLoaderRegistry reg = new DataLoaderRegistry();

        // Mapped loader: retorna Map<ID, Especie>
        BatchLoaderWithContext<Long, Especie> especieBatch = (keys, ctx) -> CompletableFuture.supplyAsync(() -> {
            List<Long> ids = new ArrayList<>(keys);
            List<Especie> list;
            try {
                list = especieService.findByIds(ids);
            } catch (SQLException e) {
                e.printStackTrace();
                // en caso de error, retornar lista vacía
                list = List.of();
            }

            return list.stream().collect(Collectors.toMap(Especie::getId, e -> e));
        }).thenApply(map -> keys.stream().map(k -> map.get(k)).toList());

        reg.register("especieLoader", DataLoaderFactory.newDataLoader(especieBatch));
        return reg;
    }
}
