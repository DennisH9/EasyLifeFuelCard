package com.newland.payment.mvc.service;

import java.util.List;

import com.newland.payment.mvc.model.EmvFailWater;

public interface EmvFailWaterService {

	public long addEmvFailWater(EmvFailWater emvFailWater);
	
	public int updateEmvFailWater(EmvFailWater emvFailWater);
	
	public EmvFailWater findEmvFailWater(long id);
	
	public void clearEmvFailWater();
	
	/**
	 * 获取流水数量
	 */
	public int getCount();
	
	/**
	 * 返回所有流水
	 * @return
	 */
	public List<EmvFailWater> findAll();
}
