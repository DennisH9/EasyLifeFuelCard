package com.newland.payment.trans.bean;

/**
 * 交易步骤Bean对象异常
 * @author chenkh
 * @date 2015-5-14
 * @time 下午3:32:51
 *
 */
public class TransStepBeanException extends Exception {

	private static final long serialVersionUID = 2897556529249079426L;

	public TransStepBeanException(String message) {
		super(message);
	}
}
