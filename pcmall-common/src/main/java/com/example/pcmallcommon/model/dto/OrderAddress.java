package com.example.pcmallcommon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class OrderAddress {
    private String province;//省
    private String city;//市
    private String district;//区
    private String addressDetail;//详细地址
    private String receiverName;//收件人
    private String phone;//手机号码
}
