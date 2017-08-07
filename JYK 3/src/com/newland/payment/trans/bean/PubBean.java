package com.newland.payment.trans.bean;

import android.annotation.SuppressLint;

import com.newland.base.dao.ann.Column;
import com.newland.payment.trans.bean.field.ISOField44;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.bean.AmountBean;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.bean.EmvBean;
import com.newland.pos.sdk.emv.EmvCoreOperator.ECSelectMode;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

import java.util.Map;

/**
 * 标准交易数据元
 * 
 * @author liugang
 * @date 2015-05-08
 * @edit
 */
@SuppressLint("DefaultLocale")
@SuppressWarnings("serial")
public class PubBean extends BaseBean{
	
	/**
	 * EMV交易数据元
	 */
	private EmvBean emvBean = new EmvBean();

	/**
	 * 用卡方式
	 */
	private int cardInputMode;

	/**
	 * 原交易类型,只做为结算调整使用
	 */
	private int oldTransType;
	
	/**
	 * 2016.12.16  查询交易流水详情
	 */
	private int transCode;

	/**
	 * EMV交易的执行状态
	 */
	private int emv_Status;

	/**
	 * 消息类型码 4字节
	 */
	private String messageID;

	/**
	 * 3 处理码 6字节
	 */
	private String processCode;
	
	/**
	 * 11 POS流水号
	 */
	private String traceNo;

	/**
	 * 12 交易时间hhmmss
	 */
	private String time;

	/**
	 * 13 交易日期yyyymmdd
	 */
	private String date;

	/**
	 * 15 清算日期(返回包时用)
	 */
	private String settleDate;

	/**
	 * 25 服务点条件码
	 */
	private String serverCode;

	/**
	 * 32 受理方标识码,pos中心号(返回包时用)
	 */
	private String acqCenterCode;

	/**
	 * 36 三道数据
	 */
	private String trackData3;

	/**
	 * 37 系统参考号(返回包时用)
	 */
	private String systemRefNum;

	/**
	 * 38 授权码
	 */
	private String authCode;

	/**
	 * 41 终端号
	 */
	private String posID;

	/**
	 * 42 商户号
	 */
	private String shopID;

	/**
	 * 48域 用法五转入卡的输入模式
	 */
	private String inputModeForTransIn;

	/**
	 * 53安全控制信息(Security Related Control Information)
	 */
	private String secctrlInfo;

	/**
	 * 64 返回包是否校验MAC标识0x01需要0x00不需要
	 */
	private int mustChkMAC;

	
	/**
	 * 原授权码
	 */
	private String oldAuthCode;

	/**
	 * 原交易参考号
	 */
	private String OldRefnum;

	/**
	 * 原交易日期 
	 */
	private String oldDate;
	
	/**
	 * 原交易类型,只做为结算调整使用
	 */
	private Long oldAmount;
	
	/**
	 * 小费金额
	 */
	private Long tipAmount;

	/**
	 * 国际组织代码(返回包时用)
	 */
	private String internationOrg;

	/**
	 * 63.1要求上送的是3位的,操作员号码
	 */
	private String currentOperNo;

	/**
	 * 手机号码
	 */
	private String mobileNo;

	/**
	 * 预约号码
	 */
	private String appointmentNo;
	

	
	/**转入卡卡号*/
	private String cardNoTransIn;

	private ISOField44 isoField44;

	private String isoField48;
	private String isoField61;
	private String isoField62;
	private String isoField63;

	private ISOField60 isoField60;
	
	private String emvAddition;
	
	private EmvTransResult emvTransResult;

	/**
	 * 原终端号(脱机退货使用)
	 */
	private String oldTerminalId;

	public String getBack_cardInfo() {
		return back_cardInfo;
	}

	public void setBack_cardInfo(String back_cardInfo) {
		this.back_cardInfo = back_cardInfo;
	}

	public String getBack_isDiscount() {
		return back_isDiscount;
	}

	public void setBack_isDiscount(String back_isDiscount) {
		this.back_isDiscount = back_isDiscount;
	}

	public String getBack_discountType() {
		return back_discountType;
	}

	public void setBack_discountType(String back_discountType) {
		this.back_discountType = back_discountType;
	}

	public String getBack_discountRate() {
		return back_discountRate;
	}

	public void setBack_discountRate(String back_discountRate) {
		this.back_discountRate = back_discountRate;
	}

	public String getBack_bonus() {
		return back_bonus;
	}

	public void setBack_bonus(String back_bonus) {
		this.back_bonus = back_bonus;
	}

	private String back_cardInfo;
	private String back_isDiscount;
	private String back_discountType;
	private String back_discountRate;
	private String back_bonus;
	private String back_name;

	private boolean isPay = false;

	private Map<String, String> cardInfo;

	public Map<String, String> getCardInfo() {
		return cardInfo;
	}
	public void setCardInfo(Map<String, String> cardInfo) {
		this.cardInfo = cardInfo;
	}
	/**
	 * EMV交易结果
	 * @author chenkh
	 * @date 2015-5-21
	 * @time 下午12:03:38
	 *
	 */
	public enum EmvTransResult {
		/**
		 * 交易成功
		 */
		SUCCESS,
		/**
		 * 交易失败
		 */
		FAIL,
		/**
		 * 降级
		 */
		FALLBACK,

		/**
		 * 交易结束（直接返回主菜单）
		 */
		TRANS_END;
	}
	
	/**
	 * 是否FALLBACK
	 */
	private boolean isFallBack = false;
	/**
	 * 预留值1
	 */
	private String reserve1;

	/**
	 * 预留值2
	 */
	private String reserve2;
	
	
	/**<第三方调用失败时返回给调用者的说明信息*/
	private String message;
	
	/**<用于存放消费交易的请求金额*/
	private Long requestAmount;
	
	private String odlTrace;
	
	/*余额查询的余额*/
	private Long balance;
	
	private String operatorNo;

	private String password;

	private int maxPasswordLen;

	private AmountBean amountBean;

	private boolean forcePassword;

	private Long price;

	private String ip;

	private String port;

	private String gasNo;

	private String refuelerNo;

	private String oilTypeNo;

	private Long oilWeight;


	private String posNo;

	//订单金额
	private Long orderAmount = -1L;

	private String oilType;

	private Long liter;

	private String macKey;

	private Long discountAmount = -1L;

	public Long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
	public String getOilType() {
		return oilType;
	}

	public void setOilType(String oilType) {
		this.oilType = oilType;
	}

	public Long getLiter() {
		return liter;
	}

	public void setLiter(Long liter) {
		this.liter = liter;
	}

	public String getMacKey() {
		return macKey;
	}

	public void setMacKey(String macKey) {
		this.macKey = macKey;
	}

	public EmvBean getEmvBean() {
		return emvBean;
	}

	public int getTransType() {
		return emvBean.getTransType();
	}

	public void setTransType(int transType) {
		emvBean.setTransType(transType);
	}
	
	

	public int getCardInputMode() {
		return cardInputMode;
	}

	public void setCardInputMode(int cardInputMode) {
		this.cardInputMode = cardInputMode;
	}
	
	public String getTransName() {
		return emvBean.getTransName();
	}

	public void setTransName(String transName) {
		emvBean.setTransName(transName);
	}

	public int getTransAttr() {
		return emvBean.getTransAttr();
	}

	public void setTransAttr(int transAttr) {
		emvBean.setTransAttr(transAttr);
	}

	public int getEmv_Status() {
		return emv_Status;
	}

	public void setEmv_Status(int emv_Status) {
		this.emv_Status = emv_Status;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getPan() {
		return emvBean.getPan();
	}

	public void setPan(String pan) {
		emvBean.setPan(pan);
	}

	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	public Long getAmount() {
		return emvBean.getAmount();
	}

	public String getAmountField(){
		return String.format("%012d", emvBean.getAmount());
	}

	public void setAmount(Long amount) {
		emvBean.setAmount(amount);
	}

	public Long getOldAmount() {
		return oldAmount;
	}

	public void setOldAmount(Long oldAmount) {
		this.oldAmount = oldAmount;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
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

	public String getExpDate() {
		return emvBean.getExpDate();
	}

	public void setExpDate(String expDate) {
		emvBean.setExpDate(expDate);
	}

	public String getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}

	public String getInputMode() {
		return emvBean.getInputMode();
	}

	public void setInputMode(String inputMode) {
		emvBean.setInputMode(inputMode);
	}

	public String getCardSerialNo() {
		return emvBean.getCardSerialNo();
	}

	public void setCardSerialNo(String cardSerialNo) {
		emvBean.setCardSerialNo(cardSerialNo);
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

	public String getTrackData2() {
		return emvBean.getTrackData2();
	}

	public void setTrackData2(String trackData2) {
		emvBean.setTrackData2(trackData2);
	}

	public String getTrackData3() {
		return trackData3;
	}

	public void setTrackData3(String trackData3) {
		this.trackData3 = trackData3;
	}

	public String getSystemRefNum() {
		return systemRefNum;
	}

	public void setSystemRefNum(String systemRefNum) {
		this.systemRefNum = systemRefNum;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getResponseCode() {
		return emvBean.getResponseCode();
	}

	public void setResponseCode(String responseCode) {
		emvBean.setResponseCode(responseCode);
	}

	public String getPosID() {
		return posID;
	}

	public void setPosID(String posID) {
		this.posID = posID;
	}

	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

	public String getInputModeForTransIn() {
		return inputModeForTransIn;
	}

	public void setInputModeForTransIn(String inputModeForTransIn) {
		this.inputModeForTransIn = inputModeForTransIn;
	}

	public String getPinBlock() {
		return emvBean.getPinBlock();
	}

	public void setPinBlock(String pinBlock) {
		emvBean.setPinBlock(pinBlock);
	}

	public String getSecctrlInfo() {
		return secctrlInfo;
	}

	public void setSecctrlInfo(String secctrlInfo) {
		this.secctrlInfo = secctrlInfo;
	}

	public int getMustChkMAC() {
		return mustChkMAC;
	}

	public void setMustChkMAC(int mustChkMAC) {
		this.mustChkMAC = mustChkMAC;
	}

	public String getOldAuthCode() {
		return oldAuthCode;
	}

	public void setOldAuthCode(String oldAuthCode) {
		this.oldAuthCode = oldAuthCode;
	}

	public String getOldRefnum() {
		return OldRefnum;
	}

	public void setOldRefnum(String oldRefnum) {
		OldRefnum = oldRefnum;
	}

	public Long getTipAmount() {
		return tipAmount;
	}

	public void setTipAmount(Long tipAmount) {
		this.tipAmount = tipAmount;
	}

	public String getInternationOrg() {
		return internationOrg;
	}

	public void setInternationOrg(String internationOrg) {
		this.internationOrg = internationOrg;
	}

	public String getCurrentOperNo() {
		return currentOperNo;
	}

	public void setCurrentOperNo(String currentOperNo) {
		this.currentOperNo = currentOperNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getCurrency() {
		return emvBean.getCurrency();
	}

	public void setCurrency(String currency) {
		emvBean.setCurrency(currency);
	}

	public String getAppointmentNo() {
		return appointmentNo;
	}

	public void setAppointmentNo(String appointmentNo) {
		this.appointmentNo = appointmentNo;
	}

	public ISOField44 getIsoField44() {
		return isoField44;
	}

	public String getIsoField48() {
		return isoField48;
	}

	public void setIsoField48(String isoField48) {
		this.isoField48 = isoField48;
	}

	public String getIsoField55() {
		return emvBean.getIsoField55();
	}

	public void setIsoField55(String isoField55) {
		emvBean.setIsoField55(isoField55);
	}

	public ISOField60 getIsoField60() {
		return isoField60;
	}
	
	public void setIsoField60(ISOField60 isoField60) {
		this.isoField60 = isoField60;
	}

	public String getIsoField61() {
		return isoField61;
	}

	public void setIsoField61(String isoField61) {
		this.isoField61 = isoField61;
	}

	public String getIsoField62() {
		return isoField62;
	}

	public void setIsoField62(String isoField62) {
		this.isoField62 = StringUtils.strToHex(isoField62);
	}

	public String getIsoField63() {
		return isoField63;
	}

	public void setIsoField63(String isoField63) {
		this.isoField63 = isoField63;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public void setIsoField44(ISOField44 isoField44) {
		this.isoField44 = isoField44;
	}



	public void setOldDate(String oldDate){
		this.oldDate = oldDate;
	}

	public String getOldDate() {
		return oldDate;
	}

	public boolean isTSIComleted() {
		return emvBean.isTSIComleted();
	}

	public void setTSIComleted(boolean isTSIComleted) {
		emvBean.setTSIComleted(isTSIComleted);
	}

	/**
	 * 0 用户选择电子现金
	 * 1 不使用电子现金
	 * 2 使用电子现金
	 */
	public void setEcSelectMode(ECSelectMode ecSelectMode) {
		emvBean.setEcSelectMode(ecSelectMode);
	}
	
	public ECSelectMode getEcSelectMode(){
		return emvBean.getEcSelectMode();
	}

	public EmvTransResult getEmvTransResult() {
		return emvTransResult;
	}

	public void setEmvTransResult(EmvTransResult emvTransResult) {
		this.emvTransResult = emvTransResult;
	}

	public Long getEcBalance() {
		return emvBean.getEcBalance();
	}

	public void setEcBalance(Long ecBalance) {
		emvBean.setEcBalance(ecBalance);
	}

	public String getEmvAddition() {
		return emvAddition;
	}

	public void setEmvAddition(String emvAddition) {
		this.emvAddition = emvAddition;
	}

	public boolean isFallBack() {
		return isFallBack;
	}

	public void setFallBack(boolean isFallBack) {
		this.isFallBack = isFallBack;
	}

	public String getCardNoTransIn() {
		return cardNoTransIn;
	}

	public void setCardNoTransIn(String cardNoTransIn) {
		this.cardNoTransIn = cardNoTransIn;
	}
	
	public String getOldTerminalId() {
		return oldTerminalId;
	}

	public void setOldTerminalId(String oldTerminalId) {
		this.oldTerminalId = oldTerminalId;
	}

	public int getOldTransType() {
		return oldTransType;
	}

	public void setOldTransType(int oldTransType) {
		this.oldTransType = oldTransType;
	}

	public int getqPbocPriority() {
		return emvBean.getqPbocPriority();
	}

	public void setqPbocPriority(int qPbocPriority) {
		emvBean.setqPbocPriority(qPbocPriority);
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setRequestAmount(Long requestAmount){
		this.requestAmount = requestAmount;
	}
	
	public Long getRequestAmount(){
		return requestAmount;
	}
	
	
	public void setOldTrace(String trace){
		this.odlTrace = trace;
	}
	
	public String getOldTrace(){
		return odlTrace;
	}
	
	
	public void setBalance(Long balance){
		this.balance = balance;
	}
	
	public Long getBalance(){
		return balance;
	}
	
	
	public String getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorNo(String operatorNo) {
		this.operatorNo = operatorNo;
	}
	
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public int getMaxPasswordLen() {
		return maxPasswordLen;
	}

	public void setMaxPasswordLen(int maxPasswordLen) {
		this.maxPasswordLen = maxPasswordLen;
	}
	
	
	public void setAmountBean(AmountBean amountBean){
		this.amountBean = amountBean;
	}
	
	public AmountBean getAmountBean(){
		return amountBean;
	}
	
	
	public void setForcePassword(boolean forcePassword){
		this.forcePassword = forcePassword;
	}
	
	public boolean getForcePassword(){
		return forcePassword;
	}

	public int getTransCode() {
		return transCode;
	}

	public void setTransCode(int transCode) {
		this.transCode = transCode;
	}

	public boolean isPay() {
		return isPay;
	}

	public void setPay(boolean pay) {
		isPay = pay;
	}

	public Long getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Long discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getGasNo() {
		return gasNo;
	}

	public void setGasNo(String gasNo) {
		this.gasNo = gasNo;
	}

	public String getRefuelerNo() {
		return refuelerNo;
	}

	public void setRefuelerNo(String refuelerNo) {
		this.refuelerNo = refuelerNo;
	}

	public String getOilTypeNo() {
		return oilTypeNo;
	}

	public void setOilTypeNo(String oilTypeNo) {
		this.oilTypeNo = oilTypeNo;
	}

	public Long getOilWeight() {
		return oilWeight;
	}

	public void setOilWeight(Long oilWeight) {
		this.oilWeight = oilWeight;
	}

	public String getPosNo() {
		return posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}
	public String getBack_name() {
		LoggerUtils.d("back _ name 1111111111111");
		if(back_name != null){
			LoggerUtils.d("back _ name22222222222222");
			try{
				return StringUtils.hexToStr(back_name);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return back_name;
	}

	public void setBack_name(String back_name) {
		this.back_name = back_name;
	}
}
