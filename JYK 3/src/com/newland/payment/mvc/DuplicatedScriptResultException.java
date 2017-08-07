package com.newland.payment.mvc;

/**
 * 重复冲正数据
 * @author linchunhui
 * @date 2015年5月12日 下午5:55:43
 *
 */
public class DuplicatedScriptResultException extends Exception {
	private static final long serialVersionUID = -3847750431444764797L;

	public DuplicatedScriptResultException(String message) {
		super(message);
	}
}
