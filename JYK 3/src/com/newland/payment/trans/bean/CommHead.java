package com.newland.payment.trans.bean;

import android.content.Context;

import com.newland.pos.sdk.util.LoggerUtils;

public class CommHead {

	/**
	 * 应用类别
	 */
	private String appType;
	
	/**
	 * 软件总版本号
	 */
	private String softVer;
	
	/**
	 * 终端状态
	 */
	private String status;
	
	/**
	 * 处理要求
	 * '0'-无处理要求
	 * '1'-下传终端参数
	 * '2'-上传终端状态信息
	 * '3'-重新签到
	 * '4'-通知终端发起更新公钥信息操作
	 * '5'-下载终端IC卡参数
	 * '6'-TMS参数下载
	 */
	private String response;
	
	/**
	 * 软件分版本号
	 */
	private String other;
	
	
	
	public CommHead(Context context){
		appType = "61";
		softVer = "32";
		status = "0";
		response = "0";
		other = "320001";
	}
	
	
	public String getString(){
		LoggerUtils.d("请求报文头:" + appType + softVer + status + response + other);
		return appType + softVer + status + response + other;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getSoftVer() {
		return softVer;
	}

	public void setSoftVer(String softVer) {
		this.softVer = softVer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}
	
	
	
}
