package com.veterinaria.cliente;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.veterinaria.cliente.dto.ClienteRequestDTO;
import com.veterinaria.cliente.mapper.ClienteMapper;
import com.veterinaria.cliente.service.ClienteService;

import java.util.Optional;

public class ClienteFunction {
    private static final ClienteService svc = new ClienteService();
    private static final ObjectMapper om = new ObjectMapper();

    @FunctionName("Cliente")
    public HttpResponseMessage handle(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                    HttpMethod.DELETE }, route = "cliente/{id?}", authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> req,
            final ExecutionContext ctx) {

        try {
            // leer id optativo
            String idPath = req.getQueryParameters().get("id");

            switch (req.getHttpMethod()) {
                case POST -> {
                    var dto = om.readValue(req.getBody().orElseThrow(), ClienteRequestDTO.class);
                    var saved = svc.crear(ClienteMapper.toEntity(dto));

                    System.out.println(saved);
                    
                    return json(req, ClienteMapper.toDTO(saved), HttpStatus.CREATED);
                }
                case GET -> {
                    if (idPath != null) {
                      var c = svc.obtener(Integer.valueOf(idPath));
                      return (c != null) ? json(req, ClienteMapper.toDTO(c), HttpStatus.OK)
                                         : text(req, "null", HttpStatus.NOT_FOUND);
                    } else {
                      var list = svc.listar().stream().map(ClienteMapper::toDTO).toList();
                      return json(req, list, HttpStatus.OK);
                    }
                  }
                case PUT -> {
                    var dto = om.readValue(req.getBody().orElseThrow(), ClienteRequestDTO.class);
                    var upd = svc.actualizar(ClienteMapper.toEntity(dto));
                    return (upd != null) ? json(req, ClienteMapper.toDTO(upd), HttpStatus.OK)
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
