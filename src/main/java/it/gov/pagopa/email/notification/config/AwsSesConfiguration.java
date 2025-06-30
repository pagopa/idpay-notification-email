package it.gov.pagopa.email.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class AwsSesConfiguration {
    private final String region;

    public AwsSesConfiguration(@Value("${app.aws.region}") String region) {
        this.region = region;
    }


    /**
     * Creates a configured {@link SesClient} for sending emails via Amazon SES.
     * Uses the region from application properties and credentials from environment variables.
     *
     * @return a configured SesClient instance
     */

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .region(Region.of(region))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }
}