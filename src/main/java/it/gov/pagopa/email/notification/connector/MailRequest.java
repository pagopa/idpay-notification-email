package it.gov.pagopa.email.notification.connector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailRequest {
    private String from;
    private String to;
    private String subject;
    private String content;
}
