package com.newland.payment.common;

/** 交易状态参数-断电保存
 * @author linld
 * @date 2015-05-23
 */
public class ParamsTrans {
	
	/**
	 * 签到状态,true-已签到,false-未签到
	 */
	public final static String PARAMS_FLAG_SIGN = "PARAMS_RUN_SIGN_FLAG";
	
	
	/**<报文头处理要求 */
	
	/**
	 * 参数传递,true-需要处理
	 */
	public final static String PARAMS_IS_PARAM_DOWN = "PARAMS_IS_PARAM_DOWN";
	/**
	 * 状态上送,true-需要处理
	 */
	public final static String PARAMS_IS_STATUS_SEND = "PARAMS_IS_STATUS_SEND";
	/**
	 * 重新签到,true-需要处理
	 */
	public final static String PARAMS_IS_RESIGN = "PARAMS_IS_RESIGN";

	/**
	 * IC公钥下载,true-需要处理
	 */
	public final static String PARAMS_IS_CAPK_DOWN = "PARAMS_IS_CAPK_DOWN";
	/**
	 * IC参数下载,true-需要处理
	 */	
	public final static String PARAMS_IS_AID_DOWN = "PARAMS_IS_AID_DOWN";
	/**
	 * TMS参数传递,true-需要处理
	 */
	public final static String PARAMS_IS_TMS_PARAM_DOWN = "PARAMS_IS_TMS_PARAM_DOWN";
	/**
	 * 黑名单下载,true-需要处理
	 */
	public final static String PARAMS_IS_BALACKLIST_DOWN = "PARAMS_IS_BALACKLIST_DOWN";
	/**
	 * 非接业务参数下载,true-需要处理
	 */
	public final static String PARAMS_IS_RF_PARAM_DOWN = "PARAMS_IS_RF_PARAM_DOWN";
	/**
	 * CardBinB更新,true-需要处理
	 */
	public final static String PARAMS_IS_CARDBIN_B_UPDATE = "PARAMS_IS_CARDBIN_B_UPDATE";
	/**
	 * CardBinC下载,true-需要处理
	 */
	public final static String PARAMS_IS_CARDBIN_C_DOWN = "PARAMS_IS_CARDBIN_C_DOWN";
	
	/**
	 * 结算时间
	 */
	public final static String PARAMS_SETTLE_TIME = "PARAMS_SETTLE_TIME";
	
	/**
	 * 内卡对账标志, "1"-对账平, "2"-对账不平, "3"-对账错
	 */
	public final static String PARAMS_FLAG_CN_ACCOUNT_CHECKING = "PARAMS_FLAG_CN_ACCOUNT_CHECKING";
	
	/**
	 * 外卡对账标志, "1"-对账平, "2"-对账不平, "3"-对账错
	 */
	public final static String PARAMS_FLAG_EN_ACCOUNT_CHECKING = "PARAMS_FLAG_EN_ACCOUNT_CHECKING";	
	

	/**
	 * 结算中断,总标志,true-需要处理
	 */
	public final static String PARAMS_IS_SETTLT_HALT = "PARAMS_IS_SETTLT_HALT";
	/**
	 * 批上送中断,true-需要处理
	 */
	public final static String PARAMS_IS_BATCHUP_HALT = "PARAMS_IS_BATCHUP_HALT";
	/**
	 * 打印结算单中断,true-需要处理
	 */
	public final static String PARAMS_IS_PRINT_SETTLE_HALT = "PARAMS_IS_PRINT_SETTLE_HALT";
	/**
	 * 打印明细中断,true-需要处理
	 */
	public final static String PARAMS_IS_PRINT_ALLWATER_HALT = "PARAMS_IS_PRINT_ALLWATER_HALT";
	/**
	 * 清除结算数据中断,true-需要处理
	 */
	public final static String PARAMS_IS_CLEAR_SETTLT_HLAT = "PARAMS_IS_CLEAR_SETTLT_HLAT";
	
	
	/**
	 *磁条离线类, 批上送中断,流水位置
	 */
	public final static String PARAMS_NUM_MAG_OFFLINE_HALT = "PARAMS_NUM_MAG_OFFLINE_HALT";
	/**
	 * IC卡成功的脱机交易, 批上送中断,流水位置
	 */
	public final static String PARAMS_NUM_EMV_OFFLINE_SUCC_HALT = "PARAMS_NUM_EMV_OFFLINE_HALT";
	/**
	 * 磁条联机,批上送中断,流水位置
	 */
	public final static String PARAMS_NUM_MAG_ONLINE_HALT = "PARAMS_NUM_MAG_ONLINE_HALT";
	/**
	 * 通知类交易,批上送中断,流水位置
	 */
	public final static String PARAMS_NUM_INFORM_HALT = "PARAMS_NUM_INFORM_HALT";
	/**
	 * IC卡成功的联机交易,批上送中断,流水位置
	 */
	public final static String PARAMS_NUM_EMV_ONLINE_SUCC_HALT = "PARAMS_NUM_EMV_ONLINE_HALT";
	/**
	 * IC卡失败的脱机交易,批上送中断,流水位置
	 */
	public final static String PARAMS_NUM_EMV_OFFLINE_FAIL_HALT = "PARAMS_NUM_MAG_OFFLINE_HALT";

	/**
	 * IC卡ARPC错但仍然承兑的联机交易,批上送中断,流水位置
	 */
	public final static String PARAMS_NUM_EMV_ONLINE_SUCC_ARPC_ERR_HALT = "PARAMS_NUM_EMV_ONLINE_SUCC_ARPC_ERR_HALT";
	
	/**
	 * 批上送,流水总笔数
	 */
	public final static String PARAMS_NUM_BATCHUP = "PARAMS_NUM_BATCHUP";
	
	/**
	 * 离线未上送笔数
	 */
	public final static String PARAMS_NUM_OFFLINE_UNSEND = "PARAMS_NUM_OFFLINE_UNSEND";
	
	
	
	/**
	 * 已冲正次数
	 */
	public final static String PARAMS_TIMES_REVERSAL_HAVE_SEND = "PARAMS_TIMES_REVERSAL_HAVE_SEND";
	
	/**
	 * 已离线上送次数
	 */
	public final static String PARAMS_TIMES_OFFLINE_HAVE_SEND = "PARAMS_TIMES_OFFLINE_HAVE_SEND";
	
	/**
	 * 离线上送中断,流水位置
	 */
	public final static String PARAMS_NUM_OFFLINE_SEND_HALT = "PARAMS_NUM_OFFLINE_SEND_HALT";
	
	/**
	 * 已上送脚本通知次数
	 */
	public final static String PARAMS_TIMES_SCRIPT_HAVE_SEND = "PARAMS_TIMES_SCRIPT_HAVE_SEND";
	
	/** EMV相关*/

	/**
	 * EMV交易序号
	 */
	public final static String PARAMS_EMV_TRANS_SERIAL = "PARAMS_EMV_TRANS_SERIAL";
	
	
	/** 电子签名相关*/
	/**
	 * 电子签名未上送笔数
	 */
	public final static String PARAMS_NUM_ELECSIGN_UNSEND = "PARAMS_NUM_ELECSIGN_UNSEND";
	/**
	 * 联机签名上送中断流水
	 */
	public final static String PARAMS_NUM_ELECSEND_ONLINE_HALT = "PARAMS_NUM_ELECSEND_ONLINE_HALT";
	/**
	 * 离线签名上送中断流水
	 */
	public final static String PARAMS_NUM_ELECSEND_OFFLINE_HALT = "PARAMS_NUM_ELECSEND_OFFLINE_HALT";

	/**
	 * 结算签名中断流水
	 */
	public final static String PARAMS_NUM_ELECSEND_SETTLE_HALT = "PARAMS_NUM_ELECSEND_ALL_HALT";
	
	/**
	 * 结算已上送签名次数
	 */
	public final static String PARAMS_TIMES_ELECSIGN_HAVE_SEND = "PARAMS_TIMES_ELECSIGN_HAVE_SEND";
	

}
