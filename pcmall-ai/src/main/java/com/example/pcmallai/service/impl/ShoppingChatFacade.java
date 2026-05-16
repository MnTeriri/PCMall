package com.example.pcmallai.service.impl;

import com.example.pcmallai.ai.service.ShoppingIntentAiService;
import com.example.pcmallai.ai.service.ShoppingReplyAiService;
import com.example.pcmallai.service.IChatHistoryService;
import com.example.pcmallcommon.model.ChatHistory;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.model.ai.AiChatEvent;
import com.example.pcmallcommon.model.ai.AiChatRequest;
import com.example.pcmallcommon.model.ai.PurchaseIntent;
import dev.langchain4j.data.message.ChatMessageSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingChatFacade {

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

        ChatHistory chatHistory = new ChatHistory()
                .setMemoryId(memoryId)
                .setMsgIndex(0)
                .setType(ChatHistory.ChatHistoryType.USER)
                .setContent(userMessage)
                .setRawJson("");
        chatHistoryService.insertChatHistory(chatHistory);

        String prompt = buildPrompt(userMessage, intent, goodsList);

        return Flux.concat(
                Flux.just(
                        new AiChatEvent(AiChatEvent.AiChatEventType.START, "开始处理"),
                        new AiChatEvent(AiChatEvent.AiChatEventType.GOODS, goodsList)
                ),
                shoppingReplyAiService.chatFlux(memoryId, prompt)
                        .map(text -> new AiChatEvent(AiChatEvent.AiChatEventType.TEXT, text)),
                Flux.just(new AiChatEvent(AiChatEvent.AiChatEventType.DONE, "完成"))
        );
    }

    private String buildPrompt(String userMessage, PurchaseIntent intent, List<Goods> goodsList) {
        return """
                用户原始需求：
                %s

                解析出的购买意图：
                %s

                候选商品列表：
                %s

                请你完成以下任务：
                1. 先给出购买建议
                2. 再结合候选商品说明推荐理由
                3. 如果候选商品不够匹配，要明确指出不足
                4. 不允许编造商品不存在的参数
                5. 使用简洁、自然、适合前端流式展示的中文输出
                """.formatted(userMessage, intent, goodsList);
    }

}
