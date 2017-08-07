package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.payment.ui.view.SettingChooseView;
import com.newland.payment.ui.view.SettingEditView;
import com.newland.payment.ui.view.SettingSwitchView;
import com.newland.pos.sdk.bean.BaseBean;
/**
 * 其它交易控制
 * @author lst
 *
 */
public class SettingTransOtherControl extends BaseSettingFragment{
	
	/** 输入主管密码*/
//	@ViewInject (R.id.setting_other_input_main_pwd)
	SettingSwitchView swIsInputMainPwd;
	/** 手工输卡号 */
//	@ViewInject (R.id.setting_other_input_cardno_byhand)
	SettingSwitchView swInputNoByhand;
	/** 缺省交易类型*/
//	@ViewInject (R.id.setting_other_default_transtype)
	SettingChooseView csDefaultTranstype;
	/** 退货最大金额 */
//	@ViewInject (R.id.setting_other_back_goods_max_amount)
	SettingEditView etBackGoodMaxAmount;
	/** 支持小额代授权 */
//	@ViewInject(R.id.setting_other_is_small_gene_auth)
	SettingSwitchView swIsSmallGeneAuth;
	/**是否支持小费 */
//	@ViewInject (R.id.setting_other_is_surpport_tip)
	SettingSwitchView swIsSurppotTip;
	/** 设置消费比率 */
//	@ViewInject (R.id.setting_other_tip_percent)
	SettingEditView etTipPercent;
	/** 授权通知模式*/
//	@ViewInject(R.id.setting_other_auth_sale_mode)
	SettingChooseView csAuthSaleMode;
	/** 键盘*/
//	@ViewInject(R.id.setting_other_keyboard_number)
	KeyboardNumber keyboardNumber;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_trans_other_control, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {

		addCheckView(etBackGoodMaxAmount);
		addCheckView(etTipPercent);
		
		setTitle(R.string.setting_trans_other);
		
		keyboardNumber.setVisibility(View.GONE);
		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());
		
		swIsInputMainPwd.setParamData(R.string.is_admin_password, ParamsConst.PARAMS_KEY_IS_ADMIN_PASSWORD);
		swIsSmallGeneAuth.setParamData(R.string.is_small_gene_auth, ParamsConst.PARAMS_KEY_IS_SMALL_GENE_AUTH);
		swIsSurppotTip.setParamData(R.string.is_tip, ParamsConst.PARAMS_KEY_IS_TIP);
		swInputNoByhand.setParamData(R.string.is_card_input, ParamsConst.PARAMS_KEY_IS_CARD_INPUT);
		
		etBackGoodMaxAmount.setTitle(R.string.max_refund_amount);
		etBackGoodMaxAmount.setParamData(12, ParamsConst.PARAMS_KEY_BASE_REFUNDAMOUNT);
		
		etTipPercent.setKeyboardNumber(keyboardNumber);
		etTipPercent.setTitle(R.string.tip_rate);
		etTipPercent.setParamData(2,ParamsConst.PARAMS_KEY_BASE_TIPRATE);
		etTipPercent.setMinLength(1);
		etTipPercent.setMinValue(1);
		
		etBackGoodMaxAmount.setTitle(R.string.max_refund_amount);
		etBackGoodMaxAmount.setKeyboardNumber(keyboardNumber);
		etTipPercent.setKeyboardNumber(keyboardNumber);
		
		
		csAuthSaleMode.setParamData(R.string.auth_sale_mode, R.string.sup_same,
				"0", R.string.sup_ask, "1", R.string.sup_notice, "2",
				ParamsConst.PARAMS_KEY_AUTH_SALE_MODE);
		if (App.SCREEN_TYPE == Const.ScreenType.IM_81) {
			csAuthSaleMode.setDialogWidth(R.dimen.dp570);
		}
		csDefaultTranstype.setParamData(R.string.default_trans_type, R.string.other_pre_auth, "0", R.string.other_consume, "1", ParamsConst.PARAMS_KEY_DEFAULT_TRANS_TYPE);
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
		csDefaultTranstype.dismissDialog();
		csAuthSaleMode.dismissDialog();
		super.onFragmentHide();
	}
}
