package com.example.pcmallai.ai.memory;

import com.example.pcmallai.ai.memory.store.PersistentChatMemoryStore;
import com.example.pcmallai.service.IChatHistoryService;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ChatMemoryConfig {

    @Bean
    public ChatMemoryStore redisChatMemoryStore(
            @Value("${spring.data.redis.host}") String host,
            @Value("${spring.data.redis.port}") Integer port,
            @Value("${spring.data.redis.username}") String username,
            @Value("${spring.data.redis.password}") String password
    ) {
        return new RedisChatMemoryStore(host, port, null, password);
    }

    @Bean
    public ChatMemoryStore persistentChatMemoryStore(
            @Qualifier("redisChatMemoryStore") ChatMemoryStore chatMemoryStore,
            @Qualifier("chatHistoryServiceImpl") IChatHistoryService chatHistoryService
    ) {
        return new PersistentChatMemoryStore(chatMemoryStore, chatHistoryService, 20);
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider(
            @Qualifier("redisChatMemoryStore") ChatMemoryStore chatMemoryStore
    ) {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .chatMemoryStore(chatMemoryStore)
                .maxMessages(20)
                .build();
    }
}
