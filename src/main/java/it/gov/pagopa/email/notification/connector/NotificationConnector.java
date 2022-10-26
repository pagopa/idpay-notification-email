package it.gov.pagopa.email.notification.connector;

public interface NotificationConnector {

    public void sendMessage(MailRequest mailRequest);

}
