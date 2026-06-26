package com.example.pcmallai.service.facade;

import com.example.pcmallai.ai.service.KnowledgeReplyAiService;
import com.example.pcmallai.ai.service.QueryRouterAiService;
import com.example.pcmallai.ai.service.ShoppingIntentAiService;
import com.example.pcmallai.ai.service.ShoppingReplyAiService;
import com.example.pcmallai.model.QueryRoute;
import com.example.pcmallai.service.IChatHistoryService;
import com.example.pcmallai.service.goods.GoodsQueryService;
import com.example.pcmallcommon.model.ai.AiChatEvent;
import com.example.pcmallcommon.model.ai.AiChatRequest;
import com.example.pcmallcommon.model.ai.PurchaseIntent;
import com.example.pcmallcommon.model.dto.ChatHistory;
import com.example.pcmallcommon.model.dto.Goods;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.service.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.example.pcmallai.model.QueryRoute.QueryType.KNOWLEDGE;
import static com.example.pcmallai.model.QueryRoute.QueryType.SHOPPING;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingChatFacade {

    private final QueryRouterAiService queryRouterAiService;
    private final KnowledgeReplyAiService knowledgeReplyAiService;
    private final ShoppingIntentAiService shoppingIntentAiService;
    private final ShoppingReplyAiService shoppingReplyAiService;
    private final IChatHistoryService chatHistoryService;
    private final GoodsQueryService goodsQueryService;

    public Flux<AiChatEvent> chatFlux(AiChatRequest request) {
        String memoryId = request.getUserId() + ":" + request.getSessionId();
        String userMessage = request.getMessage();
        log.debug("memoryId：{}", memoryId);

        PurchaseIntent intent = shoppingIntentAiService.parseIntent(userMessage);
        log.debug("AI分析的购买意图：{}", intent);
        List<Goods> goodsList = goodsQueryService.queryCandidateGoods(intent, request.getTopK());
        log.debug("查询出的商品：{}", goodsList);

        // MySQL增量存储（原始用户 prompt）
        ChatHistory chatHistory = new ChatHistory()
                .setMemoryId(memoryId)
                .setMsgIndex(-1L)
                .setType(ChatHistory.ChatHistoryType.USER)
                .setContent(userMessage)
                .setRawJson(null);
        chatHistoryService.insertChatHistory(chatHistory);
        log.debug("MySQL 增量追加: memoryId = {} , type = {}, msg_index = {}", memoryId, chatHistory.getType(), -1);

        String prompt = buildPrompt(userMessage, intent, goodsList);
        InvocationParameters parameters = InvocationParameters.from(
                Map.of("minPrice", intent.getMinPrice() == null ? BigDecimal.ZERO : intent.getMinPrice(),
                        "maxPrice", intent.getMaxPrice() == null ? BigDecimal.valueOf(Integer.MAX_VALUE) : intent.getMaxPrice()
                )
        );
        return Flux.concat(
                Flux.just(
                        new AiChatEvent(AiChatEvent.AiChatEventType.START, "开始处理"),
                        new AiChatEvent(AiChatEvent.AiChatEventType.GOODS, goodsList)
                ),
                shoppingReplyAiService.chatFlux(memoryId, prompt, parameters)
                        .map(text -> new AiChatEvent(AiChatEvent.AiChatEventType.TEXT, text)),
                Flux.just(new AiChatEvent(AiChatEvent.AiChatEventType.DONE, "完成"))
        );
    }

    public Result<String> testChat(AiChatRequest request) {
        String memoryId = request.getUserId() + ":" + request.getSessionId();
        String userMessage = request.getMessage();

        QueryRoute route = queryRouterAiService.route(userMessage);
        QueryRoute.QueryType type = route.getType();

        log.debug("AI 路由结果: {}", route);

        if (type == KNOWLEDGE) {
            return knowledgeReplyAiService.chat(userMessage);
        }

        if (type == SHOPPING) {
            PurchaseIntent intent = shoppingIntentAiService.parseIntent(userMessage);
            log.debug("AI 分析的购买意图: {}", intent);

            List<Goods> goodsList = goodsQueryService.queryCandidateGoods(intent, request.getTopK());
            log.debug("查询出的候选商品数量: {}", goodsList.size());

            InvocationParameters parameters = InvocationParameters.from(
                    Map.of(
                            "userMessage", userMessage,
                            "purchaseIntent", intent,
                            "goodsList", goodsList
                    )
            );

            return shoppingReplyAiService.chat(buildPrompt(userMessage, intent, goodsList), parameters);
        }

        return knowledgeReplyAiService.chat(userMessage);
    }

    private String buildPrompt(String userMessage, PurchaseIntent intent, List<Goods> goodsList) {
        return """
                用户原始需求：
                %s

                解析出的购买意图：
                %s

                候选商品列表：
                %s
                """.formatted(userMessage, intent, goodsList);
    }

}
