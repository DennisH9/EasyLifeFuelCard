package com.newland.payment.mvc.model;

import java.io.Serializable;

import com.newland.base.dao.ann.Column;
import com.newland.base.dao.ann.Table;
import com.newland.payment.trans.bean.field.ISOField60;

/**
 * 脚本结果通知数据
 * @author linchunhui
 * @date 2015年5月12日 下午7:10:02
 *
 */
@Table(name = "T_SCRIPT_RESULT")
public class ScriptResult implements Serializable {
	private static final long serialVersionUID = 2665097183428387336L;

	@Column(name = "ID", primaryKey = true)
	private Long id;
	
	/**
	 * 交易类型，参考 {@link com.newland.payment.common.TransType}中的类型定义
	 */
	@Column(name = "TRANS_TYPE")
	private Integer transType;
	
	/**
	 * 交易属性，取值参考 {@link com.newland.payment.common.TransConst.TransAttr}的定义
	 */
	@Column(name = "TRANS_ATTR")
	private Integer transAttr;
	
	/**
	 * 主账号
	 */
	@Column(name = "PAN")
	private String pan;
	
	/**
	 * 处理码
	 */
	@Column(name = "PROC_CODE")
	private String procCode;
	
	
	/**
	 * 金额
	 */
	@Column(name = "AMOUNT")
	private Long amount;
	
	/**
	 * 22.输入模式
	 */
	@Column(name = "INPUT_MODE")
	private String inputMode;
	
	/**
	 * 23.卡序列号
	 */
	@Column(name = "CARD_SERIAL_NO")
	private String cardSerialNo;
	
	
	/**
	 * 25.服务点条件代码
	 */
	@Column(name = "SERVER_CODE")
	private String serverCode;
	
	
	/**
	 * 32.受理方标识
	 */
	@Column(name = "ACQ_CENTER_CODE")
	private String acqCenterCode;
	
	
	/**
	 * 37.系统参考号
	 */
	@Column(name = "REF_NUM")
	private String refNum;
	
	/**
	 * 授权码
	 */
	@Column(name = "AUTH_CODE")
	private String authCode;

	
	/**
	 * 货币代码 
	 */
	@Column(name = "CURRENCY")
	private String currency;
	
	
	@Column(name = "FIELD_55")
	private String field55;
	
	
	/**
	 * 60域
	 */
	@Column(name = "FIELD_60")
	private String isoField60;
	

	/**
	 * 61.1原交易批次号
	 */
	@Column(name = "FIELD_61")
	private String field61;
	
	
	/**
	 * 预留 
	 */
	@Column(name = "ADDITION")
	private String addition;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTransType() {
		return transType;
	}

	public void setTransType(Integer transType) {
		this.transType = transType;
	}

	public Integer getTransAttr() {
		return transAttr;
	}

	public void setTransAttr(Integer transAttr) {
		this.transAttr = transAttr;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getProcCode() {
		return procCode;
	}

	public void setProcCode(String procCode) {
		this.procCode = procCode;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getInputMode() {
		return inputMode;
	}

	public void setInputMode(String inputMode) {
		this.inputMode = inputMode;
	}

	public String getCardSerialNo() {
		return cardSerialNo;
	}

	public void setCardSerialNo(String cardSerialNo) {
		this.cardSerialNo = cardSerialNo;
	}

	public String getServerCode() {
		return serverCode;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}

	public String getAcqCenterCode() {
		return acqCenterCode;
	}

	public void setAcqCenterCode(String acqCenterCode) {
		this.acqCenterCode = acqCenterCode;
	}

	public String getRefNum() {
		return refNum;
	}

	public void setRefNum(String refNum) {
		this.refNum = refNum;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getField55() {
		return field55;
	}

	public void setField55(String field55) {
		this.field55 = field55;
	}

	public String getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	

	public String getField61() {
		return field61;
	}

	public void setField61(String field61) {
		this.field61 = field61;
	}

	public ISOField60 getIsoField60() {
		return  new ISOField60(isoField60);
	}

	public void setIsoField60(ISOField60 isoField60) {
		this.isoField60 = isoField60.getString();
	}
	
	
}
