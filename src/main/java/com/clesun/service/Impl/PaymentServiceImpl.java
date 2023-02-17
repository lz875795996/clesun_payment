package com.clesun.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clesun.entity.PaymentProject;
import com.clesun.kit.dto.PaymentSuccessParams;
import com.clesun.kit.dto.RefundSuccessParams;
import com.clesun.service.IPaymentProjectService;
import com.clesun.service.PaymentService;
import com.clesun.utils.HttpUtils;
import com.clesun.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付日志 业务实现
 *
 * @author liuk
 */
@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private IPaymentProjectService paymentProjectService;

    @Override
    public void paySuccess(PaymentSuccessParams paymentSuccessParams) {
        log.info("支付成功，第三方流水：{}", paymentSuccessParams.getReceivableNo());
        //支付结果处理
        PaymentProject project = paymentProjectService.getOne(new LambdaQueryWrapper<PaymentProject>()
                .eq(PaymentProject::getUniqueCode, paymentSuccessParams.getPayParam().getUniqueCode()));

        if(StringUtils.isNull(project.getPayNotifyUrl())){
            log.info("{},项目未配置支付业务回调接口", project.getUniqueCode());
            return;
        }
        //处理请求方支付回调接口
        String resultStr = HttpUtils.sendPost(project.getPayNotifyUrl(), JSON.toJSONString(paymentSuccessParams));
        if(!"".equals(resultStr)){
            Map<String, Object> map =  JSON.parseObject(resultStr, HashMap.class);
            if(0 == Integer.parseInt(String.valueOf(map.get("code")))){
                log.info("支付业务接口回调成功：{}", resultStr);
            }
        }
    }

    @Override
    public void refundSuccess(RefundSuccessParams refundSuccessParams) {
        log.info("支付第三方流水：{}", refundSuccessParams.getRefundParam().getReceivableNo());
        //退款结果处理
        PaymentProject project = paymentProjectService.getOne(new LambdaQueryWrapper<PaymentProject>()
                .eq(PaymentProject::getUniqueCode, refundSuccessParams.getRefundParam().getUniqueCode()));
        if(StringUtils.isNull(project.getRefundNotifyUrl())){
            log.info("{},项目未配置退款业务回调接口", project.getUniqueCode());
            return;
        }
        //处理请求方支付回调接口
        String resultStr = HttpUtils.sendPost(project.getRefundNotifyUrl(), JSON.toJSONString(refundSuccessParams));
        if(!"".equals(resultStr)){
            Map<String, Object> map =  JSON.parseObject(resultStr, HashMap.class);
            if(0 == Integer.parseInt(String.valueOf(map.get("code")))){
                log.info("退款业务接口回调成功：{}", resultStr);
            }
        }
    }
}