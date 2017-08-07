package com.newland.payment.ui.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.newland.base.util.TransUtils;
import com.newland.payment.trans.AbstractBaseTrans;


/**
 * 自定义菜单适配项
 * 
 * @author CB
 * @time 2015-4-20 上午10:20:53
 */
@SuppressWarnings("serial")
public class MainMenuItem implements Serializable {
	/** 名称 */
	private String name;
	/** 配置名称 */
	private String paramsKey;
	/** 图标 */
	private int icon;
	/** 流程 */
	//private AbstractBaseTrans abstractBaseTrans;
	/** 子项,该项不为空时表示为文件夹 */
	private List<MainMenuItem> childs;
	
	private int transType;

	public AbstractBaseTrans getAbstractBaseTrans() {
		return TransUtils.getTransBean(transType);
	}


	public List<MainMenuItem> getChilds() {
		return childs;
	}

	public void setChilds(List<MainMenuItem> childs) {
		this.childs = childs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public MainMenuItem() {
	}

	public String getParamsKey() {
		return paramsKey;
	}

	public void setParamsKey(String paramsKey) {
		this.paramsKey = paramsKey;
	}
	
	public MainMenuItem(String name, int icon, String paramsKey, 
			int transType/*AbstractBaseTrans abstractBaseTrans*/,MainMenuItem...items) {
		this.name = name;
		this.paramsKey = paramsKey;
		this.icon = icon;
		//this.abstractBaseTrans = abstractBaseTrans;
		this.transType = transType;
		if (items != null && items.length > 0) {
			childs = new ArrayList<MainMenuItem>();
			for (int i = 0; i < items.length; i++) {
				childs.add(items[i]);
			}
		}
	}
	
	@Override
	public String toString() {
		return name;
	}

}
