package com.newland.payment.mvc.service;

/**
 * 通用数据库作
 * @version 1.0
 * @author spy
 * @date 2015年5月14日
 * @time 下午3:15:54
 */
public interface CommonDBService {

	/**
	 * 重建数据库
	 */
	public void recreateDatabase();
	
	/**
	 * type=0 Water 清除交易流水
	 * type=1 ReverseWater 清除冲正数据/冲正流水
	 * type=2 ScriptResult 清除脚本数据
	 * 后面定义常量
	 * @param type
	 */
	public void cleanWater(CleanType type);
	
	public static enum CleanType{
		WATER,
		REVERSE_WATER,
		SCRIPT_RESULT,
		SETTLEMENT,
		EMV_FAIL_WATER
	}
}
