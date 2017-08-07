package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newland.payment.R;
import com.newland.payment.common.TransConst;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.bean.EcLoadBean;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 查询圈存日志子界面
 * 
 * @author CB
 * @time 2015-4-21 上午9:24:23
 */
@SuppressLint({ "ValidFragment", "InflateParams" })
public class EcLoadLogItemFragment extends BaseFragment {

	/** 商户名称 */
//	@ViewInject(R.id.txt_merchant_name)
	private TextView txtMerchantName;
	/** P1 */
//	@ViewInject(R.id.txt_p1)
	private TextView txtP1;
	/** P2 */
//	@ViewInject(R.id.txt_p2)
	private TextView txtP2;
	/**圈存前余额*/
//	@ViewInject(R.id.txt_balance_old)
	private TextView txtBalanceOld;
	/** 圈存后余额 */
//	@ViewInject(R.id.txt_balance_new)
	private TextView txtBalanceNew;
	/** 终端国家代码 */
//	@ViewInject(R.id.txt_country_code)
	private TextView txtCountryCode;
	/** 日期 */
//	@ViewInject(R.id.txt_date)
	private TextView txtDate;
	/** act */
//	@ViewInject(R.id.txt_act)
	private TextView txtAtc;
	
	
	/**商户名称*/
	private String strMerchantName;
	/**P1*/
	private String p1;
	/**P2*/
	private String p2;
	/**圈存前余额*/
	private String balanceOld;
	/**圈存后余额*/
	private String balanceNew;	
	/**终端国家代码*/
	private String strCountryCode;
	/**时间日期*/
	private String strDate;
	/**交易计数器*/
	private String strAtc;

	private EcLoadBean selfBean;
	
	private BaseBean parentBean;

	public EcLoadLogItemFragment(EcLoadBean bean, BaseBean parentBean) {
		this.selfBean = bean;
		this.parentBean = parentBean;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			mFragmentView = inflater.inflate(
					R.layout.ec_load_log_item_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {

		if (selfBean != null) {
			
			strMerchantName = selfBean.getMerchantName();
			LoggerUtils.d("111 日期时间："+"20"+ selfBean.getTradeDate() + selfBean.getTradeTime());
			strDate = FormatUtils.timeFormat( "20"+ selfBean.getTradeDate() + selfBean.getTradeTime());
			LoggerUtils.d("111 格式化后：" + strDate);
			strCountryCode = selfBean.getCountryCode();
			LoggerUtils.d("111 交易计数器：" + selfBean.getTradeCount());
			strAtc = selfBean.getTradeCount();
			
			p1 = selfBean.getP1();
			p2 = selfBean.getP2();
			
			String identify = FormatUtils.getCurrencyIdentify(TransConst.CURRENCY_CODE);
			String unit = FormatUtils.getCurrencyUnit(TransConst.CURRENCY_CODE);
			
			balanceOld = identify+" "+FormatUtils.formatAmount(String.valueOf(selfBean.getBalanceOld()))+" "+unit;
			balanceNew = identify+" "+FormatUtils.formatAmount(String.valueOf(selfBean.getBalanceNew()))+" "+unit;
				
		} 

		txtMerchantName.setText(strMerchantName);
		txtCountryCode.setText(strCountryCode);
		txtDate.setText(strDate);
		txtAtc.setText(strAtc);
		txtBalanceOld.setText(balanceOld);
		txtBalanceNew.setText(balanceNew);
		txtP1.setText(p1);
		txtP2.setText(p2);
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
	

}
