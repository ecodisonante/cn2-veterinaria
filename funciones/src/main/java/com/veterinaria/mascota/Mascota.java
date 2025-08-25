package com.veterinaria.mascota;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.veterinaria.mascota.dto.MascotaRequestDTO;
import com.veterinaria.mascota.mapper.MascotaMapper;
import com.veterinaria.mascota.service.MascotaService;

import java.util.Optional;

public class Mascota {
    private static final MascotaService svc = new MascotaService();
    private static final ObjectMapper om = new ObjectMapper();

    @FunctionName("Mascota")
    public HttpResponseMessage handle(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                    HttpMethod.DELETE }, route = "mascota/{id?}", authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> req,
            final ExecutionContext ctx) {

        try {
            // leer id optativo
            String idPath = req.getQueryParameters().get("id");

            switch (req.getHttpMethod()) {
                case POST -> {
                    var dto = om.readValue(req.getBody().orElseThrow(), MascotaRequestDTO.class);
                    var saved = svc.crear(MascotaMapper.toEntity(dto));
                    return json(req, MascotaMapper.toDTO(saved), HttpStatus.CREATED);
                }
                case GET -> {
                    if (idPath != null) {
                      var m = svc.obtener(Integer.valueOf(idPath));
                      return (m != null) ? json(req, MascotaMapper.toDTO(m), HttpStatus.OK)
                                         : text(req, "null", HttpStatus.NOT_FOUND);
                    } else {
                      var list = svc.listar().stream().map(MascotaMapper::toDTO).toList();
                      return json(req, list, HttpStatus.OK);
                    }
                  }
                case PUT -> {
                    var dto = om.readValue(req.getBody().orElseThrow(), MascotaRequestDTO.class);
                    var upd = svc.actualizar(MascotaMapper.toEntity(dto));
                    return (upd != null) ? json(req, MascotaMapper.toDTO(upd), HttpStatus.OK)
                            : text(req, "{\"error\":\"no encontrado\"}", HttpStatus.NOT_FOUND);
                }
                case DELETE -> {
                    var id = (idPath != null) ? Integer.valueOf(idPath)
                                 : Integer.valueOf(req.getQueryParameters().get("id"));
                    int count = svc.eliminar(id);
                    return text(req, "{\"deleted\":"+count+"}", count>0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
                }
                default -> {
                    return text(req, "{\"error\":\"Método no soportado\"}", HttpStatus.METHOD_NOT_ALLOWED);
                }
            }
        } catch (Exception e) {
            ctx.getLogger().severe(e.getMessage());
            return text(req, "{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpResponseMessage json(HttpRequestMessage<?> req, Object body, HttpStatus code) throws Exception {
        return req.createResponseBuilder(code).header("Content-Type", "application/json")
                .body(om.writeValueAsString(body)).build();
    }

    private HttpResponseMessage text(HttpRequestMessage<?> req, String body, HttpStatus code) {
        return req.createResponseBuilder(code).header("Content-Type", "application/json").body(body).build();
    }
}
