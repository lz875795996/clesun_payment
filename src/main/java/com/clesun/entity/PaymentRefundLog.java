package com.clesun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 退款日志
 *
 * @author liuk
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="RefundLog对象", description="退款日志")

public class PaymentRefundLog extends MybatisPage {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "订单编号")
    private String sn;

    @ApiModelProperty(value = "订单总金额")
    private String totalAmount;

    @ApiModelProperty(value = "退款金额")
    private String refundAmount;

    @ApiModelProperty(value = "是否已退款")
    private Boolean isRefund;

    @ApiModelProperty(value = "退款方式")
    private String paymentName;

    @ApiModelProperty(value = "支付第三方付款流水")
    private String paymentReceivableNo;

    @ApiModelProperty(value = "退款请求流水")
    private String outOrderNo;

    @ApiModelProperty(value = "第三方退款流水号")
    private String receivableNo;

    @ApiModelProperty(value = "退款理由")
    private String refundReason;

    @ApiModelProperty(value = "退款失败原因")
    private String errorMessage;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否删除（0未删除，1已删除）")
    private String isDelete;
}