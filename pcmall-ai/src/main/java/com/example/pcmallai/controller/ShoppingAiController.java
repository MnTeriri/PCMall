package com.example.pcmallai.controller;

import com.example.pcmallai.service.facade.OrderGraphService;
import com.example.pcmallai.service.facade.ShoppingChatFacade;
import com.example.pcmallcommon.model.ai.AiChatEvent;
import com.example.pcmallcommon.model.ai.AiChatRequest;
import com.example.pcmallcommon.response.ResponseResult;
import dev.langchain4j.service.Result;
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

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ai/assistant")
@RequiredArgsConstructor
@Tag(name = "AI接口")
public class ShoppingAiController {
    private final ShoppingChatFacade shoppingChatFacade;
    private final OrderGraphService orderGraphService;

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

    @PostMapping(value = "/test")
    public ResponseResult<HashMap<String, Object>> test(@RequestBody AiChatRequest request) {
        Result<String> result = shoppingChatFacade.testChat(request);
        if (result == null) {
            return ResponseResult.ok();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", result.content());
        map.put("tokenUsage", result.tokenUsage().toString());
        map.put("sources", result.sources().stream().map(content -> {
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put("textSegment",content.textSegment().text());
                    temp.put("metadata",content.metadata().toString());
                    return temp;
                }).toList()
        );
        map.put("toolExecutions", result.toolExecutions());
        map.put("finishReason", result.finishReason());
        return ResponseResult.ok(map);
    }

    @PostMapping(value = "/test1")
    public ResponseResult<Map<String, Object>> test1(@RequestBody AiChatRequest request) {
        return ResponseResult.ok(orderGraphService.chatFlux(request));
    }
}
