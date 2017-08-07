package com.newland.payment.mvc.dao;

import java.util.List;

import com.newland.payment.mvc.model.Water;

public interface WaterDao {
	public long insert(Water water);
	
	public int update(Water water);
	
	
	public Water findById(long id);
	
	public List<Water> findAll();
	
	public Water findLastWater();
	
	public List<Water> findByPage(int pageNo, int pageSize);
	
	/**
	 * 根据属性查找记录
	 * @param propertyName 属性名称
	 * @param value 属性取值
	 * @return 
	 */
	public List<Water> findByColumnName(String propertyName, Object value);
	
	
	/**
	 * 删除所有记录
	 * @return
	 */
	public int deleteAll();
	
	public int deleteById(long id);
	
	/**
	 * 还原表自增长序列号
	 */
	public void revertSeq();
	
	
	public int getCount();
	
	/**
	 * 根据交易类型和交易状态获取流水
	 * @param transType
	 * @param transStatus
	 * @return
	 */
	public List<Water> findByTransTypeAndTransStatus(int transType, int transStatus);
	
	/**
	 * 通过流水号和时间查询交易记录
	 * @param traceNo 流水号
	 * @param time 交易时间
	 * @return
	 */
	public Water findByTraceNoAndTime(String traceNo, String time);
	
}
