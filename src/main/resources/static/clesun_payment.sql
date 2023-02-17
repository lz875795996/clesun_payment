/*
 Navicat Premium Data Transfer

 Source Server         : 农用通、商城
 Source Server Type    : MySQL
 Source Server Version : 50738
 Source Host           : 39.105.15.128:3376
 Source Schema         : clesun_payment

 Target Server Type    : MySQL
 Target Server Version : 50738
 File Encoding         : 65001

 Date: 17/02/2023 09:09:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for payment_pay_log
-- ----------------------------
DROP TABLE IF EXISTS `payment_pay_log`;
CREATE TABLE `payment_pay_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目id',
  `sn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '订单号',
  `pay_amount` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付金额',
  `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付方式',
  `payment_client` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '调起方式',
  `is_pay` bit(1) NULL DEFAULT NULL COMMENT '是否支付',
  `payment_receivable_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付第三方付款流水',
  `error_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付失败原因',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `is_delete` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '是否删除（0未删除，1已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '支付记录日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_pay_log
-- ----------------------------
INSERT INTO `payment_pay_log` VALUES (1, 1, '666777', '0.01', 'WECHAT', 'NATIVE', b'1', '4200001722202302099912781850', NULL, '2023-02-09 17:17:50', '2023-02-09 17:37:30', '0');
INSERT INTO `payment_pay_log` VALUES (2, 1, '321321', '0.01', 'WECHAT', 'NATIVE', b'1', '4200001747202302109157652159', NULL, '2023-02-10 14:52:57', '2023-02-10 14:52:57', '0');
INSERT INTO `payment_pay_log` VALUES (3, 1, '3213211', '0.01', 'WECHAT', 'NATIVE', b'1', '4200001760202302100488270504', NULL, '2023-02-10 15:06:52', '2023-02-10 15:06:52', '0');

-- ----------------------------
-- Table structure for payment_project
-- ----------------------------
DROP TABLE IF EXISTS `payment_project`;
CREATE TABLE `payment_project`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '项目名称',
  `unique_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '项目唯一标识',
  `pay_notify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付回调地址（域名+接口地址）',
  `refund_notify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '退款回调地址（域名+接口地址）',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `is_delete` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '是否删除（0未删除，1已删除）',
  PRIMARY KEY (`id`, `unique_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '项目管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_project
-- ----------------------------
INSERT INTO `payment_project` VALUES (1, '桓仁宣推', 'promotion', NULL, NULL, '2023-02-02 11:06:39', '2023-02-09 16:30:06', '0');

-- ----------------------------
-- Table structure for payment_refund_log
-- ----------------------------
DROP TABLE IF EXISTS `payment_refund_log`;
CREATE TABLE `payment_refund_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目id',
  `sn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '订单编号',
  `total_amount` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '订单总金额',
  `refund_amount` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '退款金额',
  `is_refund` bit(1) NULL DEFAULT NULL COMMENT '是否已退款',
  `payment_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '退款方式',
  `payment_receivable_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付第三方付款流水',
  `out_order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '退款请求流水',
  `receivable_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '第三方退款流水号',
  `refund_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '退款理由',
  `error_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '退款失败原因',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `is_delete` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '是否删除（0未删除，1已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '退款日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_refund_log
-- ----------------------------
INSERT INTO `payment_refund_log` VALUES (3, 1, '66669999', '0.01', NULL, b'0', 'WECHAT', '4200001749202302094703886636', 'AF66669999', NULL, '无库存', '{\"code\":\"INVALID_REQUEST\",\"message\":\"支付单号校验不一致，请核实后再试\"}', '2023-02-09 17:01:18', '2023-02-09 17:01:18', '0');
INSERT INTO `payment_refund_log` VALUES (4, 1, '666699991', '0.01', NULL, b'0', 'WECHAT', '4200001749202302094703886636', 'AF666699991', NULL, '无库存', NULL, '2023-02-09 17:02:05', '2023-02-09 17:02:05', '0');
INSERT INTO `payment_refund_log` VALUES (5, 1, '666699991', '0.01', NULL, b'0', 'WECHAT', '4200001749202302094703886636', 'AF666699991', NULL, '无库存', NULL, '2023-02-09 17:09:31', '2023-02-09 17:09:31', '0');
INSERT INTO `payment_refund_log` VALUES (6, 1, '666699991', '0.01', NULL, b'0', 'WECHAT', '4200001749202302094703886636', 'AF666699991', NULL, '无库存', NULL, '2023-02-09 17:11:03', '2023-02-09 17:11:03', '0');
INSERT INTO `payment_refund_log` VALUES (7, 1, '666777', '0.01', '0.01', b'1', 'WECHAT', '4200001722202302099912781850', 'AF666777', '50300104572023020930871317008', '无库存', NULL, '2023-02-09 17:20:42', '2023-02-09 17:20:42', '0');
INSERT INTO `payment_refund_log` VALUES (8, 1, '321321', '0.01', '0.01', b'0', 'WECHAT', '4200001747202302109157652159', 'AF321321', NULL, '拜了个拜', NULL, '2023-02-10 14:53:59', '2023-02-10 14:53:59', '0');
INSERT INTO `payment_refund_log` VALUES (9, 1, '321321', '0.01', '0.01', b'0', 'WECHAT', '4200001747202302109157652159', 'AF321321', NULL, '拜了个拜', NULL, '2023-02-10 14:59:33', '2023-02-10 14:59:33', '0');
INSERT INTO `payment_refund_log` VALUES (10, 1, '3213211', '0.01', '0.01', b'0', 'WECHAT', '4200001747202302109157652159', 'AF3213211', NULL, '拜了个拜', '{\"code\":\"INVALID_REQUEST\",\"message\":\"订单已全额退款\"}', '2023-02-10 14:59:55', '2023-02-10 14:59:55', '0');
INSERT INTO `payment_refund_log` VALUES (11, 1, '3213211', '0.01', '0.01', b'0', 'WECHAT', '42000017472023021091576521591', 'AF3213211', NULL, '拜了个拜', '{\"code\":\"PARAM_ERROR\",\"message\":\"订单号非法\"}', '2023-02-10 15:00:06', '2023-02-10 15:00:06', '0');
INSERT INTO `payment_refund_log` VALUES (12, 1, '3213211', '0.01', '0.01', b'1', 'WECHAT', '4200001760202302100488270504', 'AF3213211', '50301404582023021030909173013', '拜了个拜', NULL, '2023-02-10 15:07:17', '2023-02-10 15:07:17', '0');
INSERT INTO `payment_refund_log` VALUES (13, 1, '1625321008275685376', '0.01', '0.01', b'1', 'WECHAT', '4200001738202302143101827692', 'AF1625321008275685376', '50302304972023021431038976330', '无库存', NULL, '2023-02-14 11:02:13', '2023-02-14 11:02:13', '0');
INSERT INTO `payment_refund_log` VALUES (14, 1, '1625321656262098944', '0.01', '0.01', b'1', 'WECHAT', '4200001724202302147826940330', 'AF1625321656262098944', '50302004972023021431034814204', '无库存', NULL, '2023-02-14 11:03:21', '2023-02-14 11:03:21', '0');

-- ----------------------------
-- Table structure for payment_setting
-- ----------------------------
DROP TABLE IF EXISTS `payment_setting`;
CREATE TABLE `payment_setting`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目id',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '配置类型（WECHAT-微信、ALIPAY-支付宝）',
  `setting_value` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '配置值value',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `is_delete` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '是否删除（0未删除，1已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '支付设置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_setting
-- ----------------------------
INSERT INTO `payment_setting` VALUES (1, 1, 'WECHAT', '{\"mchId\":\"1626870395\",\"apiclient_key\":\"/data/program/crt/lili-wechatpay/apiclient_key.pem\",\"serialNumber\":\"3DE7B0B59D986718F6AE9F1AB789ADE21E2F8366\",\"apiKey3\":\"BeiJingClesunRjUvRj4UvRTQu5RTQuR\",\"serviceAppId\":\"ww00ef175f6f71bce2\",\"apiclient_cert_pem\":\"/data/program/crt/lili-wechatpay/apiclient_cert.pem\",\"apiclient_cert_p12\":\"/data/program/crt/lili-wechatpay/apiclient_cert.p12\",\"appId\":\"wxd97c1e1ebf47f976\"}', '2023-02-02 11:08:44', '2023-02-13 10:20:19', '0');
INSERT INTO `payment_setting` VALUES (2, 1, 'WECHAT', '{\"mchId\":\"1626870395\",\"apiclient_key\":\"C:/Users/liuk/Desktop/fsdownload/lili-wechatpay/apiclient_key.pem\",\"serialNumber\":\"3DE7B0B59D986718F6AE9F1AB789ADE21E2F8366\",\"apiKey3\":\"BeiJingClesunRjUvRj4UvRTQu5RTQuR\",\"serviceAppId\":\"ww00ef175f6f71bce2\",\"apiclient_cert_pem\":\"C:/Users/liuk/Desktop/fsdownload/lili-wechatpay/apiclient_cert.pem\",\"apiclient_cert_p12\":\"C:/Users/liuk/Desktop/fsdownload/lili-wechatpay/apiclient_cert.p12\",\"appId\":\"wxd97c1e1ebf47f976\"}', '2023-02-09 15:11:29', '2023-02-13 10:20:21', '1');

SET FOREIGN_KEY_CHECKS = 1;
