package it.gov.pagopa.email.notification.exception;

import it.gov.pagopa.email.notification.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler({MessageRequestException.class})
    ResponseEntity<ErrorDTO> handleFileValidationException(MessageRequestException ex) {
        log.warn(ex.toString());
        return new ResponseEntity<>(new ErrorDTO(ex.getCode(), ex.getMessage()),
                ex.getHttpStatus());
    }

}