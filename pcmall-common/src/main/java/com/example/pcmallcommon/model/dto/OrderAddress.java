package com.example.pcmallcommon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class OrderAddress implements Serializable {
    @Serial
    private static final long serialVersionUID = 5505319482509286367L;

    private String province;//省
    private String city;//市
    private String district;//区
    private String addressDetail;//详细地址
    private String receiverName;//收件人
    private String phone;//手机号码
}
