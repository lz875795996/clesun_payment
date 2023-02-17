package com.clesun.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clesun.entity.PaymentRefundLog;
import com.clesun.mapper.PaymentRefundLogMapper;
import com.clesun.service.IPaymentRefundLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 退款日志 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2023-02-02
 */
@Service
public class PaymentRefundLogServiceImpl extends ServiceImpl<PaymentRefundLogMapper, PaymentRefundLog> implements IPaymentRefundLogService {

}
