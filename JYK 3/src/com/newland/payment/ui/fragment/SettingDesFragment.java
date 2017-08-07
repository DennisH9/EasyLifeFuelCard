package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.view.SettingChooseView;
import com.newland.payment.ui.view.SettingSwitchView;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 设置DES算法界面
 *
 * @author CB
 * @date 2015-5-14 
 * @time 下午11:28:46
 */
public class SettingDesFragment extends BaseSettingFragment{

//	@ViewInject(R.id.scv_encrypt_mode)
	SettingChooseView scvEncryptMode;
//	@ViewInject(R.id.ssv_is_encrypt_track)
	SettingSwitchView ssvIsEncryptTrack;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_des_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		setTitle(R.string.setting_secret_key_des);
		scvEncryptMode.setParamData(R.string.encrypt_mode, R.string.encrypt_mode_single, 
				"0", R.string.encrypt_mode_double, "1", ParamsConst.PARAMS_KEY_ENCRYPT_MODE);
		ssvIsEncryptTrack.setParamData(R.string.is_encrypt_track, ParamsConst.PARAMS_KEY_BASE_ENCPYTRACK);
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
		scvEncryptMode.dismissDialog();
		super.onFragmentHide();
	}
}
