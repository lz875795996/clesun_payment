package com.clesun.kit.dto;

import com.clesun.enums.PaymentClientEnum;
import com.clesun.enums.PaymentMethodEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;


/**
 * <p>
 * 退款参数
 * </p>
 *
 * @author liuk
 * @since 2023-02-02
 */
@Data
@ToString
public class RefundParam {

    @ApiModelProperty(value = "项目唯一标识")
    private String uniqueCode;

    @ApiModelProperty(value = "商户订单号")
    private String sn;

    @ApiModelProperty(value = "支付第三方付款流水")
    private String receivableNo;

    @ApiModelProperty(value = "订单总金额")
    private String total;

    @ApiModelProperty(value = "退款金额")
    private String refund;

    @ApiModelProperty(value = "退款理由")
    private String reason;

    @ApiModelProperty(value = "支付方式")
    private PaymentMethodEnum paymentMethodEnum;

    @ApiModelProperty(value = "调起方式")
    private PaymentClientEnum paymentClientEnum;

}
