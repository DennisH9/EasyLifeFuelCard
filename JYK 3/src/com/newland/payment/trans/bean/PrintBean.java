package com.newland.payment.trans.bean;

import com.newland.payment.common.Const.PrintStyleConstEnum;
import com.newland.payment.mvc.model.Water;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 打印结果
 * @author lst
 * @date 20150608
 */
public class PrintBean extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 打印类型
	 */
	private  PrintStyleConstEnum printType;
	
	/**
	 * 打印提示
	 */
	private String printMessage;
	
	/**
	 * 打印流水
	 */
	private Water water;

	/**
	 * 打印结果
	 */
	private boolean printResult;

	public PrintStyleConstEnum getPrintType() {
		return printType;
	}

	public void setPrintType(PrintStyleConstEnum printType) {
		this.printType = printType;
	}

	public String getPrintMessage() {
		return printMessage;
	}

	public void setPrintMessage(String printMessage) {
		this.printMessage = printMessage;
	}

	public Water getWater() {
		return water;
	}

	public void setWater(Water water) {
		this.water = water;
	}

	public boolean getPrintResult() {
		return printResult;
	}

	public void setPrintResult(Boolean printResult) {
		this.printResult = printResult;
	}
	
	
}
