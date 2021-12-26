package com.vodafone.parkingticket.exception.util;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public record MessageUtil(Environment environment) {
    public String getMessageByStatus(Integer status) {
        String lang = environment.getProperty("app.language");
        String errorMessagesPropertyNameByLang = String.format("app.error-messages.%s.%s", lang, status);
        final String DEFAULT_ERROR_MESSAGES_PROPERTY_NAME = "app.error-messages.default.";

        String message = environment.getProperty(errorMessagesPropertyNameByLang);
        if (ObjectUtils.isEmpty(message)) {
            message = environment.getProperty(DEFAULT_ERROR_MESSAGES_PROPERTY_NAME + ErrorUtil.DEFAULT_ERROR_MESSAGE_CODE);
        }
        return message;
    }
}
