package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 系统设置界面
 *
 * @author CB
 * @time 2015-4-23 上午10:47:27
 */
public class SystemManageFragment extends BaseSettingFragment{

	public SystemManageFragment() {
		super(0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.system_manage_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		// 设置头部
		MainActivity.getInstance().setLoginStyle();
		setTitle(R.string.sliding_menu_system_settings);
	}
	
//	@OnClick({R.id.setting_shop,R.id.setting_trans,R.id.setting_system,R.id.setting_communication,
//		R.id.setting_qps_manage,
//		R.id.setting_secret_key,R.id.setting_password,R.id.setting_other,R.id.setting_print_manage})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.setting_shop) {//商户参数设置
			activity.switchContent(new SettingShopFragment());
		} else if (i == R.id.setting_trans) {//交易管理设置
			activity.switchContent(new SettingTransFragment());

		} else if (i == R.id.setting_system) {//系统参数设置
			activity.switchContent(new SettingSystemFragment());

		} else if (i == R.id.setting_communication) {//通讯参数设置
			activity.switchContent(new SettingCommunicationFragment());

		} else if (i == R.id.setting_secret_key) {//终端密钥管理
			activity.switchContent(new SettingSecretKeyFragment());

		} else if (i == R.id.setting_password) {//密码管理
			activity.switchContent(new SettingPasswordManagerFragment());

		} else if (i == R.id.setting_print_manage) {//打印管理设置
			activity.switchContent(new SettingPrintFragment());

		} else if (i == R.id.setting_qps_manage) {//免密免签管理设置
			activity.switchContent(new SettingQPSFragment());

		} else if (i == R.id.setting_other) {//其他功能设置
			activity.switchContent(new SettingOtherFragment());
		}
	}

	@Override
	protected void initEvent() {
	}
	
	@Override
	protected boolean onBackKeyDown() {
		if (App.SCREEN_TYPE == Const.ScreenType.IM_81) {
			activity.hideTitle();
		}
		return false;
	}

	@Override
	public BaseBean getBean() {
		return null;
	}
	
	@Override
	public void onDestroy() {
		MainActivity.getInstance().hideTimeOut();
		super.onDestroy();
	}
}
