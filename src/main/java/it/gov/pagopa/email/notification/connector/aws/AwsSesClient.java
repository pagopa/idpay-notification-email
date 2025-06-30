package it.gov.pagopa.email.notification.connector.aws;

import it.gov.pagopa.email.notification.dto.smtp.MailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Slf4j
@Service
public class AwsSesClient implements AwsSesConnector {

    @Autowired
    private SesClient sesClient;

    public void sendEmail(MailRequest mailRequest) {

        SendEmailRequest request = SendEmailRequest.builder()
                .source(mailRequest.getFrom())
                .destination(Destination.builder().toAddresses(mailRequest.getTo()).build())
                .message(Message.builder()
                        .subject(Content.builder().data(mailRequest.getSubject()).build())
                        .body(Body.builder().text(Content.builder().data(mailRequest.getContent()).build()).build())
                        .build())
                .build();
        SendEmailResponse response = sesClient.sendEmail(request);
        log.info("Email sent! Message ID: {}", response.messageId());
    }
}