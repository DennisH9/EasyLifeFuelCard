package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.base.util.CommunicationUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.listener.StringValueChangeListener;
import com.newland.payment.ui.view.IpEditText;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.payment.ui.view.SettingChooseView;
import com.newland.payment.ui.view.SettingEditView;
import com.newland.payment.ui.view.SettingIpView;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * cdma设置界面
 *
 * @author CB
 * @date 2015-6-16 
 * @time 下午2:46:35
 */
public class SettingCommoncationCdmaFragment extends BaseSettingFragment{

	/** 长短链接 */
//	@ViewInject(R.id.scv_connect_mode)
	SettingChooseView scvConnectMode;
	/** 接入号码 */
//	@ViewInject(R.id.sev_access_number)
	SettingEditView sevAccessNumber;
	
	/** APN 名称 */
//	@ViewInject(R.id.sev_apn_name)
	SettingChooseView sevApnName;
	/** APN */
//	@ViewInject(R.id.sev_apn)
	SettingEditView sevApn;
	
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
	/** 用户名 */
//	@ViewInject(R.id.sev_user_name)
	SettingEditView sevUserName;
	/** 密码 */
//	@ViewInject(R.id.sev_password)
	SettingEditView sevPassword;
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
		mFragmentView = inflater.inflate(R.layout.setting_communication_cdma_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		
		LoggerUtils.d("init cdma");

		sevApn.setEditTextEnabledNoChangeColor(true);
		if(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_GPRS_APN_CHOOSE, 0) == 0){
			LoggerUtils.d("公网 111");
			sevApn.setEditTextEnabledNoChangeColor(false);			
		}
		sevApn.setContent(CommunicationUtils.getPreferApn(context)?CommunicationUtils.getApn():"");
		
		
		setTitle(R.string.connect_type_cdma);

		addCheckView(sevPort1);
		addCheckView(sevPort2);
		addCheckView(sevAccessNumber);
		addCheckView(sevUserName);
		addCheckView(sevPassword);
		addCheckView(sevApn);
		addCheckView(sevTpdu);

		sevAccessNumber.setTitle(R.string.access_number);
		sivIp1.setTitle(R.string.setting_communication_ip_1);
		sevPort1.setTitle(R.string.setting_communication_port_1);
		sivIp2.setTitle(R.string.setting_communication_ip_2);
		sevPort2.setTitle(R.string.setting_communication_port_2);
		sevUserName.setTitle(R.string.common_user_name);
		sevPassword.setTitle(R.string.common_password);
		sevTpdu.setTitle(R.string.setting_communication_tpdu);
		sevApn.setTitle(R.string.connect_apn);
		
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
		
		scvConnectMode.setParamData(R.string.connect_mode, R.string.connect_mode_long, "1", 
				R.string.connect_mode_short, "0", ParamsConst.PARAMS_KEY_CDMA_MODE);
		
		sevPort1.setKeyboardNumber(keyboardNumber);
		sevPort2.setKeyboardNumber(keyboardNumber);
		
		sevApnName.setParamData(R.string.connect_apn_choose, R.string.connect_apn_name_default,"0",
				R.string.connect_apn_name_custom,"1", ParamsConst.PARAMS_KEY_GPRS_APN_CHOOSE, new StringValueChangeListener(){
			@Override
			public void onChange(String value) {
				sevApn.setEditTextEnabledNoChangeColor(true);
				if(CommunicationUtils.getPreferApn(context)){
					LoggerUtils.d("能获取apn 111");
					if(Integer.valueOf(value) == 0){
						LoggerUtils.d("公网 111");
						sevApn.setEditTextEnabledNoChangeColor(false);
						ParamsUtils.setString(ParamsConst.PARAMS_KEY_GPRS_APN, String.format("%s",sevApn.getContent()));//保存专网APN
			        	CommunicationUtils.setApn(CommunicationUtils.getApnBySim(context, "02"));
					}else{
						LoggerUtils.d("专网 111");
			        	CommunicationUtils.setApn(ParamsUtils.getString(ParamsConst.PARAMS_KEY_GPRS_APN));			
					}
					//CommunicationUtils.updateApnEntity(context);
					CommunicationUtils.updateApnEntity(context, sevUserName.getValue(), sevPassword.getValue());
					
				}else{
					LoggerUtils.d("不能获取apn 111");
					CommunicationUtils.SetDefaultApnEntity(context);
					CommunicationUtils.addApnEntity(context);
					if(Integer.valueOf(value) == 0){
						sevApn.setEditTextEnabledNoChangeColor(false);
					}
				}
				sevApn.setContent(CommunicationUtils.getApn());

			}
		});
		
		sevApn.setParamData(ParamsConst.PARAMS_KEY_GPRS_APN, false, new StringValueChangeListener() {
			
			@Override
			public void onChange(String value) {
				LoggerUtils.d("apn onchange 111");
            	if(CommunicationUtils.getPreferApn(context)){
            		CommunicationUtils.setApn(String.format("%s", sevApn.getContent()));
	            	//CommunicationUtils.updateApnEntity(context);
	            	CommunicationUtils.updateApnEntity(context, sevUserName.getValue(), sevPassword.getValue());
            	}else
            	{
            		CommunicationUtils.SetDefaultApnEntity(context);
            		CommunicationUtils.setApn(ParamsUtils.getString(ParamsConst.PARAMS_KEY_GPRS_APN));
            		CommunicationUtils.addApnEntity(context);
            	}
            	ParamsUtils.setString(ParamsConst.PARAMS_KEY_GPRS_APN, (StringUtils.isNullOrEmpty(CommunicationUtils.getApn()))?"":CommunicationUtils.getApn());
			}
		});
		
		sevAccessNumber.setParamData(ParamsConst.PARAMS_KEY_CDMA_ACCESS_NUMBER);
		sivIp1.setParamData(ParamsConst.PARAMS_KEY_CDMA_SERVERIP1, keyboardNumber);
		sevPort1.setParamData(ParamsConst.PARAMS_KEY_CDMA_PORT1);
		sivIp2.setParamData(ParamsConst.PARAMS_KEY_CDMA_SERVERIP2, keyboardNumber);
		sevPort2.setParamData(ParamsConst.PARAMS_KEY_CDMA_PORT2);
		sevUserName.setParamData(ParamsConst.PARAMS_KEY_CDMA_USERNAME);
		sevPassword.setParamData(ParamsConst.PARAMS_KEY_CDMA_PWD);

		sevTpdu.setKeyboardNumber(keyboardNumber);
		sevTpdu.setParamData(ParamsConst.PARAMS_KEY_CDMA_TPDU);
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
