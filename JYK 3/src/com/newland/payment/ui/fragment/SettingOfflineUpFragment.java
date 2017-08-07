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
 * 离线交易控制
 * 
 * @author lst
 * 
 */
public class SettingOfflineUpFragment extends BaseSettingFragment {

	/** 离线上送笔数 */
//	@ViewInject(R.id.setting_offline_up_edit)
	SettingEditView editView;
	/** 离线上送方式 */
//	@ViewInject(R.id.setting_offline_up_choose)
	SettingChooseView chooseView;
//	@ViewInject(R.id.setting_keyboard_number_offline_up)
	KeyboardNumber keyboardNumber;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_offline_up_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;

	}

	@Override
	protected void initData() {

		addCheckView(editView);

		setTitle(R.string.setting_trans_off_line);
		keyboardNumber.setVisibility(View.GONE);
		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());
		/** 离线上送笔数 */
		editView.setTitle(R.string.max_offsend_num);
		editView.setKeyboardNumber(keyboardNumber);
		editView.setMinLength(1);
		editView.setParamData(2, ParamsConst.PARAMS_KEY_MAX_OFFSEND_NUM);
		editView.setMinValue(1);

		/** 离线上送方式 */
		chooseView.setParamData(R.string.offsend_mode,
				R.string.setting_up_send_before_settlement, "0",
				R.string.setting_up_send_before_next, "1",
				ParamsConst.PARAMS_KEY_OFFSEND_MODE);
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
		chooseView.dismissDialog();
		super.onFragmentHide();
	}
}
