package it.gov.pagopa.email.notification.exception;

import it.gov.pagopa.email.notification.constants.EmailNotificationConstants;
import it.gov.pagopa.email.notification.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailPreparationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler({MailPreparationException.class})
    ResponseEntity<ErrorDTO> handleMailPreparationException(MailPreparationException ex) {
        log.warn(ex.getMessage());
        return new ResponseEntity<>(new ErrorDTO(EmailNotificationConstants.Exception.BadRequest.CODE, ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

}