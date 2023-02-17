package com.clesun.service;


import com.clesun.kit.dto.PaymentSuccessParams;
import com.clesun.kit.dto.RefundSuccessParams;

/**
 * 支付日志 业务层
 *
 * @author liuk
 */
public interface PaymentService {

    /**
     * 支付成功通知
     *
     * @param paymentSuccessParams 支付成功回调参数
     */
    void paySuccess(PaymentSuccessParams paymentSuccessParams);

    /**
     * 退款结果通知
     * @param refundSuccessParams
     */
    void refundSuccess(RefundSuccessParams refundSuccessParams);
}