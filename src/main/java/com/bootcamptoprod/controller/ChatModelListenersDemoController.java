package com.bootcamptoprod.controller;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ChatModelListenersDemoController {

    @Autowired
    private ChatModel chatModel; // Injected ChatModel with all registered listeners

    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> simpleChat(@RequestBody Map<String, String> request) {

        // Extract user message from the incoming request
        String userMessage = request.get("message");

        // Call the ChatModel - all registered listeners will be triggered automatically
        String response = chatModel.chat(userMessage);

        // Build and return the response
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("aiResponse", response);

        return ResponseEntity.ok(result);
    }
}