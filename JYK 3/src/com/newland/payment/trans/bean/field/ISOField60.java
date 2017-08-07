package com.newland.payment.trans.bean.field;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.common.Const;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransType;

public class ISOField60 {
	/**
	 * 60.1 消息类型码 2字节
	 */
	private String funcCode = "";

	/**
	 * 60.2 批次号 6字节
	 */
	private String batchNum = "";

	/**
	 * 60.3 网络管理信息 3字节
	 */
	private String netManCode = "";

	/**
	 * 60.4 终端处理能力 1字节
	 */
	private String termCapacity = "";

	/**
	 * 60.5 基于PBOC借/贷记标准的IC卡条件代码 1字节
	 */
	private String icConditionCode = "";

	/**
	 * 60.6 支持部分扣款和返回余额标志 1字节
	 */
	private String partSaleflag = "";

	/**
	 * 60.7 账户类型 3字节
	 */
	private String accountType = "";

	public ISOField60() {
		funcCode = "00";
		batchNum = ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_BATCHNO);
		netManCode = "";
		termCapacity = "";
		icConditionCode = "";
		partSaleflag = "";
		accountType = "";
	}
	
	public ISOField60(String data) {
		int len = data.length();
		if (len >= 2){
			funcCode = data.substring(0, 2);
		}
		if (len >= 8){
			batchNum = data.substring(2, 8);
		}
		if (len >= 11){
			netManCode = data.substring(8, 11);
		}
		if (len >= 12){
			termCapacity = data.substring(11, 12);
		}
		if (len >= 13){
			icConditionCode = data.substring(12, 13);
		}
		if (len >= 14){
			partSaleflag = data.substring(13, 14);
		}
		if (len >= 17){
			accountType = data.substring(14, 17);
		}
		
	}
	
	public ISOField60(int transType, boolean isFallback) {
		
		batchNum = ParamsUtils.getString( 
				ParamsConst.PARAMS_KEY_BASE_BATCHNO);
		switch (transType) {
		//管理交易
		case TransType.TRANS_BATCHUP:
			funcCode = "00";
			netManCode = "201";
			break;
		case TransType.TRANS_SCRIPT:
			funcCode = "00";
			netManCode = "951";
			break;
		case TransType.TRANS_LOGIN:
			funcCode = "00";
			// 1-双倍长，0-单倍长
			if (Const.DesMode.DES3.equals(ParamsUtils.getString( 
					ParamsConst.PARAMS_KEY_ENCRYPT_MODE))) {
				// 磁道加密  1-加密，0-不支持加密
				if ("1".equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_ENCPYTRACK))) {
					netManCode = "004";
				} else {
					netManCode = "003";
				}
			} else {
				netManCode = "001";
			}
			break;
		case TransType.TRANS_LOGOUT:
			funcCode = "00";
			netManCode = "002";
			break;
		case TransType.TRANS_SETTLE:
			funcCode = "00";
			netManCode = "201";
			break;
		case TransType.TRANS_CASHIER_LOGIN:
			funcCode = "00";
			netManCode = "401";
			break;
		case TransType.TRANS_PARAM_TRANSFER:
			funcCode = "00";
			netManCode = "360";
			break;
		case TransType.TRANS_STATUS_SEND:
			funcCode = "00";
			netManCode = "362";
			break;
		case TransType.TRANS_ECHO:
			funcCode = "00";
			netManCode = "301";
			break;
			//传统交易
		case TransType.TRANS_BALANCE:
			funcCode = "01";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
			break;
		case TransType.TRANS_SALE: 
		case TransType.TRANS_INSERT_CARD_SALE: 
			funcCode = "22";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
//			partSaleflag = "1";
			break;
		case TransType.TRANS_VOID_SALE:	
			funcCode =  "23";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
//			partSaleflag = "1";
			break;
		case TransType.TRANS_REFUND:
			funcCode =  "25";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
//			partSaleflag = "1";
			break;
		case TransType.TRANS_PREAUTH:
			funcCode =  "10";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
			//if (TransAttr.ATTR_MAG == transAttr) {
				//partSaleflag = "0";
			//}
//			partSaleflag = "1";
			break;
   		case TransType.TRANS_VOID_PREAUTH:
   			funcCode = "11";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
   			break;
		case TransType.TRANS_VOID_AUTHSALE:
			funcCode =  "21";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
//			partSaleflag = "1";
			break;
		case TransType.TRANS_AUTHSALE:
			funcCode =  "20";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
//			partSaleflag = "1";
			break;
		case TransType.TRANS_AUTHSALEOFF:
			funcCode =  "24";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
//			partSaleflag = "1";
			break;
			
		case TransType.TRANS_EMV_REFUND:
			funcCode = "27";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
			partSaleflag = "1";
			break;
//			
//		case TransType.TRANS_OFFLINE:
//			funcCode =  "30";
//			break;
//			
//		case TransType.TRANS_ADJUST:
//			funcCode =  "34";
//			break;
//			//订购交易
//		case TransType.TRANS_ORDER_SALE:
//			funcCode = "22";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			
//		case TransType.TRANS_VOID_ORDER_SALE:
//			funcCode =  "23";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			
//		case TransType.TRANS_ORDER_PREAUTH:
//			funcCode =  "10";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;	
//			
//		case TransType.TRANS_VOID_ORDER_PREAUTH:
//			funcCode = "11";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";

//			break;
//			
//		case TransType.TRANS_ORDER_AUTHSALE:
//			funcCode =  "20";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";

//			break;
//			
//		case TransType.TRANS_VOID_ORDER_AUTHSALE:
//			funcCode =  "21";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			
//		case TransType.TRANS_ORDER_AUTHSALEOFF:
//			funcCode =  "24";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//				
//		case TransType.TRANS_ORDER_REFUND:
//			funcCode =  "25";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//				
//		case TransType.TRANS_ORDER_CVM:
//			funcCode = "01";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			//积分交易
//		case TransType.TRANS_BONUS_IIS_SALE:
//			funcCode = "22";
//			accountType = "048";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//		case TransType.TRANS_BONUS_ALLIANCE:
//			funcCode = "22";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			accountType = "065";
//			break;
//		case TransType.TRANS_VOID_BONUS_IIS_SALE:
//			funcCode = "23";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			accountType = "048";
//			break;
//		case TransType.TRANS_VOID_BONUS_ALLIANCE:
//			funcCode = "23";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			accountType = "065";
//			break;
//		case TransType.TRANS_ALLIANCE_BALANCE:
//			funcCode = "03";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			accountType = "065";
//			break;
//		case TransType.TRANS_ALLIANCE_REFUND:
//			funcCode = "25";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			accountType = "065";
//			break;
//
//
//			//分期交易
//		case TransType.TRANS_INSTALMENT:
//			funcCode =  "22";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			
//		case TransType.TRANS_VOID_INSTALMENT:
//			funcCode =  "23";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
			
		//电子现金交易
		case TransType.TRANS_EC_LOAD:
			funcCode = "45";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
//			partSaleflag = "1";
			break;
			
		case TransType.TRANS_EC_LOAD_CASH:
			funcCode = "46";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
//			partSaleflag = "1";
			break;
			
		case TransType.TRANS_EC_LOAD_NOT_BIND:
			funcCode = "47";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
//			partSaleflag = "1";
			break;
			
		case TransType.TRANS_EC_VOID_LOAD_CASH:
			funcCode = "51";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
//			partSaleflag = "1";
			break;
			
		case TransType.TRANS_EC_PURCHASE:
			funcCode =  "22";
			netManCode = "000";
			termCapacity = "6";
			icConditionCode = "0";
			partSaleflag = "1";
			break;
			
//			//磁卡预付费卡交易
//		case TransType.TRANS_MAG_LOAD_CASH:
//			funcCode = "48";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//		case TransType.TRANS_MAG_LOAD_CONFIRM:
//			funcCode = "48";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			
//		case TransType.TRANS_MAG_LOAD_ACCOUNT:
//			funcCode = "49";
//			break;
//			
//		case TransType.TRANS_MAG_ACCOUNT_VERIFY:
//			funcCode = "01";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			
//			//预约消费
//		case TransType.TRANS_VOID_APPOINTMENT_SALE:
//			funcCode = "53";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			
//		case TransType.TRANS_APPOINTMENT_SALE:
//			funcCode = "54";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			
//		//手机芯片交易	
//		case TransType.TRANS_VOID_PHONE_AUTHSALE:
//			funcCode =  "21";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			
//		case TransType.TRANS_PHONE_SALE:
//			funcCode = "22";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			
//		case TransType.TRANS_VOID_PHONE_SALE:
//			funcCode =  "23";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			
//		case TransType.TRANS_PHONE_AUTHSALEOFF:
//			funcCode =  "24";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//			
//		case TransType.TRANS_REFUND_PHONE_SALE:
//			funcCode =  "25";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//		case TransType.TRANS_PHONE_BALANCE:
//			funcCode = "01";
//			funcCode =  "25";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//		case TransType.TRANS_PHONE_PREAUTH:
//			funcCode =  "10";
//			funcCode =  "25";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
//   		case TransType.TRANS_VOID_PHONE_PREAUTH:
//   			funcCode = "11";
//   			funcCode =  "25";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//   			break;	
//		case TransType.TRANS_PHONE_AUTHSALE:
//			funcCode =  "20";
//			funcCode =  "25";
//			netManCode = "000";
//			termCapacity = "6";
//			icConditionCode = "0";
//			partSaleflag = "1";
//			break;
		default:
			funcCode = "00";
			netManCode = "000";
			break;
		}
		
		if (isFallback){
			if (!icConditionCode.isEmpty()){
				icConditionCode = "2";
			}
		}
		
	}
	
	public String getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getNetManCode() {
		return netManCode;
	}

	public void setNetManCode(String netManCode) {
		this.netManCode = netManCode;
	}

	public String getTermCapacity() {
		return termCapacity;
	}

	public void setTermCapacity(String termCapacity) {
		this.termCapacity = termCapacity;
	}

	public String getIcConditionCode() {
		return icConditionCode;
	}

	public void setIcConditionCode(String icConditionCode) {
		this.icConditionCode = icConditionCode;
	}

	public String getPartSaleflag() {
		return partSaleflag;
	}

	public void setPartSaleflag(String partSaleflag) {
		this.partSaleflag = partSaleflag;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getString() {
			return funcCode + batchNum + netManCode + termCapacity + icConditionCode
					+ partSaleflag + accountType;
	}
}