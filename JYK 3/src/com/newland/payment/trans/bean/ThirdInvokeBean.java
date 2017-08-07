package com.newland.payment.trans.bean;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;

import com.newland.pos.sdk.bean.AmountBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

public class ThirdInvokeBean {
	
	Map<String, String> map = new HashMap<String, String>();
	
	public final static String STORENO = "StoreNumber";
	
	public final static String OPER = "operatorNo";
	
	public final static String CARDNO = "CardNumber";
	
	public final static String CARDTYPE = "CardType";
	
	public final static String TRANSTYPE = "transType";
	
	public final static String AMOUNT = "amount";
	
	public final static String OLDTRACENO = "oriTraceNo";
	
	public final static String OLDVOUCHERNO = "oriVoucherNo";

	public final static String VOUCHERNO = "voucherNo";
	
	public final static String AUTHCODE = "oriAuthCode";
	
	public final static String OLDREFNO = "HostserialNumber";
	
	public final static String OLDDATE = "oriDate";
	
	public final static String EXPDATE = "ExpireDate";
	
	public final static String MEMO = "MeMo";
	
	public final static String TRANSID = "TransId";
	
	public final static String TIPS = "Tips";
	
	public final static String TOTAL = "total";
	
	public final static String BALANCEAMOUNT = "BalanceAmount";
	
	public final static String TRACENO = "PosTraceNumber";
	
	public final static String BATCHNO = "BatchNumber";
	
	public final static String MERCHANTNO = "MerchantNumber ";
	
	public final static String MERCHANTNAME = "MerchantName";
	
	public final static String TERMINALNO = "TerminalNumber";
	
	public final static String RESPCODE = "RejCode";
	
	public final static String ISSNO = "IssNumber";
	
	public final static String ISSNAME = "IssName";
	
	public final static String OLDTIME = "TransTime";
	
	public final static String RESPEXPLAIN = "RejCodeExplain";
	
	public final static String OLDREFERENCENO = "oriReferenceNo";

	public final static String PRICE = "price";

	public final static String OILTYPE = "oilType";

	public final static String LITER = "liter";

	public final static String MACKEY = "macKey";

	public final static String IP = "ip";

	public final static String PORT = "port";

	public final static String GASNO = "gasNo";

	public final static String REFUELERNO = "refuelerNo";

	public final static String OILTYPENO = "oilTypeNo";

	public final static String POSNO = "posNo";

	public final static String OILWEIGHT = "oilWeight";



	private String respExplain;
	
	private String oldTime;
	
	private String issName;
	
	private String issNo;
	
	private String respCode;
	
	private String terminalNo;
	
	private String mechantName;
	
	private String mechantNo;
	
	private String batchNo;
	
	private String traceNo;
	
	private String balanceAmount;
	
	private String total;
	
	private String tip;
	            
	private AmountBean amountBean = null;
	
	private String storeNo;
	
	private String operator;
	
	private String cardNo;
	
	private String cardType;
	
	private int transType;
	
	private String amount;
	
	private String oldTraceNo;
	
	private String oriVoucherNo;
	
	private String authCode;
	
	private String oldRefNo;
	
	private String oldDate;
	
	private String expDate;
	
	private String meMo;
	
	private String transId;
	
	private String payAuthCode;
	
	private String oriReferenceNo;



	private Long price;

	private String oilType;

	private Long liter;

	private String macKey;

	private String ip;

	private String port;

	private String gasNo;

	private String refuelerNo;

	private String oilTypeNo;

	private String posNo;

	private Long oilWeight;

	public ThirdInvokeBean(Intent intent){
		if(intent == null){
			return;
		}
		
//		Bundle bundle = intent.getExtras();
//		
//		SerializableMap myMap = (SerializableMap)bundle.get("map");
//		
//		this.posNo = myMap.getMap().get(POSNO);
//		this.storeNo = myMap.getMap().get(STORENO);
//		this.operator = myMap.getMap().get(OPER);
//		this.cardNo = myMap.getMap().get(CARDNO);
//		this.cardType = myMap.getMap().get(CARDTYPE);
//		this.transType = myMap.getMap().get(TRANSTYPE);
//		this.amount = myMap.getMap().get(AMOUNT);
//		this.oldTraceNo = myMap.getMap().get(OLDTRACENO);
//		LoggerUtils.d("lxb ---getoldTraceNo: " + this.oldTraceNo);
//		
//		this.authCode = myMap.getMap().get(AUTHCODE);
//		this.oldRefNo = myMap.getMap().get(OLDREFNO);
//		this.oldDate = myMap.getMap().get(OLDDATE);
//		this.expDate = myMap.getMap().get(EXPDATE);
//		this.meMo = myMap.getMap().get(MEMO);
//		this.transId = myMap.getMap().get(TRANSID);
//		
//		amountBean = getAmountBean(myMap.getMap());
		
		LoggerUtils.d("lxb getAmonut 111:"+ intent.getLongExtra(AMOUNT, -1));
		
		if (intent.getLongExtra(AMOUNT, -1) != -1) {
			AmountBean amountBean = getAmountBean(intent);
			this.amountBean = amountBean;
		}
		
		this.transType = intent.getIntExtra(TRANSTYPE, 0);
		this.operator = intent.getStringExtra(OPER);
		//this.amount = intent.getStringExtra(AMOUNT);
		this.oldTraceNo = intent.getStringExtra(OLDTRACENO);
		this.authCode = intent.getStringExtra(AUTHCODE);
		this.oldDate = intent.getStringExtra(OLDDATE);
		this.oriReferenceNo = intent.getStringExtra(OLDREFERENCENO);
		this.traceNo = intent.getStringExtra(VOUCHERNO);
		this.oriVoucherNo = intent.getStringExtra(OLDVOUCHERNO);

		this.price = intent.getLongExtra(PRICE,-1);
		this.oilType = intent.getStringExtra(OILTYPE);
		this.liter = intent.getLongExtra(LITER,-1);
		this.macKey = intent.getStringExtra(MACKEY);

		this.ip = intent.getStringExtra(IP);
		this.port = intent.getStringExtra(PORT);
		this.gasNo = intent.getStringExtra(GASNO);
		this.refuelerNo = intent.getStringExtra(REFUELERNO);
		this.oilTypeNo = intent.getStringExtra(OILTYPENO);
		this.posNo = intent.getStringExtra(POSNO);
		this.oilWeight = intent.getLongExtra(OILWEIGHT,-1);
//		this.oriAuthCode = intent.getStringExtra(OLD_AUTHCODE_TAG);
//		this.oriTraceNo = intent.getStringExtra(OLD_TRACENO_TAG);
//		this.oriTransTime = intent.getStringExtra(OLD_TRANS_TIME_TAG);
//		this.oriReferenceNo = intent.getStringExtra(OLD_REFNO_TAG);
//		this.responseCode = intent.getStringExtra(RESP_TAG);
//		this.message = intent.getStringExtra(MESS_TAG);
//		this.transTime = intent.getStringExtra(TRANS_TIME_TAG);
//		this.cardId = intent.getStringExtra(CARD_ID_TAG);
//		this.traceNo = intent.getStringExtra(TRACENO_TAG);
//		this.referenceNo = intent.getStringExtra(REFNO_TAG);
//		this.oriDate = intent.getStringExtra(OLD_DATE_TAG);
		
		//根据需求自己添加
	}
	
	public ThirdInvokeBean() {
		
	}

	public AmountBean getAmountBean(){
		return amountBean;
	}
	
	private AmountBean getAmountBean(Intent intent) {
		AmountBean amountBean = new AmountBean();
		amountBean.setThirdInvoke(true);
		long amount = intent.getLongExtra("amount", -1);
		LoggerUtils.d("lxb getAmonut :"+ amount);
		if (amount == -1) {
			return null;
		}
		
		amountBean.setAmount(amount);
		
		String currency = intent.getStringExtra("currenty");
		if (currency == null) {
			currency = "156";
		}
		amountBean.setCurrency(currency);
		
		int decimalsNum = intent.getIntExtra("decimalsNum", 2);
		if (decimalsNum < 0) {
			decimalsNum = 2;
		}
		amountBean.setFormat(decimalsNum);
		
		
		return amountBean;
		
	}
	
	private AmountBean getAmountBean(Map<String, String> map) {
		AmountBean amountBean = new AmountBean();
		amountBean.setThirdInvoke(true);
		
		String amountString = map.get(AMOUNT);
		if(StringUtils.isNullOrEmpty(amountString)){
			return null;
		}
		System.out.println(amountString);
		long amount = Long.parseLong(amountString);
		System.out.println(amount);
		if (amount == 0) {
			return null;
		}	
		amountBean.setAmount(amount);
		
		String currency = map.get("currenty");
		if (currency == null) {
			currency = "156";
		}
		amountBean.setCurrency(currency);
		
		int decimalsNum;
		if(StringUtils.isNullOrEmpty(map.get("decimalsNum"))){
			decimalsNum = 2;
		}else{
			decimalsNum = Integer.valueOf(map.get("decimalsNum"));
			if (decimalsNum < 0) {
				decimalsNum = 2;
			}			
		}
		amountBean.setFormat(decimalsNum);
		LoggerUtils.d("111 amount:"+amount+"\ncurrenty:"+currency+"\ndec:"+decimalsNum);		
		
		return amountBean;
		
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public int getTransType() {
		return transType;
	}

	public void setTransType(int transType) {
		this.transType = transType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOldTraceNo() {
		return oldTraceNo;
	}

	public void setOldTraceNo(String oldTraceNo) {
		this.oldTraceNo = oldTraceNo;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getOldRefNo() {
		return oldRefNo;
	}

	public void setOldRefNo(String oldRefNo) {
		this.oldRefNo = oldRefNo;
	}

	public String getOldDate() {
		return oldDate;
	}

	public void setOldDate(String oldDate) {
		this.oldDate = oldDate;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getMeMo() {
		return meMo;
	}

	public void setMeMo(String meMo) {
		this.meMo = meMo;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getOriVoucherNo() {
		return oriVoucherNo;
	}

	public void setOriVoucherNo(String oriVoucherNo) {
		this.oriVoucherNo = oriVoucherNo;
	}

	public String getPayAuthCode() {
		return payAuthCode;
	}

	public void setPayAuthCode(String payAuthCode) {
		this.payAuthCode = payAuthCode;
	}

	public void setAmountBean(AmountBean amountBean) {
		this.amountBean = amountBean;
	}

	public String getRespExplain() {
		return respExplain;
	}

	public void setRespExplain(String respExplain) {
		this.respExplain = respExplain;
	}

	public String getOldTime() {
		return oldTime;
	}

	public void setOldTime(String oldTime) {
		this.oldTime = oldTime;
	}

	public String getIssName() {
		return issName;
	}

	public void setIssName(String issName) {
		this.issName = issName;
	}

	public String getIssNo() {
		return issNo;
	}

	public void setIssNo(String issNo) {
		this.issNo = issNo;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getMechantName() {
		return mechantName;
	}

	public void setMechantName(String mechantName) {
		this.mechantName = mechantName;
	}

	public String getMechantNo() {
		return mechantNo;
	}

	public void setMechantNo(String mechantNo) {
		this.mechantNo = mechantNo;
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

	public String getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(String balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getOldRefnum() {
		return oriReferenceNo;
	}

	public void setOldRefnum(String oriReferenceNo) {
		this.oriReferenceNo = oriReferenceNo;
	}

	public String getMacKey() {
		return macKey;
	}

	public void setMacKey(String macKey) {
		this.macKey = macKey;
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

	public String getPosNo() {
		return posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}

	public Long getOilWeight() {
		return oilWeight;
	}

	public void setOilWeight(Long oilWeight) {
		this.oilWeight = oilWeight;
	}

	@Override
	public String toString() {
		return "ThirdInvokeBean [respExplain=" + respExplain + ", oldTime="
				+ oldTime + ", issName=" + issName + ", issNo=" + issNo
				+ ", respCode=" + respCode + ", terminalNo=" + terminalNo
				+ ", mechantName=" + mechantName + ", mechantNo=" + mechantNo
				+ ", batchNo=" + batchNo + ", traceNo=" + traceNo
				+ ", balanceAmount=" + balanceAmount + ", total=" + total
				+ ", tip=" + tip + ", amountBean=" + amountBean + ", posNo="
				+ posNo + ", storeNo=" + storeNo + ", operator=" + operator
				+ ", cardNo=" + cardNo + ", cardType=" + cardType
				+ ", transType=" + transType + ", amount=" + amount
				+ ", oldTraceNo=" + oldTraceNo + ", authCode=" + authCode
				+ ", oldRefNo=" + oldRefNo + ", oldDate=" + oldDate
				+ ", expDate=" + expDate + ", meMo=" + meMo + ", transId="
				+ transId + ", payAuthCode=" + payAuthCode + "]";
	}
	

	
}
