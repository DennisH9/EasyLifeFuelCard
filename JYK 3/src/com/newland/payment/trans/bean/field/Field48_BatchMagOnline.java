package com.newland.payment.trans.bean.field;

public class Field48_BatchMagOnline {
	/** 交易笔数*/
	private int num;
	/**
	 * 交易明细 
	 */
	/** 卡类别*/
	private String cardType;
	/** 交易流水号*/
	private String trace;
	/** 卡号*/
	private String pan;
	/** 金额*/
	private String amount;
	
	/** 上送48域*/
	private String field48;
	
	public Field48_BatchMagOnline(){
		num = 0;
		cardType = "";
		trace = "";
		pan = "";
		amount = "";
		field48 = "";
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getField48() {
		return field48;
	}

	public void setField48(String field48) {
		this.field48 = field48;
	}

	public void add(){
		num++;
		field48 += cardType + trace + pan + amount;
	}
	
	public String getString(){
		return String.format("%02d", num) + field48;
	}
	
}
