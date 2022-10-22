package it.gov.pagopa.email.notification.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class MessageRequestException extends RuntimeException {
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
