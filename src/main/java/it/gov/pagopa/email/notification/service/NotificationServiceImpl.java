package it.gov.pagopa.email.notification.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import it.gov.pagopa.email.notification.dto.EmailMessageDTO;
import it.gov.pagopa.email.notification.dto.smtp.MailRequest;
import it.gov.pagopa.email.notification.mapper.MailMessageMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final Configuration freemarkerConfig;
    private final JavaMailSender javaMailSender;
    private final MailMessageMapper mailMessageMapper;
    private final MessageService messageService;
    private final String assistanceRecipientMailAddress;
    private final String assistanceSubjectPrefix;
    private final String noReplySenderMailAddress;
    private final String noReplySubjectPrefix;

    @Autowired
    NotificationServiceImpl(
            Configuration freemarkerConfig,
            JavaMailSender javaMailSender,
            MailMessageMapper mailMessageMapper,
            MessageService messageService,
            @Value("${app.email.notification.assistance.recipient}") String assistanceRecipientMailAddress,
            @Value("${app.email.notification.assistance.subject-prefix}") String assistanceSubjectPrefix,
            @Value("${app.email.notification.no-reply.sender}") String noReplySenderMailAddress,
            @Value("${app.email.notification.no-reply.subject-prefix}") String noReplySubjectPrefix

    ) {
        this.freemarkerConfig = freemarkerConfig;
        this.javaMailSender = javaMailSender;
        this.mailMessageMapper = mailMessageMapper;
        this.messageService = messageService;
        this.assistanceRecipientMailAddress = assistanceRecipientMailAddress;
        this.assistanceSubjectPrefix = assistanceSubjectPrefix;
        this.noReplySenderMailAddress = noReplySenderMailAddress;
        this.noReplySubjectPrefix = noReplySubjectPrefix;
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
                htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, placeHolderWithInternationalization);
            }
            else{
                log.info("[SEND MESSAGE] Processing to assistance");
                this.processToAssistance(emailMessageDTO);
                htmlContent = emailMessageDTO.getContent();
            }
            emailMessageDTO.setContent(htmlContent);
            MailRequest mailRequest = this.mailMessageMapper.toMessageRequest(emailMessageDTO);
            this.sendMessage(mailRequest);
        } catch (Exception e) {
            throw new MailPreparationException(e);
        }

        log.info("[SEND MESSAGE] End processing message");
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

    @SneakyThrows
    public void sendMessage(MailRequest mailRequest) {
        log.info("[SEND MESSAGE SMTP] Start processing message");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false,  "UTF-8");
        helper.setFrom(mailRequest.getFrom());
        helper.setText(mailRequest.getContent(), true);
        helper.setTo(InternetAddress.parse(mailRequest.getTo()));
        helper.setSubject(mailRequest.getSubject());
        javaMailSender.send(mimeMessage);
        log.info("[SEND MESSAGE SMTP] End processing message");
    }

}
