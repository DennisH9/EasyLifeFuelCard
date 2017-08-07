package com.newland.payment.trans.invoke;

/**
 * 第三方调用时的参数异常
 * @author linchunhui
 * @date 2015年5月28日 上午10:26:44
 *
 */
public class InvokeParamException extends Exception {
	private static final long serialVersionUID = 4247805902396200184L;

	public InvokeParamException(String message){
		super(message);
	}
}
