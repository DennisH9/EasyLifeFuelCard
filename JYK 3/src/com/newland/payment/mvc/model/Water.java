package com.newland.payment.mvc.model;

import com.newland.base.dao.ann.Column;
import com.newland.base.dao.ann.Table;

/**
 * 交易流水
 * @author chenkh
 * 2015-05-06
 */
@Table(name="t_water")
public class Water{
	@Column(name = "ID", primaryKey = true)
	private Integer id;
	
	/**
	 * 交易类型，参考 {@link com.newland.payment.common.TransType}中的类型定义
	 */
	@Column(name = "TRANS_TYPE")
	private Integer transType;
	
	
	/**
	 * 2016.12.16  查询交易流水详情
	 */
	@Column(name = "TRANS_CODE")
	private Integer transCode;
	/**
	 * 2016.12.16  外部订单号
	 */
	@Column(name = "OUTORDERNO")
	private String outOrderNo;
	
	/**
	 * 原交易类型， 在结算调整时使用
	 */
	@Column(name = "OLD_TRANS_TYPE")
	private Integer oldTransType;
	
	/**
	 * 交易属性，取值参考 {@link com.newland.payment.common.TransConst.TransAttr}的定义
	 */
	@Column(name = "TRANS_ATTR")
	private Integer transAttr;
	
	/**
	 * 交易状态
	 * <li>0-初始状态</li>
	 * <li>1-已撤销</li>
	 * <li>2-已调整</li>
	 * <li>3-已退货</li>
	 * <li>4-上送后被调整</li>
	 * 
	 */
	@Column(name = "TRANS_STATUS")
	private Integer transStatus;
	
	/**
	 * Emv交易的执行结果：
	 * <li>1-脱机失败</li>
	 * <li>2-脱机成功</li>
	 * <li>3-联机失败</li>
	 * <li>4-联机成功</li>
	 * 
	 */
	@Column(name = "EMV_STATUS")
	private Integer emvStatus;
	
	
	/**
	 * 主账号
	 */
	@Column(name = "PAN")
	private String pan;
	
	/**
	 * 金额
	 */
	@Column(name = "AMOUNT")
	private Long amount;
	/**
	 * 折扣金额
	 */
	@Column(name = "DISCOUNT_AMOUNT")
	private Long discountAmount;
	/**
	 * 订单金额
	 */
	@Column(name = "ORDER_AMOUNT")
	private Long orderAmount;

	
	/**
	 * 交易流水
	 */
	@Column(name = "TRACE")
	private String trace;
	
	
	
	/**
	 * 交易时间，格式：
	 * <li>hhmmss</li>
	 */
	@Column(name = "TIME")
	private String time;
	
	
	
	/**
	 * 交易日期, 格式：
	 * <li>yyyymmdd</li>
	 * <li>yymmdd</li>
	 * <li>mmdd</li>
	 */
	@Column(name = "DATE")
	private String date;
	
	
	/**
	 * 卡有效期
	 */
	@Column(name = "EXP_DATE")
	private String expDate;
	
	
	/**
	 * 清算日期
	 */
	@Column(name = "SETTLE_DATE")
	private String settleDate;
	
	
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
	 * 二磁道数据
	 */
	@Column(name = "TRACK2")
	private String track2;
	
	
	/**
	 * 三磁道数据
	 */
	@Column(name = "TRACK3")
	private String track3;
	
	
	/**
	 * 系统参考号
	 */
	@Column(name = "REFER_NUM")
	private String referNum;
	
	/**
	 * 38.授权码
	 */
	@Column(name = "AUTH_CODE")
	private String authCode;
	
	
	/**
	 * 39.响应码
	 */
	@Column(name = "RESP_CODE")
	private String respCode;
	
	
	/**
	 * 批次号
	 */
	@Column(name = "BATCH_NUM")
	private String batchNum;
	
	
	/**
	 * 60.1消息类型码
	 */
	@Column(name = "FUNC_CODE")
	private String funcCode;
	
	
	/**
	 * 原批次号
	 */
	@Column(name = "OLD_BATCH")
	private String oldBatch;
	
	
	/**
	 * 原流水号
	 */
	@Column(name = "OLD_TRACK")
	private String oldTrace;
	
	
	/**
	 * 原授权码
	 */
	@Column(name = "OLD_AUTH_CODE")
	private String oldAuthCode;
	
	
	/**
	 * 原交易参考号
	 */
	@Column(name = "OLD_REF_NUM")
	private String oldRefNum;
	
	
	/**
	 * 手机号
	 */
	@Column(name = "TEL_NO")
	private String telNo;
	
	
	/**
	 * 预约号码
	 */
	@Column(name = "APPOINTMENT_NO")
	private String appointmentNo;
	
	
	/**
	 * 操作员号
	 */
	@Column(name = "OPER")
	private String oper;
	
	
	/**
	 * 国际组织代码
	 */
	@Column(name = "INTER_ORG")
	private String interOrg;
	
	
	/**
	 * 发卡行标识码,接收机构标识码
	 */
	@Column(name = "IIS_CODE")
	private String iisCode;
	
	
	/**
	 * 收单机构标识码,商户结算行标识码
	 */
	@Column(name = "ACQ_CODE")
	private String acqCode;


	/**
	 * 卡片类型
	 */
	@Column(name = "CARD_TYPE")
	private String cardType;
	
	
	/**
	 * POS中心号
	 */
	@Column(name = "ACQ_CENTER_CODE")
	private String acqCenterCode;
	
	
	/**
	 * 48.小费金额
	 */
	@Column(name = "TIP_AMOUNT")
	private Long tipAmount;
	
	
	/**
	 * 63.1发卡行信息
	 */
	@Column(name = "IIS_INFO")
	private String iisInfo;
	
	
	
	/**
	 * 63.2中国银联信息
	 */
	@Column(name = "CUP_INFO")
	private String cupInfo;
	
	
	/**
	 * 63.3商户收单信息
	 */
	@Column(name = "MER_ACQ_INFO")
	private String merAcqInfo;
	
	
	/**
	 * 63.4POS终端特殊交易信息
	 */
	@Column(name = "TERM_INFO")
	private String termInfo;
	
	
	/**
	 * 离线上送标志:
	 * <li>0-未上送</li>
	 * <li>0xFD-已上送</li>
	 * <li>0xFE-上送被拒</li>
	 * <li>0xFF-上送失败</li>
	 * <li>其他值-已上送次数</li>
	 */
	@Column(name = "OFF_SEND_FLAG")
	private Integer offSendFlag;
	
	
	/**
	 * 批上送标志
	 * <li>0-未上送</li>
	 * <li>0xFD-已上送</li>
	 * <li>0xFE-上送被拒</li>
	 * <li>0xFF-上送失败</li>
	 * <li>其他值-已上送次数</li>
	 */
	@Column(name = "BATCH_UP_FLAG")
	private Integer batchUpFlag;
	
	/**
	 * 原交易日期
	 */
	@Column(name = "OLD_DATE")
	private String oldDate;
	
	/**
	 * EMV附加内容
	 */
	@Column(name = "EMV_ADDITION")
	private String emvAddition;
	
	
	/**
	 * 附加流水内容
	 */
	@Column(name = "ADDITION")
	private String addition;

	/**
	 * 币种（国际代码）
	 */
	@Column(name = "CURRENCY")
	private String currency;
	
	/**
	 * 授权响应码(tag-8A)
	 */
	@Column(name = "EMV_AUTH_RESP")
	private String emvAuthResp;
	
	/**
	 * 终端验证结果(tag-95)
	 */
	@Column(name = "TVR")
	private String tvr;
	
	/**
	 * 交易状态信息(tag-9B)
	 */
	@Column(name = "TSI")
	private String tsi;
	
	/**
	 * 55域
	 */
	@Column(name = "FIELD55")
	private String field55;
	
	/**
	 * 电子现金余额
	 */
	@Column(name = "EC_BALANCE")
	private Long ecBalance;
	
	/**
	 * 原终端号(脱机退货需要)
	 */
	@Column(name = "OLD_TERMINALID")
	private String oldTerminalId;
	
	/**
	 * 电子签名标志
	 */
	@Column(name = "SIGNATURE_FLAG")
	private Boolean signatureFlag;
	
	/**
	 * 电子签名上送标志,目前未使用
	 *  * <li>0-未上送</li>
	 * <li>0xFD-已上送</li>
	 * <li>0xFE-上送被拒</li>
	 * <li>0xFF-上送失败</li>
	 * <li>其他值-已上送次数</li>
	 */ 
	@Column(name = "SIGN_SEND_FLAG")
	private Integer signSendFlag;

	/**
	 * 持卡人手机号,电子签名输入
	 */
	@Column(name = "CARDHOLDER_TEL")
	private String cardHolderTelphone;
	
	/**
	 * 转入卡卡号
	 */
	@Column(name = "CARDNO_TRANS_IN")
	private String cardNoTransIn;
	
	/**
	 * 可用余额(54域返回)
	 */
	@Column(name = "BALANCE")
	private Long balance;
	

	/**
	 * 免签标志
	 */
	@Column(name = "NO_SIGN_FLAG")
	private Boolean noSignFlag;


	/**
	 * 免密标志
	 */
	@Column(name = "NO_PSW_FLAG")
	private Boolean noPswFlag;
	

	/**
	 * 数据库记录唯一值
	 * @return
	 */
	public Integer getId() {
		return id;
	}


	/**
	 * 数据库记录唯一值
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}


	/**
	 * 交易类型
	 * @return 参考 {@link com.newland.payment.common.TransType} 中的值定义
	 */
	public Integer getTransType() {
		return transType;
	}


	/**
	 * 交易类型
	 * @param transType 参考 {@link com.newland.payment.common.TransType} 中的值定义
	 */
	public void setTransType(Integer transType) {
		this.transType = transType;
	}


	/**
	 * 原交易类型
	 * @return 参考 {@link com.newland.payment.common.TransType} 中的值定义
	 */
	public Integer getOldTransType() {
		return oldTransType;
	}


	/**
	 * 原交易类型
	 * @param oldTransType 参考 {@link com.newland.payment.common.TransType} 中的值定义
	 */
	public void setOldTransType(Integer oldTransType) {
		this.oldTransType = oldTransType;
	}


	/**
	 * 交易属性
	 * @return 参考 {@link com.newland.payment.common.TransConst.TransAttr} 中的值定义
	 */
	public Integer getTransAttr() {
		return transAttr;
	}

	

	/**
	 * 交易属性
	 * @param transAttr
	 */
	public void setTransAttr(Integer transAttr) {
		this.transAttr = transAttr;
	}


	/**
	 * 交易状态
	 * @return 返回值：
	 * <li>1-已取消</li>
	 * <li>2-已调整</li>
	 * <li>3-已退货</li>
	 * <li>4-上送后被调整</li>
	 */
	public Integer getTransStatus() {
		return transStatus;
	}


	/**
	 * 交易状态
	 * @param transStatus 取值：<li>1-已取消</li>
	 * <li>2-已调整</li>
	 * <li>3-已退货</li>
	 * <li>4-上送后被调整</li>
	 */
	public void setTransStatus(Integer transStatus) {
		this.transStatus = transStatus;
	}


	/**
	 * EMV交易的执行结果
	 * @return 返回值：
	 * <li>1-脱机失败</li>
	 * <li>2-脱机成功</li>
	 * <li>3-联机失败</li>
	 * <li>4-联机成功</li>
	 */
	public Integer getEmvStatus() {
		return emvStatus;
	}


	/**
	 * EMV交易的执行结果
	 * @param emvStatus 取值：
	 * <li>1-脱机失败</li>
	 * <li>2-脱机成功</li>
	 * <li>3-联机失败</li>
	 * <li>4-联机成功</li>
	 */
	public void setEmvStatus(Integer emvStatus) {
		this.emvStatus = emvStatus;
	}


	/**
	 * 主账号
	 * @return 返回主账号
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
	 * 交易流水
	 * @return
	 */
	public String getTrace() {
		return trace;
	}


	/**
	 * 交易流水
	 * @param trace
	 */
	public void setTrace(String trace) {
		this.trace = trace;
	}


	/**
	 * 交易时间
	 * @return 格式：<li>hhmmss</li>
	 */
	public String getTime() {
		return time;
	}


	/**
	 * 交易时间
	 * @param time 格式：<li>hhmmss</li>
	 */
	public void setTime(String time) {
		this.time = time;
	}


	/**
	 * 交易日期
	 * @return 格式：<li>yyyymmdd</li>
	 * <li>yymmdd</li>
	 * <li>mmdd</li>
	 */
	public String getDate() {
		return date;
	}


	/**
	 * 交易日期
	 * @param date 格式：<li>yyyymmdd</li>
	 * <li>yymmdd</li>
	 * <li>mmdd</li>
	 */
	public void setDate(String date) {
		this.date = date;
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
	 * 清算日期
	 * @return
	 */
	public String getSettleDate() {
		return settleDate;
	}


	/**
	 * 清算日期
	 * @param settleDate
	 */
	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}


	/**
	 * 输入模式
	 * @return 输入模式
	 */
	public String getInputMode() {
		return inputMode;
	}


	/**
	 * 输入模式
	 * @param inputMode 输入模式
	 */
	public void setInputMode(String inputMode) {
		this.inputMode = inputMode;
	}


	/**
	 * 卡序列号
	 * @return 卡序列号
	 */
	public String getCardSerialNo() {
		return cardSerialNo;
	}


	/**
	 * 卡序列号
	 * @param cardSerialNo 卡序列号
	 */
	public void setCardSerialNo(String cardSerialNo) {
		this.cardSerialNo = cardSerialNo;
	}


	/**
	 * 二磁道数据
	 * @return 二磁道数据
	 */
	public String getTrack2() {
		return track2;
	}


	/**
	 * 二磁道数据
	 * @param track2 二磁道数据
	 */
	public void setTrack2(String track2) {
		this.track2 = track2;
	}


	/**
	 * 三磁道数据
	 * @return 三磁道数据
	 */
	public String getTrack3() {
		return track3;
	}


	/**
	 * 三磁道数据
	 * @param track3
	 */
	public void setTrack3(String track3) {
		this.track3 = track3;
	}


	/**
	 * 系统参考号
	 * @return 系统参考号
	 */
	public String getReferNum() {
		return referNum;
	}


	/**
	 * 系统参考号
	 * @param referNum 系统参考号
	 */
	public void setReferNum(String referNum) {
		this.referNum = referNum;
	}


	/**
	 * 38.授权码
	 * @return 38.授权码
	 */
	public String getAuthCode() {
		return authCode;
	}


	/**
	 * 38.授权码
	 * @param authCode 38.授权码
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}


	/**
	 * 39.响应码
	 * @return 39.响应码
	 */
	public String getRespCode() {
		return respCode;
	}


	/**
	 * 39.响应码
	 * @param respCode 39.响应码
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}


	/**
	 * 批次号
	 * @return 批次号
	 */
	public String getBatchNum() {
		return batchNum;
	}


	/**
	 * 批次号
	 * @param batchNum 批次号
	 */
	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}


	/**
	 * 60.1消息类型码
	 * @return 60.1消息类型码
	 */
	public String getFuncCode() {
		return funcCode;
	}


	/**
	 * 60.1消息类型码
	 * @param funcCode 60.1消息类型码
	 */
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}


	/**
	 * 原批次号
	 * @return 原批次号
	 */
	public String getOldBatch() {
		return oldBatch;
	}


	/**
	 * 原批次号
	 * @param oldBatch 原批次号
	 */
	public void setOldBatch(String oldBatch) {
		this.oldBatch = oldBatch;
	}


	/**
	 * 原流水号 
	 * @return 原流水号
	 */
	public String getOldTrace() {
		return oldTrace; 
	}


	/**
	 * 原流水号
	 * @param oldTrace 原流水号
	 */
	public void setOldTrace(String oldTrace) {
		this.oldTrace = oldTrace;
	}


	/**
	 * 原授权码
	 * @return 原授权码
	 */
	public String getOldAuthCode() {
		return oldAuthCode;
	}


	/**
	 * 原授权码
	 * @param oldAuthCode 原授权码
	 */
	public void setOldAuthCode(String oldAuthCode) {
		this.oldAuthCode = oldAuthCode;
	}


	/**
	 * 原交易系统参考号
	 * @return 原交易系统参考号
 	 */
	public String getOldRefNum() {
		return oldRefNum;
	}


	/**
	 * 原交易参考号
	 * @param oldRefNum 原交易参考号
	 */
	public void setOldRefNum(String oldRefNum) {
		this.oldRefNum = oldRefNum;
	}


	/**
	 * 手机号
	 * @return 手机号
	 */
	public String getTelNo() {
		return telNo;
	}


	/**
	 * 手机号
	 * @param telNo 手机号
	 */
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}


	/**
	 * 预约号码
	 * @return 预约号码
	 */
	public String getAppointmentNo() {
		return appointmentNo;
	}


	/**
	 * 预约号码
	 * @param appointmentNo 预约号码
	 */
	public void setAppointmentNo(String appointmentNo) {
		this.appointmentNo = appointmentNo;
	}


	/**
	 * 操作员
	 * @return 操作员
	 */
	public String getOper() {
		return oper;
	}


	/**
	 * 操作员
	 * @param oper 操作员
	 */
	public void setOper(String oper) {
		this.oper = oper;
	}


	/**
	 * 国际组织代码
	 * @return 国际组织代码
	 */
	public String getInterOrg() {
		return interOrg;
	}


	/**
	 * 国际组织代码
	 * @param interOrg 国际组织代码
	 */
	public void setInterOrg(String interOrg) {
		this.interOrg = interOrg;
	}


	/**
	 * 发卡行标识码,接收机构标识码
	 * @return 发卡行标识码,接收机构标识码
	 */
	public String getIisCode() {
		return iisCode;
	}


	/**
	 * 发卡行标识码,接收机构标识码
	 * @param iisCode 发卡行标识码,接收机构标识码
	 */
	public void setIisCode(String iisCode) {
		this.iisCode = iisCode;
	}


	/**
	 * 收单机构标识码,商户结算行标识码
	 * @return 收单机构标识码,商户结算行标识码
	 */
	public String getAcqCode() {
		return acqCode;
	}


	/**
	 * 收单机构标识码,商户结算行标识码
	 * @param acqCode 收单机构标识码,商户结算行标识码
	 */
	public void setAcqCode(String acqCode) {
		this.acqCode = acqCode;
	}


	/**
	 * POS中心号
	 * @return POS中心号
	 */
	public String getAcqCenterCode() {
		return acqCenterCode;
	}


	/**
	 * POS中心号
	 * @param acqCenterCode POS中心号
	 */
	public void setAcqCenterCode(String acqCenterCode) {
		this.acqCenterCode = acqCenterCode;
	}


	/**
	 * 48.小费金额
	 * @return 48.小费金额
	 */
	public Long getTipAmount() {
		return tipAmount;
	}


	/**
	 * 48.小费金额
	 * @param tipAmount 48.小费金额
	 */
	public void setTipAmount(Long tipAmount) {
		this.tipAmount = tipAmount;
	}


	/**
	 * 63.1发卡行信息
	 * @return 63.1发卡行信息
	 */
	public String getIisInfo() {
		return iisInfo;
	}


	/**
	 * 63.1发卡行信息
	 * @param iisInfo 63.1发卡行信息
	 */
	public void setIisInfo(String iisInfo) {
		this.iisInfo = iisInfo;
	}


	/**
	 * 63.2中国银联信息
	 * @return 63.2中国银联信息
	 */
	public String getCupInfo() {
		return cupInfo;
	}


	/**
	 * 63.2中国银联信息
	 * @param cupInfo 63.2中国银联信息
	 */
	public void setCupInfo(String cupInfo) {
		this.cupInfo = cupInfo;
	}


	/**
	 * 63.3商户收单信息
	 * @return 63.3商户收单信息
	 */
	public String getMerAcqInfo() {
		return merAcqInfo;
	}


	/**
	 * 63.3商户收单信息
	 * @param merAcqInfo 63.3商户收单信息
	 */
	public void setMerAcqInfo(String merAcqInfo) {
		this.merAcqInfo = merAcqInfo;
	}


	/**
	 * 63.4POS终端特殊交易信息
	 * @return 63.4POS终端特殊交易信息
	 */
	public String getTermInfo() {
		return termInfo;
	}


	/**
	 * 63.4POS终端特殊交易信息
	 * @param termInfo 63.4POS终端特殊交易信息
	 */
	public void setTermInfo(String termInfo) {
		this.termInfo = termInfo;
	}


	/**
	 * 离线上送标志:
	 * @return 取值：
	 * <li>0-未上送</li>
	 * <li>0xFD-已上送</li>
	 * <li>0xFE-上送被拒</li>
	 * <li>0xFF-上送失败</li>
	 * <li>其他值-已上送次数</li>
	 */
	public Integer getOffSendFlag() {
		return offSendFlag;
	}


	/**
	 * 离线上送标志
	 * @param offSendFlag 取值：
	 * <li>0-未上送</li>
	 * <li>0xFD-已上送</li>
	 * <li>0xFE-上送被拒</li>
	 * <li>0xFF-上送失败</li>
	 * <li>其他值-已上送次数</li>
	 */
	public void setOffSendFlag(Integer offSendFlag) {
		this.offSendFlag = offSendFlag;
	}


	/**
	 * 批次上送标志
	 * @return 取值：
	 * <li>0-未上送</li>
	 * <li>0xFD-已上送</li>
	 * <li>0xFE-上送被拒</li>
	 * <li>0xFF-上送失败</li>
	 * <li>其他值-已上送次数</li>
	 */ 
	public Integer getBatchUpFlag() {
		return batchUpFlag;
	}


	/**
	 * 批次上送标志
	 * @param batchUpFlag 取值
	 * <li>0-未上送</li>
	 * <li>0xFD-已上送</li>
	 * <li>0xFE-上送被拒</li>
	 * <li>0xFF-上送失败</li>
	 * <li>其他值-已上送次数</li>
	 */
	public void setBatchUpFlag(Integer batchUpFlag) {
		this.batchUpFlag = batchUpFlag;
	}
	
	


	/**
	 * EMV附加内容
	 * @return EMV附加内容
	 */
	public String getEmvAddition() {
		return emvAddition;
	}


	/**
	 * EMV附加内容
	 * @param emvAddition EMV附加内容
	 */
	public void setEmvAddition(String emvAddition) {
		this.emvAddition = emvAddition;
	}


	/**
	 * 附加流水内容
	 * @return
	 */
	public String getAddition() {
		return addition;
	}


	/**
	 * 附加流水内容
	 * @param addition
	 */
	public void setAddition(String addition) {
		this.addition = addition;
	}
	
	/**
	 * 币种
	 * @return
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * 币种
	 * @param currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getOldDate() {
		return oldDate;
	}

	public void setOldDate(String oldDate) {
		this.oldDate = oldDate;
	}

	public String getEmvAuthResp() {
		return emvAuthResp;
	}

	public void setEmvAuthResp(String emvAuthResp) {
		this.emvAuthResp = emvAuthResp;
	}

	public String getTvr() {
		return tvr;
	}

	public void setTvr(String tvr) {
		this.tvr = tvr;
	}

	public String getTsi() {
		return tsi;
	}

	public void setTsi(String tsi) {
		this.tsi = tsi;
	}

	public String getField55() {
		return field55;
	}

	public void setField55(String field55) {
		this.field55 = field55;
	}


	/**
	 * 获取电子现金余额
	 * @return
	 */
	public Long getEcBalance() {
		return ecBalance;
	}


	/**
	 * 设置电子现金余额
	 * @param ecBalance
	 */
	public void setEcBalance(Long ecBalance) {
		this.ecBalance = ecBalance;
	}

	public String getOldTerminalId() {
		return oldTerminalId;
	}

	public void setOldTerminalId(String oldTerminalId) {
		this.oldTerminalId = oldTerminalId;
	}


	public Boolean getSignatureFlag() {
		return signatureFlag;
	}

	public void setSignatureFlag(Boolean signatureFlag) {
		this.signatureFlag = signatureFlag;
	}
	
	public int getSignSendFlag() {
		return signSendFlag;
	}


	public void setSignSendFlag(int signSendFlag) {
		this.signSendFlag = signSendFlag;
	}


	public String getCardNoTransIn() {
		return cardNoTransIn;
	}


	public void setCardNoTransIn(String cardNoTransIn) {
		this.cardNoTransIn = cardNoTransIn;
	}


	public void setSignSendFlag(Integer signSendFlag) {
		this.signSendFlag = signSendFlag;
	}


	public Long getBalance() {
		return balance;
	}


	public void setBalance(Long balance) {
		this.balance = balance;
	}


	public String getCardHolderTelphone() {
		return cardHolderTelphone;
	}


	public void setCardHolderTelphone(String cardHolderTelphone) {
		this.cardHolderTelphone = cardHolderTelphone;
	}


	public Boolean getNoSignFlag() {
		return noSignFlag;
	}


	public void setNoSignFlag(Boolean noSignFlag) {
		this.noSignFlag = noSignFlag;
	}


	public Boolean getNoPswFlag() {
		return noPswFlag;
	}


	public void setNoPswFlag(Boolean noPswFlag) {
		this.noPswFlag = noPswFlag;
	}


	public Integer getTransCode() {
		return transCode;
	}


	public void setTransCode(Integer transCode) {
		this.transCode = transCode;
	}


	public String getOutOrderNo() {
		return outOrderNo;
	}


	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

	public Long getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Long discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
}
