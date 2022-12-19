package it.gov.pagopa.email.notification.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {MessageService.class})
@ExtendWith(SpringExtension.class)
class MessageServiceTest {
    private static final String CODE_NOT_PRESENT_IN_MESSAGE_RESOURCE = "CODE_NOT_PRESENT_IN_MESSAGE_RESOURCE";
    private static final String KEY_1 = "Key_1";
    private static final String KEY_2 = "Key_2";
    private static final String DRAFT_VALUE = "DRAFT";
    private static final String IN_REVISION_VALUE = "IN_REVISION";
    private static final String DRAFT_VALUE_IT_TRANSLATED = "BOZZA";
    private static final String IN_REVISION_VALUE_IT_TRANSLATED = "IN REVISIONE";
    @Autowired
    private MessageService messageService;

    @MockBean
    private MessageSource messageSource;

    @Autowired
    private DelegatingMessageSource delegatingMessageSource;

//    @BeforeAll
//    public void setUp() {
//        messageSource = Mockito.mock(MessageSource.class);
//        when(messageSource.getMessage(anyString(), any(Object[].class),any(Locale.class))).thenReturn("");
//        delegatingMessageSource.setParentMessageSource(messageSource);
//    }

    /**
     * Method under test: {@link MessageService#getMessages(Map)}
     */
    @Test
    void whenPassingEmptyMap_thenTranslationWillReturnTheSameEmptyMap() {
        HashMap<String, String> stringStringMap = new HashMap<>();
        Map<String, String> actualMessages = messageService.getMessages(stringStringMap);
        assertSame(stringStringMap, actualMessages);
        assertTrue(actualMessages.isEmpty());
    }

    /**
     * Method under test: {@link MessageService#getMessages(Map)}
     */
    @Test
    void whenOneLabelIsFoundInMessageResourceBundleForLocaleITALIAN_thenTranslationWillBeDoneOnThem() {
        HashMap<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put(KEY_1, DRAFT_VALUE);

        LocaleContext lc = new SimpleLocaleContext(Locale.ITALIAN);
        LocaleContextHolder.setLocaleContext(lc);
        Assertions.assertThat(LocaleContextHolder.getLocaleContext()).isSameAs(lc);
        Assertions.assertThat(LocaleContextHolder.getLocale()).isEqualTo(Locale.ITALIAN);
        Assertions.assertThat(LocaleContextHolder.getTimeZone()).isEqualTo(TimeZone.getDefault());

        when(messageSource.getMessage(DRAFT_VALUE, null, DRAFT_VALUE, Locale.ITALIAN))
                .thenReturn(DRAFT_VALUE_IT_TRANSLATED);
        delegatingMessageSource.setParentMessageSource(messageSource);

        Map<String, String> actualMessages = messageService.getMessages(stringStringMap);
        assertSame(stringStringMap, actualMessages);
        assertEquals(1, actualMessages.size());
        assertEquals(DRAFT_VALUE_IT_TRANSLATED, actualMessages.get(KEY_1));
    }

    /**
     * Method under test: {@link MessageService#getMessages(Map)}
     */
    @Test
    void whenManyLabelsAreFoundInMessageResourceBundleForLocale_thenTranslationWillBeDoneOnThem() {
        HashMap<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put(KEY_1, DRAFT_VALUE);
        stringStringMap.put(KEY_2, IN_REVISION_VALUE);

        LocaleContext lc = new SimpleLocaleContext(Locale.ITALIAN);
        LocaleContextHolder.setLocaleContext(lc);
        Assertions.assertThat(LocaleContextHolder.getLocaleContext()).isSameAs(lc);
        Assertions.assertThat(LocaleContextHolder.getLocale()).isEqualTo(Locale.ITALIAN);
        Assertions.assertThat(LocaleContextHolder.getTimeZone()).isEqualTo(TimeZone.getDefault());

        when(messageSource.getMessage(DRAFT_VALUE, null, DRAFT_VALUE, Locale.ITALIAN))
                .thenReturn(DRAFT_VALUE_IT_TRANSLATED);
        when(messageSource.getMessage(IN_REVISION_VALUE, null, IN_REVISION_VALUE, Locale.ITALIAN))
                .thenReturn(IN_REVISION_VALUE_IT_TRANSLATED);
        delegatingMessageSource.setParentMessageSource(messageSource);

        Map<String, String> actualMessages = messageService.getMessages(stringStringMap);
        assertSame(stringStringMap, actualMessages);
        assertEquals(2, actualMessages.size());
        assertEquals(DRAFT_VALUE_IT_TRANSLATED, actualMessages.get(KEY_1));
        assertEquals(IN_REVISION_VALUE_IT_TRANSLATED, actualMessages.get(KEY_2));
    }
}

