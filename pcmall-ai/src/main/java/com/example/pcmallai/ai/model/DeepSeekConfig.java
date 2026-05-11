package com.example.pcmallai.ai.model;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Slf4j
@Configuration
public class DeepSeekConfig {
    @Value("${langchain4j.open-ai.chat-model.base-url}")
    private String baseUrl;
    @Value("${langchain4j.open-ai.chat-model.api-key}")
    private String apiKey;
    @Value("${langchain4j.open-ai.chat-model.model-name}")
    private String modelName;

    @Value("${langchain4j.open-ai.streaming-chat-model.base-url}")
    private String streamingBaseUrl;
    @Value("${langchain4j.open-ai.streaming-chat-model.api-key}")
    private String streamingApiKey;
    @Value("${langchain4j.open-ai.streaming-chat-model.model-name}")
    private String streamingModelName;

    @Bean
    public ChatModel deepSeekChatModel(
            @Qualifier("openAiChatModelHttpClientBuilder") HttpClientBuilder httpClientBuilder
    ) {
        return OpenAiChatModel.builder()
                .httpClientBuilder(httpClientBuilder)
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .logRequests(true)
                .logResponses(true)
                .customParameters(Map.of(
                        "thinking", Map.of("type", "disabled")
                ))
                .build();
    }

    @Bean
    public StreamingChatModel deepSeekStreamingChatModel(
            @Qualifier("openAiStreamingChatModelHttpClientBuilder") HttpClientBuilder httpClientBuilder
    ) {
        return OpenAiStreamingChatModel.builder()
                .httpClientBuilder(httpClientBuilder)
                .baseUrl(streamingBaseUrl)
                .apiKey(streamingApiKey)
                .modelName(streamingModelName)
                .logRequests(true)
                .logResponses(true)
                .customParameters(Map.of(
                        "thinking", Map.of("type", "disabled")
                ))
                .build();
    }
}
