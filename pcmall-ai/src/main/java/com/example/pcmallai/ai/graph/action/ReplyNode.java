package com.example.pcmallai.ai.graph.action;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallai.ai.service.OrderReplyAiService;
import com.example.pcmallcommon.model.dto.Order;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.service.TokenStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.example.pcmallai.ai.graph.state.OrderGraphState.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReplyNode implements NodeAction<OrderGraphState> {

    private final OrderReplyAiService orderReplyAiService;

    @Override
    public Map<String, Object> apply(OrderGraphState state) throws Exception {
        String userMessage = state.userMessage();
        List<Order> orders = state.candidateOrders();
        String prompt = buildPrompt(userMessage, orders);

        var generator = StreamingChatGenerator.<OrderGraphState>builder()
                .mapResult(response -> Map.of(MESSAGES_STATE, response.aiMessage()))
                .startingNode("generateReply")
                .startingState(state)
                .build();

        StreamingChatResponseHandler handler = generator.handler();

        orderReplyAiService.chatStream(prompt)
                .onPartialResponse(handler::onPartialResponse)
                .onCompleteResponse(handler::onCompleteResponse)
                .onError(handler::onError)
                .start();

        return Map.of(KEY_STREAMING, generator);
    }

    private String buildPrompt(String userMessage, List<Order> orders) {
        StringBuilder sb = new StringBuilder();
        sb.append("用户说：").append(userMessage).append("\n\n");
        sb.append("找到以下订单：\n");
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            sb.append(i + 1).append(". 订单号: ").append(o.getOid())
                    .append(", 状态: ").append(o.getStatus() != null ? o.getStatus().getName() : "未知")
                    .append(", 金额: ").append(o.getPrice()).append("元")
                    .append(", 时间: ").append(o.getCreateTime()).append("\n");
        }
        return sb.toString();
    }
}
