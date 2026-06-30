package com.example.pcmallai.ai.graph;

import com.example.pcmallai.ai.graph.action.OrderIntentNode;
import com.example.pcmallai.ai.graph.state.OrderGraphState;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bsc.langgraph4j.GraphDefinition.END;
import static org.bsc.langgraph4j.GraphDefinition.START;
import static org.bsc.langgraph4j.GraphRepresentation.Type.PLANTUML;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

@Slf4j
@Configuration
public class OrderGraphConfig {

    @Bean
    public StateGraph<OrderGraphState> stateGraph(
            @Qualifier("orderIntentNode") OrderIntentNode orderIntentNode
    ) throws GraphStateException {
        StateGraph<OrderGraphState> stateGraph = new StateGraph<>(OrderGraphState::new)
                .addNode("resolveIntent", node_async(orderIntentNode))

                // ---- 边：定义执行顺序 ----
                .addEdge(START, "resolveIntent")
                .addEdge("resolveIntent", END);

        log.debug(stateGraph.getGraph(PLANTUML, "test").content());

        return stateGraph;
    }

    @Bean
    public CompiledGraph<OrderGraphState> orderGraph(
            @Qualifier("stateGraph") StateGraph<OrderGraphState> stateGraph
    ) throws GraphStateException {
        return stateGraph.compile();
    }
}
