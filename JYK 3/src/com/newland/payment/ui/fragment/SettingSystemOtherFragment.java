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
import com.newland.payment.ui.view.SettingSwitchView;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 系统其它设置
 *
 * @author CB
 * @date 2015-5-15 
 * @time 下午3:27:37
 */
public class SettingSystemOtherFragment extends BaseSettingFragment{
 
	/** 输入本地地区码 */
//	@ViewInject(R.id.sev_shop_code)
	SettingEditView sevShopCode;
	/** 商行代码 */
//	@ViewInject(R.id.sev_local_code)
	SettingEditView sevLocalCode;
	/** 预授权屏蔽卡号 */
//	@ViewInject(R.id.ssv_is_preauth_shield_pan)
	SettingSwitchView ssvIsPreauthShieldPan;
	/** IC卡交易支持 */
//	@ViewInject(R.id.ssv_is_support_ic)
	SettingSwitchView ssvIsSupportIc;
	/** 是否显示TVR/TSI */
//	@ViewInject(R.id.ssv_is_display_trv_tsi)
	SettingSwitchView ssvIsDisplayTrvtsi;
	/** 消费交易是否支持挥卡 */
//	@ViewInject(R.id.ssv_is_sale_flick)
	SettingSwitchView ssvIsSaleFlick;
	/** 是否支持射频卡 */
//	@ViewInject(R.id.ssv_is_support_rf)
	SettingSwitchView ssvIsSupportRf;
	/** 非接寻卡延迟 */
//	@ViewInject(R.id.ssv_is_rf_delay)
	SettingSwitchView ssvIsRfDelay;
	/** 内外置非接设置 */
//	@ViewInject(R.id.scv_is_extend_rf)
	SettingChooseView scvIsExtendRf;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;
	/** 非接交易通道开关 */
//	@ViewInject(R.id.scv_qpboc_priority)
	SettingChooseView scvQpbocPriority;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_system_other_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {

		addCheckView(sevLocalCode);
		addCheckView(sevShopCode);
		
		setTitle(R.string.setting_system_other);
		
		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());

		sevShopCode.setTitle(R.string.shop_code);
		sevLocalCode.setTitle(R.string.local_code);

		sevShopCode.setKeyboardNumber(keyboardNumber);
		sevLocalCode.setKeyboardNumber(keyboardNumber);

		sevShopCode.setParamData(ParamsConst.PARAMS_KEY_BASE_MERCODE);
		sevShopCode.setMinValue(0000);
		sevShopCode.setMaxLength(4);
		
		sevLocalCode.setParamData(ParamsConst.PARAMS_KEY_BASE_LOCALCODE);
		sevLocalCode.setMinValue(0000);
		sevLocalCode.setMaxLength(4);
		
		ssvIsPreauthShieldPan.setParamData(R.string.is_preauth_shield_pan, ParamsConst.PARAMS_KEY_IS_PREAUTH_SHIELD_PAN);
		ssvIsSupportIc.setParamData(R.string.is_support_ic, ParamsConst.PARAMS_KEY_IS_SUPPORT_IC);
		ssvIsDisplayTrvtsi.setParamData(R.string.is_display_trv_tsi, ParamsConst.PARAMS_KEY_IS_DISPLAY_TRV_TSI);
		ssvIsSaleFlick.setParamData(R.string.is_sale_flick, ParamsConst.PARAMS_KEY_IS_SALE_FLICK);
		ssvIsSupportRf.setParamData(R.string.is_support_rf, ParamsConst.PARAMS_KEY_IS_SUPPORT_RF);
		ssvIsRfDelay.setParamData(R.string.is_rf_delay, ParamsConst.PARAMS_KEY_IS_RF_DELAY);
		
		scvIsExtendRf.setParamData(R.string.is_extend_rf, R.string.inside, "0",
				R.string.extend, "1", ParamsConst.PARAMS_KEY_IS_EXTEND_RF);
		
		scvQpbocPriority.setParamData(R.string.qpboc_priority, R.string.qpboc_priority_emv, "0", 
				R.string.qpboc_priority_ec, "1", ParamsConst.PARAMS_KEY_QPBOC_PRIORITY, 20);
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
		scvIsExtendRf.dismissDialog();
		super.onFragmentHide();
	}
}
