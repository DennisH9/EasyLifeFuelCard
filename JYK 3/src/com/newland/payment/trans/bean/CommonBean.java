package com.newland.payment.trans.bean;

import com.newland.payment.common.TransConst.CommonBeanConst;
import com.newland.pos.sdk.bean.BaseBean;


/**
 * 通用Bean
 * @version 1.0
 * @author spy
 * @date 2015年5月16日
 * @time 下午12:06:41
 */
public class CommonBean<T> extends BaseBean {
	private static final long serialVersionUID = 4314213589743105611L;
	
	/**值*/
	private T value;
	
	/**标题*/
	private String title;
	
	/**内容*/
	private String content;
	
	/**是否允许空标记*/
	/**false=0=不可以为空;true=1=可以为空 */
	private boolean emptyFlag;

	/** 超时时间 */
	private long timeOut;
	
	/**
	 * 结果
	 */
	private boolean result = false;

	public CommonBean(){
		this.timeOut = CommonBeanConst.TIME_OUT;
	}
	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
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

	public boolean isEmptyFlag() {
		return emptyFlag;
	}

	public void setEmptyFlag(boolean emptyFlag) {
		this.emptyFlag = emptyFlag;
	}
	
}
