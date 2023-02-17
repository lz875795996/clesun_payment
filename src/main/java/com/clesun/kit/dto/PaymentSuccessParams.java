package com.clesun.kit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付成功参数
 *
 * @author liuk
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessParams {

    /**
     * 第三方流水
     */
    private String receivableNo;

    /**
     * 支付参数
     */
    private PayParam payParam;
}
