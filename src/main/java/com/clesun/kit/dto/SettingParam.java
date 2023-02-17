package com.clesun.kit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;


/**
 * <p>
 * 系统配置参数
 * </p>
 *
 * @author liuk
 * @since 2023-02-02
 */
@Data
@ToString
@Builder
public class SettingParam {

    @ApiModelProperty(value = "配置类型")
    private String type;

    @ApiModelProperty(value = "项目唯一标识")
    private String uniqueCode;

}
