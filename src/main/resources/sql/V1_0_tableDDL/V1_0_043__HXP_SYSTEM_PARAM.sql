/*Table structure for table `hxp_system_param` */

CREATE TABLE `hxp_system_param` (
  `PARAM_ID` varchar(10) NOT NULL COMMENT '参数ID，主键',
  `PARAM_TYPE` char(1) NOT NULL COMMENT '参数类型，1：系统；2业务',
  `BUSINESS_NAME` varchar(50) NOT NULL COMMENT '业务名称',
  `FUNCTION_CODE` varchar(20) DEFAULT NULL COMMENT '功能代码',
  `FUNCTION_NAME` varchar(50) DEFAULT NULL COMMENT '功能名称',
  `ON_OFF` varchar(2) DEFAULT NULL COMMENT '生效开关，Y：开；N：关',
  `PARAM_VAL` varchar(256) DEFAULT NULL COMMENT '参数值',
  `UNIT` varchar(20) DEFAULT NULL COMMENT '单位，简单描述',
  `CREATE_OPERATOR_ID` varchar(20) DEFAULT NULL COMMENT '建立人员',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_CREATE_ID` varchar(20) DEFAULT NULL COMMENT '维护人员',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '维护时间',
  `BUSINESS_NAME_EN` varchar(100) NOT NULL COMMENT '业务名称（英文）',
  `BUSINESS_NAME_FR` varchar(100) NOT NULL COMMENT '业务名称（法文）',
  `FUNCTION_NAME_EN` varchar(100) DEFAULT NULL COMMENT '功能名称（英文）',
  `FUNCTION_NAME_FR` varchar(100) DEFAULT NULL COMMENT '功能名称（法文）',
  `REMARK` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`PARAM_ID`),
  KEY `UN_FUNCTION_CODE` (`FUNCTION_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统参数表';