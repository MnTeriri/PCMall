package com.example.pcmallai.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface KnowledgeReplyAiService {
    @SystemMessage(fromResource = "knowledge-reply-system-prompt.txt")
    Flux<String> chatFlux(@MemoryId String memoryId, @UserMessage String userMessage);

    @SystemMessage(fromResource = "knowledge-reply-system-prompt.txt")
    Result<String> chat(@MemoryId String memoryId, @UserMessage String userMessage);
}
