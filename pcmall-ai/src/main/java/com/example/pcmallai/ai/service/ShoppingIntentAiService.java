package com.example.pcmallai.ai.service;

import com.example.pcmallcommon.model.ai.PurchaseIntent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ShoppingIntentAiService {
    @SystemMessage(fromResource = "intent-system-prompt.txt")
    PurchaseIntent parseIntent(@UserMessage String userMessage);
}
