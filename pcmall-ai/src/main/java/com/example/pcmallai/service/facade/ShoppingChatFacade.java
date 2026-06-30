package com.example.pcmallai.service.facade;

import com.example.pcmallai.ai.service.*;
import com.example.pcmallai.model.PurchaseIntent;
import com.example.pcmallai.model.QueryRoute;
import com.example.pcmallai.service.IChatHistoryService;
import com.example.pcmallai.service.goods.GoodsQueryService;
import com.example.pcmallcommon.model.ai.AiChatEvent;
import com.example.pcmallcommon.model.ai.AiChatRequest;
import com.example.pcmallcommon.model.dto.Goods;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.service.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static com.example.pcmallai.model.QueryRoute.QueryType.KNOWLEDGE;
import static com.example.pcmallai.model.QueryRoute.QueryType.SHOPPING;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingChatFacade {

    private final QueryRouterAiService queryRouterAiService;
    private final ChatReplyAiService chatReplyAiService;
    private final KnowledgeReplyAiService knowledgeReplyAiService;
    private final PurchaseIntentAiService purchaseIntentAiService;
    private final ShoppingReplyAiService shoppingReplyAiService;
    private final IChatHistoryService chatHistoryService;
    private final GoodsQueryService goodsQueryService;

    public Flux<AiChatEvent> chatFlux(AiChatRequest request) {
        String memoryId = request.getUserId() + ":" + request.getSessionId();
        String userMessage = request.getMessage();

        QueryRoute route = queryRouterAiService.route(userMessage);
        QueryRoute.QueryType type = route.getType();
        log.debug("AI 路由结果: {}", route);

        Flux<AiChatEvent> flux;

        if (type == SHOPPING) {
            PurchaseIntent intent = purchaseIntentAiService.parseIntent(userMessage);
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

            flux = Flux.concat(
                    Flux.just(new AiChatEvent(AiChatEvent.AiChatEventType.GOODS, goodsList)),
                    shoppingReplyAiService.chatFlux(memoryId, buildPrompt(userMessage, intent, goodsList), parameters)
                            .map(text -> new AiChatEvent(AiChatEvent.AiChatEventType.TEXT, text))
            );
        } else if (type == KNOWLEDGE) {
            flux = knowledgeReplyAiService.chatFlux(memoryId, userMessage)
                    .map(text -> new AiChatEvent(AiChatEvent.AiChatEventType.TEXT, text));
        } else {
            flux = chatReplyAiService.chatFlux(memoryId, userMessage)
                    .map(text -> new AiChatEvent(AiChatEvent.AiChatEventType.TEXT, text));
        }

        return Flux.concat(
                Flux.just(new AiChatEvent(AiChatEvent.AiChatEventType.START, "开始处理")),
                flux,
                Flux.just(new AiChatEvent(AiChatEvent.AiChatEventType.DONE, "完成"))
        );
    }

    public Result<String> testChat(AiChatRequest request) {
        String memoryId = request.getUserId() + ":" + request.getSessionId();
        String userMessage = request.getMessage();

        QueryRoute route = queryRouterAiService.route(userMessage);
        QueryRoute.QueryType type = route.getType();
        log.debug("AI 路由结果: {}", route);

        if (type == SHOPPING) {
            PurchaseIntent intent = purchaseIntentAiService.parseIntent(userMessage);
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

            return shoppingReplyAiService.chat(memoryId, buildPrompt(userMessage, intent, goodsList), parameters);
        } else if (type == KNOWLEDGE) {
            return knowledgeReplyAiService.chat(memoryId, userMessage);
        } else {
            return chatReplyAiService.chat(memoryId, userMessage);
        }
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
