package com.example.pcmallcommon.model.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class AiChatEvent {
    private AiChatEventType type;
    private Object data;

    public enum AiChatEventType {
        START,// 开始处理请求
        INTENT,// 解析用户购买意图
        GOODS,// 返回候选商品列表
        RAG,// 返回RAG知识检索结果
        TEXT,// 流式文本内容（模型生成过程）
        DONE,// 处理完成
        ERROR,// 错误信息
    }
}
