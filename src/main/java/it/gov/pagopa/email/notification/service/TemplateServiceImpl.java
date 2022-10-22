package it.gov.pagopa.email.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class TemplateServiceImpl implements TemplateService {
    @Override
    public String processTemplate(String content, String templateName, Map<String, String> templatePlaceHoldersMap) {
        return null;
    }
}
