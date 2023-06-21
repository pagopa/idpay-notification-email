package it.gov.pagopa.email.notification.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "spring.mail.host=HOST_TEST",
                "spring.mail.port=000",
                "spring.mail.username=USER_TEST",
                "spring.mail.password=PWD_TEST",
                "spring.mail.protocol=SMTP",
                "spring.mail.properties.mail.smtp.auth=false",
                "spring.mail.properties.mail.smtp.starttls.enable=false",
                "spring.mail.test-connection=false"
        })
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MailConfiguration.class})
class MailConfigurationTest {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailConfiguration mailConfiguration;

    @Test
    void senderMailTest(){
        JavaMailSender mailSender = mailConfiguration.mailSender();
        Assertions.assertNotNull(mailSender);
    }
}
