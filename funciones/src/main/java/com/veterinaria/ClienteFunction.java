package com.veterinaria;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.veterinaria.dto.ClienteRequestDTO;
import com.veterinaria.service.ClienteService;
import com.veterinaria.util.HttpUtils;

import java.sql.SQLException;
import java.util.Optional;

public class ClienteFunction {
    private static final ClienteService srv = new ClienteService();
    private static final ObjectMapper om = new ObjectMapper();

    @FunctionName("Cliente")
    public HttpResponseMessage handle(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                    HttpMethod.DELETE }, route = "cliente/{id?}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> req,
            final ExecutionContext ctx) {

        try {
            // Obtener ID desde path
            Long id = HttpUtils.getIdFromQueryString(req);
            HttpResponseMessage response;

            switch (req.getHttpMethod()) {
                // Create
                case POST -> response = handlePost(req);
                
                // Read
                case GET -> response = handleGet(id, req);
                
                // Update
                case PUT -> response = handleUpdate(id, req);
                
                // Delete
                case DELETE -> response = handleDelete(id, req);

                // Default
                default -> response = req.createResponseBuilder(HttpStatus.METHOD_NOT_ALLOWED).build();
            }

            return response;

        } catch (Exception e) {
            ctx.getLogger().log(java.util.logging.Level.SEVERE, "Error al ejecutar ClienteFunction", e);
            return HttpUtils.internalErrorMessage(req, e.getMessage());
        }
    }

    private HttpResponseMessage handleGet(Long id, HttpRequestMessage<Optional<String>> req) throws SQLException {
        if (id != null) {
            var result = srv.getById(id);

            if (result != null) {
                return HttpUtils.createMessage(req, result, 200);
            } else
                return HttpUtils.notFoundMessage(req, "Cliente");

        } else {
            return HttpUtils.createMessage(req, srv.getAll(), 200);
        }
    }

    private HttpResponseMessage handlePost(HttpRequestMessage<Optional<String>> req)
            throws JsonProcessingException, SQLException {
        var dto = om.readValue(req.getBody().orElseThrow(), ClienteRequestDTO.class);
        var saved = srv.create(dto);

        return HttpUtils.createMessage(req, saved, 201);
    }

    private HttpResponseMessage handleUpdate(Long id, HttpRequestMessage<Optional<String>> req)
            throws JsonProcessingException, SQLException {
        if (id == null) {
            return HttpUtils.badRequestMessage(req, "El ID es requerido para actualizar un cliente.");
        }

        var dto = om.readValue(req.getBody().orElseThrow(), ClienteRequestDTO.class);
        var updated = srv.update(id, dto);

        if (updated != null) {
            return HttpUtils.createMessage(req, updated, 200);
        } else
            return HttpUtils.notFoundMessage(req, "Cliente");
    }

    private HttpResponseMessage handleDelete(Long id, HttpRequestMessage<Optional<String>> req) throws SQLException {
        if (id == null) {
            return HttpUtils.badRequestMessage(req, "El ID es requerido para eliminar un cliente.");
        }

        srv.delete(id);
        return HttpUtils.createMessage(req, "{\"eliminado\": " + id + "}", 200);
    }
}
