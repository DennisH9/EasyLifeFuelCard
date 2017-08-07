package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.AmountEditText;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.payment.ui.view.SettingSwitchView;
import com.newland.pos.sdk.bean.BaseBean;

public class SettingQPSFragment extends BaseSettingFragment {

//	@ViewInject(R.id.ssv_is_support_no_psw)
	SettingSwitchView ssvNoPsw;
	
//	@ViewInject(R.id.ssv_is_support_no_sign)
	SettingSwitchView ssvNoSign;
	
//	@ViewInject(R.id.ssv_is_support_cdcvm)
	SettingSwitchView ssvCDCVM;
	
//	@ViewInject(R.id.ssv_is_bin_a)
	SettingSwitchView ssvBinA;
	
//	@ViewInject(R.id.ssv_is_bin_b)
	SettingSwitchView ssvBinB;
	
//	@ViewInject(R.id.aev_no_psw_limit)
	AmountEditText aevNoPswLimit;
	
//	@ViewInject(R.id.aev_no_sign_limit)
	AmountEditText aevNoSignLimit;

//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;
	
	private AbstractKeyBoardListener keyBoardListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_qps_fragment,null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
		setTitle(R.string.common_qps_manage);
		
		ssvNoPsw.setParamData(R.string.common_qps_no_psw, ParamsConst.PARAMS_KEY_QPS_IS_NO_PSW);
		ssvNoSign.setParamData(R.string.common_qps_no_sign, ParamsConst.PARAMS_KEY_QPS_IS_NO_SINGNATURE);
		ssvCDCVM.setParamData(R.string.common_qps_cdcvm, ParamsConst.PARAMS_KEY_QPS_IS_CDCVM);
		ssvBinA.setParamData(R.string.common_qps_bin_a, ParamsConst.PARAMS_KEY_QPS_CARD_BIN_A);
		ssvBinB.setParamData(R.string.common_qps_bin_b, ParamsConst.PARAMS_KEY_QPS_CARD_BIN_B);
		
		aevNoPswLimit.setParam(context, ParamsConst.PARAMS_KEY_QPS_NO_PSW_LIMIT, keyboardNumber);
		aevNoPswLimit.setTitle(R.string.common_qps_no_psw_limit);
		
		aevNoSignLimit.setParam(context, ParamsConst.PARAMS_KEY_QPS_NO_SINGNATURE_LIMIT, keyboardNumber);
		aevNoSignLimit.setTitle(R.string.common_qps_no_sign_limit);
	}

	@Override
	protected void initClickEvent(View view) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	protected BaseBean getBean() {
		// TODO Auto-generated method stub
		return null;
	}

}
