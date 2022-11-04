package it.gov.pagopa.email.notification.connector;

public interface SMTPConnector {

    public void sendMessage(MailRequest mailRequest);

}
