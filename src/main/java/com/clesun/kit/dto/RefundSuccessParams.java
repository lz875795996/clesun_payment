package com.clesun.kit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 退款成功参数
 *
 * @author liuk
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundSuccessParams {

    /**
     * 是否已退款
     */
    private Boolean isRefund;

    /**
     * 退款失败原因
     */
    private String errorMessage;

    /**
     * 退款参数
     */
    private RefundParam refundParam;
}
