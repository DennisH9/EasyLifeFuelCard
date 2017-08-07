package com.newland.payment.trans;


/**
 * 交易结果监听
 * @author linchunhui
 * @date 2015年5月28日 上午11:03:55
 *
 */
public interface TransResultListener {
	
	/**
	 * 交易失败
	 * @param message 失败信息
	 */
	public void fail(String message);
	
	/**
	 * 交易成功
	 */
	public void succ();
}
