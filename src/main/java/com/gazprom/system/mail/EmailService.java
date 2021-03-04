package com.gazprom.system.mail;

import com.gazprom.system.model.Request;
import com.gazprom.system.model.User;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

public interface EmailService {

  void sendMessageUsingThymeleafTemplate(String to, String subject,
      Map<String, Object> templateModel, String pathToHtml)
      throws IOException, MessagingException;

  Map<String, Object> getTemplate(Request request);

  void sendEmailToUsers(Request request, String pathToHtml, String subject, String reason,
      boolean isConfirm);

  void sendEmailToAdmin(Request request, User user, boolean isAdmin);
}