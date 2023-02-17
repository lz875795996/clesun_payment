package com.clesun.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clesun.entity.PaymentPayLog;
import com.clesun.mapper.PaymentPayLogMapper;
import com.clesun.service.IPaymentPayLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付记录日志 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2023-02-02
 */
@Service
public class PaymentPayLogServiceImpl extends ServiceImpl<PaymentPayLogMapper, PaymentPayLog> implements IPaymentPayLogService {

}
