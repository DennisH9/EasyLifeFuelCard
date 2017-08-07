package com.newland.payment.common;


public class TransConst {

	/**
	 * EMV参数下载类型
	 * @version 1.0
	 * @author spy
	 * @date 2015年5月27日
	 * @time 下午8:28:18
	 */
	public static enum DownloadEmvParamType{
		AID,
		CAPK,
	}
	/**
	 * Water交易状态
	 * @version 1.0
	 * @author spy
	 * @date 2015年5月21日
	 * @time 上午9:24:01
	 */
	public static class TransStatus{
		/**
		 * 交易状态
		 * <li>0-初始状态</li>
		 * <li>1-已撤销</li>
		 * <li>2-已调整</li>
		 * <li>3-已退货</li>
		 * <li>4-上送后被调整</li>
		 * 
		 */
		/**正常*/
		public static final int NORMAL = 0;
		/**已撤销*/
		public static final int REV = 1;
		/**已调整*/
		public static final int ADJUST = 2;
		/**已退货*/
		public static final int RETURN = 3;
		/**上送后被调整*/
		public static final int SEND_AND_ADJ = 4;
	}
	
	/**
	 * 上送状态(离线上送与批上送通用)
	 * @author linld
	 */
	
	public static class SendStatus{
		
		/**正常,未上送0*/
		public static final int NORMAL = 0;
		/**已上送,上送成功0xFD*/
		public static final int SUCC = 0xFD;
		/**上送被拒-0xFE*/
		public static final int DECLINE = 0xFE;
		/**上送失败-0xFF*/
		public static final int FAIL = 0xFF;

	}
	
	/**
	 * 对账情况
	 * @author linld
	 */
	public static class AccountStatus{
		/**对账平*/
		public static final String EQUAL = "1";
		/**对账不平*/
		public static final String UNEQUAL = "2";
		/**对账错*/
		public static final String ERROR = "3";
		
	}
	
	
	public static class EmvStatus{
		/**
		 * EMV交易状态
		 */
		/** 脱机失败*/
		public static final int EMV_STATUS_OFFLINE_FAIL = 1;
		/** 脱机成功*/
		public static final int	EMV_STATUS_OFFLINE_SUCC = 2;
		/** 联机失败*/
		public static final int	EMV_STATUS_ONLINE_FAIL = 3;
		/** 联机成功*/
		public static final int	EMV_STATUS_ONLINE_SUCC = 4;
	}
	
	/**
	 * 读卡bean对象默认常量定义
	 * @author chenkh
	 * @date 2015-5-18
	 * @time 下午3:55:59
	 *
	 */
	public static class CardBeanConst {
		/**
		 * 优先磁道，默认2磁
		 */
		public static final int MAIN_TRACK = Maintk.TK2;
		/**
		 * 手输最小长度
		 */
		public static final int INPUT_MIN_LEN = 13;
		/**
		 * 手输最大长度
		 */
		public static final int INPUT_MAX_LEN = 19;
		/**
		 * 超时时间,单位秒
		 */
		public static final int TIME_OUT = 60;
		/**
		 * 是否支持功能键,0不支持(默认)
		 */
		public static final int FUN_KEY_FLAG = 0;
		
		/**
		 * 是否支持卡号确认
		 */
		public static final boolean COMFRIM_FLAG = true;
		
		/**
		 * 是否支持ic卡刷卡
		 */
		public static final boolean ALLOW_ICCARD_SWIPED = true;
	}
	
	
	
	
	/**
	 * POS最大交易限额,2亿
	 * 超过限额提示结算
	 * @author linld
	 */
	public static final long POS_MAX_AMOUNT = 20000000000L;
	
	
	/**
	 * 消息提示bean对象常量定义
	 * @author chenkh
	 * @date 2015-5-18
	 * @time 下午4:06:01
	 *
	 */
	public static class MessageBeanConst {
		/** 超时时间 */
		public static final int TIME_OUT = 60;
	}
	
	/**
	 * 输日期bean对象常量定义
	 * @author chenkh
	 * @date 2015-5-18
	 * @time 下午4:06:01
	 *
	 */
	public static class DateBeanConst {
		/** 超时时间 */
		public static final int TIME_OUT = 60;
	}
	/**
	 * 输入信息bean对象常量定义
	 * @author chenkh
	 * @date 2015-5-18
	 * @time 下午4:06:01
	 *
	 */
	public static class InputInfoBeanConst {
		/** 是否允许为空 false-不可为空，true-可以为空， 默认不允空*/
		public static final boolean EMPTY_FLAG = false;
		/** 超时时间 */
		public static final int TIME_OUT = 60;
	}
	
	/**
	 * 菜单选择bean对象常量定义
	 * @author chenkh
	 * @date 2015-5-18
	 * @time 下午4:06:01
	 *
	 */
	public static class MenuSelectBeanConst {
		/** 超时时间 */
		public static final int TIME_OUT = 60;
	}
	
	public static class CommonBeanConst{
		/** 超时时间 */
		public static final int TIME_OUT = 60;
	}
	
	public static class Maintk{
		/** 磁道1 **/
		public static final int TK1 = 1;
		/** 磁道2 **/
		public static final int TK2 = 2;
		/** 磁道3 **/
		public static final int TK3 = 3;
	}

	public static class ReadcardType {
		/** 未指定*/
		public static final int INPUT_NO = 0x00;	
		
		/** 手输卡号 **/
		public static final int HAND_INPUT = 0x01;
		/** 刷卡 **/
		public static final int SWIPE = 0x02;
		/** 插卡 **/
		public static final int ICCARD = 0x04;
		/** 挥卡 **/
		public static final int RFCARD = 0x08;
		
		/** 转出卡**/
		public static final int SWIPE_OUT = 0x0100;
		/** 转入卡**/
		public static final int SWIPE_IN = 0x0200;
		/** 关闭IC卡刷卡检测标志**/
		public static final int ALLOW_IC_SWIPE = 0x0400;
		
	}
	
	public static class FuncKeyFlag{
		/** 支持 **/
		public static final int SUPPORT = 1;
		/** 不支持 **/
		public static final int NOT_SUPPORT = 0;
	}
	
	
	
	
	/**
	 * 冲正原因码
	 * @author chenkh
	 * @date 2015-5-23
	 * @time 下午8:24:27
	 */
	public static class ReverseReasonCode {
		
		/** POS 终端在时限内未能收到 POS 中心的应答 */ 
		public final static String NO_ANSWER = "98";
		
		/** POS 终端收到 POS 中心的批准应答消息，但由于 POS 机故障无法完成交易 */
		public final static String DEVICE_ERROR = "96";
		
		/** POS 终端对收到 POS 中心的批准应答消息，验证 MAC 出错 */
		public final static String MAC_ERROR = "A0";
		
		/** 其他情况引发的冲正 */
		public final static String OTHER_REASON = "06";
	}
	
	/**
	 * 结算表汇总类型定义
	 * @author chenkh
	 * @date 2015-5-23
	 * @time 下午8:28:21
	 *
	 */
	public static class SettlementTableTypeConst {
		/**
		 * 消费
		 */
		public final static String SALE = "sale";
		/**
		 * 消费撤销
		 */
		public final static String VOID_SALE = "void_sale";		
		/**
		 * 预授权完成（联机）
		 */
		public final static String AUTH_SALE = "auth_sale";
		/**
		 * 预授权完成（离线）
		 */
		public final static String AUTH_SALE_OFF = "auth_sale_off";
		/**
		 * 撤销预授权完成（联机）
		 */
		public final static String VOID_AUTH_SALE = "void_auth_sale";
		/**
		 * 退货
		 */
		public final static String REFUND = "refund";
		/**
		 * 离线结算
		 */
		public final static String OFFLINE = "offline";
		/**
		 * 结算调整
		 */
		public final static String ADJUST = "adjust";
		/**
		 * 基于PBOC借/贷记标准的小额支付脱机消费
		 */
		public final static String EC_SALE = "ec_sale";
		/**
		 * 基于PBOC借贷记卡标准的离线交易退货
		 */
		public final static String EMV_REFUND = "emv_refund";
		/**
		 * 现金充值圈存
		 */
		public final static String CASH_ECLOAD = "cash_ecload";
		/**
		 * 基于PBOC借贷记卡标准的现金充值撤销
		 */
		public final static String VOID_CASH_ECLOAD = "void_cash_ecload";
		/**
		 * 非指定帐户转帐圈存
		 */
		public final static String NOT_BIN_ECLOAD = "not_bin_ecload";
		/**
		 * 磁条卡现金充值
		 */
		public final static String MAG_CASH_LOAD = "mag_cash_load";
	}
	
	/** 流程步骤最后一步 */
	public static final int STEP_FINAL = 110;
	
	/** 货币代码*/
	public static final String CURRENCY_CODE = "156";
	
	
	public static class TransDeal{
		/**
		 * 交易正常处理
		 */
		public final static int NORMAL = 0;
		
		/**
		 * 交易特殊处理
		 */
		public final static int SPECIAL = 1;

	}
	
	
	
	
}
