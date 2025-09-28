package com.veterinaria.notifications;

import com.azure.communication.email.*;
import com.azure.communication.email.models.*;
import java.util.List;

public class EmailSender {
  private static final EmailClient CLIENT = new EmailClientBuilder()
      .connectionString(System.getenv("ACS_EMAIL_CONNECTION_STRING"))
      .buildClient();

  public static void send(String to, String subject, String text, String html) {
    String from = System.getenv("ACS_EMAIL_SENDER");
    EmailMessage msg = new EmailMessage();
    msg.setSenderAddress(from);
    msg.setToRecipients(List.of(new EmailAddress(to)));
    msg.setSubject(subject);
    msg.setBodyPlainText(text);
    msg.setBodyHtml(html);

    CLIENT.beginSend(msg);
  }
}
