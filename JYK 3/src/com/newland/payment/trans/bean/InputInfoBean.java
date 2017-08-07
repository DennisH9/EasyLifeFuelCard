package com.newland.payment.trans.bean;

import com.newland.payment.common.TransConst.InputInfoBeanConst;
import com.newland.pos.sdk.bean.BaseBean;


/**
 * 信息输入
 * @author shipy
 * @date 2015-05-11
 */
public class InputInfoBean extends BaseBean {
	
	private static final long serialVersionUID = 2420063686519498775L;
	
	// 一般字符串 包括姓名
	public static final int INPUT_MODE_STRING=0;
	//只允许数字
	public static final int INPUT_MODE_NUMBER=1;
	//输入密码 没有用到
	public static final int INPUT_MODE_PASSWD=2;
	//只允许输入ASCII
	public static final int INPUT_MODE_ASCII=3;
	/**标题*/
	private String title;
	
	/**内容*/
	private String content;
	
	/**简短提示*/
	private String shortContent;
	
	/**最小长度*/
	private int minLen;
	
	/**最大长度*/
	private int maxLen;
	
	/**是否允许空标记*/
	/**false=0=不可以为空;true=1=可以为空 */
	private boolean emptyFlag;
	
	/**输入模式*/
	/**
	 一般字符串
	INPUT_MODE_STRING=0,
	只允许数字
	INPUT_MODE_NUMBER=1,
	输入密码
	INPUT_MODE_PASSWD=2,	
	*/
	private int mode;
	
	/**超时时间*/
	private int timeOut;
	
	/**输出*/
	private String result;
	
	/**输出长度*/
	private int len;
	
	public InputInfoBean(){
		this.emptyFlag = InputInfoBeanConst.EMPTY_FLAG;
		this.timeOut = InputInfoBeanConst.TIME_OUT;
	}

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

	public int getMinLen() {
		return minLen;
	}

	public void setMinLen(int minLen) {
		this.minLen = minLen;
	}

	public int getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(int maxLen) {
		this.maxLen = maxLen;
	}

	public boolean getEmptyFlag() {
		return emptyFlag;
	}

	public void setEmptyFlag(boolean emptyFlag) {
		this.emptyFlag = emptyFlag;
	}

	public int getMode() {
		return mode;
	}

	/**
	 * 
	 * @param mode
	 * INPUT_MODE_STRING=0,只允许数字
	 * INPUT_MODE_NUMBER=1,输入密码
	 * INPUT_MODE_PASSWD=2,	
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public String getShortContent() {
		return shortContent;
	}

	public void setShortContent(String shortContent) {
		this.shortContent = shortContent;
	}
}
