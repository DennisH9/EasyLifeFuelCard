package com.newland.payment.common;

/**
 * 参数key常量
 * 
 * @author ASUS
 * 
 */
public class ParamsConst {

	/**
	 * 签到时间
	 */
	public final static String PARAMS_RUN_LOGIN_DATE = "params_login_date";
	
	/**
	 * 主管登入标志
	 */
	public final static String PARAMS_MAIN_MANAGER_LOGIN_FLAG = "PARAMS_MAIN_MANAGER_LOGIN_FLAG";
	/**
	 * 流水累计金额（上限99999999.99）
	 */
	public final static String PARAMS_RUN_WATER_TOTAL_AMOUNT = "PARAMS_RUN_WATER_TOTAL_AMOUNT";
	/**
	 * 重连次数（对一个IP）
	 */
	public final static String PARAMS_KEY_RECONNECT_TIMES = "PARAMS_RECONNECT_TIMES";
	/** 是否记住的操作员号 */
	public static final String PARAMS_IS_REMEMBER = "PARAMS_IS_REMEMBER";
	/** 是否锁定终端 */
	public static final String PARAMS_IS_LOCK = "PARAMS_IS_LOCK";
	/** 记住的登入操作员号 */
	public static final String PARAMS_REMEMMBER_NO = "PARAMS_REMEMBER_NO";
	/** 原结算单 */
	public static final String PARAMS_OLD_SETTLEMENT = "PARAMS_OLD_SETTLEMENT";
	/** 安全密码 */
	public static final String PARAMS_SAFE_PASSWORD = "PARAMS_SAFE_PASSWORD";
	/** 不参与配置 */
	public static final String PARAMS_NULL = "NULL";
	/** 是否开启调试 */
	public static final String PARAMS_KEY_CONFIG_IS_DEBUG = "CONFIG_IS_DEBUG";
	/** 日志保存等级 */
	public static final String PARAMS_KEY_CONFIG_LOG_LEVEL = "CONFIG_LOG_LEVEL";
	/** 界面超时时间 */
	public static final String PARAMS_KEY_CONFIG_FRAGMENT_TIMEOUT = "CONFIG_FRAGMENT_TIMEOUT";
	
	/** IP */
	public static final String PARAMS_KEY_IP = "IP";
	/** port */
	public static final String PARAMS_KEY_PORT = "PORT";
	/** 参数配置文件是否第一次启动 */
	public static final String PARAMS_KEY_IS_FIRST = "PARAMS_KEY_IS_FIRST";
	/** emv是否初始化 */
//	public static final String EMV_INIT_IS_FIRST = "EMV_INIT_IS_FIRST";
	/** 商户号 */
	public static final String PARAMS_KEY_BASE_MERCHANTID = "BASE_MERCHANTID";
	/** 终端号 */
	public static final String PARAMS_KEY_BASE_POSID = "BASE_POSID";
	/** 商户名称 */
	public static final String PARAMS_KEY_BASE_MERCHANTNAME = "BASE_MERCHANTNAME";
	/** 商户名英文名称 */
	public static final String PARAMS_KEY_BASE_MERCHANTNAMEEN = "BASE_MERCHANTNAMEEN";
	/** 应用名称 */
	public static final String PARAMS_KEY_BASE_APPDISPNAME = "BASE_APPDISPNAME";
	/** 应用标签 */
	public static final String PARAMS_KEY_BASE_APPLAB = "BASE_APPLAB";
	/** 流水号 */
	public static final String PARAMS_KEY_BASE_TRACENO = "BASE_TRACENO";
	/** 批次号 */
	public static final String PARAMS_KEY_BASE_BATCHNO = "BASE_BATCHNO";
	/** 打印张数 */
	public static final String PARAMS_KEY_BASE_PRTCOUNT = "BASE_PRTCOUNT";
	/** 签购单抬头样式 0-中文 1-logo */
	public static final String PARAMS_KEY_BASE_PRINTTITLEMODE = "BASE_PRINTTITLEMODE";
	/** 套打签购单样式 0-空白 1-印制 */
	public static final String PARAMS_KEY_BASE_TAODAPRINTERMODE = "BASE_PRINTTITLEMODE";
	/** 当签购单抬头为中文时的中文抬头 */
	public static final String PARAMS_KEY_BASE_PRINTTITLECN = "BASE_PRINTTITLECN";
	/** 服务热线号码 */
	public static final String PARAMS_KEY_BASE_HOTLINE = "BASE_HOTLINE";
	/** 管理员密码 */
	public static final String PARAMS_KEY_BASE_SUPERPWD = "BASE_SUPERPWD";
	/** 退货最大金额(单位为分) */
	public static final String PARAMS_KEY_BASE_REFUNDAMOUNT = "BASE_REFUNDAMOUNT";
	/** 地区码 */
	public static final String PARAMS_KEY_BASE_LOCALCODE = "BASE_LOCALCODE";
	/** 商行代码 */
	public static final String PARAMS_KEY_BASE_MERCODE = "BASE_MERCODE";
	/** 是否使用磁道加密（1-YES,0-NO） */
	public static final String PARAMS_KEY_BASE_ENCPYTRACK = "BASE_ENCPYTRACK";
	/** 小费比率 */
	public static final String PARAMS_KEY_BASE_TIPRATE = "BASE_TIPRATE";
	/** 消费（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_SALE = "TRANS_SALE";
	/** 插卡消费（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_INSERT_SALE = "TRANS_INSERT_SALE";
	/** 消费撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_VOID = "TRANS_VOID";
	/** 退货（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_REFUND = "TRANS_REFUND";
	/** 余额查询（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_BALANCE = "TRANS_BALANCE";
	/** 预授权（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_PREAUTH = "TRANS_PREAUTH";
	/** 预授权撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_VOIDPREAUTH = "TRANS_VOIDPREAUTH";
	/** 预授权完成请求（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_AUTHSALE = "TRANS_AUTHSALE";
	/** 预授权完成通知（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_AUTHSALEOFF = "TRANS_AUTHSALEOFF";
	/** 预授权完成撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_VOIDAUTHSALE = "TRANS_VOIDAUTHSALE";
	/** 离线结算（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_SETTLEOFF = "TRANS_SETTLEOFF";
	/** 结算调整（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ADJUSTOFF = "TRANS_ADJUSTOFF";
	/** 电子现金（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_EC = "TRANS_ECTUCHSALE";
	/** 快速支付（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_QPBOC = "TRANS_ECNOTUCHSALE";
	/** 电子现金现金充值（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ECMONEYLOAD = "TRANS_ECMONEYLOAD";
	/** 电子现金非指定账户圈存（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ECNOPOINTLOAD = "TRANS_ECNOPOINTLOAD";
	/** 电子现金指定账户圈存（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ECPOINTLOAD = "TRANS_ECPOINTLOAD";
	/** 电子现金现金充值撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ECVOIDMONEYLOAD = "TRANS_ECVOIDMONEYLOAD";
	/** 电子现金脱机退货（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ECREFUND = "TRANS_ECREFUND";
	/** 电子钱包消费（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_EPSALE = "TRANS_EPSALE";
	/** 电子钱包指定账户圈存（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_EPPOINTLOAD = "TRANS_EPPOINTLOAD";
	/** 电子钱包非指定账户圈存（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_EPNOPOINTLOAD = "TRANS_EPNOPOINTLOAD";
	/** 电子钱包现金充值（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_EPMONEYLOAD = "TRANS_EPMONEYLOAD";
	/** 分期付款消费（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_INSTALLSALE = "TRANS_INSTALLSALE";
	/** 分期付款消费撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_INSTALLVOID = "TRANS_INSTALLVOID";
	/** 联盟积分消费（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_UNIONBOINTSALE = "TRANS_UNIONBOINTSALE";
	/** 发卡行积分消费（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_BANKBOINTSALE = "TRANS_BANKBOINTSALE";
	/** 联盟积分消费撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_VOIDUNIONSALE = "TRANS_VOIDUNIONSALE";
	/** 发卡行积分消费撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_VOIDBANKSALE = "TRANS_VOIDBANKSALE";
	/** 联盟积分查询（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_UNIONBALANCE = "TRANS_UNIONBALANCE";
	/** 联盟积分退货（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_UNIONREFUND = "TRANS_UNIONREFUND";
	/** 手机芯片消费（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_PESALE = "TRANS_PESALE";
	/** 手机芯片消费撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_PEVOID = "TRANS_PEVOID";
	/** 手机芯片退货（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_PEREFUND = "TRANS_PEREFUND";
	/** 手机芯片预授权（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_PEPREAUTH = "TRANS_PEPREAUTH";
	/** 手机芯片预授权撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_PEVOIDPREAUTH = "TRANS_PEVOIDPREAUTH";
	/** 手机芯片预授权完成请求（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_PEAUTHSALE = "TRANS_PEAUTHSALE";
	/** 手机芯片预授权完成通知（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_PEAUTHSALEOFF = "TRANS_PEAUTHSALEOFF";
	/** 手机芯片预授权完成撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_PEVOIDAUTHSALE = "TRANS_PEVOIDAUTHSALE";
	/** 手机芯片余额查询（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_PEBALANCE = "TRANS_PEBALANCE";
	/** 预约消费（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_APPOINTMENT = "TRANS_APPOINTMENT";
	/** 预约撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_APVOIDPOINT = "TRANS_APVOIDPOINT";
	/** 订购消费（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ORSALE = "TRANS_ORSALE";
	/** 订购消费撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ORVOID = "TRANS_ORVOID";
	/** 订购退货（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ORREFUND = "TRANS_ORREFUND";
	/** 订购预授权（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ORPREAUTH = "TRANS_ORPREAUTH";
	/** 订购预授权撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ORVOIDPREAUTH = "TRANS_ORVOIDPREAUTH";
	/** 订购预授权完成请求（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ORAUTHSALE = "TRANS_ORAUTHSALE";
	/** 订购预授权完成通知（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ORAUTHSALEOFF = "TRANS_ORAUTHSALEOFF";
	/** 订购预授权完成撤销（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ORVOIDAUTHSALE = "TRANS_ORVOIDAUTHSALE";
	/** 持卡人信息验证 */
	public static final String PARAMS_KEY_CARDHOLDER_INFORMATION = "TRANS_CARDHOLDERINFORMATION";
	/** 磁条卡现金充值（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_MSCMONEY = "TRANS_MSCMONEY";
	/** 磁条卡账户充值（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_MSCACCOUNT = "TRANS_MSCACCOUNT";
	/** 冲正次数 -与PARAMS_KEY_REVERSAL_RESEEND_TIMES重了*/
//	public static final String PARAMS_KEY_TRANS_REVERSALNUM = "TRANS_REVERSALNUM";
	/** 是否支持外接读卡器（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ISEXRF = "TRANS_ISEXRF";
	/** 是否支持射频（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ISSUPPORTRF = "TRANS_ISSUPPORTRF";
	/** 是否使用PP60-RF（1-YES,0-NO） */
	public static final String PARAMS_KEY_TRANS_ISPP60RF = "TRANS_ISPP60RF";
	/** 是否接密码键盘（1-YES,0-NO） */
	public static final String PARAMS_KEY_PIN_PINPAD = "PIN_PINPAD";
	/** 是否预拨号（1-YES,0-NO） */
	public static final String PARAMS_KEY_COMM_PREDIAL = "COMM_PREDIAL";
	/** TPDU */
	public static final String PARAMS_KEY_CDMA_TPDU = "CDMA_TPDU";
	/** TPDU */
	public static final String PARAMS_KEY_GPRS_TPDU = "GPRS_TPDU";
	/** TPDU */
	public static final String PARAMS_KEY_WIFI_TPDU = "WIFI_TPDU";
	/** 超时时间 */
	public static final String PARAMS_KEY_COMM_TIMEOUT = "COMM_TIMEOUT";
	/** 以太网参数 */
	/** 主机1IP */ 
	public static final String PARAMS_KEY_LINE_SERVERIP1 = "LINE_SERVERIP1";
	/** 主机端口1 */
	public static final String PARAMS_KEY_LINE_PORT1 = "LINE_PORT1";
	/** 主机2IP */
	public static final String PARAMS_KEY_LINE_SERVERIP2 = "LINE_SERVERIP2";
	/** 主机端口2 */
	public static final String PARAMS_KEY_LINE_PORT2 = "LINE_PORT2";
	/** 主机3IP */
	public static final String PARAMS_KEY_LINE_SERVERIP13 = "LINE_SERVERIP3";
	/** 主机端口3 */
	public static final String PARAMS_KEY_LINE_PORT3 = "LINE_PORT3";
	/** 本地IP */
	public static final String PARAMS_KEY_LINE_LOCALIP = "LINE_LOCALIP";
	/** 子网掩码 */
	public static final String PARAMS_KEY_LINE_MASK = "LINE_MASK";
	/** 网关 */
	public static final String PARAMS_KEY_LINE_GATEWAY = "LINE_GATEWAY";
	/** 拨号次数 */
	public static final String PARAMS_KEY_DIAL_DIALNUM = "DIAL_DIALNUM";
	/** 外线号码 */
	public static final String PARAMS_KEY_DIAL_PREPHONE = "DIAL_PREPHONE";
	/** 电话号码1 */
	public static final String PARAMS_KEY_DIAL_PHONE1 = "DIAL_PHONE1";
	/** 电话号码2 */
	public static final String PARAMS_KEY_DIAL_PHONE2 = "DIAL_PHONE2";
	/** 电话号码3 */
	public static final String PARAMS_KEY_DIAL_PHONE3 = "DIAL_PHONE3";
	/** GPRS参数 */
	/** 主机1IP */
	public static final String PARAMS_KEY_GPRS_SERVERIP1 = "GPRS_SERVERIP1";
	/** 主机端口1 */
	public static final String PARAMS_KEY_GPRS_PORT1 = "GPRS_PORT1";
	/** 主机2IP */
	public static final String PARAMS_KEY_GPRS_SERVERIP2 = "GPRS_SERVERIP2";
	/** 主机端口2 */
	public static final String PARAMS_KEY_GPRS_PORT2 = "GPRS_PORT2";
	/** 主机3IP */
	public static final String PARAMS_KEY_GPRS_SERVERIP3 = "GPRS_SERVERIP3";
	/** 主机端口3 */
	public static final String PARAMS_KEY_GPRS_PORT3 = "GPRS_PORT3";
	/** APN */
	public static final String PARAMS_KEY_GPRS_APN = "GPRS_APN";
	/** 用户名 */
	public static final String PARAMS_KEY_GPRS_USERNAME = "GPRS_USERNAME";
	/** 用户密码 */
	public static final String PARAMS_KEY_GPRS_PWD = "GPRS_PWD";
	/** 长短链接设置 0-短链接，1-长链接*/
	public static final String PARAMS_KEY_GPRS_MODE = "GPRS_MODE";
	/** APN选择 0-公网，1-专网*/
	public static final String PARAMS_KEY_GPRS_APN_CHOOSE = "APN_CHOOSE";	
	/** CDMA参数 */
	/** 主机1IP */
	public static final String PARAMS_KEY_CDMA_SERVERIP1 = "CDMA_SERVERIP1";
	/** 主机端口1 */
	public static final String PARAMS_KEY_CDMA_PORT1 = "CDMA_PORT1";
	/** 主机IP2 */
	public static final String PARAMS_KEY_CDMA_SERVERIP2 = "CDMA_SERVERIP2";
	/** 主机端口2 */
	public static final String PARAMS_KEY_CDMA_PORT2 = "CDMA_PORT2";
	/** 主机3IP */
	public static final String PARAMS_KEY_CDMA_SERVERIP3 = "CDMA_SERVERIP3";
	/** 主机端口3 */
	public static final String PARAMS_KEY_CDMA_PORT3 = "CDMA_PORT3";
	/** 用户名 */
	public static final String PARAMS_KEY_CDMA_USERNAME = "CDMA_USERNAME";
	/** 用户密码 */
	public static final String PARAMS_KEY_CDMA_PWD = "CDMA_PWD";
	/** 长短链接设置（CDMA长链接很耗电） 0-短链接，1-长链接 */
	public static final String PARAMS_KEY_CDMA_MODE = "CDMA_MODE";
	/** WIFI参数 */
	/** WIFIssid */
	public static final String PARAMS_KEY_WIFI_SSID = "WIFI_SSID";
	/** WIFI密码 */
	public static final String PARAMS_KEY_WIFI_PWD = "WIFI_PWD";
	/** WIFI加密模式（1.OPEN|2.WEP|3.WPA-PSK|4.WPA2-PSK|5.WPA-CCKM） */
	public static final String PARAMS_KEY_WIFI_ENCRYPT_MODE = "WIFI_ENCRYPT_MODE";
	/** 长短链接设置 0-短链接，1-长链接*/
	public static final String PARAMS_KEY_WIFI_MODE = "WIFI_MODE";
	/** 是否自动获取IP（1是,0否） */
	public static final String PARAMS_KEY_WIFI_ISDHCP = "WIFI_ISDHCP";
	/** 主机1IP */
	public static final String PARAMS_KEY_WIFI_SERVERIP1 = "WIFI_SERVERIP1";
	/** 主机端口1 */
	public static final String PARAMS_KEY_WIFI_PORT1 = "WIFI_PORT1";
	/** 主机备份端口2 */
	public static final String PARAMS_KEY_WIFI__BACK_PORT2 = "WIFI_PORTBACK2";
	/** 主机IP2 */
	public static final String PARAMS_KEY_WIFI_SERVERIP2 = "WIFI_SERVERIP2";
	/** 主机端口2 */
	public static final String PARAMS_KEY_WIFI_PORT2 = "WIFI_PORT2";
	/** 主机1IP */
	public static final String PARAMS_KEY_WIFI_SERVERIP3 = "WIFI_SERVERIP3";
	/** 主机端口1 */
	public static final String PARAMS_KEY_WIFI_PORT3 = "WIFI_PORT3";
	/** 本地IP */
	public static final String PARAMS_KEY_WIFI_LOCALIP = "WIFI_LOCALIP";
	/** 子网掩码 */
	public static final String PARAMS_KEY_WIFI_MASK = "WIFI_MASK";
	/** 网关 */
	public static final String PARAMS_KEY_WIFI_GATEWAY = "WIFI_GATEWAY";
	/** 待机界面显示银行名称(4个中文名称) */
	public static final String PARAMS_KEY_EXT_SHOWNAME = "EXT_SHOWNAME";
	/** 签购单抬头图片(50字以内) */
	public static final String PARAMS_KEY_EXT_PRNIMGNAME = "EXT_PRNIMGNAME";
	/** 抬头图片Xpos(图片左上角打印位置) */
	public static final String PARAMS_KEY_EXT_PRNXPOS = "EXT_PRNXPOS";
	/** 收单行代码1 */
	public static final String PARAMS_KEY_EXT_BANKCODE1 = "EXT_BANKCODE1";
	/** 收单行名称1 */
	public static final String PARAMS_KEY_EXT_BANKNAME1 = "EXT_BANKNAME1";
	/** 收单行代码2 */
	public static final String PARAMS_KEY_EXT_BANKCODE2 = "EXT_BANKCODE2";
	/** 收单行名称2 */
	public static final String PARAMS_KEY_EXT_BANKNAME2 = "EXT_BANKNAME2";
	/** 收单行代码3 */
	public static final String PARAMS_KEY_EXT_BANKCODE3 = "EXT_BANKCODE3";
	/** 收单行名称3 */
	public static final String PARAMS_KEY_EXT_BANKNAME3 = "EXT_BANKNAME3";

	// ===========================================================================

	/** 消费撤销是否输入密码 */
	public static final String PARAMS_KEY_IS_INPUT_TRANS_VOID = "IS_VOIDSALE_PIN";
	/** 授权撤销是否输入密码 */
	public static final String PARAMS_KEY_IS_INPUT_AUTH_VOID = "IS_VOIDAUTH_PIN";
	/** 授权完成撤销是否输入密码 */
	public static final String PARAMS_KEY_IS_AUTH_SALE_VOID_PIN = "IS_VOIDAUTHSALE_PIN";
	/** 授权完成请求是否输入密码 */
	public static final String PARAMS_KEY_IS_AUTH_SALE_PIN = "IS_AUTHSALE_PIN";
	/** 消费撤销刷卡 */
	public static final String PARAMS_KEY_TRANS_VOID_SWIPE = "IS_VOIDSALE_STRIP";
	/** 授权完成撤销刷卡 */
	public static final String PARAMS_KEY_AUTH_SALE_VOID_SWIPE = "IS_VOIDAUTHSALE_STRIP";
	/** 结算后自动签退 */
	public static final String PARAMS_KEY_IS_AUTO_LOGOUT = "IS_AUTO_LOGOUT";
	/** 离线上送笔数 */
	public static final String PARAMS_KEY_MAX_OFFSEND_NUM = "MAX_OFFSEND_NUM";
	/** 离线上送方式 0-批结算前上送，1-下笔联机上送 ,此参数无效 */
	public static final String PARAMS_KEY_OFFSEND_MODE = "OFFSEND_MODE";
	/** 离线重发次数 */
	public static final String PARAMS_KEY_OFFSEND_RESEND_TIMES = "OFFSEND_RESEND_TIMES";
	/** 冲正重发次数 */
	public static final String PARAMS_KEY_REVERSAL_RESEEND_TIMES = "REVERSAL_RESEND_TIMES";
	/** 签名重发次数 */
	public static final String PARAMS_KEY_SIGN_RESEND_TIMES = "SIGN_RESEND_TIMES";
	/** TMS重发次数 */
	public static final String PARAMS_KEY_TMS_RESEND_TIMES = "TMS_RESEND_TIMES";

	/** 输入主管密码 */
	public static final String PARAMS_KEY_IS_ADMIN_PASSWORD = "IS_ADMIN_PASSWORD";
	/** 手工输卡号 */
	public static final String PARAMS_KEY_IS_CARD_INPUT = "IS_CARD_INPUT";
	/** 缺省交易类型 0-预授权，1-消费 */
	public static final String PARAMS_KEY_DEFAULT_TRANS_TYPE = "DEFAULT_TRANS_TYPE";
	
	/** 支持小额代授权 */
	public static final String PARAMS_KEY_IS_SMALL_GENE_AUTH = "IS_SMALL_GENE_AUTH";
	/** 是否支持小费 */
	public static final String PARAMS_KEY_IS_TIP = "IS_TIP";
	
	/** 授权通知模式 0-同时支持，1-支持请求模式，2-支持通知模式 */
	public static final String PARAMS_KEY_AUTH_SALE_MODE = "AUTH_SALE_MODE";
	/** 内外置键盘 0-内置,1-外置*/
	public static final String PARAMS_KEY_INSIDE_EXTEND_KEYBOARD = "INSIDE_EXTEND_KEYBOARD";
	/** 键盘超时时间 */
	public static final String PARAMS_KEY_PINPAD_TIMEOUT = "PINPAD_TIMEOUT";
	/** 保存最大交易笔数 */
	public static final String PARAMS_KEY_MAX_TRANS_COUNT = "MAX_TRANS_COUNT";

	/** 预授权屏蔽卡号 */
	public static final String PARAMS_KEY_IS_PREAUTH_SHIELD_PAN = "IS_PREAUTH_SHIELD_PAN";
	/** IC卡交易支持 */
	public static final String PARAMS_KEY_IS_SUPPORT_IC = "IS_SUPPORT_IC";
	/** 是否显示TVR/TSI */
	public static final String PARAMS_KEY_IS_DISPLAY_TRV_TSI = "IS_DISPLAY_TRV_TSI";
	/** 是否支持射频卡 */
	public static final String PARAMS_KEY_IS_SUPPORT_RF = "IS_SUPPORT_RF";
	/** 内外置非接设置 0-内置,1-外置*/
	public static final String PARAMS_KEY_IS_EXTEND_RF = "IS_EXTEND_RF";
	/** 消费交易是否支持挥卡 */
	public static final String PARAMS_KEY_IS_SALE_FLICK = "IS_SALE_FLICK";
	
	/** 非接寻卡延迟 */
	public static final String PARAMS_KEY_IS_RF_DELAY = "IS_RF_DELAY";
	/** 非接寻卡延迟时间 */
	public static final String PARAMS_KEY_RF_DELAY_TIME = "RF_DELAY_TIME";
	/** 输入本地地区码 */
	public static final String PARAMS_KEY_LOCOL_CODE = "LOCAL_CODE";

	/** 主密钥索引 */
	public static final String PARAMS_KEY_MAIN_KEY_INDEX = "CONFIG_MAIN_KEY_INDEX";
	/** 优惠查询密钥索引 */
	public static final String PARAMS_KEY_WORK_KEY_INDEX = "CONFIG_WORK_KEY_INDEX";
	/** 手输主密钥索引 */
	public static final String PARAMS_KEY_HEAD_MAIN_KEY_INDEX = "HEAD_MAIN_KEY_INDEX";
	/** 手输主密钥 */
	public static final String PARAMS_KEY_HEAD_MAIN_KEY = "HEAD_MAIN_KEY";
	/** 密钥算法，0-单倍长密钥，1-双倍长密钥 */
	public static final String PARAMS_KEY_ENCRYPT_MODE = "ENCRYPT_MODE";

	/** 是否确认电子签名 */
	public static final String PARAMS_KEY_IS_CONFIRM_ELECSIGN = "IS_CONFIRM_ELECSIGN";
	/** 是否支持电子签名 */
	public static final String PARAMS_KEY_IS_SUPPORT_ELECSIGN = "IS_SUPPORT_ELECSIGN";
	/** 签名版位置，0-内置，1-外置 */
	public static final String PARAMS_KEY_IS_EXTERN_ELECBORD = "IS_EXTERN_ELECBORD";
	/** 电子签名签字超时时间 */
	public static final String PARAMS_KEY_ELEC_TIMEOUT = "ELEC_TIMEOUT";
	/** 电子签名重签次数 */
	public static final String PARAMS_KEY_ELEC_RESIGN_TIMES = "ELEC_RESIGN_TIMES";
	/** 电子签名最大笔数 */
	public static final String PARAMS_KEY_MAX_ELEC_COUNT = "MAX_ELEC_COUNT";
	/** 是否启用lbs定位 */
	public static final String PARAMS_KEY_IS_SUPPORT_LBS = "IS_SUPPORT_LBS";
	/** LBS间隔笔数 */
	public static final String PARAMS_KEY_LBS_NUM = "LBS_NUM";
	/** LBS主机TPDU */
	public static final String PARAMS_KEY_LBS_TPDU = "LBS_TPDU";
	/** 基站服务地址 */
	public static final String PARAMS_KEY_LBS_IP = "LBS_IP";
	/** 基站服务器端口 */
	public static final String PARAMS_KEY_LBS_PORT = "LBS_PORT";
	/** 基站接收超时时间 */
	public static final String PARAMS_KEY_LBS_TIMEOUT = "LBS_TIMEOUT";

	/** (内卡)消费金额 */
	public static final String PARAMS_KEY_INSIDE_EXPENSE_AMOUNT = "INSIDE_EXPENSE_AMOUNT";
	/** (外卡)消费金额 */
	public static final String PARAMS_KEY_OUTSIDE_EXPENSE_AMOUNT = "OUTSIDE_EXPENSE_AMOUNT";
	/** (内卡)预授权完成（请求）金额 */
	public static final String PARAMS_KEY_INSIDE_PRE_AUTH_FINISH_REQUEST_AMOUNT = "INSIDE_PRE_AUTH_FINISH_REQUEST_AMOUNT";
	/** (外卡)预授权完成（请求）金额 */
	public static final String PARAMS_KEY_OUTSIDE_PRE_AUTH_FINISH_REQUEST_AMOUNT = "OUTSIDE_PRE_AUTH_FINISH_REQUEST_AMOUNT";
	/** (内卡)预授权完成（通知）金额 */
	public static final String PARAMS_KEY_INSIDE_PRE_AUTH_FINISH_NOTIFICATION_AMOUNT = "INSIDE_PRE_AUTH_FINISH_NOTIFICATION_AMOUNT";
	/** (外卡)预授权完成（通知）金额 */
	public static final String PARAMS_KEY_OUTSIDE_PRE_AUTH_FINISH_NOTIFICATION_AMOUNT = "OUTSIDE_PRE_AUTH_FINISH_NOTIFICATION_AMOUNT";
	/** (内卡)离线结算金额 */
	public static final String PARAMS_KEY_INSIDE_OFF_LINE_SETTLEMENT_AMOUNT = "INSIDE_OFF_LINE_SETTLEMENT_AMOUNT";
	/** (外卡)离线结算金额 */
	public static final String PARAMS_KEY_OUTSIDE_OFF_LINE_SETTLEMENT_AMOUNT = "OUTSIDE_OFF_LINE_SETTLEMENT_AMOUNT";
	/** (内卡)结算调整金额 */
	public static final String PARAMS_KEY_INSIDE_SETTLEMENT_ADJUST_AMOUNT = "INSIDE_SETTLEMENT_ADJUST_AMOUNT";
	/** (外卡)结算调整金额 */
	public static final String PARAMS_KEY_OUTSIDE_SETTLEMENT_ADJUST_AMOUNT = "OUTSIDE_SETTLEMENT_ADJUST_AMOUNT";
	/** (内卡)PBOC脱机消费金额 */
	public static final String PARAMS_KEY_INSIDE_PBOC_OFFLINE_CONSUMPTION_AMOUNT = "INSIDE_PBOC_OFFLINE_CONSUMPTION_AMOUNT";
	/** (外卡)PBOC脱机消费金额 */
	public static final String PARAMS_KEY_OUTSIDE_PBOC_OFFLINE_CONSUMPTION_AMOUNT = "OUTSIDE_PBOC_OFFLINE_CONSUMPTION_AMOUNT";
	/** (内卡)PBOC非指定账户圈存交易金额 */
	public static final String PARAMS_KEY_INSIDE_PBOC_CIRCLE_DEPOSIT_TRANSACTION_AMOUNT_SPECIFIED_ACCOUNT = "INSIDE_PBOC_CIRCLE_DEPOSIT_TRANSACTION_AMOUNT_SPECIFIED_ACCOUNT";
	/** (外卡)PBOC非指定账户圈存交易金额 */
	public static final String PARAMS_KEY_OUTSIDE_PBOC_CIRCLE_DEPOSIT_TRANSACTION_AMOUNT_SPECIFIED_ACCOUNT = "OUTSIDE_PBOC_CIRCLE_DEPOSIT_TRANSACTION_AMOUNT_SPECIFIED_ACCOUNT";
	/** (内卡)消费笔数 */
	public static final String PARAMS_KEY_INSIDE_EXPENSE_COUNT = "INSIDE_EXPENSE_COUNT";
	/** (外卡)消费笔数 */
	public static final String PARAMS_KEY_OUTSIDE_EXPENSE_COUNT = "OUTSIDE_EXPENSE_COUNT";
	/** (内卡)预授权完成（请求）笔数 */
	public static final String PARAMS_KEY_INSIDE_PRE_AUTH_FINISH_REQUEST_COUNT = "INSIDE_PRE_AUTH_FINISH_REQUEST_COUNT";
	/** (外卡)预授权完成（请求）笔数 */
	public static final String PARAMS_KEY_OUTSIDE_PRE_AUTH_FINISH_REQUEST_COUNT = "OUTSIDE_PRE_AUTH_FINISH_REQUEST_COUNT";
	/** (内卡)预授权完成（通知）笔数 */
	public static final String PARAMS_KEY_INSIDE_PRE_AUTH_FINISH_NOTIFICATION_COUNT = "INSIDE_PRE_AUTH_FINISH_NOTIFICATION_COUNT";
	/** (外卡)预授权完成（通知）笔数 */
	public static final String PARAMS_KEY_OUTSIDE_PRE_AUTH_FINISH_NOTIFICATION_COUNT = "OUTSIDE_PRE_AUTH_FINISH_NOTIFICATION_COUNT";
	/** (内卡)离线结算笔数 */
	public static final String PARAMS_KEY_INSIDE_OFF_LINE_SETTLEMENT_COUNT = "INSIDE_OFF_LINE_SETTLEMENT_COUNT";
	/** (外卡)离线结算笔数 */
	public static final String PARAMS_KEY_OUTSIDE_OFF_LINE_SETTLEMENT_COUNT = "OUTSIDE_OFF_LINE_SETTLEMENT_COUNT";
	/** (内卡)结算调整笔数 */
	public static final String PARAMS_KEY_INSIDE_SETTLEMENT_FREQUENCY_OF_ADJUSTMENT = "INSIDE_SETTLEMENT_FREQUENCY_OF_ADJUSTMENT";
	/** (外卡)结算调整笔数 */
	public static final String PARAMS_KEY_OUTSIDE_SETTLEMENT_FREQUENCY_OF_ADJUSTMENT = "OUTSIDE_SETTLEMENT_FREQUENCY_OF_ADJUSTMENT";
	/** (内卡)PBOC脱机消费笔数 */
	public static final String PARAMS_KEY_INSIDE_PBOC_OFFLINE_CONSUMPTION_NUMBER = "INSIDE_PBOC_OFFLINE_CONSUMPTION_NUMBER";
	/** (外卡)PBOC脱机消费笔数 */
	public static final String PARAMS_KEY_OUTSIDE_PBOC_OFFLINE_CONSUMPTION_NUMBER = "OUTSIDE_PBOC_OFFLINE_CONSUMPTION_NUMBER";
	/** (内卡)PBOC电子现金的非指定账户圈存交易笔数 */
	public static final String PARAMS_KEY_INSIDE_PBOC_ELECTRONIC_CASH_CIRCLE_DEPOSIT_TRANSACTIONS_DESIGNATED_ACCOUNT_NUMBER = "INSIDE_PBOC_ELECTRONIC_CASH_CIRCLE_DEPOSIT_TRANSACTIONS_DESIGNATED_ACCOUNT_NUMBER";
	/** (外卡)PBOC电子现金的非指定账户圈存交易笔数 */
	public static final String PARAMS_KEY_OUTSIDE_PBOC_ELECTRONIC_CASH_CIRCLE_DEPOSIT_TRANSACTIONS_DESIGNATED_ACCOUNT_NUMBER = "OUTSIDE_PBOC_ELECTRONIC_CASH_CIRCLE_DEPOSIT_TRANSACTIONS_DESIGNATED_ACCOUNT_NUMBER";
	/** (内卡)退货金额 */
	public static final String PARAMS_KEY_INSIDE_RETURNS_AMOUNT = "INSIDE_RETURNS_AMOUNT";
	/** (外卡)退货金额 */
	public static final String PARAMS_KEY_OUTSIDE_RETURNS_AMOUNT = "OUTSIDE_RETURNS_AMOUNT";
	/** (内卡)消费撤销金额 */
	public static final String PARAMS_KEY_INSIDE_EXPENSE_REVOCATION_AMOUNT = "INSIDE_EXPENSE_REVOCATION_AMOUNT";
	/** (外卡)消费撤销金额 */
	public static final String PARAMS_KEY_OUTSIDE_EXPENSE_REVOCATION_AMOUNT = "OUTSIDE_EXPENSE_REVOCATION_AMOUNT";
	/** (内卡)预授权完成撤销金额 */
	public static final String PARAMS_KEY_INSIDE_PRE_AUTH_FINISH_REVOCATION_AMOUNT = "INSIDE_PRE_AUTH_FINISH_REVOCATION_AMOUNT";
	/** (外卡)预授权完成撤销金额 */
	public static final String PARAMS_KEY_OUTSIDE_PRE_AUTH_FINISH_REVOCATION_AMOUNT = "OUTSIDE_PRE_AUTH_FINISH_REVOCATION_AMOUNT";
	/** (内卡)PBOC现金充值 */
	public static final String PARAMS_KEY_INSIDE_PBOC_CHARGE_AMOUNT = "INSIDE_PBOC_CHARGE_AMOUNT";
	/** (外卡)PBOC现金充值 */
	public static final String PARAMS_KEY_OUTSIDE_PBOC_CHARGE_AMOUNT = "OUTSIDE_PBOC_CHARGE_AMOUNT";
	/** (内卡)预付费卡现金充值金额 */
	public static final String PARAMS_KEY_INSIDE_PREPAID_CARD_TOP_UP_AMOUNT_IN_CASH = "INSIDE_PREPAID_CARD_TOP_UP_AMOUNT_IN_CASH";
	/** (外卡)预付费卡现金充值金额 */
	public static final String PARAMS_KEY_OUTSIDE_PREPAID_CARD_TOP_UP_AMOUNT_IN_CASH = "OUTSIDE_PREPAID_CARD_TOP_UP_AMOUNT_IN_CASH";
	/** (内卡)退货笔数 */
	public static final String PARAMS_KEY_INSIDE_RETURNS_COUNT = "INSIDE_RETURNS_COUNT";
	/** (外卡)退货笔数 */
	public static final String PARAMS_KEY_OUTSIDE_RETURNS_COUNT = "OUTSIDE_RETURNS_COUNT";
	/** (内卡)消费撤销笔数 */
	public static final String PARAMS_KEY_INSIDE_EXPENSE_REVOCATION_COUNT = "INSIDE_EXPENSE_REVOCATION_COUNT";
	/** (外卡)消费撤销笔数 */
	public static final String PARAMS_KEY_OUTSIDE_EXPENSE_REVOCATION_COUNT = "OUTSIDE_EXPENSE_REVOCATION_COUNT";
	/** (内卡)预授权完成撤销笔数 */
	public static final String PARAMS_KEY_INSIDE_PRE_AUTH_FINISH_REVOCATION_COUNT = "INSIDE_PRE_AUTH_FINISH_REVOCATION_COUNT";
	/** (外卡)预授权完成撤销笔数 */
	public static final String PARAMS_KEY_OUTSIDE_PRE_AUTH_FINISH_REVOCATION_COUNT = "OUTSIDE_PRE_AUTH_FINISH_REVOCATION_COUNT";
	/** (内卡)PBOC现金充值笔数 */
	public static final String PARAMS_KEY_INSIDE_PBOC_CHARGE_COUNT = "INSIDE_PBOC_CHARGE_COUNT";
	/** (外卡)PBOC现金充值笔数 */
	public static final String PARAMS_KEY_OUTSIDE_PBOC_CHARGE_COUNT = "OUTSIDE_PBOC_CHARGE_COUNT";
	/** (内卡)预付费卡现金充值笔数 */
	public static final String PARAMS_KEY_INSIDE_PREPAID_CARD_CASH_PREPAID_PHONE_NUMBER = "INSIDE_PREPAID_CARD_CASH_PREPAID_PHONE_NUMBER";
	/** (外卡)预付费卡现金充值笔数 */
	public static final String PARAMS_KEY_OUTSIDE_PREPAID_CARD_CASH_PREPAID_PHONE_NUMBER = "OUTSIDE_PREPAID_CARD_CASH_PREPAID_PHONE_NUMBER";
	/** 普通支付 */
	public static final String PARAMS_KEY_NORMAL_PAY = "NORMAL_PAY";
//	/** 圈存 */
//	public static final String PARAMS_KEY_LOAD = "LOAD";
//	/** 明细查询 */
//	public static final String PARAMS_KEY_CHECK_DETAIL = "CHECK_DETAIL";
//	/** 脱机退货 */
//	public static final String PARAMS_KEY_OFFLINE_RETURN = "OFFLINE_RETURN";
//	/** 现金充值 */
//	public static final String PARAMS_KEY_AMOUNT_RECHARGE = "AMOUNT_RECHARGE";
	/** 积分消费 */
	public static final String PARAMS_KEY_POINT_CONSUMPTION = "POINT_CONSUMPTION";
	/** 积分消费撤销 */
	public static final String PARAMS_KEY_POINT_CONSUMPTION_CANCEL = "POINT_CONSUMPTION_CANCEL";
//	/** 手机消费 */
//	public static final String PARAMS_KEY_MOBILE_TRANS = "MOBILE_TRANS";
//	/** 手机消费撤销 */
//	public static final String PARAMS_KEY_MOBILE_TRANS_CANCEL = "MOBILE_TRANS_CANCEL";
//	/** 打印管理设置*/ 
//	public static final String PARAMS_KEY_SETTING_PRINT_MANAGE = "SETTING_PRINT_MANAGE";
//	/** 打印设置*/ 
//	public static final String PARAMS_KEY_SETTING_PRINT = "SETTING_PRINT";
//	/** 其它打印设置*/ 
	public static final String PARAMS_KEY_SETTING_PRINT_OTHER = "SETTING_PRINT_OTHER";
	/** 签购单字体选择 0-大,1-中,2-小*/ 
	public static final String PARAMS_KEY_SIGN_ORDER_FONT = "SIGN_ORDER_FONT";
	/** 签购单是否打英文*/ 
	public static final String PARAMS_KEY_IS_PRINT_SIGN_ORDER_ENGLISH = "IS_PRINT_SIGN_ORDER_ENGLISH";
	/** 是否提示打印明细*/ 
	public static final String PARAMS_KEY_IS_HINT_PRINT_DETAIL = "IS_HINT_PRINT_DETAIL";
	/** 重打印结算单*/ 
	public static final String PARAMS_KEY_ANEW_PRINT_STATEMENT = "ANEW_PRINT_STATEMENT";
//	/** 设置签购单保管年限*/ 
	public static final String PARAMS_KEY_SETTING_SIGN_ORDER_AGE_LIMIT = "SETTING_SIGN_ORDER_AGE_LIMIT";
	/** 打印所有交易明细*/ 
	public static final String PARAMS_KEY_PRINT_ALL_TRANS_DETAIL = "PRINT_ALL_TRANS_DETAIL";
	/** 打印中文收单行*/ 
	public static final String PARAMS_KEY_PRINT_CHINESE_ACQUIRER = "PRINT_CHINESE_ACQUIRER";
	/** 打印中文发卡行*/ 
	public static final String PARAMS_KEY_PRINT_CHINESE_ISSUING_BANK = "PRINT_CHINESE_ISSUING_BANK";
	/** 是否打印负号*/ 
	public static final String PARAMS_KEY_IS_PRINT_MINUS = "IS_PRINT_MINUS";
	/** 发卡行名称*/ 
	public static final String PARAMS_KEY_ISSUING_BANK_NAME = "ISSUING_BANK_NAME";
//	/** 参数打印*/ 
//	public static final String PARAMS_KEY_PARAM_PRINT = "PARAM_PRINT";
//	/** 商户信息*/ 
//	public static final String PARAMS_KEY_PRINT_SHOP_INFO = "PRINT_SHOP_INFO";
//	/** 交易控制*/ 
//	public static final String PARAMS_KEY_PRINT_TRANS_CONTROL = "PRINT_TRANS_CONTROL";
//	/** 系统控制*/ 
//	public static final String PARAMS_KEY_PRINT_SYSTEM_CONTROL = "PRINT_SYSTEM_CONTROL";
//	/** 通讯参数*/ 
//	public static final String PARAMS_KEY_PRINT_COMMUNICATION = "PRINT_COMMUNICATION";
//	/** 版本信息*/ 
//	public static final String PARAMS_KEY_PRINT_VERSION = "PRINT_VERSION";
//	/** EMV参数*/ 
//	public static final String PARAMS_KEY_PRINT_EMV = "PRINT_EMV";
//	/** 其它*/ 
//	public static final String PARAMS_KEY_PRINT_OTHER = "PRINT_OTHER";
//

	
	/** 通讯类型,cdma-0,gprs-1,wifi-2*/ 
	public static final String PARAMS_KEY_COMMUNICATION_TYPE = "COMMUNICATION_TYPE";
	/** 通讯超时时间*/ 
	public static final String PARAMS_KEY_COMMUNICATION_OUT_TIME = "COMMUNICATION_OUT_TIME";
	/** 终端应用类型 */
	public static final String PARAMS_KEY_POS_TYPE = "POS_TYPE";
	/** 管理电话号码 */
	public static final String PARAMS_KEY_MANAGE_PHONE = "MANAGE_PHONE";
	/** 基于 PBOC 借/贷记标准 IC 卡脚本处理结果通知 */
	public static final String PARAMS_KEY_PBOC_CARD_RESULT_NOTIFICATION = "PBOC_CARD_RESULT_NOTIFICATION";
	/** 电子现金脱机消费 */
//	public static final String PARAMS_KEY_ECASH_OFF_LINE_EXPENSE = "ECASH_OFF_LINE_EXPENSE";

	/** 是否使用网络 */
	public static final String PARAMS_KEY_IS_USE_HTTP = "IS_USE_HTTP";
	/** 是否输入手机号 */
	public static final String PARAMS_KEY_IS_ELECSIGN_INPUT_PHONE = "IS_ELECSIGN_INPUT_PHONE";

	/** CDMA接入号码 */
	public static final String PARAMS_KEY_CDMA_ACCESS_NUMBER = "CDMA_ACCESS_NUMBER";
	/** GPRS接入号码 */
	public static final String PARAMS_KEY_GPRS_ACCESS_NUMBER = "GPRS_ACCESS_NUMBER";
	
	//打印模版版本号
	public static final String PARAMS_PRINT_VERSION = "PRINT_VERSION";
	
	 /** 插卡隐藏开关*/	
	public static final String PARAM_IS_HIDING_SWING = "IS_HIDING_SWING";
	/** 打印模版*/	
	public static final String PARAM_PRINT_MODEL = "PRINT_MODEL";
	/** 卡bin编号*/	
	public static final String PARAM_CARD_BIN_NO = "CARD_BIN_NO";
	
		/** 免密限额*/
	public static final String PARAMS_KEY_QPS_NO_PSW_LIMIT = "QPS_NO_PSW_LIMIT";
	/** 免密开关*/
	public static final String PARAMS_KEY_QPS_IS_NO_PSW = "QPS_IS_NO_PSW";
	/** BIN表A标识*/
	public static final String PARAMS_KEY_QPS_CARD_BIN_A = "QPS_CARD_BIN_A";
	/** BIN表B标识*/
	public static final String PARAMS_KEY_QPS_CARD_BIN_B = "QPS_CARD_BIN_B";
	/** BIN表C标识*/
	public static final String PARAMS_KEY_QPS_CARD_BIN_C = "QPS_CARD_BIN_C";
	/** CDCVM标识*/
	public static final String PARAMS_KEY_QPS_IS_CDCVM = "QPS_IS_CDCVM";
	/** 免签开关*/
	public static final String PARAMS_KEY_QPS_IS_NO_SINGNATURE = "QPS_IS_NO_SINGNATURE";
	/** 免签限额*/
	public static final String PARAMS_KEY_QPS_NO_SINGNATURE_LIMIT = "QPS_NO_SINGNATURE_LIMIT";
	
	/** 非接交易通道开关 */
	public static final String PARAMS_KEY_QPBOC_PRIORITY = "QPBOC_PRIORITY";
	/** 闪卡最大笔数*/
	public static final String PARAMS_KEY_FLASH_CARD_MAX_NUM = "FLASH_CARD_MAX_NUM";
	/** 当笔闪卡重刷时间*/
	public static final String PARAMS_KEY_FLASH_CARD_RESWIP_TIME_OUT = "FLASH_CARD_RESWIP_TIME_OUT";
	/** 冲正测试开关*/
	public static final String PARAMS_IS_REVERSE_TEST = "IS_REVERSE_TEST";


	/** 闪卡可处理时间*/
	public static final String PARAMS_KEY_FLASH_CARD_CAN_DEAL_TIME_OUT = "FLASH_CARD_CAN_DEAL_TIME_OUT";
	/** 油品类型*/
	public static final String PARAMS_KEY_OILTYPE_BIN= "OILTYPE_BIN";

	public static final String PARAMS_IS_USE_DISTRI_BUTION = "IS_USE_DISTRI_BUTION";

	public static final String PARAMS_TRANSPORT_KEY = "TRANSPORT_KEY";

	/** 是否启用备份IP和端口（1-YES,0-NO） */
	public static final String PARAMS_IS_USING_OTHER_IP_AND_PORT = "USING_OTHER_IP_AND_PORT";
}
