package com.newland.payment.mvc.dao;

import com.newland.payment.mvc.model.ReverseWater;

/**
 * 冲正流水的DAO
 * @author linchunhui
 * @date 2015年5月12日 下午5:48:34
 *
 */
public interface ReverseWaterDao {
	
	/**
	 * 取数据库中唯一的冲正流水
	 * @return
	 */
	public ReverseWater get();
	
	/**
	 * 添加一条记录
	 * @param reverseWater
	 * @return
	 */
	public long insert(ReverseWater reverseWater);
	
	/**
	 * 根据ID删除记录
	 * @param id
	 * @return
	 */
	public int delete(long id);
	
	/**
	 * 删除所有记录
	 * @return
	 */
	public int deleteAll();
	
	/**
	 * 还原表自增长序列号
	 */
	public void revertSeq();
	
	
	public int update(ReverseWater rw);
}
