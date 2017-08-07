package com.newland.payment.trans.bean.field;

import com.newland.pos.sdk.util.LoggerUtils;

public class ISOField44 {

	/**
	 * 44.1发卡行标识码,接收机构标识码(返回包时用) 11个字节
	 */
	private String iisCode;

	/**
	 * 44.2收单机构标识码,商户结算行标识码(返回包时用) 11个字节
	 */
	private String acqCode;

	private String cardType;

	public void setField(String fieldData) {
		LoggerUtils.d("field44 :"+fieldData);
		this.iisCode = fieldData.substring(0, 11).trim();
		this.acqCode = fieldData.substring(11, 22).trim();
		LoggerUtils.d("field44 len:"+fieldData.length());
		if(fieldData.length() > 22){
			cardType = fieldData.substring(22, fieldData.length());
			LoggerUtils.d("field44 cardType:"+cardType);
		}
	}

	public String getIisCode() {
		return iisCode;
	}

	public void setIisCode(String iisCode) {
		this.iisCode = iisCode;
	}

	public String getAcqCode() {
		return acqCode;
	}

	public void setAcqCode(String acqCode) {
		this.acqCode = acqCode;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
}