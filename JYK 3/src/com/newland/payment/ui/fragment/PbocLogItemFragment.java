package com.newland.payment.ui.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newland.payment.R;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.bean.PbocDetailBean;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 查询交易明细子界面
 * 
 * @author CB
 * @time 2015-4-21 上午9:24:23
 */
@SuppressLint({ "ValidFragment", "InflateParams" })
public class PbocLogItemFragment extends BaseFragment {

	/** 商户名称 */
//	@ViewInject(R.id.txt_merchant_name)
	TextView txtMerchantName;
	
	/** 商户号 */
//	@ViewInject(R.id.txt_merchant_no)
	TextView txtMerchantNo;

	/**交易类型*/
//	@ViewInject(R.id.txt_trans_type)
	TextView txtTransType;
	
	/**国家代码*/
//	@ViewInject(R.id.txt_country_code)
	TextView txtCountryCode;
	
	/**交易货币代码*/
//	@ViewInject(R.id.txt_currency_code)
	TextView txtCurrencyCode;
	
	/** 日期 */
//	@ViewInject(R.id.txt_date)
	TextView txtDate;

	/** 金额 */
//	@ViewInject(R.id.txt_integer)
	TextView txtAmount;
	/** act */
//	@ViewInject(R.id.txt_act)
	TextView txtAct;
	
//	@ViewInject(R.id.txt_other_integer)
	TextView txtOtherAmount;
	
	private String strMerchantName;

	private String strTransType;
	private String strCountryCode;
	private String strCurrencyCode;
	private String strDate;
	private String strAmount;
	private String strOtherAmount;
	private String strAct;

	private PbocDetailBean selfBean;
	
	private BaseBean parentBean;

	public PbocLogItemFragment(PbocDetailBean bean, BaseBean parentBean) {
		this.selfBean = bean;
		this.parentBean = parentBean;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			mFragmentView = inflater.inflate(
					R.layout.pboc_log_item_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {

		if (selfBean != null) {
			strMerchantName = selfBean.getMerchantName();
			//这个可能需要修改
			//strDate = selfBean.getTradeDate() + selfBean.getTradeTime();
			strDate = FormatUtils.timeFormat( "20"+ selfBean.getTradeDate() + selfBean.getTradeTime());
			
			strTransType = getTransType(selfBean.getTradeType());
			strCountryCode = selfBean.getCountryCode();
			LoggerUtils.d("111 终端国家代码：" + strCountryCode);
			strAct = selfBean.getTradeCount();
			strCurrencyCode = StringUtils.fill(selfBean.getCurrency(), "0", 4, true);
			LoggerUtils.d("111 交易货币代码：" + strCurrencyCode);
			String identify = FormatUtils.getCurrencyIdentify(selfBean.getCurrency());
			String unit = FormatUtils.getCurrencyUnit(selfBean.getCurrency());
			
			if (selfBean.getTradeAmount() != null) {
				strAmount = identify+" "+FormatUtils.formatAmount(String.valueOf(selfBean.getTradeAmount()))+" "+unit;
			}
			if (selfBean.getOtherAmount() != null) {
				strOtherAmount = identify+" "+FormatUtils.formatAmount(String.valueOf(selfBean.getOtherAmount()))+" "+unit;
			}
		} 

		txtDate.setText(strDate);
		txtAmount.setText(strAmount);
		txtOtherAmount.setText(strOtherAmount);
		txtMerchantName.setText(strMerchantName);
		txtTransType.setText(strTransType);
		txtCountryCode.setText(strCountryCode);
		txtCurrencyCode.setText(strCurrencyCode);
		txtAct.setText(strAct);
	}

	@Override
	protected void initClickEvent(View view) {

	}

	@Override
	protected void initEvent() {

	}

	@Override
	public BaseBean getBean() {
		return parentBean;
	}
	
	private String getTransType(String type) {
		String value = "";
		if ("00".equals(type)) {
			value = getResources().getStringArray(R.array.trans_type)[0];
		} else if ("60".equals(type)) {
			value = getResources().getStringArray(R.array.trans_type)[1];
		} else if ("62".equals(type)) {
			value = getResources().getStringArray(R.array.trans_type)[2];
		} else if ("63".equals(type)) {
			value = getResources().getStringArray(R.array.trans_type)[3];
		} else if ("17".equals(type)) {
			value = getResources().getStringArray(R.array.trans_type)[4];
		} else { //未知交易
			value = getResources().getStringArray(R.array.trans_type)[5] + " " + type;
		}
		
		return value;
	}

}
