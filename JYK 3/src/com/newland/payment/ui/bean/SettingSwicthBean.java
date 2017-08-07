package com.newland.payment.ui.bean;

import java.io.Serializable;

import com.newland.payment.ui.listener.BooleanValueChangeListener;

/**
 * 开关适配器实体类
 *
 * @author CB
 * @date 2015-5-13 
 * @time 下午7:48:05
 */
@SuppressWarnings("serial")
public class SettingSwicthBean implements Serializable{

	/** 标题资源 */
	private int resTitle;
	/** 存入参数中的key */
	private String key;
	/** 开关改变时监听 */
	private BooleanValueChangeListener booleanValueChangeListener;
	
	public int getResTitle() {
		return resTitle;
	}
	public void setResTitle(int resTitle) {
		this.resTitle = resTitle;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public BooleanValueChangeListener getBooleanValueChangeListener() {
		return booleanValueChangeListener;
	}
	public void setBooleanValueChangeListener(
			BooleanValueChangeListener booleanValueChangeListener) {
		this.booleanValueChangeListener = booleanValueChangeListener;
	}

}
