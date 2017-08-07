package com.newland.payment.trans.bean.field;

import com.newland.pos.sdk.util.LoggerUtils;


public class ISOField62_Installment {
	//req
	private String m_InstallmentNum;
	private String m_ItemCode;
	private String m_Rfu;
	//resp
	private String m_FirstAmt;
	private String m_Currency;
	private String m_Fee;
	private String m_Bonus;
	private String m_Type;
	private String m_FirstFee;
	private String m_NormalFee;
	private String m_Other;
	private String m_RespField62;
	
	public ISOField62_Installment(){
		m_InstallmentNum = "";
		m_ItemCode = "";
		m_Rfu = "";
		m_FirstAmt = "0";
		m_Currency = "";
		m_Fee = "0";
		m_Bonus = "";
		m_Type = "";
		m_FirstFee = "0";
		m_NormalFee = "0";
		m_Other = "";
		m_RespField62 = "";
	}
	
	public ISOField62_Installment(String installmentWater){
		LoggerUtils.d("installmentWater：" + installmentWater);
		int len = installmentWater.length();
		m_InstallmentNum = installmentWater.substring(0, 2);
		if (len >= 14){
			m_FirstAmt = installmentWater.substring(2, 14);
		}
		if (len >= 17){
			m_Currency = installmentWater.substring(14, 17);
		}
		if (len >= 29){
			m_Fee = installmentWater.substring(17, 29);
		}
		if (len >= 41){
			m_Bonus = installmentWater.substring(29, 41);	
		}
		if (len >= 42){
			m_Type = installmentWater.substring(41,42);
		}
		if (len >= 54){
			m_FirstFee = installmentWater.substring(42, 54);
		}
		
		if (len >= 66){
			m_NormalFee = installmentWater.substring(54, 66);
		}else if (len > 54){
			m_NormalFee = installmentWater.substring(54, len);
		}

		if(len >= 79){
			m_Other = installmentWater.substring(66, 79);
		}else if(len > 66){
			m_Other = installmentWater.substring(66, len);
		}
		
	}
	
	public void setInstallmentNum(String installmentNum){
		m_InstallmentNum = installmentNum;
	}
	
	public void setItemCode(String itemCode){
		m_ItemCode = itemCode;
	}
	
	public void setRfu(String rfu){
		m_Rfu = rfu;
	}
	
	public void setResp(String field62){
		m_RespField62 = field62;
		
	}
	
	public String getInstallmentNum(){
		return	m_InstallmentNum;
	}	
	
	public String getFirstAmt(){
		return	m_FirstAmt;
	}	
	
	public String getCurrency(){
		return	m_Currency;
	}
	
	public String getFee(){
		return	m_Fee;
	}
	
	public String getBonus(){
		return	m_Bonus;
	}
	
	public String getType(){
		return	m_Type;
	}
	
	public String getFirstFee(){
		return	m_FirstFee;
	}
	
	public String getNormalFee(){
		return	m_NormalFee;
	}
	
	public String getOther(){
		return	m_Other;
	}
	
	public String getReq(){
		return m_InstallmentNum + m_ItemCode + m_Rfu;	
	}
	
	public String getResp(){
		return m_InstallmentNum + m_RespField62;
		
	}
}
