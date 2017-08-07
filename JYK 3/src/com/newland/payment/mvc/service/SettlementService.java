package com.newland.payment.mvc.service;

/**
 * 结算数据处理
 * @author chenkh
 * @date 2015-5-23
 * @time 下午9:14:24
 */
public interface SettlementService {

	/**
	 * 通过类型查找该类型的内卡笔数
	 * @param type 结算类型
	 * @return
	 */
	public int getSettleNum_NK(String type);
	
	/**
	 * 通过类型查找该类型的内卡金额
	 * @param type 结算类型
	 * @return
	 */
	public long getSettleAmount_NK(String type);
	
	
	/**
	 * 通过类型查找该类型的外卡笔数
	 * @param type 结算类型
	 * @return
	 */
	public int getSettleNum_WK(String type);
	
	/**
	 * 通过类型查找该类型的外卡金额
	 * @param type 结算类型
	 * @return
	 */
	public long getSettleAmount_WK(String type);
	
	/**
	 * 增加内卡指定结算类型笔数
	 * @return
	 */
	public int addSettleNum_NK(String type, int num);
	/**
	 * 增加内卡指定结算类型金额
	 * @return
	 */
	public int addSettleAmount_NK(String type, long amount);
	/**
	 * 增加外卡指定结算类型笔数
	 * @return
	 */
	public int addSettleNum_WK(String type, int num);
	/**
	 * 增加外卡指定结算类型金额
	 * @return
	 */
	public int addSettleAmount_WK(String type, long amount);
	
	/**
	 * 重置结算数据表
	 * @return
	 */
	public void clearSettlement();
	
}
