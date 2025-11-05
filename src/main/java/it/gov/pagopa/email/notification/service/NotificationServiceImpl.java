package it.gov.pagopa.email.notification.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import it.gov.pagopa.email.notification.connector.aws.AwsSesConnector;
import it.gov.pagopa.email.notification.dto.EmailMessageDTO;
import it.gov.pagopa.email.notification.dto.smtp.MailRequest;
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
    private final MailMessageMapper mailMessageMapper;
    private final MessageService messageService;
    private final String assistanceRecipientMailAddress;
    private final String assistanceSubjectPrefix;
    private final String noReplySenderMailAddress;
    private final String noReplySubjectPrefix;

    private final AwsSesConnector awsSesConnector;

    @Value("${app.email.notification.no-reply.assisted-link}")
    private final String assistedLink;

    private final String MAGANED_ENTITY = "managedEntity";

    @Autowired
    NotificationServiceImpl(
            Configuration freemarkerConfig,
            MailMessageMapper mailMessageMapper,
            MessageService messageService,
            @Value("${app.email.notification.assistance.recipient}") String assistanceRecipientMailAddress,
            @Value("${app.email.notification.assistance.subject-prefix}") String assistanceSubjectPrefix,
            @Value("${app.email.notification.no-reply.sender}") String noReplySenderMailAddress,
            @Value("${app.email.notification.no-reply.subject-prefix}") String noReplySubjectPrefix,
            @Value("${app.email.notification.no-reply.assisted-link}") String assistedLink,
            AwsSesConnector awsSesConnector

    ) {
        this.freemarkerConfig = freemarkerConfig;
        this.mailMessageMapper = mailMessageMapper;
        this.messageService = messageService;
        this.assistanceRecipientMailAddress = assistanceRecipientMailAddress;
        this.assistanceSubjectPrefix = assistanceSubjectPrefix;
        this.noReplySenderMailAddress = noReplySenderMailAddress;
        this.noReplySubjectPrefix = noReplySubjectPrefix;
        this.awsSesConnector = awsSesConnector;
        this.assistedLink = assistedLink;
    }

    @Override
    public void sendMessage(EmailMessageDTO emailMessageDTO) {
        log.info("[SEND MESSAGE] Start processing message");
        log.info("[SEND MESSAGE] emailMessageDTO = {}", emailMessageDTO);

        try {
            String htmlContent;
            if(StringUtils.isNotBlank(emailMessageDTO.getTemplateName())) {
                log.info("[SEND MESSAGE] Processing general mail");
                this.processGeneralEmail(emailMessageDTO);
                Template template = this.freemarkerConfig.getTemplate(emailMessageDTO.getTemplateName() + "\\index.html");
                Map<String, String> placeHolderWithInternationalization = messageService.getMessages(emailMessageDTO.getTemplateValues());
                if(placeHolderWithInternationalization.get(MAGANED_ENTITY) != null && placeHolderWithInternationalization.get(MAGANED_ENTITY).equalsIgnoreCase("Assistenza")){
                    placeHolderWithInternationalization.put(MAGANED_ENTITY, "<a href=\"" + assistedLink + "\" style=\"color: #0073E6;\">Assistenza</a>");
                }
                htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, placeHolderWithInternationalization);
            }
            else{
                log.info("[SEND MESSAGE] Processing to assistance");
                this.processToAssistance(emailMessageDTO);
                htmlContent = emailMessageDTO.getContent();
            }
            emailMessageDTO.setContent(htmlContent);
            MailRequest mailRequest = this.mailMessageMapper.toMessageRequest(emailMessageDTO);
            awsSesConnector.sendEmail(mailRequest);
        } catch (Exception e) {
            log.error("Error while sending email: {}", e.getMessage());
            throw new MailPreparationException(e);
        }

        log.info("[SEND MESSAGE] End processing message");
    }

    private void processGeneralEmail(EmailMessageDTO emailMessageDTO) {
        if(emailMessageDTO.getSenderEmail() == null)
            emailMessageDTO.setSenderEmail(noReplySenderMailAddress);
        emailMessageDTO.setSubject(String.format("%s", noReplySubjectPrefix)
                + StringUtils.defaultIfBlank(emailMessageDTO.getSubject(), StringUtils.EMPTY));
    }

    private void processToAssistance(EmailMessageDTO emailMessageDTO) {
        emailMessageDTO.setRecipientEmail(assistanceRecipientMailAddress);

        emailMessageDTO.setSubject(String.format("%s", assistanceSubjectPrefix)
                + StringUtils.defaultIfBlank(emailMessageDTO.getSubject(), StringUtils.EMPTY));
    }

}
