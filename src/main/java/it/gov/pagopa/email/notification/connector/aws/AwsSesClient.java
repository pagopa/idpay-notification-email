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
                .destination(Destination.builder().toAddresses(mailRequest.getTo()).build())
                .message(Message.builder()
                        .subject(Content.builder().data(mailRequest.getSubject()).build())
                        .body(Body.builder().html(Content.builder().data(mailRequest.getContent()).charset(CHARSET).build()).build())
                        .build())
                .build();
        SendEmailResponse response = sesClient.sendEmail(request);
        log.info("Email sent! Message ID: {}", response.messageId());
    }
}