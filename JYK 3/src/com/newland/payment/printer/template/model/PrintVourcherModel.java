package com.newland.payment.printer.template.model;

/**
 * 打印签购单的数据模型
 * @author linchunhui
 * @date 2015年5月8日
 * @time 上午9:22:03
 * @edit by chenkh at 2015-05-12 添加模型的属性
 */
public class PrintVourcherModel {

	/**
	 * 商户联标识（非商户联时该值为null或者"0"）
	 */
	private String merchantFlag;
	
	/**
	 * 银行联标识（非银行联时该值为null或者"0"）
	 */
	private String bankFlag;
	
	/**
	 * 客户联标识（非客户联时该值为null或者"0"）
	 */
	private String customerFlag;
	
	/**
	 * 重打印标识（非重打印时该值为null或者"0"）
	 */
	private String reprintFlag;
	
	/**
	 * 是否电子现金卡标示（非电子现金卡时该值为null或者"0"）
	 */
	private String isCashCard;
	
	/**
	 * 凭条抬头
	 */
	private String printTopName;
	
	/**
	 * 凭条抬头图片
	 */
	private String printTopImg;
	
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
	 * 收单行代码
	 */
	private String acqCode;
	
	/**
	 * 发卡行代码
	 */
	private String iisCode;
	
	/**
	 * 卡号
	 */
	private String cardNo;
	
	/**
	 * 卡号输入方式
	 */
	private String entryMode;
	
	/**
	 * 卡有效期
	 */
	private String expDate;
	
	/**
	 * 卡序号
	 */
	private String cardSerialNo;
	
	/**
	 * 交易类型
	 */
	private String transType_cn;
	
	/**
	 * 批次号
	 */
	private String batchNo;
	
	/**
	 * 凭证号
	 */
	private String traceNo;
	
	/**
	 * 参考号
	 */
	private String refNum;
	
	/**
	 * 授权码
	 */
	private String authCode;
	
	/**
	 * 交易时间日期
	 */
	private String dateTime;
	
	/**
	 * 卡国际组织机构
	 */
	private String interOrg;
	
	/**
	 * 交易金额
	 */
	private String amount;
	
	/**
	 * 原交易金额
	 */
	private String oldAmount;
	
	/**
	 * 小费金额
	 */
	private String feeAmount;
	
	/**
	 * 总金额
	 */
	private String totalAmount;
	
	/**
	 * 电子现金余额
	 */
	private String balance;
	
	/**
	 * 原交易参考号
	 */
	private String oldRefNum;
	
	/**
	 * 原交易批次号
	 */
	private String oldBatchNo;
	
	/**
	 * 原交易授权码
	 */
	private String oldAuthCode;
	
	/**
	 * 累计授权金额
	 */
	private String totalAuthAmount;
	
	/**
	 * 分期付款还款币种
	 */
	private String currency;
	
	/**
	 * 分期数
	 */
	private String installNum;
	
	/**
	 * 分期付款首期金额
	 */
	private String firstAmt;
	
	/**
	 * 持卡人手续费（分期手续费）
	 */
	private String installFee;
	
	/**
	 * 奖励积分
	 */
	private String bonus;
	
	/**
	 * 首期手续费
	 */
	private String firstFee;
	
	/**
	 * 每期手续费
	 */
	private String normalFee;
	
	/**
	 * 商品代码
	 */
	private String code;
	
	/**
	 * 积分余额
	 */
	private String bonusBalance;
	
	/**
	 * 兑换积分数
	 */
	private String bonusSpoint;
	
	/**
	 * 自付金额
	 */
	private String selfAmt;
	
	
	private  String cardType;
	
	/**
	 * 
	 */
	private String telNo;
	
	/**
	 * 
	 */
	private String transIncardNo;
	
	/**
	 * 
	 */
	private String oldTraceNo;
	
	/**
	 * 
	 */
	private String oldDate;
	
	/**
	 * 
	 */
	private String iisInfo;
	
	/**
	 * 
	 */
	private String cupInfo;
	
	/**
	 * 
	 */
	private String acqInfo;
	
	/**
	 * 
	 */
	private String adsInfo;
	
	/**
	 * 发卡行认证密文（0x9f26）
	 */
	private String arqc;
	
	/**
	 * 应用密文（0x9f26）
	 */
	private String tc;
	
	/**
	 * (9f06)
	 */
	private String aid;
	
	/**
	 * 卡序列号 23域（0x5F34）
	 */
	private String csn;
	
	/**
	 * 持卡人验证方法结果(0x9f34)
	 */
	private String cvm;
	
	/**
	 *  (0x9b)
	 */
	private String tsi;
	
	/**
	 * 终端认证结果(TVR)（0x95）
	 */
	private String tvr;
	
	/**
	 *  （0x9f40）
	 */
	private String atc;
	
	/**
	 * 不可预知数（0x9f37）
	 */
	private String unpr;
	
	/**
	 * 应用交互特征（0x82）
	 */
	private String aip;
	
	/**
	 * 终端性能(0x9f33)
	 */
	private String termCap;
	
	/**
	 * 发卡行应用数据(0x9f10)
	 */
	private String iad;
	
	/**
	 *  (0x50)
	 */
	private String applab;
	
	/**
	 * (0x9f12)
	 */
	private String appName;
	
	/**
	 * 卡产品标识0x9F63
	 */
	private String cardProductId;
	
	/**
	 * 电话热线
	 */
	private String hotLine;
	
	
	
	/**
	 * 电子签名数据
	 */
	private String eleSignTureDate;
	/**
	 * 是否手写签名
	 */
	private String isSignature;
	/**
	 * 转入卡号
	 */
	private String cardNoTransIn;
	/**
	 * 持卡人签名
	 */
	private String isEleSign;	

	/**
	 *免密免签金额
	 */
	private String amountLimit;

	/**
	 *是否免签
	 */
	private String isNoSign;
	
	/**
	 *是否免密
	 */
	private String isNoPsw;
	
	/**
	 *是否免密免签
	 */
	private String isNoSignAndPsw;


	/**
	 * 优惠金额

	 * */
	private String discountAmount;
	/**
	 * 订单金额
	 * */
	private String orderAmount;

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getMerchantFlag() {
		return merchantFlag;
	}

	public void setMerchantFlag(String merchantFlag) {
		this.merchantFlag = merchantFlag;
	}

	public String getBankFlag() {
		return bankFlag;
	}

	public void setBankFlag(String bankFlag) {
		this.bankFlag = bankFlag;
	}

	public String getCustomerFlag() {
		return customerFlag;
	}

	public void setCustomerFlag(String customerFlag) {
		this.customerFlag = customerFlag;
	}

	public String getReprintFlag() {
		return reprintFlag;
	}

	public void setReprintFlag(String reprintFlag) {
		this.reprintFlag = reprintFlag;
	}

	public String getPrintTopName() {
		return printTopName;
	}

	public void setPrintTopName(String printTopName) {
		this.printTopName = printTopName;
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

	public String getAcqCode() {
		return acqCode;
	}

	public void setAcqCode(String acqCode) {
		this.acqCode = acqCode;
	}

	public String getIisCode() {
		return iisCode;
	}

	public void setIisCode(String iisCode) {
		this.iisCode = iisCode;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getEntryMode() {
		return entryMode;
	}

	public void setEntryMode(String entryMode) {
		this.entryMode = entryMode;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getCardSerialNo() {
		return cardSerialNo;
	}

	public String getIsEleSign() {
		return isEleSign;
	}

	public void setIsEleSign(String isEleSign) {
		this.isEleSign = isEleSign;
	}

	public void setCardSerialNo(String cardSerialNo) {
		this.cardSerialNo = cardSerialNo;
	}

	public String getTransType_cn() {
		return transType_cn;
	}

	public void setTransType_cn(String transType_cn) {
		this.transType_cn = transType_cn;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
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

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getInterOrg() {
		return interOrg;
	}

	public void setInterOrg(String interOrg) {
		this.interOrg = interOrg;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOldAmount() {
		return oldAmount;
	}

	public void setOldAmount(String oldAmount) {
		this.oldAmount = oldAmount;
	}

	public String getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getOldRefNum() {
		return oldRefNum;
	}

	public void setOldRefNum(String oldRefNum) {
		this.oldRefNum = oldRefNum;
	}

	public String getOldBatchNo() {
		return oldBatchNo;
	}

	public void setOldBatchNo(String oldBatchNo) {
		this.oldBatchNo = oldBatchNo;
	}

	public String getOldAuthCode() {
		return oldAuthCode;
	}

	public void setOldAuthCode(String oldAuthCode) {
		this.oldAuthCode = oldAuthCode;
	}

	public String getTotalAuthAmount() {
		return totalAuthAmount;
	}

	public void setTotalAuthAmount(String totalAuthAmount) {
		this.totalAuthAmount = totalAuthAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getInstallNum() {
		return installNum;
	}

	public void setInstallNum(String installNum) {
		this.installNum = installNum;
	}

	public String getFirstAmt() {
		return firstAmt;
	}

	public void setFirstAmt(String firstAmt) {
		this.firstAmt = firstAmt;
	}

	public String getInstallFee() {
		return installFee;
	}

	public void setInstallFee(String installFee) {
		this.installFee = installFee;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	public String getFirstFee() {
		return firstFee;
	}

	public void setFirstFee(String firstFee) {
		this.firstFee = firstFee;
	}

	public String getNormalFee() {
		return normalFee;
	}

	public void setNormalFee(String normalFee) {
		this.normalFee = normalFee;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBonusBalance() {
		return bonusBalance;
	}

	public void setBonusBalance(String bonusBalance) {
		this.bonusBalance = bonusBalance;
	}

	public String getBonusSpoint() {
		return bonusSpoint;
	}

	public void setBonusSpoint(String bonusSpoint) {
		this.bonusSpoint = bonusSpoint;
	}

	public String getSelfAmt() {
		return selfAmt;
	}

	public void setSelfAmt(String selfAmt) {
		this.selfAmt = selfAmt;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getTransIncardNo() {
		return transIncardNo;
	}

	public void setTransIncardNo(String transIncardNo) {
		this.transIncardNo = transIncardNo;
	}

	public String getOldTraceNo() {
		return oldTraceNo;
	}

	public void setOldTraceNo(String oldTraceNo) {
		this.oldTraceNo = oldTraceNo;
	}

	public String getOldDate() {
		return oldDate;
	}

	public void setOldDate(String oldDate) {
		this.oldDate = oldDate;
	}

	public String getIisInfo() {
		return iisInfo;
	}

	public void setIisInfo(String iisInfo) {
		this.iisInfo = iisInfo;
	}

	public String getCupInfo() {
		return cupInfo;
	}

	public void setCupInfo(String cupInfo) {
		this.cupInfo = cupInfo;
	}

	public String getAcqInfo() {
		return acqInfo;
	}

	public void setAcqInfo(String acqInfo) {
		this.acqInfo = acqInfo;
	}

	public String getAdsInfo() {
		return adsInfo;
	}

	public void setAdsInfo(String adsInfo) {
		this.adsInfo = adsInfo;
	}

	public String getArqc() {
		return arqc;
	}

	public void setArqc(String arqc) {
		this.arqc = arqc;
	}

	public String getTc() {
		return tc;
	}

	public void setTc(String tc) {
		this.tc = tc;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getCsn() {
		return csn;
	}

	public void setCsn(String csn) {
		this.csn = csn;
	}

	public String getCvm() {
		return cvm;
	}

	public void setCvm(String cvm) {
		this.cvm = cvm;
	}

	public String getTsi() {
		return tsi;
	}

	public void setTsi(String tsi) {
		this.tsi = tsi;
	}

	public String getTvr() {
		return tvr;
	}

	public void setTvr(String tvr) {
		this.tvr = tvr;
	}

	public String getAip() {
		return aip;
	}

	public void setAip(String aip) {
		this.aip = aip;
	}

	public String getTermCap() {
		return termCap;
	}

	public void setTermCap(String termCap) {
		this.termCap = termCap;
	}

	public String getIad() {
		return iad;
	}

	public void setIad(String iad) {
		this.iad = iad;
	}

	

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getHotLine() {
		return hotLine;
	}

	public void setHotLine(String hotLine) {
		this.hotLine = hotLine;
	}

	public String getEleSignTureDate() {
		return eleSignTureDate;
	}

	public void setEleSignTureDate(String eleSignTureDate) {
		this.eleSignTureDate = eleSignTureDate;
	}
	public String getIsCashCard() {
		return isCashCard;
	}
	public void setIsCashCard(String isCashCard) {
		this.isCashCard = isCashCard;
	}

	public String getAtc() {
		return atc;
	}

	public void setAtc(String atc) {
		this.atc = atc;
	}

	public String getUnpr() {
		return unpr;
	}

	public void setUnpr(String unpr) {
		this.unpr = unpr;
	}

	public String getApplab() {
		return applab;
	}

	public void setApplab(String applab) {
		this.applab = applab;
	}

	public String getIsSignature() {
		return isSignature;
	}

	public void setIsSignature(String isSignature) {
		this.isSignature = isSignature;
	}

	public String getCardNoTransIn() {
		return cardNoTransIn;
	}

	public void setCardNoTransIn(String cardNoTransIn) {
		this.cardNoTransIn = cardNoTransIn;
	}

	public String getPrintTopImg() {
		return printTopImg;
	}

	public void setPrintTopImg(String printTopImg) {
		this.printTopImg = printTopImg;
	}
	public String getCardProductId() {
		return cardProductId;
	}

	public void setCardProductId(String cardProductId) {
		this.cardProductId = cardProductId;
	}
	
	
	public String getIsNoSign() {
		return isNoSign;
	}
	
	public void setIsNoSign(String isNoSign) {
		this.isNoSign = isNoSign;
	}
	
	
	public String getIsNoPsw() {
		return isNoPsw;
	}
	
	public void setIsNoPsw(String isNoPsw) {
		this.isNoPsw = isNoPsw;
	}
	

	public String getIsNoSignAndPsw() {
		return isNoSignAndPsw;
	}

	public void setIsNoSignAndPsw(String isNoSignAndPsw) {
		this.isNoSignAndPsw = isNoSignAndPsw;
	}
	
	public String getAmountLimit() {
		return amountLimit;
	}

	public void setAmountLimit(String amountLimit) {
		this.amountLimit = amountLimit;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
}
