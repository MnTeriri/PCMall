package com.example.pcmallcommon.model.message;

import com.example.pcmallcommon.model.dto.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class GoodsChangeMessage {
    private Integer goodsId;

    /**
     * ADD / UPDATE / RECOVER / STATUS_CHANGE 时携带完整商品对象
     * DELETE 时为 null
     */
    private Goods goods;
    private Action action;

    /** 消息生产时间戳，消费者用于跳过过期消息 */
    private Long timestamp;

    public enum Action {
        ADD, UPDATE, DELETE, RECOVER, STATUS_CHANGE
    }
}
