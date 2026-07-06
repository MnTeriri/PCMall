package com.example.pcmallai.ai.graph.action;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallai.ai.service.OrderReplyAiService;
import com.example.pcmallai.model.OrderIntent;
import com.example.pcmallcommon.model.dto.Order;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_STREAMING;
import static com.example.pcmallai.ai.graph.state.OrderGraphState.MESSAGES_STATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderReplyNode implements NodeAction<OrderGraphState> {

    private final OrderReplyAiService orderReplyAiService;

    @Override
    public Map<String, Object> apply(OrderGraphState state) throws Exception {
        String memoryId = state.memoryId();
        String prompt = buildPrompt(state);

        var generator = StreamingChatGenerator.<OrderGraphState>builder()
                .mapResult(response -> Map.of(MESSAGES_STATE, response.aiMessage()))
                .startingNode("generateReply")
                .startingState(state)
                .build();

        StreamingChatResponseHandler handler = generator.handler();

        orderReplyAiService.chatStream(memoryId, prompt)
                .onPartialResponse(handler::onPartialResponse)
                .onCompleteResponse(handler::onCompleteResponse)
                .onError(handler::onError)
                .start();

        return Map.of(KEY_STREAMING, generator);
    }

    private String buildPrompt(OrderGraphState state) {
        OrderIntent orderIntent = state.orderIntent();
        String userMessage = state.userMessage();
        String intentRoute = state.intentRoute();
        log.debug("intentRoute={}", intentRoute);

        if ("query".equals(intentRoute)) {
            List<Order> orders = state.candidateOrders();
            return buildQueryPrompt(userMessage, orders);
        }

        if ("action".equals(intentRoute)) {
            String actionRoute = state.actionRoute();
            Boolean approval = state.approval();
            Boolean successExecute = state.isSuccessExecute();
            log.debug("actionRoute={}, approval={}, successExecute={}", actionRoute, approval, successExecute);

            if ("failed".equals(actionRoute)) {
                return buildFailedPrompt(userMessage, state.failureMessage());
            }

            if (!approval) {
                return buildRejectedPrompt(userMessage, orderIntent);
            }

            if (successExecute) {
                return buildSuccessPrompt(userMessage, orderIntent.getOid());
            } else {
                return buildFailedPrompt(userMessage, state.failureMessage());
            }
        }

        return buildUnknownPrompt(userMessage);
    }

    private String buildQueryPrompt(String userMessage, List<Order> orders) {
        if (orders.isEmpty()) {
            return "用户说：" + userMessage + "\n\n"
                    + "但没有找到任何订单结果。请友好地告诉用户，并建议尝试其他查询方式。";
        }

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

    private String buildSuccessPrompt(String userMessage, String oid) {
        return "用户说：" + userMessage + "\n\n"
                + "执行结果：订单 " + oid + " 操作执行成功。\n"
                + "请生成简短确认回复（一句话）。";
    }

    private String buildFailedPrompt(String userMessage, String failureMessage) {
        return "用户说：" + userMessage + "\n\n"
                + "错误信息：" + failureMessage + "\n"
                + "请根据以上错误信息生成一句简短的回复告知用户。";
    }

    private String buildRejectedPrompt(String userMessage, OrderIntent intent) {
        return "用户说：" + userMessage + "\n\n"
                + "但用户取消了「" + intent.getAction().name() + "」操作。请生成简短确认回复（一句话）。";
    }

    private String buildUnknownPrompt(String userMessage) {
        return "用户说：" + userMessage + "\n\n"
                + "但用户说了一些无法理解的话。请友好地告诉用户，你可以帮助：查询订单、查看待付款订单、付款、取消订单、申请退货、确认收货。";
    }
}
