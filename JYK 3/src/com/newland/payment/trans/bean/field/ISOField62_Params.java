package com.newland.payment.trans.bean.field;

import java.io.UnsupportedEncodingException;

import com.newland.payment.trans.manage.ParamsHelper;

/**
 * 62域用法三,参数传递
 *
 * @author CB
 * @date 2015-5-21 
 * @time 上午12:04:43
 */
public class ISOField62_Params {

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
	/** 商户名称（中文简称或英文简称） */
	private String shopName;
	/** 交易重发次数 */
	private String transRetryTime;
	/** 主密钥 */
	private String mainKey;
	/** 支持的交易类型 */
	private String supportTransType;
	
	public ISOField62_Params(String data) throws UnsupportedEncodingException{
		//LoggerUtils.d("ISOField62_Params：" + data);
		
		ParamsHelper params = new ParamsHelper(data);

		posAppType = params.get(2);
		timeout = params.get(2);
		retryTime = params.get(1);
		transPhone1 = params.get(14);
		transPhone2 = params.get(14);
		transPhone3 = params.get(14);
		managePhone = params.get(14);
		isSupportTip = params.get(1);
		tipPercent = params.get(2);
		isSupportHandInputCardNo = params.get(1);
		isAutoSignOut = params.get(1);
		shopName = params.get(40);
		transRetryTime = params.get(1); 
//		params.get(1);//规范没有编码为24的值，挡板有，测试时过滤掉
		mainKey = params.get(1);
		supportTransType = params.get(4);
		 
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

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getTransRetryTime() {
		return transRetryTime;
	}

	public void setTransRetryTime(String transRetryTime) {
		this.transRetryTime = transRetryTime;
	}

	public String getMainKey() {
		return mainKey;
	}

	public void setMainKey(String mainKey) {
		this.mainKey = mainKey;
	}

	public String getSupportTransType() {
		return supportTransType;
	}

	public void setSupportTransType(String supportTransType) {
		this.supportTransType = supportTransType;
	}
	
}
