package com.newland.payment.trans.bean.field;

import java.util.HashMap;
import java.util.Map;

import com.newland.payment.R;
import com.newland.payment.ui.activity.App;
import com.newland.pos.sdk.util.FormatUtils;

public class Field_54 {
	/**
	 * 可用余额的记账方向ASCII码字符“C”表示贷方余额，“D”表示借方余额，
	 */
	private String availableBalanceType;

	private String balanceAmouont;

	private String additionalAmount;

//	public Field_54(String additionalAmount) {
//		this.additionalAmount = additionalAmount;
//		//LogUtil.d("F54:"+BytesUtils.bcdToString(additionalAmount));
//		availableBalanceType = availableBalanceTypeMap.get(getData(0, 2));
//		balanceAmouont = "";
//		String type = getData(4, 3);
//		if ("156".equals(type)) {
//			balanceAmouont = "￥ ";
//		} else if ("999".equals(type)){
//			balanceAmouont = App.getInstance().getResources().getString(R.string.balance_bonus);;
//		}
//		String amount_sign = getData(7, 1);
//		if ("C".equals(amount_sign)) {
//			balanceAmouont += " + ";
//		} else if("D".equals(amount_sign)){
//			balanceAmouont += " - ";
//		}
//		balanceAmouont += FormatUtils.formatAmount(getData(8, 12).trim());
//
//		if ("156".equals(type)) {
//			balanceAmouont += App.getInstance().getResources().getString(R.string.balance_yuan);
//		} else if ("999".equals(type)){
//			balanceAmouont += App.getInstance().getResources().getString(R.string.balance_point);;
//		}
//	}
	public Field_54(String additionalAmount) {
		this.additionalAmount = additionalAmount;
		balanceAmouont = "";
//		balanceAmouont = "￥ ";
		String amount_sign = getData(0, 1);
		if ("C".equals(amount_sign)) {
//			balanceAmouont += " + ";
		} else if("D".equals(amount_sign)){
			balanceAmouont += " - ";
		}
		balanceAmouont += FormatUtils.formatAmount(getData(1, 12).trim());
//		balanceAmouont += App.getInstance().getResources().getString(R.string.balance_yuan);
	}
	private String getData(int index, int length) {
		String data = additionalAmount.substring(index, index + length);
		return data;
	}

	public String getAvailableBalanceType() {
		return availableBalanceType;
	}

	public void setAvailableBalanceType(String availableBalanceType) {
		this.availableBalanceType = availableBalanceType;
	}

	public String getBalanceAmouont() {
		return balanceAmouont;
	}

	public void setBalanceAmouont(String balanceAmouont) {
		this.balanceAmouont = balanceAmouont;
	}

	
	private Map<String, String> availableBalanceTypeMap = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("10", "储蓄账户");
			put("20", "支票账户");
			put("30", "信用卡账户");
			put("90", "积分账户");
		}
	};
}
