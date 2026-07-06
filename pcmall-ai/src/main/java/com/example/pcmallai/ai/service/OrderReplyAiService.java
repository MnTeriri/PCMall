package com.example.pcmallai.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

public interface OrderReplyAiService {
    @SystemMessage(fromResource = "order-reply-system-prompt.txt")
    TokenStream chatStream(@MemoryId String memoryId, @UserMessage String prompt);
}
