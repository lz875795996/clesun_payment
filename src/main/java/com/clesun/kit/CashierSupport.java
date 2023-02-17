package com.clesun.kit;

import com.clesun.config.SpringContextUtil;
import com.clesun.dto.ResultEntity;
import com.clesun.enums.PaymentClientEnum;
import com.clesun.enums.PaymentMethodEnum;
import com.clesun.enums.ResultCode;
import com.clesun.exception.ServiceException;
import com.clesun.kit.dto.PayParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 收银台工具
 *
 * @author liuk
 */
@Component
@Slf4j
public class CashierSupport {

    /**
     * 支付
     * @param paymentMethodEnum 支付方式
     * @param paymentClientEnum 调起方式
     * @param request
     * @param payParam 支付参数
     * @return
     */
    public ResultEntity payment(PaymentMethodEnum paymentMethodEnum, PaymentClientEnum paymentClientEnum,
                                HttpServletRequest request, PayParam payParam) {
        if (paymentClientEnum == null || paymentMethodEnum == null) {
            throw new ServiceException(ResultCode.PAY_NOT_SUPPORT);
        }
        //获取支付插件
        Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());
        log.info("支付请求：客户端：{},支付类型：{},请求：{}", paymentClientEnum.name(), paymentMethodEnum.name(), payParam.toString());

        //支付方式调用
        switch (paymentClientEnum) {
            case H5:
            case JSAPI:
                return null;
            case MP:
                return payment.mpPay(request, payParam);
            case APP:
                return payment.appPay(request, payParam);
            case NATIVE:
                return payment.nativePay(request, payParam);
            default:
                return null;
        }
    }

    /**
     * 支付通知
     * @param paymentMethodEnum 支付方式
     * @param request
     * @param uniqueCode 项目唯一标识
     */
    public void notify(PaymentMethodEnum paymentMethodEnum,
                       HttpServletRequest request,
                       String uniqueCode) {

        log.info("支付异步通知：支付类型：{}", paymentMethodEnum.name());

        //获取支付插件
        Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());

        PayParam payParam = new PayParam();
        payParam.setUniqueCode(uniqueCode);
        payParam.setPaymentMethodEnum(paymentMethodEnum);
        payment.payNotify(request, payParam);
    }
}
