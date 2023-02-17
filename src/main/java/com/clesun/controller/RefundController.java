package com.clesun.controller;

import com.clesun.dto.ResultEntity;
import com.clesun.enums.PaymentMethodEnum;
import com.clesun.exception.ServiceException;
import com.clesun.kit.dto.RefundParam;
import com.clesun.kit.plugin.RefundSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 退款接口
 */
@Slf4j
@RestController
@Api(tags = "退款接口")
@RequestMapping("/payment/cashierRefund")
public class RefundController {


    @Autowired
    private RefundSupport refundSupport;

    @GetMapping(value = "/refund")
    @ApiOperation(value = "退款")
    public ResultEntity refund(@Validated RefundParam refundParam) {
        try {
            return refundSupport.refund(refundParam.getPaymentMethodEnum(), refundParam);
        } catch (ServiceException se) {
            log.info("退款异常", se);
            throw se;
        } catch (Exception e) {
            log.error("退款异常", e);
        }
        return ResultEntity.failed("退款异常");
    }

    @ApiOperation(value = "退款异步通知")
    @RequestMapping(value = "/notify/{paymentMethod}/{uniqueCode}/{sn}", method = {RequestMethod.GET, RequestMethod.POST})
    public void notify(HttpServletRequest request, @PathVariable String paymentMethod, @PathVariable String uniqueCode, @PathVariable String sn) {
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.valueOf(paymentMethod);
        refundSupport.notify(paymentMethodEnum, request, uniqueCode, sn);
    }


}
