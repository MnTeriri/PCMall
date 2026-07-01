package com.example.pcmallcommon.model.dto;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Storage implements Serializable {
    @Serial
    private static final long serialVersionUID = -9011615350298559813L;

    private Integer id;//库存编号
    private Integer gid;//商品编号
    private String uid;//用户编号
    private Integer count;//数量
    private StorageState status;//状态 0入库、1出库、2卖出、3退货、4取消订单

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;//创建时间

    @AllArgsConstructor
    @ToString
    @Getter
    public enum StorageState {
        IN_STOCK(0, "入库"),
        OUT_STOCK(1, "出库"),
        SOLD(2, "卖出"),
        RETURNED(3, "退货"),
        CANCELED(4, "取消订单");

        @JsonValue
        @EnumValue
        private final Integer code;
        private final String name;

        @JsonCreator
        public static StorageState fromCode(Integer code) {
            for (StorageState state : values()) {
                if (Objects.equals(state.code, code)) {
                    return state;
                }
            }
            return null;
        }
    }
}
