package com.newland.payment.trans.bean.field;

import com.newland.pos.sdk.util.LoggerUtils;


public class ISOField62_Bonus {
	//req
	private String m_ItemCode;
	
	//resp
	private String m_BonusPoint;
	private String m_SelfAmt;
	private String m_BonusBalance;
	private String m_Rfu;
	private String m_Field62;
	
	public ISOField62_Bonus(){
		m_ItemCode = "";
		m_Rfu = "";
		m_BonusPoint = "";
		m_SelfAmt = "0";
		m_BonusBalance = "0";
		m_Field62 = "";
	}
	
	public ISOField62_Bonus(String bonusWater){
		LoggerUtils.d("bonusWater：" + bonusWater);
		int len = bonusWater.length();
		if (len >= 30){
			m_ItemCode = bonusWater.substring(0, 30);
		}
		if (len >= 40){
			m_BonusPoint = bonusWater.substring(30, 40);
		}
		if (len >= 52){
			m_SelfAmt = bonusWater.substring(40, 52);
		}
		if (len >= 64){
			m_BonusBalance = bonusWater.substring(52, 64);
		}else if (len > 52){
			m_BonusBalance = bonusWater.substring(52, len);
		}
	}
	
	public void setItemCode(String itemCode){
		m_ItemCode = itemCode;
	}
	
	public String getItemCode(){
		return m_ItemCode;
	}
	
	public void setRfu(String rfu){
		m_Rfu = rfu;
	}
		
	public String getReq(){
		return m_ItemCode + m_Rfu;	
	}
	
	
	public String getBonusPoint(){
		return m_BonusPoint;
	}
	
	public String getSelfAmt(){
		return m_SelfAmt;
	}
	
	public String getBonusBalance(){
		return m_BonusBalance;
	}
	
	public void setBonusBalance(String bonusBalance){
		m_BonusBalance = bonusBalance;
	}
	
	public void setBonusPoint(String bonusPoint){
		m_BonusPoint = bonusPoint;
	}
	
	public void setSelfAmt(String selfAmt){
		m_SelfAmt = selfAmt;
	}
	
	public void setField62(String field62){
		m_Field62 = field62;
	}
	
	public String getResp(){
		return m_ItemCode + m_Field62 + m_BonusBalance;
	}
}
