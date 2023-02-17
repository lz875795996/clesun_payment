package com.clesun.kit.plugin;

import com.clesun.config.SpringContextUtil;
import com.clesun.dto.ResultEntity;
import com.clesun.entity.PaymentRefundLog;
import com.clesun.enums.PaymentMethodEnum;
import com.clesun.kit.Payment;
import com.clesun.kit.dto.RefundParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 退款支持
 *
 * @author liuk
 */
@Component
@Slf4j
public class RefundSupport {

    /**
     * 退款
     */
    public ResultEntity refund(PaymentMethodEnum paymentMethodEnum, RefundParam refundParam){
        //获取支付插件
        Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());

        PaymentRefundLog paymentRefundLog = PaymentRefundLog.builder()
                .isRefund(false)
                .totalAmount(refundParam.getTotal())
                .refundAmount(refundParam.getRefund())
                .paymentName(paymentMethodEnum.name())
                .paymentReceivableNo(refundParam.getReceivableNo())
                .outOrderNo("AF" + refundParam.getSn())
                .sn(refundParam.getSn())
                .refundReason(refundParam.getReason())
                .build();

       return  payment.refund(paymentRefundLog, refundParam);
    }


    /**
     * 退款通知
     *
     * @param paymentMethodEnum 支付渠道
     */
    public void notify(PaymentMethodEnum paymentMethodEnum,
                       HttpServletRequest request,
                       String uniqueCode,
                       String sn) {

        //获取支付插件
        Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());

        RefundParam refundParam = new RefundParam();
        refundParam.setUniqueCode(uniqueCode);
        refundParam.setPaymentMethodEnum(paymentMethodEnum);
        refundParam.setSn(sn);
        payment.refundNotify(request, refundParam);
    }

}
