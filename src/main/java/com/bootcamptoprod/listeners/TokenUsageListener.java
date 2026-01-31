package com.bootcamptoprod.listeners;

import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.output.TokenUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
@Order(3) // Runs Third: Updates metrics after the transaction is complete
public class TokenUsageListener implements ChatModelListener {

    private static final Logger log = LoggerFactory.getLogger(TokenUsageListener.class);

    // Thread-safe atomic counters for global token tracking
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong globalInputTokens = new AtomicLong(0);
    private final AtomicLong globalOutputTokens = new AtomicLong(0);

    @Override
    public void onRequest(ChatModelRequestContext requestContext) {
        // Increment the total request counter
        long currentCount = totalRequests.incrementAndGet();
        log.info("Global Request Count incremented by 1. Current request count: {}", currentCount);
    }

    @Override
    public void onResponse(ChatModelResponseContext responseContext) {
        // Extract token usage information from the response
        TokenUsage usage = responseContext.chatResponse().tokenUsage();

        if (usage != null) {
            // Update global token counters atomically (thread-safe)
            long totalIn = globalInputTokens.addAndGet(usage.inputTokenCount());
            long totalOut = globalOutputTokens.addAndGet(usage.outputTokenCount());

            // Log detailed token metrics for this request and cumulative totals
            log.info("Token Metrics Updated | This Req [In: {}, Out: {}, Total: {}] | Global Total [In: {}, Out: {}, Total: {}]",
                    usage.inputTokenCount(),
                    usage.outputTokenCount(),
                    usage.totalTokenCount(),
                    totalIn,
                    totalOut,
                    totalIn + totalOut);
        } else {
            log.warn("Token usage data not available.");
        }
    }
}