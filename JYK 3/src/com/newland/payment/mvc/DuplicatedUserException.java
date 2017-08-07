package com.newland.payment.mvc;

/**
 * 重复的一般操作员
 * @author linchunhui
 * @date 2015年5月12日 下午2:48:58
 *
 */
public class DuplicatedUserException extends Exception {

	private static final long serialVersionUID = 3211999842233211381L;

	public DuplicatedUserException(String message) {
		super(message);
	}
}
