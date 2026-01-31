package com.bootcamptoprod.listeners;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2) // Runs Second: Logs the request and starts the timer
public class LoggingAndTimingListener implements ChatModelListener {

    private static final Logger log = LoggerFactory.getLogger(LoggingAndTimingListener.class);
    private static final String START_TIME_KEY = "req_start_time";

    @Override
    public void onRequest(ChatModelRequestContext chatModelRequestContext) {

        // 1. Store the start time in the context attributes for later retrieval
        chatModelRequestContext.attributes().put(START_TIME_KEY, System.currentTimeMillis());

        // 2. Log the outgoing request
        ChatRequest request = chatModelRequestContext.chatRequest();

        request.messages()
                .forEach(message -> {
                            if (message instanceof UserMessage userMessage) {
                                logRequest(userMessage.singleText());
                            } else if (message instanceof SystemMessage systemMessage) {
                                logRequest(systemMessage.text());
                            } else if (message instanceof AiMessage aiMessage) {
                                logRequest(aiMessage.text());
                            }
                        }
                );
    }

    @Override
    public void onResponse(ChatModelResponseContext chatModelResponseContext) {
        // 1. Retrieve the start time from attributes and calculate duration
        Long startTime = (Long) chatModelResponseContext.attributes().get(START_TIME_KEY);
        long duration = System.currentTimeMillis() - startTime;

        // 2. Log the response along with timing information
        log.info("<< Received Response from LLM in {} ms. Response: {}", duration, chatModelResponseContext.chatResponse().aiMessage().text());
    }

    @Override
    public void onError(ChatModelErrorContext chatModelErrorContext) {

        // 1. Retrieve start time and calculate duration even for failed requests
        Long startTime = (Long) chatModelErrorContext.attributes().get(START_TIME_KEY);
        long duration = System.currentTimeMillis() - startTime;

        // 2. Log error details along with timing
        log.error("!! Error during LLM interaction: {}. Request processing duration: {} ms", chatModelErrorContext.error().getMessage(), duration);
    }

    // Helper method to log outgoing requests
    private void logRequest(String text) {
        log.info(">> Sending Request to LLM: {}", text);
    }
}