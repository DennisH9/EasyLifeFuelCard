package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.IpEditText;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.payment.ui.view.SettingChooseView;
import com.newland.payment.ui.view.SettingEditView;
import com.newland.payment.ui.view.SettingSwitchView;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 通讯参数设置界面
 *
 * @author CB
 * @date 2015-5-14 
 * @time 下午8:21:58
 */
public class SettingCommunicationFragment extends BaseSettingFragment{

	/** 是否使用http */
//	@ViewInject(R.id.ssv_http)
	SettingSwitchView ssvHttp;
	/** 通讯类型 */
//	@ViewInject(R.id.scv_connect_type)
	SettingChooseView scvConnectType;
	/** 超时时间 */
//	@ViewInject(R.id.sev_out_time)
	SettingEditView sevOutTime;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;

	private AbstractKeyBoardListener keyBoardListener;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_communication_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}


	@Override
	protected void initData() {
		
		setTitle(R.string.setting_communication);

		addCheckView(sevOutTime);
		
		sevOutTime.setTitle(R.string.communication_out_time);
		
		keyBoardListener = new AbstractKeyBoardListener(){
			@Override
			public void onKeyClick(int key) {
				super.onKeyClick(key);
				if (getTargetView() instanceof IpEditText) {

					((IpEditText) getTargetView()).onKeyClick(key);
				}
			}
			
			@Override
			public void setTargetView(View view) {
				super.setTargetView(view);
				if (getTargetView() instanceof IpEditText) {
					keyboardNumber.setIpStyle();
				} else {
					keyboardNumber.setTelephoneKeyDisplay(true);
				}
			}
		};
		keyboardNumber.setKeyBoardListener(keyBoardListener);
		
		sevOutTime.setKeyboardNumber(keyboardNumber);

		sevOutTime.setParamData(ParamsConst.PARAMS_KEY_COMMUNICATION_OUT_TIME);
		
		scvConnectType.setParamData(R.string.communication_type, 
				R.string.connect_type_cdma, "0",  
				R.string.connect_type_gprs, "1",  
				R.string.connect_type_wifi, "2", 
				ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE);
		
		sevOutTime.setMinValue(10l, R.string.error_min_second);
		sevOutTime.setMinLength(2);
		sevOutTime.setMaxLength(3);
		
		ssvHttp.setParamData(R.string.setting_is_use_http, ParamsConst.PARAMS_KEY_IS_USE_HTTP);	
	}

//	@OnClick({R.id.txt_gprs,R.id.txt_cdma,R.id.txt_wifi})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_gprs) {
			activity.switchContent(new SettingCommoncationGprsFragment());


		} else if (i == R.id.txt_cdma) {
			activity.switchContent(new SettingCommoncationCdmaFragment());

		} else if (i == R.id.txt_wifi) {
			activity.switchContent(new SettingCommoncationWifiFragment());

		}
	}

	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		
	}


}
