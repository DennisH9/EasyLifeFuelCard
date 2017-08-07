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
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 闪卡参数设置
 *
 * @author linld
 * @date 2015-7-23 
 * @time 上午9:32:01
 */
public class SettingFlashCardFragment extends BaseSettingFragment{
	
	/** 闪卡记录条数 */
//	@ViewInject(R.id.sev_setting_flash_card_num)
	SettingEditView sevVsettingFlashCardNum;
	/** 当笔闪卡重刷时间*/
//	@ViewInject(R.id.sev_setting_flash_card_reswip_timeout)
	SettingEditView sevSettingFlashCardReSwipeTimeout;
	/** 闪卡记录处理时间*/
//	@ViewInject(R.id.sev_setting_flash_card_can_deal_timeout)
	SettingEditView sevSettingFlashCardCanDealTimeout;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;
	

	public SettingFlashCardFragment() {
		super(0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_flash_card_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {

		addCheckView(sevVsettingFlashCardNum);
		addCheckView(sevSettingFlashCardReSwipeTimeout);
		addCheckView(sevSettingFlashCardCanDealTimeout);
		
		setTitle(R.string.setting_system_flash_card);
		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());
		
		sevVsettingFlashCardNum.setTitle(R.string.setting_flash_card_num);
		sevSettingFlashCardReSwipeTimeout.setTitle(R.string.setting_flash_card_reswip_timeout);
		sevSettingFlashCardCanDealTimeout.setTitle(R.string.setting_flash_card_can_deal_timeout);

		sevVsettingFlashCardNum.setKeyboardNumber(keyboardNumber);
		sevSettingFlashCardReSwipeTimeout.setKeyboardNumber(keyboardNumber);
		sevSettingFlashCardCanDealTimeout.setKeyboardNumber(keyboardNumber);
		
		sevVsettingFlashCardNum.setParamData(ParamsConst.PARAMS_KEY_FLASH_CARD_MAX_NUM);
		sevVsettingFlashCardNum.setMinValue(1);
		sevVsettingFlashCardNum.setMaxLength(1);
		sevSettingFlashCardReSwipeTimeout.setParamData(ParamsConst.PARAMS_KEY_FLASH_CARD_RESWIP_TIME_OUT);
		sevSettingFlashCardReSwipeTimeout.setMinValue(1);
		sevSettingFlashCardReSwipeTimeout.setMaxLength(3);
		sevSettingFlashCardCanDealTimeout.setParamData(ParamsConst.PARAMS_KEY_FLASH_CARD_CAN_DEAL_TIME_OUT);
		sevSettingFlashCardCanDealTimeout.setMinValue(1);
		sevSettingFlashCardCanDealTimeout.setMaxLength(3);
		
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
