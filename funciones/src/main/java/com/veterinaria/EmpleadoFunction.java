package com.veterinaria;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.veterinaria.dto.EmpleadoRequestDTO;
import com.veterinaria.service.EmpleadoService;
import com.veterinaria.util.HttpUtils;

import java.sql.SQLException;
import java.util.Optional;

public class EmpleadoFunction {
    private static final EmpleadoService srv = new EmpleadoService();
    private static final ObjectMapper om = new ObjectMapper();

    @FunctionName("Empleado")
    public HttpResponseMessage handle(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                    HttpMethod.DELETE }, route = "empleado/{id?}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> req,
            final ExecutionContext ctx) {

        try {
            Long id = HttpUtils.getIdFromQueryString(req);
            HttpResponseMessage response;

            switch (req.getHttpMethod()) {
                case POST -> response = handlePost(req);
                case GET -> response = handleGet(id, req);
                case PUT -> response = handleUpdate(id, req);
                case DELETE -> response = handleDelete(id, req);
                default -> response = req.createResponseBuilder(HttpStatus.METHOD_NOT_ALLOWED).build();
            }

            return response;

        } catch (Exception e) {
            ctx.getLogger().log(java.util.logging.Level.SEVERE, "Error al ejecutar EmpleadoFunction", e);
            return HttpUtils.internalErrorMessage(req, e.getMessage());
        }
    }

    private HttpResponseMessage handleGet(Long id, HttpRequestMessage<Optional<String>> req) throws SQLException {
        if (id != null) {
            var result = srv.getById(id);
            if (result != null) {
                return HttpUtils.createMessage(req, result, 200);
            } else {
                return HttpUtils.notFoundMessage(req, "Empleado");
            }
        } else {
            return HttpUtils.createMessage(req, srv.getAll(), 200);
        }
    }

    private HttpResponseMessage handlePost(HttpRequestMessage<Optional<String>> req)
            throws JsonProcessingException, SQLException {
        var dto = om.readValue(req.getBody().orElseThrow(), EmpleadoRequestDTO.class);
        var saved = srv.create(dto);
        return HttpUtils.createMessage(req, saved, 201);
    }

    private HttpResponseMessage handleUpdate(Long id, HttpRequestMessage<Optional<String>> req)
            throws JsonProcessingException, SQLException {
        if (id == null) {
            return HttpUtils.badRequestMessage(req, "El ID es requerido para actualizar un empleado.");
        }
        var dto = om.readValue(req.getBody().orElseThrow(), EmpleadoRequestDTO.class);
        var updated = srv.update(id, dto);
        if (updated != null) {
            return HttpUtils.createMessage(req, updated, 200);
        } else {
            return HttpUtils.notFoundMessage(req, "Empleado");
        }
    }

    private HttpResponseMessage handleDelete(Long id, HttpRequestMessage<Optional<String>> req) throws SQLException {
        if (id == null) {
            return HttpUtils.badRequestMessage(req, "El ID es requerido para eliminar un empleado.");
        }
        srv.delete(id);
        return HttpUtils.createMessage(req, "{\"eliminado\": " + id + "}", 200);
    }
}
