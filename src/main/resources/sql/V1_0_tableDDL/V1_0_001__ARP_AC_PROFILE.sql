/*Table structure for table `arp_ac_profile` */

CREATE TABLE `arp_ac_profile` (
  `CUST_ID` varchar(14) DEFAULT NULL COMMENT '客户编号',
  `PAY_AC_NO` varchar(16) NOT NULL COMMENT '客户开立的账号，为14位的客户编号加2位的顺序号：00~99',
  `AC_TYPE` char(2) NOT NULL COMMENT '账户类型 01-现金账户；02-红包账户；03-平台账户；04-担保账户；05-保证金账户；06-待交服务费账户',
  `AC_KIND` char(2) DEFAULT NULL COMMENT '账户种类 01-内部账户；02-外部账户；03-专用账户',
  `BR_NO` varchar(10) DEFAULT NULL COMMENT '机构号',
  `CCY` char(3) NOT NULL COMMENT '货币',
  `CASH_AC_BAL` varchar(15) DEFAULT NULL COMMENT '可以结算的账户余额',
  `MIN_STL_BALANCE` varchar(15) DEFAULT NULL COMMENT '收取的保证金金额',
  `FROZ_BALANCE` varchar(15) DEFAULT NULL COMMENT '冻结金额',
  `GL_CODE` varchar(10) DEFAULT NULL COMMENT '归属科目',
  `AC_STATUS` char(1) DEFAULT NULL COMMENT '账户状态 0-正常；1-未激活；2-冻结；9-已销户',
  `LIST_STS_FLG` char(1) DEFAULT NULL COMMENT '名单状态标志 0-一般；1-灰名单；2-黑名单；3-红名单',
  `RESV_FLG` varchar(30) DEFAULT NULL COMMENT '预留标志',
  `ACC_SUM_NUM` varchar(15) DEFAULT NULL COMMENT '账户积数',
  `LST_TX_DATE` char(8) DEFAULT NULL COMMENT '最后交易日期',
  `LST_TX_TIME` char(6) DEFAULT NULL COMMENT '最后交易时间',
  PRIMARY KEY (`PAY_AC_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户主档案表zhu';