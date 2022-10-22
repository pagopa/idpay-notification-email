package it.gov.pagopa.email.notification.service;

import it.gov.pagopa.email.notification.connector.MailRequest;
import it.gov.pagopa.email.notification.connector.NotificationConnector;
import it.gov.pagopa.email.notification.dto.EmailMessageDTO;
import it.gov.pagopa.email.notification.mapper.MailMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationConnector notificationConnector;
    private final TemplateService templateService;
    private final MailMessageMapper mailMessageMapper;
    private final String assistanceMailAddress;
    private final String noReplyMailAddress;

    @Autowired
    NotificationServiceImpl(
            NotificationConnector notificationConnector,
            TemplateService templateService,
            MailMessageMapper mailMessageMapper,
            @Value("${app.email.notification.assistance}") String assistanceMailAddress,
            @Value("${app.email.notification.no-reply}") String noReplyMailAddress
    ) {
        this.notificationConnector = notificationConnector;
        this.templateService = templateService;
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
        }
        else {
            processNoReply(emailMessageDTO);
            emailMessageDTO.setContent(templateService.processTemplate(emailMessageDTO.getContent(), emailMessageDTO.getTemplateName(), emailMessageDTO.getTemplateValues()));
        }
        MailRequest mailRequest = mailMessageMapper.toMessageRequest(emailMessageDTO);
        notificationConnector.sendMessage(mailRequest);
        log.trace("sendMessageToCustomerCare end");
    }

    private void processNoReply(EmailMessageDTO emailMessageDTO) {
        emailMessageDTO.setSenderEmail(noReplyMailAddress);
    }

    private void processAssistance(EmailMessageDTO emailMessageDTO) {
        emailMessageDTO.setRecipientEmail(assistanceMailAddress);
    }

}
