package com.example.pcmallai.service.facade;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallcommon.model.ai.AiChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphInput;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.RunnableConfig;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_APPROVAL;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderGraphService {
    private final CompiledGraph<OrderGraphState> orderGraph;

    public Flux<NodeOutput<OrderGraphState>> chatFlux(AiChatRequest request) {
        String memoryId = request.getUserId() + ":" + request.getSessionId();
        Boolean approval = request.getApproval();

        Map<String, Object> initialState = Map.of(
                OrderGraphState.KEY_USER_ID, request.getUserId(),
                OrderGraphState.KEY_SESSION_ID, request.getSessionId(),
                OrderGraphState.KEY_MEMORY_ID, memoryId,
                OrderGraphState.KEY_USER_MESSAGE, request.getMessage()
        );

        RunnableConfig config = RunnableConfig.builder()
                .threadId(memoryId)
                .build();

        AsyncGenerator<NodeOutput<OrderGraphState>> stream;

        if (approval == null) {
            stream = orderGraph.stream(initialState, config);
        } else {
            stream = orderGraph.stream(GraphInput.resume(Map.of(KEY_APPROVAL, approval)), config);
        }

        return Flux.push(sink -> {
            try {
                for (NodeOutput<OrderGraphState> out : stream) {
                    sink.next(out);
                }
                sink.complete();
            } catch (Exception e) {
                sink.error(e);
            }
        });
    }
}
