package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.payment.ui.view.SettingEditView;
import com.newland.payment.ui.view.SettingSwitchView;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 电子签名设置
 *
 * @author CB
 * @date 2015-5-15 
 * @time 下午5:38:29
 */
public class SettingOtherElecSignFragment extends BaseSettingFragment{
	

	/** 电子签名签字超时时间 */
//	@ViewInject(R.id.sev_elec_timeout)
	SettingEditView sevElecTimeout;
	/** 电子签名重签次数 */
//	@ViewInject(R.id.sev_elec_resign_times)
	SettingEditView sevElecResignTimes;
	/** 电子签名最大笔数 */
//	@ViewInject(R.id.sev_max_elec_count)
	SettingEditView sevMaxElecCount;
	
	/** 是否支持电子签名 */
//	@ViewInject(R.id.ssv_is_support_elecsign)
	SettingSwitchView ssvIsSupportElecsign;
	/** 确认签名 */
//	@ViewInject(R.id.ssv_is_confirm_elecsign)
	SettingSwitchView ssvIsConfirmElecsign;
	/** 是否输入手机号 */
//	@ViewInject(R.id.ssv_is_elecsign_input_phone)
	SettingSwitchView ssvIsElecsignInputPhone;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_other_elec_sign_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {

		addCheckView(sevElecResignTimes);
		addCheckView(sevElecTimeout);
		addCheckView(sevMaxElecCount);
		
		setTitle(R.string.setting_other_elecsign);
		
		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());

		sevElecTimeout.setTitle(R.string.elec_timeout);
		sevElecResignTimes.setTitle(R.string.elec_resign_times);
		sevMaxElecCount.setTitle(R.string.max_elec_count);

		sevElecTimeout.setParamData(ParamsConst.PARAMS_KEY_ELEC_TIMEOUT);
		sevElecTimeout.setMinLength(1);
		sevElecTimeout.setMaxLength(3);
		sevElecTimeout.setMinValue(10, R.string.error_min_second);
		sevElecResignTimes.setParamData(ParamsConst.PARAMS_KEY_ELEC_RESIGN_TIMES);
		sevElecResignTimes.setMinLength(1);
		sevElecResignTimes.setMaxLength(2);
		sevMaxElecCount.setParamData(ParamsConst.PARAMS_KEY_MAX_ELEC_COUNT);
		sevMaxElecCount.setMinLength(1);
		sevMaxElecCount.setMaxLength(3);
		sevMaxElecCount.setMinValue(1);

		sevElecTimeout.setKeyboardNumber(keyboardNumber);
		sevElecResignTimes.setKeyboardNumber(keyboardNumber);
		sevMaxElecCount.setKeyboardNumber(keyboardNumber);

		ssvIsSupportElecsign.setParamData(R.string.is_support_elecsign, ParamsConst.PARAMS_KEY_IS_SUPPORT_ELECSIGN);
		ssvIsConfirmElecsign.setParamData(R.string.is_confirm_elecsign, ParamsConst.PARAMS_KEY_IS_CONFIRM_ELECSIGN);
		ssvIsElecsignInputPhone.setParamData(R.string.is_elecsign_input_phone, ParamsConst.PARAMS_KEY_IS_ELECSIGN_INPUT_PHONE);
		
		ssvIsSupportElecsign.setWaterEnabled();
	}

	@Override
	protected void initClickEvent(View view) {
		
	}

	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		
	}

}
