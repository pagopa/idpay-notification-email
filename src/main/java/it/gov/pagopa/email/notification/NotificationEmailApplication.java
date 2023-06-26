package it.gov.pagopa.email.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "it.gov.pagopa")
public class NotificationEmailApplication {

  public static void main(String[] args) {
    SpringApplication.run(NotificationEmailApplication.class, args);
  }

}

