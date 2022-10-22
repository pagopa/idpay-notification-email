package it.gov.pagopa.email.notification.controller;

import it.gov.pagopa.email.notification.dto.EmailMessageDTO;
import it.gov.pagopa.email.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EmailNotificationApiControllerImpl implements EmailNotificationApiController {

    private final NotificationService notificationService;

    public EmailNotificationApiControllerImpl(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> sendEmail(EmailMessageDTO emailMessageDTO) {
        notificationService.sendMessage(emailMessageDTO);
        return ResponseEntity.noContent().build();
    }

}
