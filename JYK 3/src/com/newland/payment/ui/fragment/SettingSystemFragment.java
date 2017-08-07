package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.listener.StringValueChangeListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.payment.ui.view.SettingEditView;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 系统参数设置
 *
 * @author CB
 * @date 2015-5-15 
 * @time 上午9:32:01
 */
public class SettingSystemFragment extends BaseSettingFragment{
	
	/** 流水号 */
//	@ViewInject(R.id.sev_setting_system_serial_no)
	SettingEditView sevSettingSystemSerialNo;
	/** 批次号 */
//	@ViewInject(R.id.sev_setting_system_batch_no)
	SettingEditView sevSettingSystemBatchNo;
	/** 最大交易笔数 */
//	@ViewInject(R.id.sev_vsetting_system_max_trans_no)
	SettingEditView sevVsettingSystemMaxTSrans_no;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;
	/** 界面超时时间 */
//	@ViewInject(R.id.sev_time_out)
	SettingEditView sevTimeOut;
	

	public SettingSystemFragment() {
		super(0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_system_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {

		addCheckView(sevSettingSystemBatchNo);
		addCheckView(sevSettingSystemSerialNo);
		addCheckView(sevTimeOut);
		addCheckView(sevVsettingSystemMaxTSrans_no);
		
		setTitle(R.string.setting_system);
		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());
		
		sevTimeOut.setTitle(R.string.fragment_timeout); 
		sevSettingSystemSerialNo.setTitle(R.string.setting_system_serial_no);
		sevSettingSystemBatchNo.setTitle(R.string.setting_system_batch_no);
		sevVsettingSystemMaxTSrans_no.setTitle(R.string.setting_system_max_trans_no);

		sevTimeOut.setKeyboardNumber(keyboardNumber);
		sevSettingSystemSerialNo.setKeyboardNumber(keyboardNumber);
		sevSettingSystemBatchNo.setKeyboardNumber(keyboardNumber);
		sevVsettingSystemMaxTSrans_no.setKeyboardNumber(keyboardNumber);
		
		sevTimeOut.setParamData(3,ParamsConst.PARAMS_KEY_CONFIG_FRAGMENT_TIMEOUT, new StringValueChangeListener() {
			
			@Override
			public void onChange(String value) {
				LoggerUtils.i("change time out:" + value);
				if (value.length() > 0) {
					App.FRAGMENT_TIME = Long.valueOf(value);
				}
			}
		});
		sevTimeOut.setMinValue(10l, R.string.error_min_second);
		sevTimeOut.addValueList("0");
		sevSettingSystemSerialNo.setParamData(6, ParamsConst.PARAMS_KEY_BASE_TRACENO);
		sevSettingSystemSerialNo.setMinValue(1);
		sevSettingSystemSerialNo.setFixLength(6);
		sevSettingSystemSerialNo.setMaxLength(6);
		sevSettingSystemBatchNo.setParamData(ParamsConst.PARAMS_KEY_BASE_BATCHNO);
		sevSettingSystemBatchNo.setMinValue(1);
		sevSettingSystemBatchNo.setFixLength(6);
		sevSettingSystemBatchNo.setMaxLength(6);
		sevVsettingSystemMaxTSrans_no.setParamData(ParamsConst.PARAMS_KEY_MAX_TRANS_COUNT);
		sevVsettingSystemMaxTSrans_no.setMaxValue(500);
		sevVsettingSystemMaxTSrans_no.setMinValue(1);
		sevVsettingSystemMaxTSrans_no.setMinLength(1);
		sevVsettingSystemMaxTSrans_no.setMaxLength(3);
		
		//终端存在流水，不允许修改批次号和凭证号
		sevSettingSystemSerialNo.setWaterEnabled(R.string.has_water_please_first_settlement);
		sevSettingSystemBatchNo.setWaterEnabled(R.string.has_water_please_first_settlement);
	}

//	@OnClick({R.id.txt_setting_system_keypoard_password,
//			R.id.txt_setting_system_now_time,
//			R.id.txt_setting_system_flash_card,
//			R.id.txt_setting_system_other})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_setting_system_keypoard_password) {//密码键盘设置
			activity.switchContent(new SettingKeyboardFragment());

		} else if (i == R.id.txt_setting_system_now_time) {//系统当前时间
			activity.switchContent(new SettingTimeFragment());

		} else if (i == R.id.txt_setting_system_flash_card) {//闪卡参数设置
			activity.switchContent(new SettingFlashCardFragment());

		} else if (i == R.id.txt_setting_system_other) {//其他系统设置
			activity.switchContent(new SettingSystemOtherFragment());

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
