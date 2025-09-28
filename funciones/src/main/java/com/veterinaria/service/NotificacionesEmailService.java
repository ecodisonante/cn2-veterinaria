package com.veterinaria.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.veterinaria.events.CrudAction;
import com.veterinaria.notifications.EmailSender;

public class NotificacionesEmailService {
    private static final ClienteService clienteService = new ClienteService();

    public void enviarEmail(String type, JsonNode data) throws IOException, SQLException {
        // enviar email
        if (!esTipoValido(type)) {
            throw new IllegalArgumentException("Tipo de evento no soportado: " + type);
        }

        String[] parts = type.split("\\.");
        CrudAction action = CrudAction.valueOf(parts[3].toUpperCase());

        if (parts[2].equals("mascota")) {
            enviarEmailMascota(data, action);
        } else if (parts[2].equals("cliente")) {
            enviarEmailCliente(data, action);
        }
    }

    private void enviarEmailMascota(JsonNode data, CrudAction action) throws IOException, SQLException {
        long clienteId = data.path("clienteId").asLong(-1);
        var cliente = clienteService.getById(clienteId);
        if (cliente == null || cliente.email() == null || cliente.email().isBlank()) {
            throw new IllegalArgumentException("Cliente no encontrado o sin email");
        }

        String subject = switch (action) {
            case CREATED -> "Nueva mascota registrada";
            case UPDATED -> "Datos de mascota actualizados";
            case DELETED -> "Registro de mascota eliminado";
        };
        String mascotaNombre = data.path("nombre").asText("");

        Map<String, String> placeholders = Map.of(
                "--nombre--", cliente.nombre(),
                "--nombre_mascota--", mascotaNombre);

        enviarNotificacion("Mascota", action, cliente.email(), subject, placeholders);
    }

    private void enviarEmailCliente(JsonNode data, CrudAction action) throws IOException {
        String email = data.path("email").asText();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email del cliente no encontrado en el evento");
        }

        String subject = switch (action) {
            case CREATED -> "Bienvenido a VETCARE";
            case UPDATED -> "Tus datos en VETCARE han sido actualizados";
            case DELETED -> "Lamentamos verte partir de VETCARE";
        };
        String nombreCliente = data.path("nombre").asText("");
        
        Map<String, String> placeholders = Map.of(
                "--nombre--", nombreCliente);

        enviarNotificacion("Cliente", action, email, subject, placeholders);
    }

    private void enviarNotificacion(String entity, CrudAction action, String toEmail, String subject,
            Map<String, String> placeholders) throws IOException {
        String templatePath = "/EmailTemplates/" + entity
                + action.name().substring(0, 1).toUpperCase()
                + action.name().substring(1).toLowerCase();

        try (InputStream htmlStream = getClass().getResourceAsStream(templatePath + ".html");
                InputStream txtStream = getClass().getResourceAsStream(templatePath + ".txt")) {

            if (htmlStream == null || txtStream == null) {
                throw new IOException("No se encontraron las plantillas para: " + templatePath);
            }

            String htmlTemplate = new String(htmlStream.readAllBytes(), StandardCharsets.UTF_8);
            String txtTemplate = new String(txtStream.readAllBytes(), StandardCharsets.UTF_8);

            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                htmlTemplate = htmlTemplate.replace(entry.getKey(), entry.getValue());
                txtTemplate = txtTemplate.replace(entry.getKey(), entry.getValue());
            }

            EmailSender.send(toEmail, subject, txtTemplate, htmlTemplate);
        }
    }

    public void enviarSMS(String phoneNumber, String message) {
        // Proximamente
    }

    public void enviarPushNotification(String deviceToken, String title, String message) {
        // Proximamente
    }

    private boolean esTipoValido(String type) {
        List<String> tiposValidos = List.of(
                "com.veterinaria.mascota.created",
                "com.veterinaria.mascota.updated",
                "com.veterinaria.mascota.deleted",
                "com.veterinaria.cliente.created",
                "com.veterinaria.cliente.updated",
                "com.veterinaria.cliente.deleted");

        return tiposValidos.contains(type.toLowerCase());
    }
}
