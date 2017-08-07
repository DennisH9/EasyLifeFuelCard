package com.newland.payment.printer.template.model;

/**
 * 流水结算model(后缀1为外卡)
 * @author linst
 * @date 2015年5月18日
 * 
 */
public class PrintSettleModel {
	
	
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
	 * 操作员号
	 */
	private String operId;
	
	/**
	 * 批次号
	 */
	private String batchNo;
	
	/**
	 * 交易时间日期
	 */
	private String dateTime;
	
	/**
	 * 内卡对账平（否则该值为null或者"0"）
	 */
	private String isSettleEok0EqualFlag;
	
	/**
	 * 内卡对账不平（否则该值为null或者"0"）
	 */
	private String isSettleEok0NotEqualFlag;
	
	/**
	 * 内卡对账错（否则该值为null或者"0"）
	 */
	private String isSettleEok0ErrFlag;
	
	/**
	 * 消费
	 */
	private String saleAmt0;
	
	/**
	 * 消费撤销
	 */
	private String voidSaleAmt0;
	
	/**
	 * 退货
	 */
	private String refundAmt0;
	
	/**
	 * 完成请求
	 */
	private String authSaleAmt0;
	
	/**
	 * 完成请求撤销
	 */
	private String voidAuthSaleAmt0;
	
	/**
	 * 离线
	 */
	private String offlineAmt0;
	
	/**
	 * 完成通知
	 */
	private String authSaleOffAmt0;
	
	/**
	 * 圈存
	 */
	private String LoadAmt0;
	
	
	/**
	 * EC消费金额
	 */
	private String ECSaleAmt0;
	
	/**
	 * 总计
	 */
	private String totalAmt0;
	/**
	 * 消费笔数
	 */
	private String saleNum0;
	
	/**
	 * 消费撤销笔数
	 */
	private String voidSaleNum0;
	
	/**
	 * 退货笔数
	 */
	private String refundNum0;
	
	/**
	 * 完成请求笔数
	 */
	private String authSaleNum0;
	
	/**
	 * 完成请求撤销笔数
	 */
	private String voidAuthSaleNum0;
	
	/**
	 * 离线笔数
	 */
	private String offlineNum0;
	
	/**
	 * 完成通知笔数
	 */
	private String authSaleOffNum0;
	
	/**
	 * 圈存笔数
	 */
	private String LoadNum0;
	

	
	/**
	 * EC消费笔数
	 */
	private String ECSaleNum0;
	
	/**
	 * 总计笔数
	 */
	private String totalNum0;
	
	/**
	 * 外卡对账平（否则该值为null或者"0"）
	 */
	private String isSettleEok1EqualFlag;
	
	/**
	 * 外卡对账不平（否则该值为null或者"0"）
	 */
	private String isSettleEok1NotEqualFlag;
	
	/**
	 * 外卡对账错（否则该值为null或者"0"）
	 */
	private String isSettleEok1ErrFlag;
	
	/**
	 * 消费
	 */
	private String saleAmt1;
	
	/**
	 * 消费撤销
	 */
	private String voidSaleAmt1;
	
	/**
	 * 退货
	 */
	private String refundAmt1;
	
	/**
	 * 完成请求
	 */
	private String authSaleAmt1;
	
	/**
	 * 完成请求撤销
	 */
	private String voidAuthSaleAmt1;
	
	/**
	 * 离线
	 */
	private String offlineAmt1;
	
	/**
	 * 完成通知
	 */
	private String authSaleOffAmt1;
	
	/**
	 * 圈存
	 */
	private String LoadAmt1;
	
	/**
	 * EC消费金额
	 */
	private String ECSaleAmt1;
	
	
	/**
	 * 消费笔数
	 */
	private String saleNum1;
	
	/**
	 * 消费撤销笔数
	 */
	private String voidSaleNum1;
	
	/**
	 * 退货笔数
	 */
	private String refundNum1;
	
	/**
	 * 完成请求笔数
	 */
	private String authSaleNum1;
	
	/**
	 * 完成请求撤销笔数
	 */
	private String voidAuthSaleNum1;
	
	/**
	 * 离线笔数
	 */
	private String offlineNum1;
	
	/**
	 * 完成通知笔数
	 */
	private String authSaleOffNum1;
	
	/**
	 * 圈存笔数
	 */
	private String LoadNum1;
	

	
	/**
	 * EC消费笔数
	 */
	private String ECSaleNum1;
	
	/**
	 * 总计笔数
	 */
	private String totalNum1;
	/**
	 * 总计
	 */
	private String totalAmt1;
	
	public String getTotalAmt1() {
		return totalAmt1;
	}

	public void setTotalAmt1(String totalAmt1) {
		this.totalAmt1 = totalAmt1;
	}

	/**
	 * 是否重打印
	 */
	private String isReprint;
	
	

	public String getTotalAmt0() {
		return totalAmt0;
	}

	public void setTotalAmt0(String totalAmt0) {
		this.totalAmt0 = totalAmt0;
	}

	public String getSaleNum0() {
		return saleNum0;
	}

	public void setSaleNum0(String saleNum0) {
		this.saleNum0 = saleNum0;
	}

	public String getVoidSaleNum0() {
		return voidSaleNum0;
	}

	public void setVoidSaleNum0(String voidSaleNum0) {
		this.voidSaleNum0 = voidSaleNum0;
	}

	public String getRefundNum0() {
		return refundNum0;
	}

	public void setRefundNum0(String refundNum0) {
		this.refundNum0 = refundNum0;
	}

	public String getAuthSaleNum0() {
		return authSaleNum0;
	}

	public void setAuthSaleNum0(String authSaleNum0) {
		this.authSaleNum0 = authSaleNum0;
	}

	public String getVoidAuthSaleNum0() {
		return voidAuthSaleNum0;
	}

	public void setVoidAuthSaleNum0(String voidAuthSaleNum0) {
		this.voidAuthSaleNum0 = voidAuthSaleNum0;
	}

	public String getOfflineNum0() {
		return offlineNum0;
	}

	public void setOfflineNum0(String offlineNum0) {
		this.offlineNum0 = offlineNum0;
	}

	public String getAuthSaleOffNum0() {
		return authSaleOffNum0;
	}

	public void setAuthSaleOffNum0(String authSaleOffNum0) {
		this.authSaleOffNum0 = authSaleOffNum0;
	}

	public String getLoadNum0() {
		return LoadNum0;
	}

	public void setLoadNum0(String loadNum0) {
		LoadNum0 = loadNum0;
	}

	public String getECSaleNum0() {
		return ECSaleNum0;
	}

	public void setECSaleNum0(String eCSaleNum0) {
		ECSaleNum0 = eCSaleNum0;
	}

	public String getTotalNum0() {
		return totalNum0;
	}

	public void setTotalNum0(String totalNum0) {
		this.totalNum0 = totalNum0;
	}

	public String getSaleNum1() {
		return saleNum1;
	}

	public void setSaleNum1(String saleNum1) {
		this.saleNum1 = saleNum1;
	}

	public String getVoidSaleNum1() {
		return voidSaleNum1;
	}

	public void setVoidSaleNum1(String voidSaleNum1) {
		this.voidSaleNum1 = voidSaleNum1;
	}

	public String getRefundNum1() {
		return refundNum1;
	}

	public void setRefundNum1(String refundNum1) {
		this.refundNum1 = refundNum1;
	}

	public String getAuthSaleNum1() {
		return authSaleNum1;
	}

	public void setAuthSaleNum1(String authSaleNum1) {
		this.authSaleNum1 = authSaleNum1;
	}

	public String getVoidAuthSaleNum1() {
		return voidAuthSaleNum1;
	}

	public void setVoidAuthSaleNum1(String voidAuthSaleNum1) {
		this.voidAuthSaleNum1 = voidAuthSaleNum1;
	}

	public String getOfflineNum1() {
		return offlineNum1;
	}

	public void setOfflineNum1(String offlineNum1) {
		this.offlineNum1 = offlineNum1;
	}

	public String getAuthSaleOffNum1() {
		return authSaleOffNum1;
	}

	public void setAuthSaleOffNum1(String authSaleOffNum1) {
		this.authSaleOffNum1 = authSaleOffNum1;
	}

	public String getLoadNum1() {
		return LoadNum1;
	}

	public void setLoadNum1(String loadNum1) {
		LoadNum1 = loadNum1;
	}

	public String getECSaleNum1() {
		return ECSaleNum1;
	}

	public void setECSaleNum1(String eCSaleNum1) {
		ECSaleNum1 = eCSaleNum1;
	}

	public String getTotalNum1() {
		return totalNum1;
	}

	public void setTotalNum1(String totalNum1) {
		this.totalNum1 = totalNum1;
	}

	public String getIsReprint() {
		return isReprint;
	}

	public void setIsReprint(String isReprint) {
		this.isReprint = isReprint;
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

	public String getOperId() {
		return operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getSaleAmt0() {
		return saleAmt0;
	}

	public void setSaleAmt0(String saleAmt0) {
		this.saleAmt0 = saleAmt0;
	}

	public String getVoidSaleAmt0() {
		return voidSaleAmt0;
	}

	public void setVoidSaleAmt0(String voidSaleAmt0) {
		this.voidSaleAmt0 = voidSaleAmt0;
	}

	public String getRefundAmt0() {
		return refundAmt0;
	}

	public void setRefundAmt0(String refundAmt0) {
		this.refundAmt0 = refundAmt0;
	}

	public String getAuthSaleAmt0() {
		return authSaleAmt0;
	}

	public void setAuthSaleAmt0(String authSaleAmt0) {
		this.authSaleAmt0 = authSaleAmt0;
	}

	public String getVoidAuthSaleAmt0() {
		return voidAuthSaleAmt0;
	}

	public void setVoidAuthSaleAmt0(String voidAuthSaleAmt0) {
		this.voidAuthSaleAmt0 = voidAuthSaleAmt0;
	}

	public String getOfflineAmt0() {
		return offlineAmt0;
	}

	public void setOfflineAmt0(String offlineAmt0) {
		this.offlineAmt0 = offlineAmt0;
	}

	public String getAuthSaleOffAmt0() {
		return authSaleOffAmt0;
	}

	public void setAuthSaleOffAmt0(String authSaleOffAmt0) {
		this.authSaleOffAmt0 = authSaleOffAmt0;
	}

	public String getLoadAmt0() {
		return LoadAmt0;
	}

	public void setLoadAmt0(String loadAmt0) {
		LoadAmt0 = loadAmt0;
	}

	
	public String getECSaleAmt0() {
		return ECSaleAmt0;
	}

	public void setECSaleAmt0(String eCSaleAmt0) {
		ECSaleAmt0 = eCSaleAmt0;
	}

	public String getSaleAmt1() {
		return saleAmt1;
	}

	public void setSaleAmt1(String saleAmt1) {
		this.saleAmt1 = saleAmt1;
	}

	public String getVoidSaleAmt1() {
		return voidSaleAmt1;
	}

	public void setVoidSaleAmt1(String voidSaleAmt1) {
		this.voidSaleAmt1 = voidSaleAmt1;
	}

	public String getRefundAmt1() {
		return refundAmt1;
	}

	public void setRefundAmt1(String refundAmt1) {
		this.refundAmt1 = refundAmt1;
	}

	public String getAuthSaleAmt1() {
		return authSaleAmt1;
	}

	public void setAuthSaleAmt1(String authSaleAmt1) {
		this.authSaleAmt1 = authSaleAmt1;
	}

	public String getVoidAuthSaleAmt1() {
		return voidAuthSaleAmt1;
	}

	public void setVoidAuthSaleAmt1(String voidAuthSaleAmt1) {
		this.voidAuthSaleAmt1 = voidAuthSaleAmt1;
	}

	public String getOfflineAmt1() {
		return offlineAmt1;
	}

	public void setOfflineAmt1(String offlineAmt1) {
		this.offlineAmt1 = offlineAmt1;
	}

	public String getAuthSaleOffAmt1() {
		return authSaleOffAmt1;
	}

	public void setAuthSaleOffAmt1(String authSaleOffAmt1) {
		this.authSaleOffAmt1 = authSaleOffAmt1;
	}

	public String getLoadAmt1() {
		return LoadAmt1;
	}

	public void setLoadAmt1(String loadAmt1) {
		LoadAmt1 = loadAmt1;
	}

	public String getECSaleAmt1() {
		return ECSaleAmt1;
	}

	public void setECSaleAmt1(String eCSaleAmt1) {
		ECSaleAmt1 = eCSaleAmt1;
	}

	public String getIsSettleEok0EqualFlag() {
		return isSettleEok0EqualFlag;
	}

	public void setIsSettleEok0EqualFlag(String isSettleEok0EqualFlag) {
		this.isSettleEok0EqualFlag = isSettleEok0EqualFlag;
	}

	public String getIsSettleEok0NotEqualFlag() {
		return isSettleEok0NotEqualFlag;
	}

	public void setIsSettleEok0NotEqualFlag(String isSettleEok0NotEqualFlag) {
		this.isSettleEok0NotEqualFlag = isSettleEok0NotEqualFlag;
	}

	public String getIsSettleEok0ErrFlag() {
		return isSettleEok0ErrFlag;
	}

	public void setIsSettleEok0ErrFlag(String isSettleEok0ErrFlag) {
		this.isSettleEok0ErrFlag = isSettleEok0ErrFlag;
	}

	public String getIsSettleEok1EqualFlag() {
		return isSettleEok1EqualFlag;
	}

	public void setIsSettleEok1EqualFlag(String isSettleEok1EqualFlag) {
		this.isSettleEok1EqualFlag = isSettleEok1EqualFlag;
	}

	public String getIsSettleEok1NotEqualFlag() {
		return isSettleEok1NotEqualFlag;
	}

	public void setIsSettleEok1NotEqualFlag(String isSettleEok1NotEqualFlag) {
		this.isSettleEok1NotEqualFlag = isSettleEok1NotEqualFlag;
	}

	public String getIsSettleEok1ErrFlag() {
		return isSettleEok1ErrFlag;
	}

	public void setIsSettleEok1ErrFlag(String isSettleEok1ErrFlag) {
		this.isSettleEok1ErrFlag = isSettleEok1ErrFlag;
	}
	
}
