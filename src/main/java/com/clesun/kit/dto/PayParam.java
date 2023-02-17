package com.clesun.kit.dto;

import com.clesun.enums.PaymentClientEnum;
import com.clesun.enums.PaymentMethodEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;


/**
 * <p>
 * 支付参数
 * </p>
 *
 * @author liuk
 * @since 2023-02-02
 */
@Data
@ToString
public class PayParam{

    @ApiModelProperty(value = "商户订单号")
    private String sn;

    @ApiModelProperty(value = "项目唯一标识")
    private String uniqueCode;

    @ApiModelProperty(value = "支付金额")
    private String amount;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "支付方式")
    private PaymentMethodEnum paymentMethodEnum;

    @ApiModelProperty(value = "调起方式")
    private PaymentClientEnum paymentClientEnum;

    @ApiModelProperty("联合登录id，微信小程序支付专用")
    private String unionId;

}
