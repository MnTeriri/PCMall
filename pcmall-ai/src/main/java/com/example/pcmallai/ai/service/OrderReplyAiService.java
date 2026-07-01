package com.example.pcmallai.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface OrderReplyAiService {
    @SystemMessage(fromResource = "order-reply-system-prompt.txt")
    String chat(@UserMessage String prompt);
}
