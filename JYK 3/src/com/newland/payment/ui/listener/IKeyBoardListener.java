package com.newland.payment.ui.listener;


public interface IKeyBoardListener {

//	/**
//	 * 按下时的事件
//	 * 
//	 * @param code ascill码的值
//	 * @return true-自己完成事件，不再执行默认事件 false-自己未完成事件，执行默认事件
//	 */
//	public boolean onTouchDown(int code);

	/**
	 * 按下时的事件
	 * 
	 * @param code ascill码的值
	 * @return true-自己完成事件，不再执行默认事件 false-自己未完成事件，执行默认事件
	 */
	public void onClick(int code);

//	/**
//	 * 按钮弹起来后的事件
//	 * 
//	 * @param code ascill码的值
//	 * @return 自己完成事件，不再执行默认事件 false-自己未完成事件，执行默认事件
//	 */
//	public boolean onTouchUp(int code);

	/**
	 * 当前输入的所有文本
	 * 
	 * @param text
	 */
	public void onChangeText(String text);

	/**
	 * 确认事件
	 */
	public void onEnter();
	/**
	 * 退格事件
	 * @return 
	 */
	public boolean onBackspeace();
	/**
	 * 清除事件
	 */
	public void onClear();
	/**
	 * 取消事件
	 */
	public void onCancel();


}
