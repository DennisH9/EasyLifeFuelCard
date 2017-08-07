package com.newland.payment.trans;

public class NoStepMethodException extends Exception {
	private static final long serialVersionUID = -7126506707986956349L;
	private String code;
	public NoStepMethodException(String code, String message) {
		super(message);
		this.code = code;
	}
	
	public NoStepMethodException(String message) {
		super(message);
		this.code = "0000";
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
