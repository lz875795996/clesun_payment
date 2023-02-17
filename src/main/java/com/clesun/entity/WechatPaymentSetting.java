package com.clesun.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微信支付设置
 * </p>
 *
 * @author liuk
 * @since 2023-02-02
 */
@Data
@Accessors(chain = true)
public class WechatPaymentSetting {

    /**
     * APP应用id
     */
    private String appId;
    /**
     * 小程序应用id
     */
    private String mpAppId;
    /**
     * 服务号应用id
     */
    private String serviceAppId;
    /**
     * 商户号
     */
    private String mchId;
    /**
     * 私钥
     */
    private String apiclient_key;
    /**
     * pem 证书
     */
    private String apiclient_cert_pem;
    /**
     * p12 证书
     */
    private String apiclient_cert_p12;
    /**
     * 商户证书序列号
     */
    private String serialNumber;
    /**
     * apiv3私钥
     */
    private String apiKey3;
}
