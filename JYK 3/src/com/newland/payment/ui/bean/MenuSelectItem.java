package com.newland.payment.ui.bean;

import java.io.Serializable;


/**
 * 菜单选择适配项
 * 
 * @author CB
 * @time 2015-4-20 上午10:20:53
 */
@SuppressWarnings("serial")
public class MenuSelectItem implements Serializable {
	/** 名称 */
	private String name;
	/** 值 */
	private Object value;
	/** 是否选中 */
	private boolean check;

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MenuSelectItem() {
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}
	
	public MenuSelectItem(String name, boolean check) {
		this.name = name;
		this.check = check;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MenuSelectItem [name=");
		builder.append(name);
		builder.append(", check=");
		builder.append(check);
		builder.append("]");
		return builder.toString();
	}



}
