package com.clesun.enums;

/**
 * 返回状态码
 * 第一位 1:商品；2:用户；3:交易,4:促销,5:店铺,6:页面,7:设置,8:其他
 *
 * @author liuk
 */
public enum ResultCode {

    /**
     * 成功状态码
     */
    SUCCESS(200, "成功"),

    /**
     * 失败返回码
     */
    ERROR(400, "服务器繁忙，请稍后重试"),

    /**
     * 参数异常
     */
    PARAMS_ERROR(4002, "参数异常"),

    /**
     * 微信相关异常
     */
    WECHAT_CONNECT_NOT_SETTING(80300, "微信联合登陆信息未配置"),
    WECHAT_PAYMENT_NOT_SETTING(80301, "微信支付信息未配置"),
    WECHAT_QRCODE_ERROR(80302, "微信二维码生成异常"),
    WECHAT_MP_MESSAGE_ERROR(80303, "微信小程序小消息订阅异常"),
    WECHAT_JSAPI_SIGN_ERROR(80304, "微信JsApi签名异常"),
    WECHAT_CERT_ERROR(80305, "证书获取失败"),
    WECHAT_MP_MESSAGE_TMPL_ERROR(80306, "未能获取到微信模版消息id"),
    WECHAT_ERROR(80307, "微信接口异常"),
    APP_VERSION_EXIST(80307, "APP版本已存在"),
    WECHAT_MP_UNION_ID_NULL_ERROR(80308, "联合登录用户服务标识为空"),


    /**
     * 支付
     */
    PAY_UN_WANTED(32000, "当前订单不需要付款，返回订单列表等待系统订单出库即可"),
    PAY_SUCCESS(32001, "支付成功"),
    PAY_INCONSISTENT_ERROR(32002, "付款金额和应付金额不一致"),
    PAY_DOUBLE_ERROR(32003, "订单已支付，不能再次进行支付"),
    PAY_CASHIER_ERROR(32004, "收银台信息获取错误"),
    PAY_ERROR(32005, "支付业务异常，请稍后重试"),
    PAY_BAN(32006, "当前订单不需要付款，请返回订单列表重新操作"),
    PAY_PARTIAL_ERROR(32007, "该订单已部分支付，请前往订单中心进行支付"),
    PAY_NOT_SUPPORT(32008, "支付暂不支持"),
    PAY_CLIENT_TYPE_ERROR(32009, "错误的客户端"),

    /**
     * 调起方项目
     */
    PROJECT_UN_MACHING_ERROR(31000, "项目唯一标识匹配失败，请检查")


    ;

    private final Integer code;
    private final String message;


    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

}
