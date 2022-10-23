package it.gov.pagopa.email.notification.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import it.gov.pagopa.email.notification.connector.MailRequest;
import it.gov.pagopa.email.notification.connector.NotificationConnector;
import it.gov.pagopa.email.notification.dto.EmailMessageDTO;
import it.gov.pagopa.email.notification.mapper.MailMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailPreparationException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final Configuration freemarkerConfig;
    private final NotificationConnector notificationConnector;
    private final MailMessageMapper mailMessageMapper;
    private final String assistanceMailAddress;
    private final String noReplyMailAddress;

    @Autowired
    NotificationServiceImpl(
            Configuration freemarkerConfig,
            NotificationConnector notificationConnector,
            MailMessageMapper mailMessageMapper,
            @Value("${app.email.notification.assistance}") String assistanceMailAddress,
            @Value("${app.email.notification.no-reply}") String noReplyMailAddress

    ) {
        this.freemarkerConfig = freemarkerConfig;
        this.notificationConnector = notificationConnector;
        this.mailMessageMapper = mailMessageMapper;
        this.assistanceMailAddress = assistanceMailAddress;
        this.noReplyMailAddress = noReplyMailAddress;
    }

    @Override
    public void sendMessage(EmailMessageDTO emailMessageDTO) {
        log.trace("sendMessageToCustomerCare start");
        log.debug("sendMessageToCustomerCare emailMessageDTO = {}", emailMessageDTO);

        if (emailMessageDTO.getSenderEmail() != null) {
            processAssistance(emailMessageDTO);
        } else {
            processNoReply(emailMessageDTO);
        }

        try {
            //emailMessageDTO.setContent(this.templateService.processTemplate(emailMessageDTO.getContent(), emailMessageDTO.getTemplateName(), emailMessageDTO.getTemplateValues()));
            String htmlContent = StringUtils.EMPTY;
            if (StringUtils.isNotBlank(emailMessageDTO.getContent())) {
                htmlContent = emailMessageDTO.getContent();
            } else if (StringUtils.isNotBlank(emailMessageDTO.getTemplateName())) {
                Template template = this.freemarkerConfig.getTemplate(emailMessageDTO.getTemplateName() + "\\index.html");
                htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, emailMessageDTO.getTemplateValues());
            }
            emailMessageDTO.setContent(htmlContent);
            MailRequest mailRequest = this.mailMessageMapper.toMessageRequest(emailMessageDTO);
            this.notificationConnector.sendMessage(mailRequest);
        } catch (Exception e) {
            throw new MailPreparationException(e);
        }
        log.trace("sendMessageToCustomerCare end");
    }
/*
    private void sendNotification(String email, String templateName, String subject, Map<String, String> dataModel) {

        try {
            Template template = freemarkerConfig.getTemplate(templateName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dataModel);
            MessageRequest messageRequest = new MessageRequest();
            messageRequest.setContent(html);
            messageRequest.setReceiverEmail(email);
            messageRequest.setSubject(subject);
            notificationConnector.sendNotificationToUser(messageRequest);
        } catch (Exception e) {
            throw new MailPreparationException(e);
        }
    }
*/
    private void processNoReply(EmailMessageDTO emailMessageDTO) {
        emailMessageDTO.setSenderEmail(noReplyMailAddress);
    }

    private void processAssistance(EmailMessageDTO emailMessageDTO) {
        emailMessageDTO.setRecipientEmail(assistanceMailAddress);
    }

}
