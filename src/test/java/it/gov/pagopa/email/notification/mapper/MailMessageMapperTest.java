package it.gov.pagopa.email.notification.mapper;

import it.gov.pagopa.email.notification.dto.smtp.MailRequest;
import it.gov.pagopa.email.notification.dto.EmailMessageDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {MailMessageMapper.class})
@ExtendWith(SpringExtension.class)
class MailMessageMapperTest {
    private static final String CONTENT = "CONTENT";
    private static final String RECIPIENT_EMAIL = "RECIPIENT@RECIPIENT.RECIPIENT";
    private static final String SENDER_EMAIL = "SENDER@SENDER.SENDER";
    private static final String SUBJECT = "SUBJECT";
    @Autowired
    private MailMessageMapper mailMessageMapper;

    /**
     * Method under test: {@link MailMessageMapper#toMessageRequest(EmailMessageDTO)}
     */
    @Test
    void givenNullBodyOfMessage_thenMessageContainNullBody() {
        MailRequest actualToMessageRequestResult = mailMessageMapper.toMessageRequest(new EmailMessageDTO());
        assertNull(actualToMessageRequestResult.getContent());
        assertNull(actualToMessageRequestResult.getTo());
        assertNull(actualToMessageRequestResult.getSubject());
        assertNull(actualToMessageRequestResult.getFrom());
    }

    /**
     * Method under test: {@link MailMessageMapper#toMessageRequest(EmailMessageDTO)}
     */
    @Test
    void givenMessageDTOValid_thenMessageReturnTheSame() {
        EmailMessageDTO emailMessageDTO = mock(EmailMessageDTO.class);
        when(emailMessageDTO.getContent()).thenReturn(CONTENT);
        when(emailMessageDTO.getRecipientEmail()).thenReturn(RECIPIENT_EMAIL);
        when(emailMessageDTO.getSenderEmail()).thenReturn(SENDER_EMAIL);
        when(emailMessageDTO.getSubject()).thenReturn(SUBJECT);
        MailRequest actualToMessageRequestResult = mailMessageMapper.toMessageRequest(emailMessageDTO);
        assertEquals(CONTENT, actualToMessageRequestResult.getContent());
        assertEquals(RECIPIENT_EMAIL, actualToMessageRequestResult.getTo());
        assertEquals(SUBJECT, actualToMessageRequestResult.getSubject());
        assertEquals(SENDER_EMAIL, actualToMessageRequestResult.getFrom());
        verify(emailMessageDTO).getContent();
        verify(emailMessageDTO).getRecipientEmail();
        verify(emailMessageDTO).getSenderEmail();
        verify(emailMessageDTO).getSubject();
    }
}

