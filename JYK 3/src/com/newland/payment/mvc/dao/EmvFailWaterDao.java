package com.newland.payment.mvc.dao;

import java.util.List;

import com.newland.payment.mvc.model.EmvFailWater;


public interface EmvFailWaterDao {
	
	public long insert(EmvFailWater emvFailWater);
	
	public int update(EmvFailWater emvFailWater);
	
	public int deleteAll();
	
	public void revertSeq();
	
	public EmvFailWater findById(long id);
	
	/**
	 * 获取失败流水数量
	 * @return
	 */
	public int getCount();
	
	/**
	 * 返回所有流水
	 * @return
	 */
	public List<EmvFailWater> findAll();
}
