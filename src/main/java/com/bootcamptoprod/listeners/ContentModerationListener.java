package com.bootcamptoprod.listeners;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
@Order(1) // SUMMARY: Executed First. Checks for PII or sensitive info.
public class ContentModerationListener implements ChatModelListener {

    private static final Logger log = LoggerFactory.getLogger(ContentModerationListener.class);
    private static final List<String> SENSITIVE_KEYWORDS = List.of("password", "credit card", "otp", "secret");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+");

    @Override
    public void onRequest(ChatModelRequestContext context) {
        // Iterate through all messages in the request and check for sensitive content
        context.chatRequest().messages().forEach(message -> {
            if (message instanceof UserMessage userMessage) {
                checkContent(userMessage.singleText(), "REQUEST");
            } else if (message instanceof SystemMessage systemMessage) {
                checkContent(systemMessage.text(), "REQUEST");
            } else if (message instanceof AiMessage aiMessage) {
                checkContent(aiMessage.text(), "REQUEST");
            }
        });
    }

    @Override
    public void onResponse(ChatModelResponseContext context) {
        // Extract the AI's response text
        String response = context.chatResponse().aiMessage().text();

        // Check for sensitive content in the response
        checkContent(response, "RESPONSE");
    }

    private void checkContent(String text, String source) {
        if (text == null || text.isEmpty()) return;

        // Check for sensitive keywords
        SENSITIVE_KEYWORDS.stream()
                .filter(text.toLowerCase()::contains)
                .forEach(keyword ->
                        log.warn("{} contains sensitive keyword: {}", source, keyword)
                );

        // Check for email addresses
        if (EMAIL_PATTERN.matcher(text).find()) {
            log.warn("{} contains an email address", source);
        }
    }
}