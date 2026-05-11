package com.example.pcmallcommon.model.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class AiChatRequest {
    private String userId;//用户ID
    private String sessionId;//会话ID
    private String message;//用户发送的消息内容
    private Integer topK;//候选商品返回数量
}
