package it.gov.pagopa.email.notification.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.services.ses.SesClient;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "app.aws.region=LOCATION"
        })
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AwsSesConfig.class})
class AwsSesConfigTest {
    @Autowired
    private AwsSesConfig awsSesConfig;

    @Test
    void sesClientTest(){
        SesClient client = awsSesConfig.sesClient();
        Assertions.assertNotNull(client);
    }
}
