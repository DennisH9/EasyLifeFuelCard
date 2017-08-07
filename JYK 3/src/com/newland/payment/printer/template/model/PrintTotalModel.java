package com.newland.payment.printer.template.model;
/**
 * 交易汇总单
 * @author lst
 * @date 20150519 
 */
public class PrintTotalModel {

	
	/**
	 * 借记笔数（内卡）
	 */
	private String debitNum_nk;
	
	/**
	 * 借记金额（内卡）
	 */
	private String debitAmt_nk;
	
	/**
	 * 借记笔数（外卡）
	 */
	private String debitNum_wk;
	
	/**
	 * 借记金额（外卡）
	 */
	private String debitAmt_wk;

	/**
	 * 贷记笔数（内卡）
	 */
	private String creditNum_nk;
	
	/**
	 * 贷记金额（内卡）
	 */
	private String creditAmt_nk;
	

	
	public String getDebitNum_nk() {
		return debitNum_nk;
	}

	public void setDebitNum_nk(String debitNum_nk) {
		this.debitNum_nk = debitNum_nk;
	}

	public String getDebitAmt_nk() {
		return debitAmt_nk;
	}

	public void setDebitAmt_nk(String debitAmt_nk) {
		this.debitAmt_nk = debitAmt_nk;
	}

	public String getDebitNum_wk() {
		return debitNum_wk;
	}

	public void setDebitNum_wk(String debitNum_wk) {
		this.debitNum_wk = debitNum_wk;
	}

	public String getDebitAmt_wk() {
		return debitAmt_wk;
	}

	public void setDebitAmt_wk(String debitAmt_wk) {
		this.debitAmt_wk = debitAmt_wk;
	}

	public String getCreditNum_nk() {
		return creditNum_nk;
	}

	public void setCreditNum_nk(String creditNum_nk) {
		this.creditNum_nk = creditNum_nk;
	}

	public String getCreditAmt_nk() {
		return creditAmt_nk;
	}

	public void setCreditAmt_nk(String creditAmt_nk) {
		this.creditAmt_nk = creditAmt_nk;
	}

	public String getCreditNum_wk() {
		return creditNum_wk;
	}

	public void setCreditNum_wk(String creditNum_wk) {
		this.creditNum_wk = creditNum_wk;
	}

	public String getCreditAmt_wk() {
		return creditAmt_wk;
	}

	public void setCreditAmt_wk(String creditAmt_wk) {
		this.creditAmt_wk = creditAmt_wk;
	}

	/**
	 * 贷记笔数（外卡）
	 */
	private String creditNum_wk;
	
	/**
	 * 贷记金额（外卡）
	 */
	private String creditAmt_wk;
	
}
