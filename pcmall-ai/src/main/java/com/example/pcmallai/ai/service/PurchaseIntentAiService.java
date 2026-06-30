package com.example.pcmallai.ai.service;

import com.example.pcmallai.model.PurchaseIntent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface PurchaseIntentAiService {
    @SystemMessage(fromResource = "purchase-intent-system-prompt.txt")
    PurchaseIntent parseIntent(@UserMessage String userMessage);
}
