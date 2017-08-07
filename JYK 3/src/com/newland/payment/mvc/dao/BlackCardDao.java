package com.newland.payment.mvc.dao;

import java.util.List;

import com.newland.payment.mvc.model.BlackCard;

public interface BlackCardDao {

	/**
	 * 插入一条黑名单
	 * @return
	 */
	public long insert(BlackCard blackCard);
	
	/**
	 * 查询所有
	 * @return
	 */
	public List<BlackCard> findAll();
	
	/**
	 * 查询一条
	 * @return
	 */
	public BlackCard findByCardBin(String cardBin);
	
	/**
	 * 删除所有黑名单
	 * @return
	 */
	public int deleteAll();
	
	/**
	 * 重置id从1开始
	 * @return
	 */
	public void revertSeq();
	
	/**
	 * 获取黑名单数量
	 * @return
	 */
	public int getCount();
}
