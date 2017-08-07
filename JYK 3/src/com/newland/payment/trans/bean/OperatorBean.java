package com.newland.payment.trans.bean;

import com.newland.pos.sdk.bean.BaseBean;

public class OperatorBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String content;
	private String OperatorNo;
	private String password;
	private int passwordLen = 4;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOperatorNo() {
		return OperatorNo;
	}
	public void setOperatorNo(String operatorNo) {
		OperatorNo = operatorNo;
	}
	
	public int getPasswordLen() {
		return passwordLen;
	}
	public void setPasswordLen(int passwordLen) {
		this.passwordLen = passwordLen;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
