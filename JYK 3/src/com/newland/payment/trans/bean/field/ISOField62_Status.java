package com.newland.payment.trans.bean.field;

/**
 * 62域状态上传
 *
 * @author CB
 * @date 2015-5-21 
 * @time 下午12:07:49
 */
public class ISOField62_Status {
	/** 键盘状态 N1 1 —正常， 0—不正常 */
	private String statusKeyboard;
	/** 密码键盘状态 N1 1 —正常， 0—不正常 */
	private String statusKeyboardPassword;
	/** 读卡器状态 */
	private String statusCardReader;
	/** 打印机状态 */
	private String statusPrint;
	/** 显示器状态 */
	private String statusDisplay;
	/** POS 终端应用类型 */
	private String posAppType;
	/** 超时时间 */
	private String timeout;
	/** 重试次数 */
	private String retryTime;
	/** 三个交易电话号码之一 */
	private String transPhone1;
	/** 三个交易电话号码之二 */
	private String transPhone2;
	/** 三个交易电话号码之三 */
	private String transPhone3;
	/** 一个管理电话号码 */
	private String managePhone;
	/** 是否支持小费 */
	private String isSupportTip;
	/** 小费百分比 */
	private String tipPercent;
	/** 是否支持手工输入卡号 */
	private String isSupportHandInputCardNo;
	/** 是否自动签退 */
	private String isAutoSignOut;
	/** 交易重发次数 */
	private String transRetryTime;
	/** 主密钥 */
	private String mainKeyIndex;
	/** 满足自动上送的累计笔数 */
	private String autoUploadTotal;
	/** 拨通率 */
	private String dialPercent;
	public String getStatusKeyboard() {
		return statusKeyboard;
	}
	public void setStatusKeyboard(String statusKeyboard) {
		this.statusKeyboard = statusKeyboard;
	}
	public String getStatusKeyboardPassword() {
		return statusKeyboardPassword;
	}
	public void setStatusKeyboardPassword(String statusKeyboardPassword) {
		this.statusKeyboardPassword = statusKeyboardPassword;
	}
	public String getStatusCardReader() {
		return statusCardReader;
	}
	public void setStatusCardReader(String statusCardReader) {
		this.statusCardReader = statusCardReader;
	}
	public String getStatusPrint() {
		return statusPrint;
	}
	public void setStatusPrint(String statusPrint) {
		this.statusPrint = statusPrint;
	}
	public String getStatusDisplay() {
		return statusDisplay;
	}
	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
	}
	public String getPosAppType() {
		return posAppType;
	}
	public void setPosAppType(String posAppType) {
		this.posAppType = posAppType;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	public String getRetryTime() {
		return retryTime;
	}
	public void setRetryTime(String retryTime) {
		this.retryTime = retryTime;
	}
	public String getTransPhone1() {
		return transPhone1;
	}
	public void setTransPhone1(String transPhone1) {
		this.transPhone1 = transPhone1;
	}
	public String getTransPhone2() {
		return transPhone2;
	}
	public void setTransPhone2(String transPhone2) {
		this.transPhone2 = transPhone2;
	}
	public String getTransPhone3() {
		return transPhone3;
	}
	public void setTransPhone3(String transPhone3) {
		this.transPhone3 = transPhone3;
	}
	public String getManagePhone() {
		return managePhone;
	}
	public void setManagePhone(String managePhone) {
		this.managePhone = managePhone;
	}
	public String getIsSupportTip() {
		return isSupportTip;
	}
	public void setIsSupportTip(String isSupportTip) {
		this.isSupportTip = isSupportTip;
	}
	public String getTipPercent() {
		return tipPercent;
	}
	public void setTipPercent(String tipPercent) {
		this.tipPercent = tipPercent;
	}
	public String getIsSupportHandInputCardNo() {
		return isSupportHandInputCardNo;
	}
	public void setIsSupportHandInputCardNo(String isSupportHandInputCardNo) {
		this.isSupportHandInputCardNo = isSupportHandInputCardNo;
	}
	public String getIsAutoSignOut() {
		return isAutoSignOut;
	}
	public void setIsAutoSignOut(String isAutoSignOut) {
		this.isAutoSignOut = isAutoSignOut;
	}
	public String getTransRetryTime() {
		return transRetryTime;
	}
	public void setTransRetryTime(String transRetryTime) {
		this.transRetryTime = transRetryTime;
	}
	public String getMainKeyIndex() {
		return mainKeyIndex;
	}
	public void setMainKeyIndex(String mainKeyIndex) {
		this.mainKeyIndex = mainKeyIndex;
	}
	public String getAutoUploadTotal() {
		return autoUploadTotal;
	}
	public void setAutoUploadTotal(String autoUploadTotal) {
		this.autoUploadTotal = autoUploadTotal;
	}
	public String getDialPercent() {
		return dialPercent;
	}
	public void setDialPercent(String dialPercent) {
		this.dialPercent = dialPercent;
	}
	@Override
	public String toString() {
		return "01" + statusKeyboard
				+"02" + statusKeyboardPassword 
				+ "03" + statusCardReader
				+ "04" + statusPrint
				+ "05" + statusDisplay
				+ "11" + posAppType 
				+ "12" + timeout 
				+ "13" + retryTime 
				+ "14" + transPhone1 
				+ "15" + transPhone2 
				+ "16" + transPhone3 
				+ "17" + managePhone 
				+ "18" + isSupportTip 
				+ "19" + tipPercent 
				+ "20" + isSupportHandInputCardNo 
				+ "21" + isAutoSignOut 
				+ "23" + transRetryTime 
				+ "25" + mainKeyIndex 
				+ "27" + autoUploadTotal 
				+ "51" + dialPercent;
	}
 
	
	
}
