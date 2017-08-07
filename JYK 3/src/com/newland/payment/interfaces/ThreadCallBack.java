package com.newland.payment.interfaces;
/**
 * 线程运行回调
 * 
 * @author CB
 * @time 2014年12月25日 下午5:53:33
 */
public interface ThreadCallBack {

	/**
	 * 在后台执行不会阻塞UI
	 */
	void onBackGround();
	
	/**
	 * 在主线程执行，会阻塞UI
	 */
	void onMain();
}
