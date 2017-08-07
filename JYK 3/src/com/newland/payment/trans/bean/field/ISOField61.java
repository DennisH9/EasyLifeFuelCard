package com.newland.payment.trans.bean.field;


public class ISOField61 {
	/**
	 * 61.1 原批次号N6
	 */
	private String oldBatchNum;

	/**
	 * 61.2 原流水号N6
	 */
	private String oldTrace;

	/**
	 * 61.3 原交易日期N4
	 */
	private String oldDate;

	/**
	 * 61.4 原交易授权方式 N2
	 */
	private String oldAuthWay;

	/**
	 * 61.5 原交易授权机构代码 N11
	 */
	private String oldAuthOrgCode;

	public ISOField61() {
		oldBatchNum = "";
		oldTrace = "";
		oldDate = "";
		oldAuthWay = "";
		oldAuthOrgCode = "";
	}
	
	public ISOField61(String info){
		oldBatchNum = "";
		oldTrace = "";
		oldDate = "";
		oldAuthWay = "";
		oldAuthOrgCode = "";
		
		int len = info.length();
		if (len >=6){
			oldBatchNum = info.substring(0, 6);
		}
		
		if (len >= 12){
			oldTrace = info.substring(6, 12);
		}
		
		if (len >= 16){
			oldDate = info.substring(12, 16);
		}
		
		if (len >= 18){
			oldAuthWay = info.substring(16, 18);
		}
		
		if (len >= 29){
			oldAuthOrgCode = info.substring(18, 29);
		}
	}
	
	public String getOldBatchNum() {
		return oldBatchNum;
	}

	public void setOldBatchNum(String oldBatchNum) {
		this.oldBatchNum = oldBatchNum;
	}

	public String getOldTrace() {
		return oldTrace;
	}

	public void setOldTrace(String oldTrace) {
		this.oldTrace = oldTrace;
	}

	public String getOldDate() {
		return oldDate;
	}

	public void setOldDate(String oldDate) {
		this.oldDate = oldDate;
	}

	public String getOldAuthWay() {
		return oldAuthWay;
	}

	public void setOldAuthWay(String oldAuthWay) {
		this.oldAuthWay = oldAuthWay;
	}

	public String getOldAuthOrgCode() {
		return oldAuthOrgCode;
	}

	public void setOldAuthOrgCode(String oldAuthOrgCode) {
		this.oldAuthOrgCode = oldAuthOrgCode;
	}

	public String getString(){
		return oldBatchNum + oldTrace + oldDate + oldAuthWay + oldAuthOrgCode;
	}
}