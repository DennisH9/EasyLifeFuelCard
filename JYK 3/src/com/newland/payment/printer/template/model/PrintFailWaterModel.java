package com.newland.payment.printer.template.model;
/**
 * 失败流水单
 * @author lst
 * @date 20150519 
 */
public class PrintFailWaterModel {
	/**
	 * 失败上送明细
	 */
	private String unsucc;
	
	/**
	 * 上送被拒明细
	 */
	private String denied;
	
	/**
	 * 终端编号
	 */
	private String isDescript;
	
	/**
	 * 凭证号
	 */
	private String traceNo;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 卡号
	 */
	private String cardNo;
	
	/**
	 * 金额
	 */
	private String amount;
	
	/**
	 * 是否结束
	 */
	private String isEnd;

	public String getUnsucc() {
		return unsucc;
	}

	public void setUnsucc(String unsucc) {
		this.unsucc = unsucc;
	}

	public String getDenied() {
		return denied;
	}

	public void setDenied(String denied) {
		this.denied = denied;
	}

	public String getIsDescript() {
		return isDescript;
	}

	public void setIsDescript(String isDescript) {
		this.isDescript = isDescript;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}
	
	
	
}
