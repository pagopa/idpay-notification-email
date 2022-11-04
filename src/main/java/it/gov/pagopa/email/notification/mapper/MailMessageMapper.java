package it.gov.pagopa.email.notification.mapper;

import it.gov.pagopa.email.notification.connector.MailRequest;
import it.gov.pagopa.email.notification.dto.EmailMessageDTO;
import org.springframework.stereotype.Component;

@Component
public class MailMessageMapper {

    public MailRequest toMessageRequest(EmailMessageDTO emailMessageDTO) {
        return MailRequest.builder()
                .from(emailMessageDTO.getSenderEmail())
                .to(emailMessageDTO.getRecipientEmail())
                .subject(emailMessageDTO.getSubject())
                .content(emailMessageDTO.getContent())
                .build();
    }
}
