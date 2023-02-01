package it.gov.pagopa.email.notification.freemarkerTaskHandler;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskHandler {

    private Configuration configuration;

    @Autowired
    public void setConfiguration(Configuration aConfiguration)
    {
        configuration = aConfiguration;
    }

    public Template handle() throws Exception
    {
        return configuration.getTemplate("test.html");
    }
}
