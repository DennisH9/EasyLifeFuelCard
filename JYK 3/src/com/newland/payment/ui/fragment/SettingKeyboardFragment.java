package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.payment.ui.view.SettingChooseView;
import com.newland.payment.ui.view.SettingEditView;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 密码键盘设置
 *
 * @author CB
 * @date 2015-5-15 
 * @time 上午10:06:38
 */
public class SettingKeyboardFragment extends BaseSettingFragment{

	/** 内外置键盘 */
//	@ViewInject(R.id.scv_inside_extend_keyboard)
	SettingChooseView scvInsideExtendKeyboard;
	/** 键盘超时时间 */

//	@ViewInject(R.id.sev_pinpad_timeout)
	SettingEditView sevPinpadTimeout;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_keyboard_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
		addCheckView(sevPinpadTimeout);
		
		setTitle(R.string.setting_system_keypoard_password);
		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());
		
		scvInsideExtendKeyboard.setParamData(R.string.inside_extend_keyboard,
				R.string.inside, "0", R.string.extend, "1",
				ParamsConst.PARAMS_KEY_INSIDE_EXTEND_KEYBOARD);
		//禁止内置键盘
		scvInsideExtendKeyboard.setDisabled(getString(R.string.support_inside_keyboard));
		
		sevPinpadTimeout.setTitle(R.string.pinpad_timeout);
		sevPinpadTimeout.setKeyboardNumber(keyboardNumber);
		sevPinpadTimeout.setParamData(ParamsConst.PARAMS_KEY_PINPAD_TIMEOUT);
		sevPinpadTimeout.setMaxLength(3);
		sevPinpadTimeout.setMinValue(10);
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
	
	@Override
	public void onFragmentHide() {
		scvInsideExtendKeyboard.dismissDialog();
		super.onFragmentHide();
	}
}
