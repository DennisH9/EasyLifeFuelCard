package com.newland.payment.trans.bean;

import com.newland.payment.mvc.model.Water;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 交易结果
 * @author linst
 * @date 2015-05-011
 * 
 */
@SuppressWarnings("serial")
public class TransResultBean extends BaseBean{
	
	/** 
	 * 应答码
	 */
	private String respCode;
	/** 
	 * 是否成功
	 */
	private Boolean isSucess;

	/** 标题*/
	private String title;
	
	/** 提示内容 */
	private String content;
	
	/** 是否打印 */
	private Boolean isPrint;

	private int transType = -1;



	/**
	 * 流水
	 * 若需要打印，传流水
	 */
	private Water water;
	public int getTransType() {
		return transType;
	}

	public void setTransType(int transType) {
		this.transType = transType;
	}
	public Boolean getIsPrint() {
		return isPrint;
	}
	public void setIsPrint(Boolean isPrint) {
		this.isPrint = isPrint;
	}
	public Water getWater() {
		return water;
	}
	public void setWater(Water water) {
		this.water = water;
	}
	public Boolean getIsSucess() {
		return isSucess;
	}
	public void setIsSucess(Boolean isSucess) {
		this.isSucess = isSucess;
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
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	
}
