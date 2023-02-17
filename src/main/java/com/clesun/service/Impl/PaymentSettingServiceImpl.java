package com.clesun.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clesun.entity.PaymentSetting;
import com.clesun.mapper.PaymentSettingMapper;
import com.clesun.service.IPaymentSettingService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付设置 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2023-02-02
 */
@Service
public class PaymentSettingServiceImpl extends ServiceImpl<PaymentSettingMapper, PaymentSetting> implements IPaymentSettingService {

}
