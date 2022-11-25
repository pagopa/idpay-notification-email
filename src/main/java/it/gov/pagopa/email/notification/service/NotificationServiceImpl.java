package it.gov.pagopa.email.notification.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import it.gov.pagopa.email.notification.connector.MailRequest;
import it.gov.pagopa.email.notification.connector.SMTPConnector;
import it.gov.pagopa.email.notification.dto.EmailMessageDTO;
import it.gov.pagopa.email.notification.mapper.MailMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailPreparationException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final Configuration freemarkerConfig;
    private final SMTPConnector SMTPConnector;
    private final MailMessageMapper mailMessageMapper;
    private final MessageService messageService;
    private final String assistanceRecipientMailAddress;
    private final String assistanceSubjectPrefix;
    private final String noReplySenderMailAddress;
    private final String noReplySubjectPrefix;

    @Autowired
    NotificationServiceImpl(
            Configuration freemarkerConfig,
            SMTPConnector SMTPConnector,
            MailMessageMapper mailMessageMapper,
            MessageService messageService,
            @Value("${app.email.notification.assistance.recipient}") String assistanceRecipientMailAddress,
            @Value("${app.email.notification.assistance.subject-prefix}") String assistanceSubjectPrefix,
            @Value("${app.email.notification.no-reply.sender}") String noReplySenderMailAddress,
            @Value("${app.email.notification.no-reply.subject-prefix}") String noReplySubjectPrefix

    ) {
        this.freemarkerConfig = freemarkerConfig;
        this.SMTPConnector = SMTPConnector;
        this.mailMessageMapper = mailMessageMapper;
        this.messageService = messageService;
        this.assistanceRecipientMailAddress = assistanceRecipientMailAddress;
        this.assistanceSubjectPrefix = assistanceSubjectPrefix;
        this.noReplySenderMailAddress = noReplySenderMailAddress;
        this.noReplySubjectPrefix = noReplySubjectPrefix;
    }

    @Override
    public void sendMessage(EmailMessageDTO emailMessageDTO) {
        log.trace("sendMessage start");
        log.debug("sendMessage emailMessageDTO = {}", emailMessageDTO);

        try {
            String htmlContent;
            if(StringUtils.isNotBlank(emailMessageDTO.getTemplateName())) {
                this.processGeneralEmail(emailMessageDTO);
                Template template = this.freemarkerConfig.getTemplate(emailMessageDTO.getTemplateName() + "\\index.html");
                Map<String, String> placeHolderWithInternationalization = messageService.getMessages(emailMessageDTO.getTemplateValues());
                htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, placeHolderWithInternationalization);
            }
            else{
                this.processToAssistance(emailMessageDTO);
                htmlContent = emailMessageDTO.getContent();
            }
            emailMessageDTO.setContent(htmlContent);
            MailRequest mailRequest = this.mailMessageMapper.toMessageRequest(emailMessageDTO);
            this.SMTPConnector.sendMessage(mailRequest);
        } catch (Exception e) {
            throw new MailPreparationException(e);
        }

        log.trace("sendMessage end");
    }

    private void processGeneralEmail(EmailMessageDTO emailMessageDTO) {
        if(emailMessageDTO.getSenderEmail() == null)
            emailMessageDTO.setSenderEmail(noReplySenderMailAddress);
        emailMessageDTO.setSubject(String.format("%s", noReplySubjectPrefix)+emailMessageDTO.getSubject());
    }

    private void processToAssistance(EmailMessageDTO emailMessageDTO) {
        emailMessageDTO.setRecipientEmail(assistanceRecipientMailAddress);
        emailMessageDTO.setSubject(String.format("%s", assistanceSubjectPrefix)+emailMessageDTO.getSubject()); //FIXME Check UseCase for Subject 'null' to avoid concatenation like "PREFIXnull"
    }

}
