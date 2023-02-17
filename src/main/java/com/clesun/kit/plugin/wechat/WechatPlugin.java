package com.clesun.kit.plugin.wechat;

import cn.hutool.core.net.URLEncoder;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clesun.dto.ResultEntity;
import com.clesun.entity.*;
import com.clesun.enums.ResultCode;
import com.clesun.exception.ServiceException;
import com.clesun.kit.Payment;
import com.clesun.kit.core.PaymentHttpResponse;
import com.clesun.kit.core.enums.RequestMethodEnums;
import com.clesun.kit.core.enums.SignType;
import com.clesun.kit.core.kit.AesUtil;
import com.clesun.kit.core.kit.HttpKit;
import com.clesun.kit.core.kit.PayKit;
import com.clesun.kit.core.kit.WxPayKit;
import com.clesun.kit.dto.*;
import com.clesun.kit.plugin.wechat.enums.WechatApiEnum;
import com.clesun.kit.plugin.wechat.enums.WechatDomain;
import com.clesun.kit.plugin.wechat.model.Payer;
import com.clesun.kit.plugin.wechat.model.RefundModel;
import com.clesun.service.*;
import com.clesun.utils.CurrencyUtil;
import com.clesun.utils.DateTimeZoneUtil;
import com.clesun.utils.ExpiryMap;
import com.clesun.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;

/**
 * 微信支付
 *
 * @author liuk
 */
@Slf4j
@Component
public class WechatPlugin implements Payment {

    /**
     * 支付参数对象
     */
    private static ExpiryMap<String, PayParam> payParamMap = new ExpiryMap<>();

    /**
     * 配置参数对象
     */
    private static ExpiryMap<String, Object> settingParamMap = new ExpiryMap<>();

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private IPaymentSettingService paymentSettingService;
    @Autowired
    private IPaymentProjectService paymentProjectService;
    @Autowired
    private IPaymentPayLogService paymentPayLogService;
    @Autowired
    private IPaymentRefundLogService paymentRefundLogService;

    @Value("${clesun.payment.domain}")
    private String domain;

    @Override
    public ResultEntity mpPay(HttpServletRequest request, PayParam payParam) {
        try {
            if (payParam.getUnionId() == null) {
                throw new ServiceException(ResultCode.WECHAT_MP_UNION_ID_NULL_ERROR);
            }

            Payer payer = new Payer();
            payer.setOpenid(payParam.getUnionId());

            //支付金额
            Integer fen = CurrencyUtil.fen(Double.parseDouble(payParam.getAmount()));
            //第三方付款订单
            String outOrderNo = payParam.getSn();
            //过期时间
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);

            //微信小程序，appid 需要单独获取，这里读取了联合登陆配置的appid ，实际场景小程序自动登录，所以这个appid是最为保险的做法
            //如果有2开需求，这里需要调整，修改这个appid的获取途径即可
            SettingParam settingParam = getSettingParam(payParam.getPaymentMethodEnum().name(), payParam.getUniqueCode());
            String appid = wechatPaymentSetting(settingParam).getMpAppId();
            if (StringUtils.isNull(appid)) {
                throw new ServiceException(ResultCode.WECHAT_PAYMENT_NOT_SETTING);
            }
            String attach = URLEncoder.createDefault().encode(JSONUtil.toJsonStr(payParam), StandardCharsets.UTF_8);

            WechatPaymentSetting setting = wechatPaymentSetting(settingParam);
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(appid)
                    .setMchid(setting.getMchId())
                    .setDescription(payParam.getDescription())
                    .setOut_trade_no(outOrderNo)
                    .setTime_expire(timeExpire)
                    .setAttach(attach)
                    .setNotify_url(domain + "/payment/cashier/notify/"
                            + payParam.getPaymentMethodEnum().name() + "/" + payParam.getUniqueCode())
                    .setAmount(new Amount().setTotal(fen))
                    .setPayer(payer);

            log.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            PaymentHttpResponse response = WechatApi.v3(
                    RequestMethodEnums.POST,
                    WechatDomain.CHINA.toString(),
                    WechatApiEnum.JS_API_PAY.toString(),
                    setting.getMchId(),
                    setting.getSerialNumber(),
                    null,
                    setting.getApiclient_key(),
                    JSONUtil.toJsonStr(unifiedOrderModel)
            );
            //根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, getPlatformCert(settingParam));
            log.info("verifySignature: {}", verifySignature);
            log.info("统一下单响应 {}", response);

            if (verifySignature) {
                String body = response.getBody();
                JSONObject jsonObject = JSONUtil.parseObj(body);
                String prepayId = jsonObject.getStr("prepay_id");
                Map<String, String> map = WxPayKit.jsApiCreateSign(appid, prepayId, setting.getApiclient_key());
                log.info("唤起支付参数:{}", map);

                return ResultEntity.success("响应成功", map);
            }
            log.error("微信支付参数验证错误，请及时处理");
            throw new ServiceException(ResultCode.PAY_ERROR);
        } catch (Exception e) {
            log.error("支付异常", e);
            throw new ServiceException(ResultCode.PAY_ERROR);
        }
    }

    @Override
    public ResultEntity appPay(HttpServletRequest request, PayParam payParam) {
        try {
            //支付金额
            Integer fen = CurrencyUtil.fen(Double.parseDouble(payParam.getAmount()));
            //第三方付款订单
            String outOrderNo = payParam.getSn();
            //过期时间
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
            //本地缓存回传参数（官方最大限制128，有超出风险，在本地缓存，失效时间30分钟）
            String attachKey = "appPay_" + StringUtils.getUUID();
            payParamMap.put(attachKey, payParam, 1000 * 60 * 30);

            SettingParam settingParam = getSettingParam(payParam.getPaymentMethodEnum().name(), payParam.getUniqueCode());
            WechatPaymentSetting setting = wechatPaymentSetting(settingParam);
            String appid = setting.getAppId();
            if (appid == null) {
                throw new ServiceException(ResultCode.WECHAT_PAYMENT_NOT_SETTING);
            }
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(appid)
                    .setMchid(setting.getMchId())
                    .setDescription(payParam.getDescription())
                    .setOut_trade_no(outOrderNo)
                    .setTime_expire(timeExpire)
                    .setAttach(attachKey)
                    .setNotify_url(domain + "/payment/cashier/notify/"
                            + payParam.getPaymentMethodEnum().name() + "/" + payParam.getUniqueCode())
                    .setAmount(new Amount().setTotal(fen));


            log.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            PaymentHttpResponse response = WechatApi.v3(
                    RequestMethodEnums.POST,
                    WechatDomain.CHINA.toString(),
                    WechatApiEnum.APP_PAY.toString(),
                    setting.getMchId(),
                    setting.getSerialNumber(),
                    null,
                    setting.getApiclient_key(),
                    JSONUtil.toJsonStr(unifiedOrderModel)
            );
            //根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, getPlatformCert(settingParam));
            log.info("verifySignature: {}", verifySignature);
            log.info("统一下单响应 {}", response);

            if (verifySignature) {
                JSONObject jsonObject = JSONUtil.parseObj(response.getBody());
                String prepayId = jsonObject.getStr("prepay_id");
                Map<String, String> map = WxPayKit.appPrepayIdCreateSign(appid,
                        setting.getMchId(),
                        prepayId,
                        setting.getApiclient_key(), SignType.HMACSHA256);
                log.info("唤起支付参数:{}", map);

                return ResultEntity.success("响应成功", map);
            }
            log.error("微信支付参数验证错误，请及时处理");
            throw new ServiceException(ResultCode.PAY_ERROR);
        } catch (Exception e) {
            log.error("支付异常", e);
            throw new ServiceException(ResultCode.PAY_ERROR);
        }

//        return null;
    }

    @Override
    public ResultEntity nativePay(HttpServletRequest request, PayParam payParam) {
        try {
            //支付金额
            Integer fen = CurrencyUtil.fen(Double.parseDouble(payParam.getAmount()));
            //第三方付款订单
            String outOrderNo = payParam.getSn();
            //过期时间
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
            //本地缓存回传参数（官方最大限制128，有超出风险，在本地缓存，失效时间30分钟）
            String attachKey = "nativePay_" + StringUtils.getUUID();
            payParamMap.put(attachKey, payParam, 1000 * 60 * 30);

            SettingParam settingParam = getSettingParam(payParam.getPaymentMethodEnum().name(), payParam.getUniqueCode());
            WechatPaymentSetting setting = wechatPaymentSetting(settingParam);
            String appid = setting.getServiceAppId();
            if (appid == null) {
                throw new ServiceException(ResultCode.WECHAT_PAYMENT_NOT_SETTING);
            }

            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(appid)
                    .setMchid(setting.getMchId())
                    .setDescription(payParam.getDescription())
                    .setOut_trade_no(outOrderNo)
                    .setTime_expire(timeExpire)
                    .setAttach(attachKey)
                    .setNotify_url(domain + "/payment/cashier/notify/"
                            + payParam.getPaymentMethodEnum().name() + "/" + payParam.getUniqueCode())
                    .setAmount(new Amount().setTotal(fen));

            log.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            PaymentHttpResponse response = WechatApi.v3(
                    RequestMethodEnums.POST,
                    WechatDomain.CHINA.toString(),
                    WechatApiEnum.NATIVE_PAY.toString(),
                    setting.getMchId(),
                    setting.getSerialNumber(),
                    null,
                    setting.getApiclient_key(),
                    JSONUtil.toJsonStr(unifiedOrderModel)
            );
            log.info("统一下单响应 {}", response);
            //根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, getPlatformCert(settingParam));
            log.info("verifySignature: {}", verifySignature);

            if (verifySignature) {
                return ResultEntity.success("响应成功", new JSONObject(response.getBody()).getStr("code_url"));
            } else {
                log.error("微信支付参数验证错误，请及时处理");
                throw new ServiceException(ResultCode.PAY_ERROR);
            }
        } catch (ServiceException e) {
            log.error("支付异常", e);
            throw new ServiceException(ResultCode.PAY_ERROR);
        } catch (Exception e) {
            log.error("支付异常", e);
            throw new ServiceException(ResultCode.PAY_ERROR);
        }
    }

    @Override
    public void payNotify(HttpServletRequest request,PayParam payParam) {
        try {
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String serialNo = request.getHeader("Wechatpay-Serial");
            String signature = request.getHeader("Wechatpay-Signature");

            log.info("timestamp:{} nonce:{} serialNo:{} signature:{}", timestamp, nonce, serialNo, signature);
            String result = HttpKit.readData(request);
            log.info("微信支付通知密文 {}", result);

            SettingParam settingParam = getSettingParam(payParam.getPaymentMethodEnum().name(), payParam.getUniqueCode());
            PaymentProject project = wechatPaymentProject(settingParam);
            WechatPaymentSetting setting = wechatPaymentSetting(settingParam);
            //校验服务器端响应
            String plainText = WxPayKit.verifyNotify(serialNo, result, signature, nonce, timestamp,
                    setting.getApiKey3(), Objects.requireNonNull(getPlatformCert(settingParam)));

            log.info("微信支付通知明文 {}", plainText);
            JSONObject jsonObject = JSONUtil.parseObj(plainText);

            String attachKey = "nativePay_" + jsonObject.getStr("attach");
            payParam = payParamMap.get(attachKey);
            log.info("attachKey：{}， 主体内容：{}", attachKey, JSON.toJSONString(payParam));
            payParamMap.remove(attachKey);

            String tradeNo = jsonObject.getStr("transaction_id");
            String outTradeNo = jsonObject.getStr("out_trade_no");
            Double totalAmount = CurrencyUtil.reversalFen(jsonObject.getJSONObject("amount").getDouble("total"));

            //存支付日志
            PaymentPayLog paymentPaydLog = PaymentPayLog.builder()
                    .projectId(project.getId())
                    .sn(outTradeNo)
                    .is_pay(true)
                    .payAmount(String.valueOf(totalAmount))
                    .paymentMethod(payParam.getPaymentMethodEnum().name())
                    .paymentClient(payParam.getPaymentClientEnum().name())
                    .paymentReceivableNo(tradeNo)
                    .build();
            paymentPayLogService.save(paymentPaydLog);

            //项目调用方 发起支付成功通知
            PaymentSuccessParams paymentSuccessParams = new PaymentSuccessParams(tradeNo,  payParam);
            paymentService.paySuccess(paymentSuccessParams);

            log.info("微信支付回调：支付成功{}", plainText);

        } catch (Exception e) {
            log.error("支付异常", e);
        }
    }

    @Override
    public ResultEntity refund(PaymentRefundLog paymentRefundLog, RefundParam refundParam) {
        try {
            Amount amount = new Amount().setRefund(CurrencyUtil.fen(Double.parseDouble(refundParam.getRefund())))
                    .setTotal(CurrencyUtil.fen(Double.parseDouble(refundParam.getTotal())));

            //退款参数准备
            RefundModel refundModel = new RefundModel()
                    .setTransaction_id(paymentRefundLog.getPaymentReceivableNo())
                    .setOut_refund_no(paymentRefundLog.getOutOrderNo())
                    .setReason(paymentRefundLog.getRefundReason())
                    .setAmount(amount)
                    .setNotify_url(domain + "/payment/cashierRefund/notify/" +
                            refundParam.getPaymentMethodEnum().name() + "/" + refundParam.getUniqueCode() + "/" + refundParam.getSn());

            SettingParam settingParam = getSettingParam(refundParam.getPaymentMethodEnum().name(), refundParam.getUniqueCode());
            PaymentProject project = wechatPaymentProject(settingParam);
            WechatPaymentSetting setting = wechatPaymentSetting(settingParam);

            log.info("微信退款参数 {}", JSONUtil.toJsonStr(refundModel));
            PaymentHttpResponse response = WechatApi.v3(
                    RequestMethodEnums.POST,
                    WechatDomain.CHINA.toString(),
                    WechatApiEnum.DOMESTIC_REFUNDS.toString(),
                    setting.getMchId(),
                    setting.getSerialNumber(),
                    null,
                    setting.getApiclient_key(),
                    JSONUtil.toJsonStr(refundModel)
            );
            log.info("微信退款响应 {}", response);
            paymentRefundLog.setProjectId(project.getId());

            //退款申请成功
            if (response.getStatus() == 200) {
                paymentRefundLogService.save(paymentRefundLog);
                return ResultEntity.success("退款申请成功");
            } else {
                //退款申请失败
                paymentRefundLog.setErrorMessage(response.getBody());
                paymentRefundLogService.save(paymentRefundLog);
                return ResultEntity.failed("退款申请失败，原因：" + response.getBody());
            }
        } catch (Exception e) {
            log.error("微信退款申请失败", e);
        }
        return ResultEntity.failed("退款申请异常");
    }

    @Override
    public void refundNotify(HttpServletRequest request, RefundParam refundParam) {
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String serialNo = request.getHeader("Wechatpay-Serial");
        String signature = request.getHeader("Wechatpay-Signature");

        log.info("timestamp:{} nonce:{} serialNo:{} signature:{}", timestamp, nonce, serialNo, signature);
        String result = HttpKit.readData(request);
        log.info("微信退款通知密文 {}", result);
        JSONObject ciphertext = JSONUtil.parseObj(result);

        try { //校验服务器端响应¬
            SettingParam settingParam = getSettingParam(refundParam.getPaymentMethodEnum().name(), refundParam.getUniqueCode());
            String plainText = WxPayKit.verifyNotify(serialNo, result, signature, nonce, timestamp,
                    wechatPaymentSetting(settingParam).getApiKey3(), Objects.requireNonNull(getPlatformCert(settingParam)));
            log.info("微信退款通知明文 {}", plainText);

            JSONObject jsonObject = JSONUtil.parseObj(plainText);
            String transactionId = jsonObject.getStr("transaction_id");
            PaymentRefundLog paymentRefundLog = null;
            if (("REFUND.SUCCESS").equals(ciphertext.getStr("event_type"))) {
                log.info("退款成功 {}", plainText);
                //校验服务器端响应
                String refundId = jsonObject.getStr("refund_id");
                paymentRefundLog = paymentRefundLogService.getOne(new LambdaQueryWrapper<PaymentRefundLog>()
                        .eq(PaymentRefundLog::getPaymentReceivableNo, transactionId)
                        .eq(PaymentRefundLog::getSn, refundParam.getSn()));
                if (paymentRefundLog != null) {
                    paymentRefundLog.setIsRefund(true);
                    paymentRefundLog.setReceivableNo(refundId);
                    paymentRefundLogService.saveOrUpdate(paymentRefundLog);
                }
            } else {
                log.info("退款失败 {}", plainText);
                String refundId = jsonObject.getStr("refund_id");

                paymentRefundLog = paymentRefundLogService.getOne(new LambdaQueryWrapper<PaymentRefundLog>()
                        .eq(PaymentRefundLog::getPaymentReceivableNo, transactionId)
                        .eq(PaymentRefundLog::getSn, refundParam.getSn()));
                if (paymentRefundLog != null) {
                    paymentRefundLog.setReceivableNo(refundId);
                    paymentRefundLog.setErrorMessage(ciphertext.getStr("summary"));
                    paymentRefundLogService.saveOrUpdate(paymentRefundLog);
                }
            }

            //项目调用方 发起退款通知
            RefundSuccessParams refundSuccessParams = new RefundSuccessParams(paymentRefundLog.getIsRefund(),
                    paymentRefundLog.getErrorMessage(), refundParam);
            paymentService.refundSuccess(refundSuccessParams);

            log.info("微信退款回调：{}", plainText);
        } catch (Exception e) {
            log.error("微信退款失败", e);
        }
    }

    /**
     * 获取项目配置
     *
     * @return
     */
    private PaymentProject wechatPaymentProject(SettingParam settingParam) {
        try {
            //自定义key
            String key = "projectKey_" + settingParam.getType()+ "_" + settingParam.getUniqueCode();

            PaymentProject project = null;
            if(!CollectionUtils.isEmpty(settingParamMap) && !"null".equals(String.valueOf(settingParamMap.get(key)))){
                project = (PaymentProject)settingParamMap.get(key);
            }else{
                project = paymentProjectService.getOne(new LambdaQueryWrapper<PaymentProject>()
                        .eq(PaymentProject::getUniqueCode, settingParam.getUniqueCode()));
                if(null == project){
                    log.error("项目唯一标识匹配失败", settingParam.getUniqueCode());
                    throw new ServiceException(ResultCode.PROJECT_UN_MACHING_ERROR);
                }

                //设置有效期 3分钟，避免重复请求数据库
                settingParamMap.put(key, project, 60 * 3 * 1000);
            }

            return project;
        } catch (Exception e) {
            log.error("微信支付暂不支持", e);
            throw new ServiceException(ResultCode.PAY_NOT_SUPPORT);
        }
    }
    /**
     * 获取微信支付配置
     *
     * @return
     */
    private WechatPaymentSetting wechatPaymentSetting(SettingParam settingParam) {
        try {
            //自定义key
            String key = "setting_" + settingParam.getType()+ "_" + settingParam.getUniqueCode();
            WechatPaymentSetting wechatPaymentSetting = null;
            if(!CollectionUtils.isEmpty(settingParamMap) && !"null".equals(String.valueOf(settingParamMap.get(key)))){
                wechatPaymentSetting = (WechatPaymentSetting)settingParamMap.get(key);
            }else{
                PaymentProject project = wechatPaymentProject(settingParam);
                PaymentSetting setting =  paymentSettingService.getOne(new LambdaQueryWrapper<PaymentSetting>()
                        .eq(PaymentSetting::getProjectId, project.getId())
                        .eq(PaymentSetting::getType, settingParam.getType()));
                wechatPaymentSetting = JSONUtil.toBean(setting.getSettingValue(), WechatPaymentSetting.class);
                //设置有效期 3分钟，避免重复请求数据库
                settingParamMap.put(key, wechatPaymentSetting, 60 * 3 * 1000);
            }

            return wechatPaymentSetting;
        } catch (Exception e) {
            log.error("微信支付暂不支持", e);
            throw new ServiceException(ResultCode.PAY_NOT_SUPPORT);
        }
    }

    /**
     * 配置
     * @param type
     * @param uniqueCode
     * @return
     */
    private SettingParam getSettingParam(String type, String uniqueCode){
        SettingParam settingParam = SettingParam.builder()
                .type(type)
                .uniqueCode(uniqueCode)
                .build();
        return settingParam;
    }


    /**
     * 获取平台公钥
     *
     * @return 平台公钥
     */
    private X509Certificate getPlatformCert(SettingParam settingParam) {
        //平台公钥
        String publicCert = "";
        //获取平台证书列表
        try {
            WechatPaymentSetting setting = wechatPaymentSetting(settingParam);
            PaymentHttpResponse response = WechatApi.v3(
                    RequestMethodEnums.GET,
                    WechatDomain.CHINA.toString(),
                    WechatApiEnum.GET_CERTIFICATES.toString(),
                    setting.getMchId(),
                    setting.getSerialNumber(),
                    null,
                    setting.getApiclient_key(),
                    ""
            );
            String body = response.getBody();
            log.info("获取微信平台证书body: {}", body);
            if (response.getStatus() == 200) {
                JSONObject jsonObject = JSONUtil.parseObj(body);
                JSONArray dataArray = jsonObject.getJSONArray("data");
                //默认认为只有一个平台证书
                JSONObject encryptObject = dataArray.getJSONObject(0);
                JSONObject encryptCertificate = encryptObject.getJSONObject("encrypt_certificate");
                String associatedData = encryptCertificate.getStr("associated_data");
                String cipherText = encryptCertificate.getStr("ciphertext");
                String nonce = encryptCertificate.getStr("nonce");
                publicCert = getPlatformCertStr(associatedData, nonce, cipherText, settingParam);
                long second = (PayKit.getCertificate(publicCert).getNotAfter().getTime() - System.currentTimeMillis()) / 1000;
            } else {
                log.error("证书获取失败：{}" + body);
                throw new ServiceException(ResultCode.WECHAT_CERT_ERROR);
            }
            return PayKit.getCertificate(publicCert);
        } catch (Exception e) {
            log.error("证书获取失败", e);
        }
        return null;
    }


    /**
     * 获取平台证书缓存的字符串
     * 下列各个密钥参数
     *
     * @param associatedData 密钥参数
     * @param nonce          密钥参数
     * @param cipherText     密钥参数
     * @return platform key
     * @throws GeneralSecurityException 密钥获取异常
     */
    private String getPlatformCertStr(String associatedData, String nonce, String cipherText, SettingParam settingParam) throws GeneralSecurityException {
        AesUtil aesUtil = new AesUtil(wechatPaymentSetting(settingParam).getApiKey3().getBytes(StandardCharsets.UTF_8));
        //平台证书密文解密
        //encrypt_certificate 中的  associated_data nonce  ciphertext
        return aesUtil.decryptToString(
                associatedData.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                cipherText
        );
    }
}
