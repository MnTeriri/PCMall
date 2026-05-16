package com.example.pcmallai.controller;

import com.example.pcmallai.service.impl.ShoppingChatFacade;
import com.example.pcmallcommon.model.ai.AiChatEvent;
import com.example.pcmallcommon.model.ai.AiChatRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/ai/assistant")
@RequiredArgsConstructor
@Tag(name = "AI接口")
public class ShoppingAiController {
    private final ShoppingChatFacade shoppingChatFacade;

    @Operation(summary = "AI聊天接口")
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<AiChatEvent>> chat(@RequestBody AiChatRequest request) {
        return shoppingChatFacade.chatFlux(request)
                .map(aiChatEvent -> ServerSentEvent.<AiChatEvent>builder()
                        .event(aiChatEvent.getType().name())
                        .data(aiChatEvent)
                        .build()
                );
    }
}
