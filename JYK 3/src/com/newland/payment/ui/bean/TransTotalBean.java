package com.newland.payment.ui.bean;

import java.io.Serializable;

/**
 * 交易统计类
 *
 * @author CB
 * @date 2015-5-16 
 * @time 下午5:22:02
 */
@SuppressWarnings("serial")
public class TransTotalBean implements Serializable{
	
	/** 标题 */
	private int title;
	/** 笔数 */
	private Integer count;
	/** 金额 */
	private Long amount;
	

	public int getTitle() {
		return title;
	}
	public void setTitle(int title) {
		this.title = title;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	
	
}
