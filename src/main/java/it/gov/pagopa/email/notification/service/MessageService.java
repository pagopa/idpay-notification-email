package it.gov.pagopa.email.notification.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MessageService {

    @Resource
    private MessageSource messageSource;

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, code, LocaleContextHolder.getLocale());
    }

    public Map<String, String> getMessages(Map<String, String> codes) {
        codes.replaceAll((k, v) -> getMessage(v));
        return codes;
    }
}
