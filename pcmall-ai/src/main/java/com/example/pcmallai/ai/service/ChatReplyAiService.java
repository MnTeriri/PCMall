package com.example.pcmallai.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ChatReplyAiService {
    @SystemMessage(fromResource = "chat-reply-system-prompt.txt")
    Result<String> chat(@MemoryId String memoryId, @UserMessage String userMessage);
}
