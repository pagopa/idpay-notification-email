package it.gov.pagopa.email.notification.connector.aws;

import it.gov.pagopa.email.notification.dto.smtp.MailRequest;

public interface AwsSesConnector {

    void sendEmail(MailRequest mailRequest);

}