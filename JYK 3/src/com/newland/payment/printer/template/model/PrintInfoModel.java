package com.newland.payment.printer.template.model;
/**
 * 打印商户与应用信息
 * 
 * @author lst
 * @date 20150519 
 */
public class PrintInfoModel {

	
	/**
	 * 是否打印商户参数
	 */
	private String isPrintShopParam;
	/**
	 * 商户名称
	 */
	private String merchantName;
	

	/**
	 * 商户编号
	 */
	private String shopId;
	
	/**
	 * 终端编号
	 */
	private String terminalId;
	
	/**
	 * 应用名
	 */
	private String appName;
	
	/**
	 * TMS下发广告信息
	 */
	private String adsInfo;
	
	/**
	 * 是否打印版本参数
	 */
	private String isPrintVer;
	
	
	/**
	 * 程序版本号
	 */
	private String appVer;
	

	public String getIsPrintShopParam() {
		return isPrintShopParam;
	}


	public void setIsPrintShopParam(String isPrintShopParam) {
		this.isPrintShopParam = isPrintShopParam;
	}


	public String getMerchantName() {
		return merchantName;
	}


	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}


	public String getShopId() {
		return shopId;
	}


	public void setShopId(String shopId) {
		this.shopId = shopId;
	}


	public String getTerminalId() {
		return terminalId;
	}


	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getAdsInfo() {
		return adsInfo;
	}


	public void setAdsInfo(String adsInfo) {
		this.adsInfo = adsInfo;
	}


	public String getIsPrintVer() {
		return isPrintVer;
	}


	public void setIsPrintVer(String isPrintVer) {
		this.isPrintVer = isPrintVer;
	}


	public String getAppVer() {
		return appVer;
	}


	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}

}
