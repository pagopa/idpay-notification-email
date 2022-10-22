package it.gov.pagopa.email.notification.service;

import it.gov.pagopa.email.notification.dto.EmailMessageDTO;

public interface NotificationService {

    public void sendMessage(EmailMessageDTO emailMessageDTO);

}
