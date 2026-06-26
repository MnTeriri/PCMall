package com.example.pcmallai.ai.service;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface KnowledgeReplyAiService {
    @SystemMessage(fromResource = "knowledge-reply-system-prompt.txt")
    Result<String> chat(@UserMessage String userMessage);
}
