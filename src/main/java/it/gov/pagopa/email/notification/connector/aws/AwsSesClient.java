package it.gov.pagopa.email.notification.connector.aws;

import it.gov.pagopa.email.notification.dto.smtp.MailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Slf4j
@Service
public class AwsSesClient implements AwsSesConnector {
    private static final String CHARSET = "UTF-8";

    private final SesClient sesClient;

    public AwsSesClient(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    public void sendEmail(MailRequest mailRequest) {

        SendEmailRequest request = SendEmailRequest.builder()
                .source(mailRequest.getFrom())
                .destination(destination -> destination.toAddresses(mailRequest.getTo()))
                .message(message -> message
                        .subject(contentSubject -> contentSubject.data(mailRequest.getSubject()))
                        .body(body -> body.html(contentHtml -> contentHtml.data(mailRequest.getContent()).charset(CHARSET))))
                .build();
        SendEmailResponse response = sesClient.sendEmail(request);
        log.info("Email sent! Message ID: {}", response.messageId());
    }
}