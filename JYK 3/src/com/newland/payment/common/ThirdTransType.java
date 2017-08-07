package com.newland.payment.common;

public class ThirdTransType {
	
	/** 空交易类型 */
	public final static int TRANS_NULL = 0;
	
	/** 消费 */
	public final static int TRANS_SALE = 2;

	/** 消费撤销 */
	public final static int TRANS_VOID_SALE = 7;
	
	/** 退货 */
	public final static int TRANS_REFUND = 6;
	
	/** 预授权 */
	public final static int TRANS_PREAUTH = 5;
	
	/** 预授权撤销 */
	public final static int TRANS_VOID_PREAUTH = 9;
	
	/** 授权完成请求 */
	public final static int TRANS_AUTHSALE = 3;

	/** 预授权完成撤销 */
	public final static int TRANS_VOID_AUTHSALE = 8;
	
	/** 查询 */
	public final static int TRANS_BALANCE = 1;
	
	/** 批结算 */
	public final static int TRANS_SETTLE = 54;
	
	/** 重打印*/
	public final static int TRANS_REPRINT = 9001;
	
	/** 重打印结算单*/
	public final static int TRANS_REPRINT_SETTLE = 19;
	
	/** 查询交易明细*/
	public final static int NATIVE_TRANS_SELECT = 20;
	
	
	
	
	
	
	
	
	
	
	/** 授权完成通知 */
	public final static int TRANS_AUTHSALEOFF = 4;




	





	/** 离线结算 */
	public final static int TRANS_OFFLINE = 10;

	/** 结算调整 */
	public final static int TRANS_ADJUST = 11;

	/** 分期付款 */
	public final static int TRANS_INSTALMENT = 12;

	/** 撤销分期 */
	public final static int TRANS_VOID_INSTALMENT = 13;

	/** EMV脚本结果通知 */
	public final static int TRANS_EMV_SCRIPE = 14;

	/** EMV脱机退货 */
	public final static int TRANS_EMV_REFUND = 15;

	/** 发卡行积分消费 */
	public final static int TRANS_BONUS_IIS_SALE = 16;

	/** 撤销发卡行积分消费 */
	public final static int TRANS_VOID_BONUS_IIS_SALE = 17;

	/** 联盟积分消费 */
	public final static int TRANS_BONUS_ALLIANCE = 18;

	/** 撤销联盟积分消费 */
	public final static int TRANS_VOID_BONUS_ALLIANCE = 19;

	/** 联盟积分查询 */
	public final static int TRANS_ALLIANCE_BALANCE = 20;

	/** 联盟积分退货 */
	public final static int TRANS_ALLIANCE_REFUND = 21;

	/** 电子现金消费 */
	public final static int TRANS_EC_PURCHASE = 22;

	/** 电子现金圈存 指定帐户 */
	public final static int TRANS_EC_LOAD = 23;

	/** 电子现金圈存现金 */
	public final static int TRANS_EC_LOAD_CASH = 24;

	/** 电子现金圈存非指定账户 */
	public final static int TRANS_EC_LOAD_NOT_BIND = 25;

	/** 电子现金圈存现金撤销 */
	public final static int TRANS_EC_VOID_LOAD_CASH = 26;

	/** 无卡预约消费 */
	public final static int TRANS_APPOINTMENT_SALE = 27;

	/** 撤销无卡预约消费 */
	public final static int TRANS_VOID_APPOINTMENT_SALE = 28;

	/** 磁条预付费卡现金充值 */
	public final static int TRANS_MAG_LOAD_CASH = 29;

	/** 磁条预付费卡账户充值 */
	public final static int TRANS_MAG_LOAD_ACCOUNT = 30;

	/** 手机芯片消费 */
	public final static int TRANS_PHONE_SALE = 31;

	/** 撤销手机芯片消费 */
	public final static int TRANS_VOID_PHONE_SALE = 32;

	/** 手机芯片退货 */
	public final static int TRANS_REFUND_PHONE_SALE = 33;

	/** 手机芯片预授权 */
	public final static int TRANS_PHONE_PREAUTH = 34;

	/** 撤销手机芯片预授权 */
	public final static int TRANS_VOID_PHONE_PREAUTH = 35;

	/** 手机芯片预授权完成 */
	public final static int TRANS_PHONE_AUTHSALE = 36;

	/** 手机芯片完成通知 */
	public final static int TRANS_PHONE_AUTHSALEOFF = 37;

	/** 撤销手机完成请求 */
	public final static int TRANS_VOID_PHONE_AUTHSALE = 38;

	/** 手机芯片余额查询 */
	public final static int TRANS_PHONE_BALANCE = 39;

	/** 订购消费 */
	public final static int TRANS_ORDER_SALE = 40;

	/** 订购消费撤销 */
	public final static int TRANS_VOID_ORDER_SALE = 41;

	/** 订购预授权 */
	public final static int TRANS_ORDER_PREAUTH = 42;

	/** 订购预授权撤销 */
	public final static int TRANS_VOID_ORDER_PREAUTH = 43;

	/** 订购预授权完成请求 */
	public final static int TRANS_ORDER_AUTHSALE = 44;

	/** 订购预授权完成撤销 */
	public final static int TRANS_VOID_ORDER_AUTHSALE = 45;

	/** 订购预授权完成通知 */
	public final static int TRANS_ORDER_AUTHSALEOFF = 46;

	/** 订购退货 */
	public final static int TRANS_ORDER_REFUND = 47;

	/** 持卡人信息验证 */
	public final static int TRANS_ORDER_CVM = 48;

	/** 借贷记消费 */
	public final static int TRANS_SALE_EMV = 49;

	/** 签到 */
	public final static int TRANS_LOGIN = 50;

	/** 签退 */
	public final static int TRANS_LOGOUT = 51;

	/** 冲正 */
	public final static int TRANS_REVERSAL = 52;

	/** 脚本结果通知 */
	public final static int TRANS_SCRIPT = 53;



	/** 批上送 */
	public final static int TRANS_BATCHUP = 55;

	/** 参数传递 */
	public final static int TRANS_PARAM_TRANSFER = 56;

	/** 状态上送 */
	public final static int TRANS_STATUS_SEND = 57;

	/** 回响测试 */
	public final static int TRANS_ECHO = 58;

	/** 收银员积分签到 */
	public final static int TRANS_CASHIER_LOGIN = 59;

	/** 磁条预付费卡账户验证 */
	public final static int TRANS_MAG_ACCOUNT_VERIFY = 60;

	/** 磁条预付费卡充值确认 */
	public final static int TRANS_MAG_LOAD_CONFIRM = 61;
	/** 圈存日志 */
	public final static int TRANS_LOAD_DETAIL = 62;
	/** 电子现金余额查询,不在交易过程中使用 */
	public final static int TRANS_EC_BALANCE = 63;
	/** 电子现金明细查询,不在交易过程中使用 */
	public final static int TRANS_EC_DETAIL = 64;
	/** 快速支付 ,不在交易过程中使用*/
	public final static int TRANS_QPBOC = 65;
	
	/**
	 * 添加新的交易类型
	 */
	/**< 手动授权完成联机*/
	public final static int TRANS_MANUALLY_AUTHSALE = 66;	
	/**< 手动授权撤销*/
	public final static int TRANS_MANUALLY_VOID_PREAUTH = 67;		
	/**< 手动完成撤销*/
	public final static int TRANS_MANUALLY_VOID_AUTHSALE = 68;	
	/**< 预授权查询*/
	public final static int TRANS_PREAUTH_INQUIRY = 69;		
	/**手动退货*/
	public final static int TRANS_MANUALLY_REFUND = 70;			
	/**< 分期付款退货*/
	public final static int TRANS_REFUND_INSTALLMENT = 71;	
	/**收款消费*/
	public final static int TRANS_OQSSALE = 72;		
	/**收款预授权*/
	public final static int TRANS_OQSPREAUTH = 73;	
	/**收款预授权完成*/
	public final static int TRANS_OQSAUTHSALE = 74;		
	/**收款手工预授权完成*/
	public final static int TRANS_OQSHANDAUTHSALE = 75;	
	/**收款分期消费*/
	public final static int TRANS_OQSINSTALLMENT = 76;			
	/**< 交易回执*/
	public final static int TRANS_CONFIRM = 77;	
	/**储值卡充值*/
	public final static int TRANS_PREPAID = 78;	
	/**团购券验证**/
	public final static int TRANS_TICKET_VALID = 79;			
	/**实名支付**/
	public final static int TRANS_APPR_SALE = 80;				
	/**资金归集消费**/
	public final static int TRANS_PAYCOLLECT = 81;		
	/**签约*/
	public final static int TRANS_SIGNED = 82;		
	/**审核*/
	public final static int TRANS_AUDIT = 83;	
	/**商户查询*/
	public final static int TRANS_MECHANT_SEARCH = 85;
	
	/**权益消费*/
	public final static int TRANS_VERIFY_SALE = 86;
	/** 主密钥下载*/
	public final static int TRANS_MAINKEYDOWNLOAD = 87;
	/** 付款*/
	public final static int TRANS_PAY_OTHER = 88;
	/** 收款*/
	public final static int TRANS_OQS = 89;
	/**激活结算卡*/
	public final static int TRANS_ACTIVATECARD = 90;
	
	/**鉴权查询**/
	public final static int TRANS_APPR_QUERY = 91;

	/** 脱机处理 未上送交易上送*/
	public final static int UNSEND_TRANS_SEND = 92;

	/** 应用更新*/
	public final static int UPDATE_APP = 93;
	
	/** 系统设置 */
	public final static int NATIVE_SYSTEM_SETTINGS = 102;
	
	/**
	 * 扫码支付
	 */
	public final static int SCAN_CODE = 2001;
	
	/**
	 * 付款码支付
	 */
	public final static int PAY_CODE = 2002;
	
	/**
	 * 登录
	 */
	public final static int LOGIN = 9999;

	/** 母pos下载*/
	public final static int TRANS_MPOS_DOWN = 9901;
	/** 密钥灌装到n900*/
	public final static int TRANS_LOAD_LOAD = 9902;
	
}
