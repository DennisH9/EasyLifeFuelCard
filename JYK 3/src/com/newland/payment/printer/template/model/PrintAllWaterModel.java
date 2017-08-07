package com.newland.payment.printer.template.model;

/**
 * 交易明细单&失败流水单model
 * @author lst
 * @date 20150519 
 */
public class PrintAllWaterModel {
	/**
	 * 是否有标题
	 */
	private String isTitleFlag;
	
	/**
	 * 是否有声明
	 */
	private String isDescript;
	/**
	 * 是否有解释
	 */
	private String isExplain;
	
	/**
	 * 是否结束
	 */
	private String isEnd;
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
	 * 授权码
	 */
	private String authNo;
	/**
	 * 失败上送明细/UNSUCC LIST
	 */
	private String unsucc;
	
	/**
	 * >上送被拒明细/DENIED LIST
	 */
	private String denied;
	
	/**
	 * 货币类型
	 */
	private String currency;
	
	
	public String getIsTitleFlag() {
		return isTitleFlag;
	}
	public void setIsTitleFlag(String isTitleFlag) {
		this.isTitleFlag = isTitleFlag;
	}
	public String getIsDescript() {
		return isDescript;
	}
	public void setIsDescript(String isDescript) {
		this.isDescript = isDescript;
	}
	public String getIsExplain() {
		return isExplain;
	}
	public void setIsExplain(String isExplain) {
		this.isExplain = isExplain;
	}
	public String getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
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
	public String getAuthNo() {
		return authNo;
	}
	public void setAuthNo(String authNo) {
		this.authNo = authNo;
	}
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
}
