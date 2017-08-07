package com.newland.payment.common;

public class PrintType {
	/** POS签购单/故障报告单 不带英文 */
	public static final String PRINT_WATER_WITHOUT_ENGLISH = "PRINT_WATER_WITHOUT_ENGLISH";
	
	/** POS签购单/故障报告单 带英文 */
	public static final String PRINT_WATER_WITH_ENGLISH = "PRINT_WATER_WITH_ENGLISH";
	
	public enum PrintWaterType {
		/**
		 * 商户联
		 */
		MERCHANT,
		
		/**
		 * 银行联
		 */
		BANK,
		
		/**
		 * 客户联
		 */
		CUSTOMER,
	}
	
}
