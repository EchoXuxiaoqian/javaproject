/*Table structure for table `feerate` */

CREATE TABLE `feerate` (
  `FEE_CODE` varchar(10) NOT NULL COMMENT '费率代码',
  `FEE_NAME` varchar(60) DEFAULT NULL COMMENT '费率名称',
  `CCY` char(3) DEFAULT NULL COMMENT '货币类型',
  `CAL_MODE` char(1) DEFAULT NULL COMMENT '计费方式，0-定额收费；1-按比例收费',
  `MULTI_SECTION_MODE` char(1) DEFAULT NULL COMMENT '分档方法，0-不套档 ；1-套档 ；2-分段累进',
  `MAX_FEE` varchar(15) DEFAULT NULL COMMENT '最高收费',
  `MIN_FEE` varchar(15) DEFAULT NULL COMMENT '最低收费',
  `START_CAL_AMT` varchar(15) DEFAULT NULL COMMENT '起费额',
  `CREATE_DATE` char(8) DEFAULT NULL COMMENT '创建日期',
  `CREATE_TIME` char(6) DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) DEFAULT NULL COMMENT '创建人员',
  `LAST_UPD_DATE` char(8) DEFAULT NULL COMMENT '最后修改日期',
  `LAST_UPD_TIME` char(6) DEFAULT NULL COMMENT '最后修改时间',
  `LAST_UPD_USER` varchar(20) DEFAULT NULL COMMENT '最后修改人',
  `RATE_TYPE` char(1) DEFAULT NULL COMMENT '费率类型，0 结算费率；1 返佣费率；2 扣费费率',
  `FEE_DEC` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`FEE_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='费率表';