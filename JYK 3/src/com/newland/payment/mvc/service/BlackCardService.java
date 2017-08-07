package com.newland.payment.mvc.service;

import java.util.List;

import com.newland.payment.mvc.model.BlackCard;

/**
 * 黑名单操作类
 * @author chenkh
 * @date 2015-5-29
 * @time 下午10:47:30
 *
 */
public interface BlackCardService {
	
	public long addBlackCard(String cardBin);
	
	public List<BlackCard> findAll();
	
	public BlackCard findByCardBin(String cardBin);

	public long deleteAll();
	
	public long getCount();
}
