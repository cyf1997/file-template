/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : file

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 16/07/2024 14:06:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_template
-- ----------------------------
DROP TABLE IF EXISTS `file_template`;
CREATE TABLE `file_template`  (
  `ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模版ID',
  `TEMPLATE_CODE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板编号',
  `SP_TEMPLATE_ID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方模板编号',
  `FILE_ID` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件ID',
  `TENANT_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户号',
  `TEMPLATE_TYPE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模版类型',
  `TEMPLATE_NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模版名称',
  `TEMPLATE_FORMAT` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板格式',
  `TEMPLATE_HTML` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '模版HTML',
  `STATUS` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态',
  `CREATE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_DATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_DATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除状态：0-正常，1-删除',
  `RSV1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用字段1',
  `RSV2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用字段2',
  `RSV3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用字段3',
  `TEMPLATE_DESC` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`ID`, `TENANT_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件模版表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_template
-- ----------------------------
INSERT INTO `file_template` VALUES ('1795006324068597762', '1795006324060209152', 'ww', '1795006310592299009', '149002011', 'other', '测试word', 'word', '', '0', 'U1772871365525405696', '2024-05-27 16:17:12', 'U1772871365525405696', '2024-05-27 16:17:12', '1', '9', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for file_template_field
-- ----------------------------
DROP TABLE IF EXISTS `file_template_field`;
CREATE TABLE `file_template_field`  (
  `ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `TEMPLATE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模版ID',
  `PARAM_KEY` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务参数名',
  `TEMPLATE_KEY` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板参数名',
  `PARAM_TYPE` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数类型',
  `CREATE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE_TIME` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `UPDATE_DATE_TIME` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `PARAM_DESC` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数描述',
  `FILL_VALIDATION_FLG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '填充校验标记：0-校验，1-不校验',
  `DEFAULT_VALUE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认值',
  `REPLACE_VALUE` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '替换值',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UNK_TEMPLATE_ID_KEY`(`TEMPLATE_ID`, `TEMPLATE_KEY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'E签宝模板字段映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_template_field
-- ----------------------------

-- ----------------------------
-- Table structure for file_template_script
-- ----------------------------
DROP TABLE IF EXISTS `file_template_script`;
CREATE TABLE `file_template_script`  (
  `ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '主键',
  `TEMPLATE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模版ID',
  `SCRIPT` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '脚本',
  `CREATE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE_TIME` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `UPDATE_DATE_TIME` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `DELETE_FLG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否删除;0:存在;1:删除',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `idx_tid`(`TEMPLATE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'E签宝模板脚本' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_template_script
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
