package com.newland.payment.mvc.model;

import com.newland.base.dao.ann.Column;
import com.newland.base.dao.ann.Table;

/**
 * 冲正数据
 * @author linchunhui
 * @date 2015年5月12日 下午5:42:23
 * 冲正流水在数据库单中只会存在一条
 */
@Table(name = "T_REVERSE_WATER")
public class ReverseWater {
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
	 * 交易金额
	 */
	@Column(name = "AMOUNT")
	private Long amount;
	
	/**
	 * POS流水号
	 */
	@Column(name = "TRACE")
	private String trace;
	
	/**
	 * 卡有效期
	 */
	@Column(name = "EXP_DATE")
	private String expDate;
	
	/**
	 * 输入模式
	 */
	@Column(name = "INPUT_MODE")
	private String inputMode;
	
	/**
	 * 卡序列号
	 */
	@Column(name = "CARD_SERIAL_NO")
	private String cardSerialNo;
	
	/**
	 * 25.服务点条件代码
	 */
	@Column(name = "SERVER_CODE")
	private String serverCode;
	
	/**
	 * 38.授权码
	 * <p>上送包的授权码</p>
	 */
	@Column(name = "OLD_AUTH_CODE")
	private String oldAuthCode;
	
	/**
	 * 39.响应码
	 * <p>冲正原因</p>
	 */
	@Column(name = "RESPONSE")
	private String response;
	
	
	/**
	 * 48. 转入卡的输入模式
	 * <p>48域用法五</p>
	 */
	@Column(name = "INPUT_MODE_FOR_TRANS_IN")
	private String inputModeForTransIn;
	
	/**
	 * 49.货币代码
	 */
	@Column(name = "CURRENCY_CODE")
	private String currencyCode;
	
	
	@Column(name = "FIELD_55")
	private String field55;
	
	@Column(name = "FIELD_60")
	private String field60;
	
	@Column(name = "FIELD_61")
	private String field61;
	
	@Column(name = "ADDITION")
	private String addition;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * 交易类型
	 * @return 参考 {@link com.newland.payment.common.TransType}中的类型定义
	 */
	public Integer getTransType() {
		return transType;
	}


	/**
	 * 交易类型
	 * @param transType 参考 {@link com.newland.payment.common.TransType}中的类型定义
	 */
	public void setTransType(Integer transType) {
		this.transType = transType;
	}


	/**
	 * 交易属性
	 * @return 取值参考 {@link com.newland.payment.common.TransConst.TransAttr}的定义
	 */
	public Integer getTransAttr() {
		return transAttr;
	}


	/**
	 * 交易属性
	 * @param transAttr 取值参考 {@link com.newland.payment.common.TransConst.TransAttr}的定义
	 */
	public void setTransAttr(Integer transAttr) {
		this.transAttr = transAttr;
	}


	/**
	 * 主账号
	 * @return
	 */
	public String getPan() {
		return pan;
	}


	/**
	 * 主账号
	 * @param pan
	 */
	public void setPan(String pan) {
		this.pan = pan;
	}


	/**
	 * 处理码
	 * @return
	 */
	public String getProcCode() {
		return procCode;
	}


	/**
	 * 处理码
	 * @param procCode
	 */
	public void setProcCode(String procCode) {
		this.procCode = procCode;
	}


	/**
	 * 金额
	 * @return
	 */
	public Long getAmount() {
		return amount;
	}


	/**
	 * 金额
	 * @param amount
	 */
	public void setAmount(Long amount) {
		this.amount = amount;
	}


	/**
	 * POS流水号
	 * @return
	 */
	public String getTrace() {
		return trace;
	}


	/**
	 * POS流水号
	 * @param trace
	 */
	public void setTrace(String trace) {
		this.trace = trace;
	}


	/**
	 * 卡有效期
	 * @return
	 */
	public String getExpDate() {
		return expDate;
	}


	/**
	 * 卡有效期
	 * @param expDate
	 */
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}


	/**
	 * 输入模式
	 * @return
	 */
	public String getInputMode() {
		return inputMode;
	}


	/**
	 * 输入模式
	 * @param inputMode
	 */
	public void setInputMode(String inputMode) {
		this.inputMode = inputMode;
	}


	/**
	 * 卡序列号
	 * @return
	 */
	public String getCardSerialNo() {
		return cardSerialNo;
	}


	/**
	 * 卡序列号
	 * @param cardSerialNo
	 */
	public void setCardSerialNo(String cardSerialNo) {
		this.cardSerialNo = cardSerialNo;
	}


	/**
	 * 25.服务点条件代码
	 * @return
	 */
	public String getServerCode() {
		return serverCode;
	}


	/**
	 * 25.服务点条件代码
	 * @param serverCode
	 */
	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}


	/**
	 * 38.授权码
	 * <p>上送包的授权码</p>
	 * @return
	 */
	public String getOldAuthCode() {
		return oldAuthCode;
	}


	/**
	 * 38.授权码
	 * <p>上送包的授权码</p>
	 * @param oldAuthCode
	 */
	public void setOldAuthCode(String oldAuthCode) {
		this.oldAuthCode = oldAuthCode;
	}


	/**
	 * 39.响应码
	 * <p>冲正原因</p>
	 * @return
	 */
	public String getResponse() {
		return response;
	}


	/**
	 * 39.响应码
	 * <p>冲正原因</p>
	 * @param response
	 */
	public void setResponse(String response) {
		this.response = response;
	}


	/**
	 * 48. 转入卡的输入模式
	 * <p>48域用法五</p>
	 * @return
	 */
	public String getInputModeForTransIn() {
		return inputModeForTransIn;
	}


	/**
	 * 48. 转入卡的输入模式
	 * <p>48域用法五</p>
	 * @param inputModeForTransIn
	 */
	public void setInputModeForTransIn(String inputModeForTransIn) {
		this.inputModeForTransIn = inputModeForTransIn;
	}


	/**
	 * 49.货币代码
	 * @return
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}


	/**
	 * 49.货币代码
	 * @param currencyCode
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}


	public String getField55() {
		return field55;
	}


	public void setField55(String field55) {
		this.field55 = field55;
	}


	public String getField60() {
		return field60;
	}


	public void setField60(String field60) {
		this.field60 = field60;
	}


	public String getField61() {
		return field61;
	}


	public void setField61(String field61) {
		this.field61 = field61;
	}


	public String getAddition() {
		return addition;
	}


	public void setAddition(String addition) {
		this.addition = addition;
	}
	
	
}
