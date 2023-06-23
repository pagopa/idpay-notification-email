package it.gov.pagopa.email.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String protocol;
    private final String smtpAuth;
    private final String smtpStartTLS;
    private final String testConnection;

    public MailConfiguration(@Value("${spring.mail.host}") String host,
                             @Value("${spring.mail.port}") String port,
                             @Value("${spring.mail.username}") String username,
                             @Value("${spring.mail.password}") String password,
                             @Value("${spring.mail.protocol}") String protocol,
                             @Value("${spring.mail.properties.mail.smtp.auth}") String smtpAuth,
                             @Value("${spring.mail.properties.mail.smtp.starttls.enable}") String smtpStartTLS,
                             @Value("${spring.mail.test-connection}") String testConnection){
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.protocol = protocol;
        this.smtpAuth = smtpAuth;
        this.smtpStartTLS = smtpStartTLS;
        this.testConnection = testConnection;
    }

    @Bean
    public JavaMailSender mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.host);
        mailSender.setPort(Integer.parseInt(this.port));
        mailSender.setUsername(this.username);
        mailSender.setPassword(this.password);
        mailSender.setProtocol(this.protocol);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", this.smtpAuth);
        props.put("mail.smtp.starttls.enable", this.smtpStartTLS);
        props.put("mail.test-connection", this.testConnection);
        return mailSender;
    }

}
