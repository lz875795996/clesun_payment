package com.clesun.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 统一下单-订单金额
 * </p>
 *
 * @author liuk
 * @since 2023-02-02
 */
@Data
@Accessors(chain = true)
public class Amount {

    /**
     * 总金额
     */
    private Integer total;

    /**
     * 货币类型
     */
    private String currency = "CNY";

    /**
     * 退款金额
     */
    private Integer refund;
}
