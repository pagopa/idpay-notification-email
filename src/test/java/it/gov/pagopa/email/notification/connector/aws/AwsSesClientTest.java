package it.gov.pagopa.email.notification.connector.aws;

import it.gov.pagopa.email.notification.dto.smtp.MailRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

@ExtendWith(MockitoExtension.class)
class AwsSesClientTest {
    private static final String CONTENT = "CONTENT";
    private static final String RECIPIENT_EMAIL = "RECIPIENT@RECIPIENT.RECIPIENT";
    private static final String SENDER_EMAIL = "SENDER@SENDER.SENDER";
    private static final String SUBJECT = "SUBJECT";

    @Mock
    private SesClient sesClientMock;

    private AwsSesClient awsSesClient;

    @BeforeEach
    void setUp() {
        awsSesClient = new AwsSesClient(sesClientMock);
    }

    @Test
    void whenSendEmail_thenSuccess() {
        // Given
        MailRequest mailRequest = new MailRequest(SENDER_EMAIL, RECIPIENT_EMAIL, SUBJECT, CONTENT);

        SendEmailResponse responseMock = Mockito.mock(SendEmailResponse.class);
        Mockito.when(responseMock.messageId()).thenReturn("MESSAGE_ID");
        Mockito.when(sesClientMock.sendEmail(Mockito.any(SendEmailRequest.class))).thenReturn(responseMock);

        //When
        awsSesClient.sendEmail(mailRequest);

        //Then
        Mockito.verify(sesClientMock).sendEmail(Mockito.any(SendEmailRequest.class));
    }

    @Test
    void whenSendEmail_thenSesClientThrowError() {
        // Given
        String errorMessage = "DUMMY_ERROR";
        MailRequest mailRequest = new MailRequest(SENDER_EMAIL, RECIPIENT_EMAIL, SUBJECT, CONTENT);

        Mockito.when(sesClientMock.sendEmail(Mockito.any(SendEmailRequest.class))).thenThrow(new RuntimeException(errorMessage));

        //When
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> awsSesClient.sendEmail(mailRequest));

        //Then
        Assertions.assertEquals(errorMessage, exception.getMessage());

        Mockito.verify(sesClientMock).sendEmail(Mockito.any(SendEmailRequest.class));
    }
}