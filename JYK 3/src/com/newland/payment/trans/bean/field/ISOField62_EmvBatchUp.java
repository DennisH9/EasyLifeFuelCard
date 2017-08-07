package com.newland.payment.trans.bean.field;

/**
 * 批上送,IC卡联机成功明细62域用法6
 * @author linld
 * @date 2015-05-30
 */
public class ISOField62_EmvBatchUp {
	/**
	 * 用法标识
	 */
	private String use = "";
	
	/**
	 * 卡类别
	 */
	private String cardClass = "";
	/**
	 * 交易响应码
	 */
	private String respCode = "";
	/**
	 * 授权金额
	 */
	private String authAmt = "";
	/**
	 * 授权币种
	 */
	private String authCurrency = "";
	/**
	 * 应用密文类型
	 */
	private String acType = "";
	/**
	 * 卡片认证APPC的结果
	 */
	private String arpcRes = "";
	
	
	public ISOField62_EmvBatchUp(){
		use = "";
		cardClass = "";
		respCode = "";
		authAmt = "";
		authCurrency = "";
		acType = "";
		arpcRes = "";
	}
	
	public String getStringUsage6(){
		return use + cardClass + respCode + authAmt + authCurrency;
	}
	
	public String getStringUsage7(){
		return use + cardClass + respCode + authAmt + authCurrency + acType + arpcRes;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getCardClass() {
		return cardClass;
	}

	public void setCardClass(String cardClass) {
		this.cardClass = cardClass;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getAuthAmt() {
		return authAmt;
	}

	public void setAuthAmt(String authAmt) {
		this.authAmt = authAmt;
	}

	public String getAuthCurrency() {
		return authCurrency;
	}

	public void setAuthCurrency(String authCurrency) {
		this.authCurrency = authCurrency;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public String getArpcRes() {
		return arpcRes;
	}

	public void setArpcRes(String arpcRes) {
		this.arpcRes = arpcRes;
	}
	
	
}
