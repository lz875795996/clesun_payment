package com.clesun.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clesun.entity.PaymentProject;
import com.clesun.mapper.PaymentProjectMapper;
import com.clesun.service.IPaymentProjectService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目管理 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2023-02-02
 */
@Service
public class PaymentProjectServiceImpl extends ServiceImpl<PaymentProjectMapper, PaymentProject> implements IPaymentProjectService {

}
