package com.clesun.kit;


import com.clesun.dto.ResultEntity;
import com.clesun.entity.PaymentRefundLog;
import com.clesun.kit.dto.PayParam;
import com.clesun.kit.dto.RefundParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付接口
 *
 * @author liuk
 */
public interface Payment {

    /**
     * 小程序支付
     *
     * @param request  HttpServletRequest
     * @param payParam 支付参数
     * @return 二维码内容
     */
    ResultEntity mpPay(HttpServletRequest request, PayParam payParam);

    /**
     * app支付
     *
     * @param request  HttpServletRequest
     * @param payParam 支付参数
     * @return app支付所需参数
     */
    ResultEntity appPay(HttpServletRequest request, PayParam payParam);

    /**
     * 展示二维码扫描支付
     *
     * @param request  HttpServletRequest
     * @param payParam 支付参数
     * @return 二维码内容
     */
    ResultEntity nativePay(HttpServletRequest request, PayParam payParam) ;

    /**
     * 支付异步通知
     *
     * @param request HttpServletRequest
     */
    void payNotify(HttpServletRequest request, PayParam payParam) ;

    /**
     * 退款申请
     * @param paymentRefundLog
     */
    ResultEntity refund(PaymentRefundLog paymentRefundLog, RefundParam refundParam);

    /**
     * 退款异步通知
     *
     * @param request HttpServletRequest
     */
    void refundNotify(HttpServletRequest request, RefundParam refundParam);
}
