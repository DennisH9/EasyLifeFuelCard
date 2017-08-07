package com.newland.payment.mvc.model;

import com.newland.base.dao.ann.Column;
import com.newland.base.dao.ann.Table;


@Table(name="T_Card_BIN_A")
public class CardBinA {
	
	@Column(name = "ID", primaryKey = true)
	private Integer id;

	/**
	 * 卡bin
	 */
	@Column(name = "CARD_BIN", unique = true)
	private String cardBin;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCardBin() {
		return cardBin;
	}

	public void setCardBin(String cardBin) {
		this.cardBin = cardBin;
	}
}
