package com.example.pcmallai.ai.service;

import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface ShoppingReplyAiService {
    @SystemMessage(fromResource = "recommend-system-prompt.txt")
    Flux<String> chatFlux(@MemoryId String memoryId, @UserMessage String prompt, InvocationParameters parameters);

    @SystemMessage(fromResource = "recommend-system-prompt.txt")
    Result<String> chat(@MemoryId String memoryId, @UserMessage String prompt, InvocationParameters parameters);
}
