package com.example.pcmallai.ai;

import com.example.pcmallai.ai.service.ShoppingIntentAiService;
import com.example.pcmallai.ai.service.ShoppingReplyAiService;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ShoppingAiServiceFactory {
    @Autowired
    @Qualifier("deepSeekChatModel")
    private ChatModel deepseekChatModel;

    @Autowired
    @Qualifier("deepSeekStreamingChatModel")
    private StreamingChatModel deepSeekStreamingChatModel;

    @Autowired
    private ContentRetriever contentRetriever;

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.withMaxMessages(20);
    }

    @Bean
    public ShoppingIntentAiService shoppingIntentAiService() {
        return AiServices.builder(ShoppingIntentAiService.class)
                .chatModel(deepseekChatModel)
                .build();
    }

    @Bean
    public ShoppingReplyAiService shoppingReplyAiService(ChatMemoryProvider chatMemoryProvider) {
        return AiServices.builder(ShoppingReplyAiService.class)
                .chatModel(deepseekChatModel)
                .streamingChatModel(deepSeekStreamingChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .contentRetriever(contentRetriever)
                .build();
    }
}
