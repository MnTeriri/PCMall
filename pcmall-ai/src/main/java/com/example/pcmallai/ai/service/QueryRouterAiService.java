package com.example.pcmallai.ai.service;

import com.example.pcmallai.model.QueryRoute;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface QueryRouterAiService {
    @SystemMessage(fromResource = "query-router-system-prompt.txt")
    QueryRoute route(@UserMessage String message);
}
