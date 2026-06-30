package com.example.pcmallai.ai.service;

import com.example.pcmallai.model.OrderIntent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface OrderIntentAiService {
    @SystemMessage(fromResource = "order-intent-system-prompt.txt")
    OrderIntent parseIntent(@UserMessage String userMessage);
}
