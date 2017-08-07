package com.newland.payment.trans.bean.field;

import java.io.UnsupportedEncodingException;

import com.newland.pos.sdk.util.StringUtils;

/**
 * 62域用法12
 * @version 1.0
 * @author spy
 * @date 2015年5月19日
 * @time 下午9:59:44
 */
public class ISOField62_Usage12 {
	/**
	 * 信息类别
	 */
	private String infoType = "92";
	/**
	 * CVN2
	 */
	private String cvn2; 
	/**
	 * 身份证后六位
	 */
	private String idLast6;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 预约号
	 */
	private String appointmentNo;
	
	private static final String CV = "CV";
	private static final String SF = "SF";
	private static final String TX = "TX";
	private static final String NM = "NM";
	
	public ISOField62_Usage12(){
		infoType = "92";
		cvn2 = "";
		idLast6 = "";
		phone = "";
		name = "";
		appointmentNo = "";
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getCvn2() {
		return cvn2;
	}
	public void setCvn2(String cvn2) {
		this.cvn2 = cvn2;
	}
	public String getIdLast6() {
		return idLast6;
	}
	public void setIdLast6(String idLast6) {
		this.idLast6 = idLast6;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getString(){
		return getCardHoldAuthInfo();
	}
	
	public String getAppointmentNo() {
		return appointmentNo;
	}
	
	public void setAppointmentNo(String appointmentNo) {
		this.appointmentNo = appointmentNo;
	}
	
	public boolean isEmpty(){
		if(!StringUtils.isEmpty(cvn2)){
			return false;
		}
		if(!StringUtils.isEmpty(idLast6)){
			return false;
		}
		if(!StringUtils.isEmpty(name)){
			return false;
		}
		if(!StringUtils.isEmpty(phone)){
			return false;
		}
		return true;
	}

	/**
	 * 获取持卡人身份认证信息
	 */
	private String getCardHoldAuthInfo (){
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isEmpty(cvn2) 
				&& StringUtils.isEmpty(idLast6)
				&& StringUtils.isEmpty(phone)
				&& StringUtils.isEmpty(name)){
			return "";
		}
		sb.append(infoType);
		if(!StringUtils.isEmpty(cvn2)){
			sb.append(CV);
			sb.append(String.format("%03d", cvn2.length()));
			sb.append(cvn2);
		}
		if(!StringUtils.isEmpty(idLast6)){
			sb.append(SF);
			sb.append(String.format("%03d", idLast6.length()));
			sb.append(idLast6);
		}
		if(!StringUtils.isEmpty(phone)){
			sb.append(TX);
			sb.append(String.format("%03d", phone.length()));
			sb.append(phone);
		}
		if(!StringUtils.isEmpty(name)){
			try {
				String len = String.format("%03d", name.getBytes("GBK").length);
				sb.append(NM);
				sb.append(len);
				sb.append(name);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}