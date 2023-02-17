package com.clesun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 支付记录日志
 * </p>
 *
 * @author liuk
 * @since 2023-02-02
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="PaymentPayLog对象", description="支付记录日志")
public class PaymentPayLog extends MybatisPage {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "订单编号")
    private String sn;

    @ApiModelProperty(value = "支付金额")
    private String payAmount;

    @ApiModelProperty(value = "支付方式")
    private String paymentMethod;

    @ApiModelProperty(value = "调起方式")
    private String paymentClient;

    @ApiModelProperty(value = "是否已支付")
    private Boolean is_pay;

    @ApiModelProperty(value = "支付第三方付款流水")
    private String paymentReceivableNo;

    @ApiModelProperty(value = "支付失败原因")
    private String errorMessage;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否删除（0未删除，1已删除）")
    private String isDelete;


}
