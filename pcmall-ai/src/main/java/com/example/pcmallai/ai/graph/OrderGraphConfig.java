package com.example.pcmallai.ai.graph;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.langchain4j.serializer.std.LC4jStateSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.bsc.langgraph4j.GraphDefinition.END;
import static org.bsc.langgraph4j.GraphDefinition.START;
import static org.bsc.langgraph4j.GraphRepresentation.Type.PLANTUML;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

@Slf4j
@Configuration
public class OrderGraphConfig {

    @Bean
    public StateGraph<OrderGraphState> stateGraph(
            @Qualifier("orderIntentNode") NodeAction<OrderGraphState> orderIntentNode,
            @Qualifier("orderQueryNode") NodeAction<OrderGraphState> orderQueryNode,
            @Qualifier("orderActionNode") NodeAction<OrderGraphState> orderActionNode,
            @Qualifier("humanApprovalNode") NodeAction<OrderGraphState> humanApprovalNode,
            @Qualifier("orderExecuteNode") NodeAction<OrderGraphState> orderExecuteNode,
            @Qualifier("orderReplyNode") NodeAction<OrderGraphState> orderReplyNode
    ) throws GraphStateException {
        var stateSerializer = new LC4jStateSerializer<>(OrderGraphState::new);
        StateGraph<OrderGraphState> stateGraph = new StateGraph<>(OrderGraphState.SCHEMA, stateSerializer)
                // ---- 节点 ----
                .addNode("orderIntent", node_async(orderIntentNode))
                .addNode("orderQuery", node_async(orderQueryNode))
                .addNode("orderAction", node_async(orderActionNode))
                .addNode("humanApproval", node_async(humanApprovalNode))
                .addNode("orderExecute", node_async(orderExecuteNode))
                .addNode("generateReply", node_async(orderReplyNode))
                // ---- 边：定义执行顺序 ----
                .addEdge(START, "orderIntent")
                .addConditionalEdges(
                        "orderIntent",
                        edge_async(OrderGraphState::intentRoute),
                        Map.of(
                                "query", "orderQuery",
                                "action", "orderAction",
                                "unknown", "generateReply"
                        )
                )
                .addEdge("orderQuery", "generateReply")
                // action 路径分叉：校验失败 → 直接回复；校验通过 → 进确认
                .addConditionalEdges(
                        "orderAction",
                        edge_async(OrderGraphState::actionRoute),
                        Map.of(
                                "confirm", "humanApproval",
                                "failed", "generateReply"
                        )
                )
                // 确认分叉：批准 → 执行；拒绝 → 直接回复
                .addConditionalEdges(
                        "humanApproval",
                        edge_async(state -> state.approval() ? "approved" : "rejected"),
                        Map.of(
                                "approved", "orderExecute",
                                "rejected", "generateReply"
                        )
                )
                // 执行 → 回复
                .addEdge("orderExecute", "generateReply")
                .addEdge("generateReply", END);

        log.debug(stateGraph.getGraph(PLANTUML, "test").content());

        return stateGraph;
    }

    @Bean
    public CompiledGraph<OrderGraphState> orderGraph(
            @Qualifier("stateGraph") StateGraph<OrderGraphState> stateGraph
    ) throws GraphStateException {
        var compileConfig = CompileConfig.builder()
                .checkpointSaver(new MemorySaver())
                .interruptBefore("humanApproval")
                .build();

        return stateGraph.compile(compileConfig);
    }
}
