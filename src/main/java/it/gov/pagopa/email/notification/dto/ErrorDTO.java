package it.gov.pagopa.email.notification.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ErrorDTO
 */
@JsonPropertyOrder({
  ErrorDTO.JSON_PROPERTY_CODE,
  ErrorDTO.JSON_PROPERTY_MESSAGE
})
@Data
@AllArgsConstructor
public class ErrorDTO {
  public static final String JSON_PROPERTY_CODE = "code";
  private String code;

  public static final String JSON_PROPERTY_MESSAGE = "message";
  private String message;

}

