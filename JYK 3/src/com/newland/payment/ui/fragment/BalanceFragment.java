package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newland.payment.R;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 余额查询详情页
 *
 * @author CB
 * @time 2015-4-23 上午11:35:56
 */
@SuppressLint("ValidFragment")
public class BalanceFragment extends BaseFragment{

	/** 卡号 */
//	@ViewInject(R.id.txt_card_no)
	TextView txtCardNo;
	/** 金额 */
//	@ViewInject(R.id.txt_money)
	TextView txtMoney;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.balance_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		txtCardNo = (TextView)mFragmentView.findViewById(R.id.txt_card_no);
		txtMoney = (TextView)mFragmentView.findViewById(R.id.txt_money);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		setTitle(R.string.common_check_balance);
	}

	@Override
	protected void initClickEvent(View view) {
		
	}

	@Override
	protected void initEvent() {
	}

	@Override
	public BaseBean getBean() {
		return null;
	}

}
