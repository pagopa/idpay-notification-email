package it.gov.pagopa.email.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageDTO {

    private String templateName;
    @Valid
    private Map<@NotBlank String,@NotNull String> templateValues;
    private String subject;
    private String content;
    private String senderEmail;
    private String recipientEmail;

}
