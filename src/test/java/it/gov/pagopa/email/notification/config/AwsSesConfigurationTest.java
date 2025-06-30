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
@ContextConfiguration(classes = {AwsSesConfiguration.class})
class AwsSesConfigurationTest {
    @Autowired
    private AwsSesConfiguration awsSesConfiguration;

    @Test
    void sesClientTest(){
        SesClient client = awsSesConfiguration.sesClient();
        Assertions.assertNotNull(client);
    }
}
