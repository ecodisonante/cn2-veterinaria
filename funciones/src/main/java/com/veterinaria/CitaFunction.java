package com.veterinaria;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.veterinaria.dto.CitaRequestDTO;
import com.veterinaria.service.CitaService;
import com.veterinaria.util.HttpUtils;

import java.sql.SQLException;
import java.util.Optional;

public class CitaFunction {
    private static final CitaService srv = new CitaService();
    private static final ObjectMapper om = new ObjectMapper();

    @FunctionName("Cita")
    public HttpResponseMessage handle(
            @HttpTrigger(name = "req", methods = { HttpMethod.POST, HttpMethod.PUT,
                    HttpMethod.DELETE }, route = "cita/{id?}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> req,
            final ExecutionContext ctx) {

        try {
            Long id = HttpUtils.getIdFromQueryString(req);
            HttpResponseMessage response;

            switch (req.getHttpMethod()) {
                case POST -> response = handlePost(req);
                case PUT -> response = handleUpdate(id, req);
                case DELETE -> response = handleDelete(id, req);
                default -> response = req.createResponseBuilder(HttpStatus.METHOD_NOT_ALLOWED).build();
            }

            return response;

        } catch (Exception e) {
            ctx.getLogger().log(java.util.logging.Level.SEVERE, "Error al ejecutar CitaFunction", e);
            return HttpUtils.internalErrorMessage(req, e.getMessage());
        }
    }

    private HttpResponseMessage handlePost(HttpRequestMessage<Optional<String>> req)
            throws JsonProcessingException, SQLException {
        var dto = om.readValue(req.getBody().orElseThrow(), CitaRequestDTO.class);
        var saved = srv.create(dto);
        return HttpUtils.createMessage(req, saved, 201);
    }

    private HttpResponseMessage handleUpdate(Long id, HttpRequestMessage<Optional<String>> req)
            throws JsonProcessingException, SQLException {
        if (id == null) {
            return HttpUtils.badRequestMessage(req, "El ID es requerido para actualizar una cita.");
        }
        var dto = om.readValue(req.getBody().orElseThrow(), CitaRequestDTO.class);
        var updated = srv.update(id, dto);
        if (updated != null) {
            return HttpUtils.createMessage(req, updated, 200);
        } else {
            return HttpUtils.notFoundMessage(req, "Cita");
        }
    }

    private HttpResponseMessage handleDelete(Long id, HttpRequestMessage<Optional<String>> req) throws SQLException {
        if (id == null) {
            return HttpUtils.badRequestMessage(req, "El ID es requerido para eliminar una cita.");
        }
        srv.delete(id);
        return HttpUtils.createMessage(req, "{\"eliminado\": " + id + "}", 200);
    }
}
