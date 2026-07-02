package com.example.pcmallai.ai.service.factory;

import com.example.pcmallai.ai.service.*;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Slf4j
@Configuration
public class AiServiceFactory {
    @Resource(name = "deepSeekChatModel")
    private ChatModel deepseekChatModel;

    @Resource(name = "deepSeekStreamingChatModel")
    private StreamingChatModel deepSeekStreamingChatModel;

    @Bean
    public ChatReplyAiService chatReplyAiService(
            @Qualifier("chatMemoryProvider") ChatMemoryProvider chatMemoryProvider
    ) {
        return AiServices.builder(ChatReplyAiService.class)
                .chatModel(deepseekChatModel)
                .streamingChatModel(deepSeekStreamingChatModel)
                .systemMessageTransformer(systemMessage -> systemMessage + " 今天的日期是 " + LocalDate.now() + "。")
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    @Bean
    public KnowledgeReplyAiService knowledgeReplyAiService(
            @Qualifier("chatMemoryProvider") ChatMemoryProvider chatMemoryProvider,
            @Qualifier("staticContentRetriever") ContentRetriever contentRetriever
    ) {
        return AiServices.builder(KnowledgeReplyAiService.class)
                .chatModel(deepseekChatModel)
                .streamingChatModel(deepSeekStreamingChatModel)
                .systemMessageTransformer(systemMessage -> systemMessage + " 今天的日期是 " + LocalDate.now() + "。")
                .chatMemoryProvider(chatMemoryProvider)
                .contentRetriever(contentRetriever)
                .build();
    }

    @Bean
    public OrderIntentAiService orderIntentAiService() {
        return AiServices.builder(OrderIntentAiService.class)
                .chatModel(deepseekChatModel)
                .build();
    }

    @Bean
    public OrderReplyAiService orderReplyAiService() {
        return AiServices.builder(OrderReplyAiService.class)
                .chatModel(deepseekChatModel)
                .streamingChatModel(deepSeekStreamingChatModel)
                .build();
    }

    @Bean
    public QueryRouterAiService queryRouterAiService() {
        return AiServices.builder(QueryRouterAiService.class)
                .chatModel(deepseekChatModel)
                .build();
    }

    @Bean
    public PurchaseIntentAiService shoppingIntentAiService() {
        return AiServices.builder(PurchaseIntentAiService.class)
                .chatModel(deepseekChatModel)
                .build();
    }

    @Bean
    public ShoppingReplyAiService shoppingReplyAiService(
            @Qualifier("chatMemoryProvider") ChatMemoryProvider chatMemoryProvider,
            @Qualifier("retrievalAugmentor") RetrievalAugmentor retrievalAugmentor
    ) {
        return AiServices.builder(ShoppingReplyAiService.class)
                .chatModel(deepseekChatModel)
                .streamingChatModel(deepSeekStreamingChatModel)
                .systemMessageTransformer(systemMessage -> systemMessage + " 今天的日期是 " + LocalDate.now() + "。")
                .chatMemoryProvider(chatMemoryProvider)
                .retrievalAugmentor(retrievalAugmentor)
                .build();
    }
}
