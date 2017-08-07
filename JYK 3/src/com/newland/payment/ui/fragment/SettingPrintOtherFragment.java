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
 * 其它打印设置
 *
 * @author CB
 * @date 2015-5-20 
 * @time 上午9:41:26
 */
public class SettingPrintOtherFragment extends BaseSettingFragment{

	/** 签购单字体选择 */
//	@ViewInject(R.id.scv_sign_order_font)
	SettingChooseView scvSignOrderFont;
	/** 设置签购单保管年限 */
//	@ViewInject(R.id.sev_setting_sign_order_age_limit)
	SettingEditView sevSettingSignOrderAgeLimit;
	/** 发卡行名称 */
//	@ViewInject(R.id.sev_issuing_bank_name)
	SettingEditView sevIssuingBankName;
	/** 签购单是否打英文 */
//	@ViewInject(R.id.ssv_is_print_sign_order_english)
	SettingSwitchView ssvIsPrintSignOrderEnglish;
	/** 是否提示打印明细 */
//	@ViewInject(R.id.ssv_is_hint_print_detail)
	SettingSwitchView ssvIsHintPrintDetail;
	/** 重打印结算单 */
//	@ViewInject(R.id.ssv_anew_print_statement)
	SettingSwitchView ssvAnewPrintStatement;
	/** 打印所有交易明细 */
//	@ViewInject(R.id.ssv_print_all_trans_detail)
	SettingSwitchView ssvPrintAllTransDetail;
	/** 打印中文收单行 */
//	@ViewInject(R.id.ssv_print_chinese_acquirer)
	SettingSwitchView ssvPrintChineseAcquirer;
	/** 打印中文发卡行 */
//	@ViewInject(R.id.ssv_print_chinese_issuing_bank)
	SettingSwitchView ssvPrintChineseIssuingBank;
	/** 是否打印负号 */
//	@ViewInject(R.id.ssv_is_print_minus)
	SettingSwitchView ssvIsPrintMinus;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_print_other_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {

		addCheckView(sevIssuingBankName);
		addCheckView(sevSettingSignOrderAgeLimit);
		
		setTitle(R.string.setting_print_other);
		
		scvSignOrderFont.setParamData(R.string.print_sign_order_font, 
				R.string.common_big, "0",
				R.string.common_middle, "1",
				R.string.common_small, "2",
				ParamsConst.PARAMS_KEY_SIGN_ORDER_FONT);

		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());
		
		sevSettingSignOrderAgeLimit.setParamData(R.string.setting_sign_order_age_limit, keyboardNumber, ParamsConst.PARAMS_KEY_SETTING_SIGN_ORDER_AGE_LIMIT);
		sevIssuingBankName.setParamData(ParamsConst.PARAMS_KEY_ISSUING_BANK_NAME, R.string.issuing_bank_name);

		ssvIsPrintSignOrderEnglish.setParamData(
				R.string.is_print_sign_order_english,
				ParamsConst.PARAMS_KEY_IS_PRINT_SIGN_ORDER_ENGLISH);
		ssvIsHintPrintDetail.setParamData(
				R.string.is_hint_print_detail,
				ParamsConst.PARAMS_KEY_IS_HINT_PRINT_DETAIL);
		ssvAnewPrintStatement.setParamData(
				R.string.anew_print_statement,
				ParamsConst.PARAMS_KEY_ANEW_PRINT_STATEMENT);
		ssvPrintAllTransDetail.setParamData(
				R.string.print_all_trans_detail,
				ParamsConst.PARAMS_KEY_PRINT_ALL_TRANS_DETAIL);
		ssvPrintChineseAcquirer.setParamData(
				R.string.print_chinese_acquirer,
				ParamsConst.PARAMS_KEY_PRINT_CHINESE_ACQUIRER);
		ssvPrintChineseIssuingBank.setParamData(
				R.string.print_chinese_issuing_bank,
				ParamsConst.PARAMS_KEY_PRINT_CHINESE_ISSUING_BANK);
		ssvIsPrintMinus.setParamData(
				R.string.is_print_minus,
				ParamsConst.PARAMS_KEY_IS_PRINT_MINUS);
		
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
		scvSignOrderFont.dismissDialog();
		super.onFragmentHide();
	}
}
