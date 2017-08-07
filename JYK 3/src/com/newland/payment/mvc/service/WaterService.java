package com.newland.payment.mvc.service;

import java.util.List;

import com.newland.payment.mvc.DuplicatedTraceException;
import com.newland.payment.mvc.model.Water;

/**
 * 流水处理
 * @author linchunhui
 * @date 2015年5月12日 下午12:09:17
 *
 */
public interface WaterService {
	
	/**
	 * 添加一个新流水
	 * @param water
	 * @return
	 */
	public long addWater(Water water) throws DuplicatedTraceException;
	
	/**
	 * 更新一个流水
	 * @param water
	 * @return
	 */
	public int updateWater(Water water);
	
	/**
	 * 根据主键进行删除记录
	 * @param id 主键唯一ID
	 * @return
	 */
	public int deleteById(long id);
	
	
	/**
	 * 根据流水号进行删除记录
	 * @param trace 流水号
	 * @return
	 */
	public int deleteByTrace(String trace);
	
	
	/**
	 * 根据POS流水号查找记录
	 * @param trace POS流水号
	 * @return
	 */
	public Water findByTrace(String trace);
	
	/**
	 * 根据参考号查找记录
	 * @param referNum 参考号
	 * @return
	 */
	public Water findByReferNum(String referNum);
	
	/**
	 * 根据索引查找流水记录
	 * @param index
	 * @return
	 */
	public Water findById(long id);
	
	/**
	 * 获取最后一条流水记录
	 * @return
	 */
	public Water findLastWater();

	/**
	 * 获取流水数量
	 * @return
	 */
	public int getWaterCount();

	/**
	 * 翻页查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Water> findByPage(int pageNo, int pageSize);
	

	/**
	 * 通过流水号模糊查询流水对象
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Water> findAllLikeTrace(String trace);
	
	/**
	 * 查找所有流水
	 */
	public void findAll();
	
	/**
	 * 根据交易类型和交易状态获取流水
	 * @param transType
	 * @param transStatus
	 * @return
	 */
	public List<Water> findByTransTypeAndTransStatus(int transType, int transStatus);
	
	/**
	 * 重置流水
	 */
	public void clearWater();
	
	/**
	 * 根据POS流水号以及交易时间查找记录
	 * @param traceNo POS流水号
	 * @param time 交易时间
	 * @return
	 */
	public Water findByTrace(String traceNo, String time);
	
	/**
	 * 根据外部订单号查找记录
	 * @param traceNo POS流水号
	 * @param time 交易时间
	 * @return
	 */
	public Water findByOutOrderNo(String outOrderNo);
}
