package com.example.pcmallcommon.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class ChatHistoryVO {
    private String role;        // USER / AI
    private String content;     // 消息文本
    private LocalDateTime createTime;
}
