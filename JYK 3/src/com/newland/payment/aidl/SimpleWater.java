package com.newland.payment.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import com.newland.payment.mvc.model.Water;

public class SimpleWater implements Parcelable {

	/**
	 * 交易类型，参考 {@link com.newland.payment.common.TransType}中的类型定义
	 */
	private Integer transType;
	/**
	 * 主账号
	 */
	private String pan;
	/**
	 * 金额
	 */
	private Long amount;
	/**
	 * 币种（国际代码）
	 */
	private String currency;
	/**
	 * 交易流水
	 */
	private String trace;
	/**
	 * 交易时间，格式：
	 * <li>hhmmss</li>
	 */
	private String time;
	/**
	 * 交易日期, 格式：
	 * <li>yyyymmdd</li>
	 * <li>yymmdd</li>
	 * <li>mmdd</li>
	 */
	private String date;
	/**
	 * 38.授权码
	 */
	private String authCode;
	/**
	 * 系统参考号
	 */
	private String referNum;
	
	/**
	 * 批次号
	 */
	private String batchNo;

	/**
	 * 交易结构码
	 */
	private Integer transCode;

	public Integer getTransCode() {
		return transCode;
	}

	public void setTransCode(Integer transCode) {
		this.transCode = transCode;
	}

	public SimpleWater() {
	}

	public SimpleWater(Parcel source) {
		readFromParcel(source);
	}

	public static final Parcelable.Creator<SimpleWater> CREATOR = new Parcelable.Creator<SimpleWater>(){

		@Override
		public SimpleWater createFromParcel(Parcel source) {
			return new SimpleWater(source);
		}

		@Override
		public SimpleWater[] newArray(int size) {
			return new SimpleWater[size];
		}
		 
	 };
			 
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(transType);
		dest.writeValue(pan);
		dest.writeValue(amount);
		dest.writeValue(currency);
		dest.writeValue(trace);
		dest.writeValue(time);
		dest.writeValue(date);
		dest.writeValue(authCode);
		dest.writeValue(referNum);
		
		dest.writeValue(batchNo);

		dest.writeValue(transCode);
	}

	public void readFromParcel(Parcel source) {
		transType = (Integer)source.readValue(null);
		pan = (String)source.readValue(null);
		amount = (Long)source.readValue(null);
		currency = (String)source.readValue(null);
		trace = (String)source.readValue(null);
		time = (String)source.readValue(null);
		date = (String)source.readValue(null);
		authCode = (String)source.readValue(null);
		referNum = (String)source.readValue(null);
		
		batchNo = (String)source.readValue(null);

		transCode = (Integer)source.readValue(null);
	}

	public Integer getTransType() {
		return transType;
	}

	public void setTransType(Integer transType) {
		this.transType = transType;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getReferNum() {
		return referNum;
	}

	public void setReferNum(String referNum) {
		this.referNum = referNum;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public static SimpleWater waterToSimpleWater(Water water, SimpleWater simpleWater){
		simpleWater.transType = water.getTransType();
		simpleWater.pan = water.getPan();
		simpleWater.amount = water.getAmount();
		simpleWater.currency = water.getCurrency();
		simpleWater.trace = water.getTrace();
		simpleWater.time = water.getTime();
		simpleWater.date = water.getDate();
		simpleWater.authCode = water.getAuthCode();
		simpleWater.referNum = water.getReferNum();
		
		simpleWater.batchNo=water.getBatchNum();
		simpleWater.transCode=water.getTransCode();
		return simpleWater;
	}
}
