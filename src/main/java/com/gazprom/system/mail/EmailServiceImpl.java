package com.gazprom.system.mail;

import com.gazprom.system.model.Privilege;
import com.gazprom.system.model.Request;
import com.gazprom.system.model.User;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service("EmailService")
public class EmailServiceImpl implements EmailService {

  private final String NOREPLY_ADDRESS = "noreply@gazprom.com";
  private final String LOGO_IMAGE = "mail-logo.svg";

  private static final String SVG_MIME = "image/svg";

  @Autowired
  private JavaMailSender emailSender;

  @Qualifier("emailTemplateEngine")
  @Autowired
  private TemplateEngine htmlTemplateEngine;

  @Override
  public void sendMessageUsingThymeleafTemplate(
      String to, String subject, Map<String, Object> templateModel, String pathToHtml)
      throws MessagingException {

    Context thymeleafContext = new Context();
    thymeleafContext.setVariables(templateModel);

    String htmlBody = this.htmlTemplateEngine.process(pathToHtml, thymeleafContext);

    sendHtmlMessage(to, subject, htmlBody);
  }

  @Async
  public void sendHtmlMessage(String to, String subject, String htmlBody)
      throws MessagingException {

    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setFrom(NOREPLY_ADDRESS);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlBody, true);
    /*final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
    helper.addInline(imageResourceName, imageSource, imageContentType);*/
    //helper.addInline("logo", new ClassPathResource(LOGO_IMAGE), SVG_MIME);
    emailSender.send(message);
  }

  @Override
  public Map<String, Object> getTemplate(Request request) {
    Date dateNow = new Date(request.getFilingDate().getTime());
    SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
    StringBuilder roles = new StringBuilder();

    for (Privilege role : request.getInformationSystem().getPrivileges()) {
      roles.append(role.getTitle()).append(", ");
    }

    roles = new StringBuilder(roles.substring(0, roles.length() - 2));

    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("user", "");
    templateModel.put("system", request.getInformationSystem().getTitle());
    templateModel.put("roles", roles);
    templateModel.put("dateShipped", formatForDateNow.format(dateNow));
    templateModel.put("admin", false);
    templateModel.put("href", "");
    //templateModel.put("logo", LOGO_IMAGE);
    return templateModel;
  }

  @Override
  @Async
  public void sendEmailToUsers(Request request, String pathToHtml, String subject, String reason,
      boolean isConfirm) {
    Map<String, Object> templateModel = getTemplate(request);
    SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
    for (User user : request.getUsers()) {
      String userName = user.getLastName() + " " + user.getName() + " " + user.getMiddleName();
      templateModel.replace("user", userName);
      if (!reason.equals("")) {
        templateModel.put("reason", reason);
      }
      if (isConfirm) {
        templateModel.put("dateExpiry",
            formatForDateNow.format(new java.util.Date(request.getExpiryDate().getTime())));
      }
      try {
        sendMessageUsingThymeleafTemplate(user.getEmail(), subject, templateModel, pathToHtml);
      } catch (MessagingException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  @Async
  public void sendEmailToAdmin(Request request, User user, boolean isAdmin) {
    Map<String, Object> templateModel = getTemplate(request);
    String userName = user.getLastName() + " " + user.getName() + " " + user.getMiddleName();
    templateModel.replace("user", userName);
    templateModel.replace("admin", true);
    templateModel.replace("href",
        "http://u97708.test-handyhost.ru/admin=" + user.getId() + "/info/" + request.getId());
    try {
      sendMessageUsingThymeleafTemplate(user.getEmail(), "Подтверждение заявки", templateModel,
          "html/creating");
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

}
