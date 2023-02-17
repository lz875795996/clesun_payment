package com.clesun.controller;

import com.clesun.dto.ResultEntity;
import com.clesun.enums.PaymentMethodEnum;
import com.clesun.exception.ServiceException;
import com.clesun.kit.CashierSupport;
import com.clesun.kit.dto.PayParam;
import com.clesun.kit.dto.RefundParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 收银台接口
 */
@Slf4j
@RestController
@Api(tags = "收银台接口")
@RequestMapping("/payment/cashier")
public class CashierController {

    @Autowired
    private CashierSupport cashierSupport;

    @GetMapping(value = "/pay")
    @ApiOperation(value = "支付")
    public ResultEntity payment(HttpServletRequest request, @Validated PayParam payParam) {
        try {
            return cashierSupport.payment(payParam.getPaymentMethodEnum(), payParam.getPaymentClientEnum(), request, payParam);
        } catch (ServiceException se) {
            log.info("支付异常", se);
            throw se;
        } catch (Exception e) {
            log.error("收银台支付错误", e);
        }
        return null;
    }

    @ApiOperation(value = "支付异步通知")
    @RequestMapping(value = "/notify/{paymentMethod}/{uniqueCode}", method = {RequestMethod.GET, RequestMethod.POST})
    public void notify(HttpServletRequest request, @PathVariable String paymentMethod, @PathVariable String uniqueCode) {
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.valueOf(paymentMethod);
        cashierSupport.notify(paymentMethodEnum, request, uniqueCode);
    }
}
