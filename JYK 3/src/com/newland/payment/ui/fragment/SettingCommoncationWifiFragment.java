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
import com.newland.payment.ui.view.SettingIpView;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * WIFI设置界面
 *
 * @author CB
 * @date 2015-6-16 
 * @time 下午2:46:35
 */
public class SettingCommoncationWifiFragment extends BaseSettingFragment{

	/** 长短链接 */
//	@ViewInject(R.id.scv_wifi_mode)
	SettingChooseView scvWifiMode;
	/** ip1 */
//	@ViewInject(R.id.siv_ip_1)
	SettingIpView sivIp1;
	/** 端口1 */
//	@ViewInject(R.id.sev_port_1)
	SettingEditView sevPort1;
	/** ip2 */
//	@ViewInject(R.id.siv_ip_2)
	SettingIpView sivIp2;
	/** 端口2 */
//	@ViewInject(R.id.sev_port_2)
	SettingEditView sevPort2;
	/** tpdu */
//	@ViewInject(R.id.sev_tpdu)
	SettingEditView sevTpdu;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;

	private AbstractKeyBoardListener keyBoardListener;
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_communication_wifi_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		setTitle(R.string.connect_type_wifi);

		addCheckView(sevPort1);
		addCheckView(sevPort2);
		addCheckView(sevTpdu);

		sivIp1.setTitle(R.string.setting_communication_ip_1);
		sevPort1.setTitle(R.string.setting_communication_port_1);
		sivIp2.setTitle(R.string.setting_communication_ip_2);
		sevPort2.setTitle(R.string.setting_communication_port_2);
		sevTpdu.setTitle(R.string.setting_communication_tpdu);
		
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
		
		scvWifiMode.setParamData(R.string.connect_mode, R.string.connect_mode_long, "1", 
				R.string.connect_mode_short, "0", ParamsConst.PARAMS_KEY_WIFI_MODE);
		
		sevPort1.setKeyboardNumber(keyboardNumber);
		sevPort2.setKeyboardNumber(keyboardNumber);
		
		sivIp1.setParamData(ParamsConst.PARAMS_KEY_WIFI_SERVERIP1, keyboardNumber);
		sevPort1.setParamData(ParamsConst.PARAMS_KEY_WIFI_PORT1);
		sivIp2.setParamData(ParamsConst.PARAMS_KEY_WIFI_SERVERIP2, keyboardNumber);
		sevPort2.setParamData(ParamsConst.PARAMS_KEY_WIFI_PORT2);

		sevTpdu.setKeyboardNumber(keyboardNumber);
		sevTpdu.setParamData(ParamsConst.PARAMS_KEY_WIFI_TPDU);
		sevTpdu.setTitleWidth(100);
		sevTpdu.setMinLength(10);
		sevTpdu.setMaxLength(10);

		sivIp1.setTitleWidth(80);
		sevPort1.setTitleWidth(100);
		sevPort1.setMaxLength(6);
		
		sivIp2.setTitleWidth(80);
		sevPort2.setTitleWidth(100);
		sevPort2.setMaxLength(6);
		
		sevPort1.setMinValue(1);
		sevPort1.setMaxValue(65535);
		
		sevPort2.setMinValue(1);
		sevPort2.setMaxValue(65535);
		
	}

	@Override
	protected void initClickEvent(View view) {
		
	}

	@Override
	protected void initEvent() {
		
	}

	@Override
	protected BaseBean getBean() {
		return null;
	}

}
