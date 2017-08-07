package com.newland.payment.mvc.dao;

import com.newland.payment.mvc.model.Settlement;

/**
 * 结算数据处理Dao
 * @author chenkh
 * @date 2015-5-23
 * @time 下午11:51:52
 */
public interface SettlementDao {
	/**
	 * 通过结算类型和卡别查找结算对象
	 * @return
	 */
	public Settlement findSettlementByTypeAndOrganization(String type, int organization);
	
	/**
	 * 更新结算对象
	 * @param settlement
	 * @return
	 */
	public int update(Settlement settlement);
	
	public int deleteAll();
}
