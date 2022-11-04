package it.gov.pagopa.email.notification.constants;


public class EmailNotificationConstants extends AbstractConstant{

    private EmailNotificationConstants(){}

    public static final class Exception {

        public static final class BadRequest { //400
            public static final String CODE = BASE_CODE + ".bad.request";
        }
    }
}
