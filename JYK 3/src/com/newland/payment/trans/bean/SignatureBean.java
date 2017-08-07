package com.newland.payment.trans.bean;

import com.newland.pos.sdk.bean.BaseBean;


/**
 * 信息输入
 * @author shipy
 * @date 2015-05-11
 */
public class SignatureBean extends BaseBean {
	
	private static final long serialVersionUID = -6181056273362883826L;

	/**超时时间*/
	private int timeOut;
	
	/**流水号,用于合成文件名*/
	private String traceCode;
	
	/**特征码,需要在签字板上显示的*/
	private String signatureCode;
	
	/**输出jbig文件路径*/
	private String jbigFileName;
	
	/**输出png文件路径*/
	private String pngFileName;

	private String title;
	
	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public String getTraceCode() {
		return traceCode;
	}

	public void setTraceCode(String traceCode) {
		this.traceCode = traceCode;
	}

	public String getJbigFileName() {
		return jbigFileName;
	}

	public void setJbigFileName(String jbigFileName) {
		this.jbigFileName = jbigFileName;
	}

	public String getPngFileName() {
		return pngFileName;
	}

	public void setPngFileName(String pngFileName) {
		this.pngFileName = pngFileName;
	}

	public String getSignatureCode() {
		return signatureCode;
	}

	public void setSignatureCode(String signatureCode) {
		this.signatureCode = signatureCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


}
