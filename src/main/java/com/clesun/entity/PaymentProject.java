package com.clesun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 项目管理
 * </p>
 *
 * @author liuk
 * @since 2023-02-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="PaymentProject对象", description="项目管理")
public class PaymentProject extends MybatisPage {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目名称")
    private String name;

    @ApiModelProperty(value = "项目唯一标识")
    private String uniqueCode;

    @ApiModelProperty(value = "支付回调地址（域名+接口地址）")
    private String payNotifyUrl;

    @ApiModelProperty(value = "退款回调地址（域名+接口地址）")
    private String refundNotifyUrl;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否删除（0未删除，1已删除）")
    private String isDelete;


}
