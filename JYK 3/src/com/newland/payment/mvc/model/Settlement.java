package com.newland.payment.mvc.model;

import com.newland.base.dao.ann.Column;
import com.newland.base.dao.ann.Table;

/**
 * 结算数据表
 * @author chenkh
 * @date 2015-5-23
 * @time 下午8:17:47
 */
@Table(name = "T_SETTLEMENT")
public class Settlement {
	@Column(name = "ID", primaryKey = true)
	private Long id;
	
	/**
	 * 结算累计类型,参考 {@link com.newland.payment.common.TransConst.SettlementTableTypeConst}中的类型定义
	 */
	@Column(name = "TYPE")
	private String type;
	
	/**
	 * 卡别（0-内卡，1-外卡）
	 */
	@Column(name = "ORGANIZATION")
	private Integer organization;
	
	/**
	 * 金额
	 */
	@Column(name = "AMOUNT")
	private Long amount;
	
	
	/**
	 * 笔数
	 */
	@Column(name = "NUM")
	private Integer num;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getOrganization() {
		return organization;
	}

	public void setOrganization(Integer organization) {
		this.organization = organization;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
}
