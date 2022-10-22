package it.gov.pagopa.email.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageDTO {

    private String templateName;
    private String subject;
    private String senderEmail;
    private Map<String, String> templateValues;
    private String recipientEmail;
    private String content;

}
