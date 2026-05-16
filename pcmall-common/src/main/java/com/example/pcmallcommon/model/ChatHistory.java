package com.example.pcmallcommon.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName("ai_chat_history")
public class ChatHistory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String memoryId;
    private Integer msgIndex;           // 这条消息在会话中的顺序编号
    private ChatHistoryType type;       // 消息类型
    private String content;             // 可读文本，快速浏览用
    private String rawJson;             // ChatMessage 完整 JSON
    private LocalDateTime createTime;   // 消息创建时间

    @AllArgsConstructor
    @ToString
    @Getter
    public enum ChatHistoryType {
        SYSTEM(0, "系统消息"),
        USER(1, "用户原始消息"),
        RAW_USER(2, "用户消息包装后的 prompt"),
        AI(3, "AI消息");

        @JsonValue
        @EnumValue
        private final Integer code;
        private final String name;

        @JsonCreator
        public static ChatHistoryType fromCode(Integer code) {
            for (ChatHistoryType type : values()) {
                if (Objects.equals(type.code, code)) {
                    return type;
                }
            }
            return null;
        }
    }
}
