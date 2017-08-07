package com.newland.payment.trans.invoke.listener;

import android.content.Intent;


/**
 * 第三方调用结果监听
 * @author linchunhui
 * @date 2015年5月28日 上午10:50:27
 *
 */
public interface InvokeResultListener {

	/**
	 * 失败监听
	 * @param message
	 */
	public void fail(String message);
	
	/**
	 * 成功监听
	 * @param intent
	 */
	public void succ(Intent intent);
}
