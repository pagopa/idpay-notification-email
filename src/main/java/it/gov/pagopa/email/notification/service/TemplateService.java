package it.gov.pagopa.email.notification.service;

import java.util.Map;

public interface TemplateService {

    String processTemplate(String content, String templateName, Map<String,String> templatePlaceHoldersMap);

}
