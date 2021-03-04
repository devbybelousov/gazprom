package com.gazprom.system.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;

@Configuration
@PropertySource("classpath:mail/emailconfig.properties")
public class SpringMailConfig implements ApplicationContextAware, EnvironmentAware {

  public static final String EMAIL_TEMPLATE_ENCODING = "UTF-8";

  private static final String JAVA_MAIL_FILE = "classpath:mail/javamail.properties";

  private static final String HOST = "mail.server.host";
  private static final String PORT = "mail.server.port";
  private static final String PROTOCOL = "mail.server.protocol";
  private static final String USERNAME = "mail.server.username";
  private static final String PASSWORD = "mail.server.password";

  private ApplicationContext applicationContext;
  private Environment environment;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext)
      throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void setEnvironment(final Environment environment) {
    this.environment = environment;
  }

  @Bean
  public JavaMailSender mailSender() throws IOException {

    final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost(this.environment.getProperty(HOST));
    mailSender
        .setPort(Integer.parseInt(Objects.requireNonNull(this.environment.getProperty(PORT))));
    //mailSender.setProtocol(this.environment.getProperty(PROTOCOL));
    mailSender.setUsername(this.environment.getProperty(USERNAME));
    mailSender.setPassword(this.environment.getProperty(PASSWORD));
    final Properties javaMailProperties = new Properties();
    javaMailProperties.load(this.applicationContext.getResource(JAVA_MAIL_FILE).getInputStream());

    /*mailSender.setUsername("2bf476315f5e79");
    mailSender.setPassword("68048bda733595");*/

    /*Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");*/
    javaMailProperties.put("mail.debug", "true");
    //mailSender.setJavaMailProperties(javaMailProperties);

    return mailSender;

  }

  @Bean
  public TemplateEngine emailTemplateEngine() {
    final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(htmlTemplateResolver());
    return templateEngine;
  }

  private ITemplateResolver htmlTemplateResolver() {
    final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setOrder(1);
    templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
    templateResolver.setPrefix("/mail/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
    templateResolver.setCacheable(false);
    return templateResolver;
  }
}
