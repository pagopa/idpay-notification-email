package it.gov.pagopa.email.notification.controller;

import it.gov.pagopa.email.notification.constants.EmailNotificationConstants;
import it.gov.pagopa.email.notification.dto.EmailMessageDTO;
import it.gov.pagopa.email.notification.dto.ErrorDTO;
import it.gov.pagopa.email.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mail.MailPreparationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(value = {EmailNotificationApiController.class}, excludeAutoConfiguration =  { UserDetailsServiceAutoConfiguration.class , SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class EmailNotificationApiControllerImplTest {

    private static final String BASE_URL = "http://localhost:8082/idpay/email-notification";
    private static final String POST_NOTIFY_EMAIL_URL = "/notify";
    public static final String MAIL_ERROR_MESSAGE = "Mail Error";

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mvc;

    @Test
    void testNotificationOk() throws Exception {
        //create Dummy EmailMessageDTO
        EmailMessageDTO emailMessageDTO = createEmailMessageDTO();

        doNothing().when(notificationService).sendMessage((EmailMessageDTO) any());

        // Then
        mvc.perform(MockMvcRequestBuilders.post(BASE_URL + POST_NOTIFY_EMAIL_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailMessageDTO)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
                .andDo(print())
                .andReturn();

        verify(notificationService).sendMessage((EmailMessageDTO) any());
    }

    @Test
    void whenLogicRaiseMailPreparationException_thenStatusIsBadRequest() throws Exception {
        //create Dummy EmailMessageDTO
        EmailMessageDTO emailMessageDTO = createEmailMessageDTO();

        //doThrow MailPreparationException for Void method
        doThrow(new MailPreparationException(MAIL_ERROR_MESSAGE))
                .when(notificationService).sendMessage((EmailMessageDTO) any());

        // Then
        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(BASE_URL + POST_NOTIFY_EMAIL_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailMessageDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print())
                .andReturn();

        ErrorDTO error = objectMapper.readValue(res.getResponse().getContentAsString(), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getResponse().getStatus());
        assertEquals(EmailNotificationConstants.Exception.BadRequest.CODE, error.getCode());
        assertTrue(error.getMessage().contains(MAIL_ERROR_MESSAGE));

        verify(notificationService).sendMessage((EmailMessageDTO) any());
    }

    private EmailMessageDTO createEmailMessageDTO() {
        return new EmailMessageDTO();
    }

}