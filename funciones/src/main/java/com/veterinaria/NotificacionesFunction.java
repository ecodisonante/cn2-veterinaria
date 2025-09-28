package com.veterinaria;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.veterinaria.service.NotificacionesEmailService;

public class NotificacionesFunction {
  NotificacionesEmailService emailService = new NotificacionesEmailService();
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @FunctionName("Notificaciones")
  public void run(@EventGridTrigger(name = "event") String eventJson, final ExecutionContext ctx) {
    try {
      JsonNode root = MAPPER.readTree(eventJson);
      String type = root.has("eventType") ? root.get("eventType").asText()
          : root.has("type") ? root.get("type").asText() : null;
      JsonNode data = root.get("data");
      ctx.getLogger().info("Evento: " + type + " data=" + data);

      emailService.enviarEmail(type, data);

    } catch (Exception e) {
      ctx.getLogger().severe("Error Notificaciones: " + e.getMessage());
      throw new RuntimeException(e); // permite reintentos de Event Grid
    }
  }

}
