package com.example.pcmallai.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface ShoppingReplyAiService {
    @SystemMessage(fromResource = "recommend-system-prompt.txt")
    Flux<String> chatFlux(@MemoryId String memoryId, @UserMessage String prompt);

    @SystemMessage(fromResource = "recommend-system-prompt.txt")
    String chat(@UserMessage String prompt);
}
