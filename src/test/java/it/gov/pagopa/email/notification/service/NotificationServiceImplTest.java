package it.gov.pagopa.email.notification.service;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import it.gov.pagopa.email.notification.dto.smtp.MailRequest;
import it.gov.pagopa.email.notification.connector.smtp.SMTPConnector;
import it.gov.pagopa.email.notification.dto.EmailMessageDTO;
import it.gov.pagopa.email.notification.freemarkerTaskHandler.TaskHandler;
import it.gov.pagopa.email.notification.mapper.MailMessageMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailPreparationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "app.email.notification.assistance.subject-prefix=A_SUBJECT_PREFIX",
                "app.email.notification.assistance.recipient=A@R.R",
                "app.email.notification.no-reply.sender=NR@S.S",
                "app.email.notification.no-reply.subject-prefix=NR_SUBJECT_PREFIX"
        })
@SpringJUnitConfig
@ImportAutoConfiguration(classes = {NotificationServiceImpl.class, String.class/*, FreeMarkerConfigurationFactoryBean.class*/})
class NotificationServiceImplTest {

    private static final String CONTENT = "CONTENT";
    private static final String RECIPIENT_EMAIL = "RECIPIENT@RECIPIENT.RECIPIENT";
    private static final String SENDER_EMAIL = "SENDER@SENDER.SENDER";
    private static final String SUBJECT = "SUBJECT";
    private static final String ASSISTANCE_RECIPIENT_MAIL_ADDRESS = "A@R.R";
    private static final String ASSISTANCE_SUBJECT_PREFIX = "A_SUBJECT_PREFIX";
    private static final String NO_REPLY_SENDER_MAIL_ADDRESS = "NR@S.S";
    private static final String NO_REPLY_SUBJECT_PREFIX = "NR_SUBJECT_PREFIX";

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private Configuration configuration;

    @MockBean
    private MailMessageMapper mailMessageMapper;

    @MockBean
    private MessageService messageService;

    @MockBean
    private SMTPConnector sMTPConnector;

    private final Configuration configTest = new Configuration(Configuration.VERSION_2_3_23);

    /**
     * Method under test: {@link NotificationServiceImpl#sendMessage(EmailMessageDTO)}
     */
    @Test
    void whenNoTemplateNameProvidedForEmailMessageDTO_thenProcessAssistance() {
        MailRequest mailRequest = new MailRequest(SENDER_EMAIL, RECIPIENT_EMAIL, SUBJECT, CONTENT);
        doNothing().when(sMTPConnector).sendMessage((MailRequest) any());
        when(mailMessageMapper.toMessageRequest((EmailMessageDTO) any())).thenReturn(mailRequest);

        EmailMessageDTO emailMessageDTO = new EmailMessageDTO();
        emailMessageDTO.setSubject(SUBJECT);
        emailMessageDTO.setContent(CONTENT);
        emailMessageDTO.setSenderEmail(SENDER_EMAIL);
        notificationService.sendMessage(emailMessageDTO);

        verify(sMTPConnector).sendMessage((MailRequest) any());
        verify(mailMessageMapper).toMessageRequest((EmailMessageDTO) any());
        assertEquals(ASSISTANCE_SUBJECT_PREFIX + SUBJECT, emailMessageDTO.getSubject());
        assertEquals(ASSISTANCE_RECIPIENT_MAIL_ADDRESS, emailMessageDTO.getRecipientEmail());
        assertEquals(CONTENT, emailMessageDTO.getContent());
    }

    @Test
    void whenTemplateNameProvidedForEmailMessageDTO_thenProcessGeneralEmail() throws TemplateException, IOException {
        TaskHandler taskHandler = new TaskHandler();
        configTest.setDirectoryForTemplateLoading(new File("src/test/resources/templates"));
        configTest.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_23));
        taskHandler.setConfiguration(configTest);

        Template template;
        try {
            template = taskHandler.handle();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        MailRequest mailRequest = new MailRequest(SENDER_EMAIL, RECIPIENT_EMAIL, SUBJECT, CONTENT);
        doNothing().when(sMTPConnector).sendMessage(any());
        when(mailMessageMapper.toMessageRequest(any())).thenReturn(mailRequest);
        when(configuration.getTemplate(anyString())).thenReturn(template);

        EmailMessageDTO emailMessageDTO = new EmailMessageDTO();
        emailMessageDTO.setSubject(SUBJECT);
        emailMessageDTO.setContent(CONTENT);
        emailMessageDTO.setTemplateName("test");
        notificationService.sendMessage(emailMessageDTO);

        verify(sMTPConnector).sendMessage(any());
        verify(mailMessageMapper).toMessageRequest(any());
        assertEquals(NO_REPLY_SENDER_MAIL_ADDRESS, emailMessageDTO.getSenderEmail());
        assertEquals(NO_REPLY_SUBJECT_PREFIX + SUBJECT, emailMessageDTO.getSubject());
        assertEquals(CONTENT, emailMessageDTO.getContent());
    }

    /**
     * Method under test: {@link NotificationServiceImpl#sendMessage(EmailMessageDTO)}
     */
    @Test
    void whenNotificationServiceRaiseException_thenGetException() {
        doNothing().when(sMTPConnector).sendMessage((MailRequest) any());
        when(mailMessageMapper.toMessageRequest((EmailMessageDTO) any()))
                .thenThrow(new MailPreparationException("sendMessage start"));

        assertThrows(MailPreparationException.class, () -> notificationService.sendMessage(new EmailMessageDTO()));
        verify(mailMessageMapper).toMessageRequest((EmailMessageDTO) any());
    }
}

