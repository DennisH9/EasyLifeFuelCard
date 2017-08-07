package com.newland.payment.common;

import com.newland.pos.sdk.common.EmvTransType;


/**
 * 交易类型
 * @author linchunhui
 * @date 2015年5月11日
 * @time 下午9:06:52
 *
 */
public class TransType {

	/** 空交易类型 */
	public final static int TRANS_NULL = 0;
	
	/** 查询 */
	public final static int TRANS_BALANCE = EmvTransType.TRANS_BALANCE;

	/** 消费 */
	public final static int TRANS_SALE = EmvTransType.TRANS_SALE;

	/** 授权完成请求 */
	public final static int TRANS_AUTHSALE = EmvTransType.TRANS_AUTHSALE;

	/** 授权完成通知 */
	public final static int TRANS_AUTHSALEOFF = EmvTransType.TRANS_AUTHSALEOFF;

	/** 预授权 */
	public final static int TRANS_PREAUTH = EmvTransType.TRANS_PREAUTH;

	/** 退货 */
	public final static int TRANS_REFUND = EmvTransType.TRANS_REFUND;
	
	/** 消费撤销 */
	public final static int TRANS_VOID_SALE = EmvTransType.TRANS_VOID_SALE;

	/** 预授权完成撤销 */
	public final static int TRANS_VOID_AUTHSALE = EmvTransType.TRANS_VOID_AUTHSALE;

	/** 预授权撤销 */
	public final static int TRANS_VOID_PREAUTH = EmvTransType.TRANS_VOID_PREAUTH;

	/** EMV脚本结果通知 */
	public final static int TRANS_EMV_SCRIPE = 14;

	/** EMV脱机退货 */
	public final static int TRANS_EMV_REFUND = EmvTransType.TRANS_EMV_REFUND;

	/** 电子现金消费 */
	public final static int TRANS_EC_PURCHASE = EmvTransType.TRANS_EC_PURCHASE;

	/** 电子现金圈存 指定帐户 */
	public final static int TRANS_EC_LOAD = EmvTransType.TRANS_EC_LOAD;

	/** 电子现金圈存现金 */
	public final static int TRANS_EC_LOAD_CASH = EmvTransType.TRANS_EC_LOAD_CASH;

	/** 电子现金圈存非指定账户 */
	public final static int TRANS_EC_LOAD_NOT_BIND = EmvTransType.TRANS_EC_LOAD_NOT_BIND;

	/** 电子现金圈存现金撤销 */
	public final static int TRANS_EC_VOID_LOAD_CASH = EmvTransType.TRANS_EC_VOID_LOAD_CASH;

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

	/** 批结算 */
	public final static int TRANS_SETTLE = 54;

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

	/** 圈存日志 */
	public final static int TRANS_LOAD_DETAIL = 62;
	
	/** 电子现金余额查询 */
	public final static int TRANS_EC_BALANCE = EmvTransType.TRANS_EC_BALANCE;
	
	/** 电子现金明细查询,不在交易过程中使用 */
	public final static int TRANS_EC_DETAIL = 64;
	
	/** 快速支付 ,不在交易过程中使用*/
	public final static int TRANS_QPBOC = 65;
	
	/** 交易上送 */
	public final static int TRANS_SENT_INFO = 52;
	
	/** 插卡消费*/
	public final static int TRANS_INSERT_CARD_SALE = 71;

	/** 重打印*/
	public final static int TRANS_REPRINT = 1002;

	/** 母pos下载*/
	public final static int TRANS_MPOS_DOWN = 9901;
	/** 密钥灌装到n900*/
	public final static int TRANS_LOAD_LOAD = 9902;
}
