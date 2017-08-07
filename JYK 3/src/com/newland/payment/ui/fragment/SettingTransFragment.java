package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.bean.SettingSwicthBean;
import com.newland.pos.sdk.bean.BaseBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 交易管理设置
 *
 * @author CB
 * @date 2015-5-13 
 * @time 下午7:22:51
 */
public class SettingTransFragment extends BaseSettingFragment{

	private final List<SettingSwicthBean> beans = new ArrayList<SettingSwicthBean>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_trans_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		setTitle(R.string.setting_trans);
	}
	
//	@OnClick({R.id.txt_setting_trans_switch,
//		R.id.txt_setting_trans_input_password,
//		R.id.txt_setting_trans_swipe,
//		R.id.txt_setting_trans_closing,
//		R.id.txt_setting_trans_off_line,
//		R.id.txt_setting_trans_retry_no,
//		R.id.txt_setting_trans_other})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_setting_trans_switch) {//交易开关控制
			activity.switchContent(new SettingTransSwitchFragment());

		} else if (i == R.id.txt_setting_trans_input_password) {//交易输密控制
			beans.clear();
			/** 消费撤销密码*/
			addItem(R.string.is_voidsale_pin, ParamsConst.PARAMS_KEY_IS_INPUT_TRANS_VOID);
			/** 授权撤销密码*/
			addItem(R.string.is_voidauth_pin, ParamsConst.PARAMS_KEY_IS_INPUT_AUTH_VOID);
			/** 授权完成撤销密码*/
			addItem(R.string.is_voidauthsale_pin, ParamsConst.PARAMS_KEY_IS_AUTH_SALE_VOID_PIN);
			/** 授权完成请求密码*/
			addItem(R.string.is_authsale_pin, ParamsConst.PARAMS_KEY_IS_AUTH_SALE_PIN);

			activity.switchContent(new SettingSwitchListFragment(R.string.setting_trans_input_password, beans));

		} else if (i == R.id.txt_setting_trans_swipe) {//交易刷卡控制
			beans.clear();
			/** 消费撤销刷卡*/
			addItem(R.string.is_voidsale_strip, ParamsConst.PARAMS_KEY_TRANS_VOID_SWIPE);
			/** 授权完成撤销刷卡*/
			addItem(R.string.is_voidauthsale_strip, ParamsConst.PARAMS_KEY_AUTH_SALE_VOID_SWIPE);
			activity.switchContent(new SettingSwitchListFragment(R.string.setting_trans_swipe, beans));


		} else if (i == R.id.txt_setting_trans_closing) {//结算交易控制
			beans.clear();
			/** 结算后自动签退 */
			addItem(R.string.is_auto_logout, ParamsConst.PARAMS_KEY_IS_AUTO_LOGOUT);
			activity.switchContent(new SettingSwitchListFragment(R.string.setting_trans_closing, beans));


		} else if (i == R.id.txt_setting_trans_off_line) {//离线交易控制

			activity.switchContent(new SettingOfflineUpFragment());


		} else if (i == R.id.txt_setting_trans_retry_no) {//重发次数控制

			activity.switchContent(new SettingResendTimesFragment());


		} else if (i == R.id.txt_setting_trans_other) {//其它交易控制
			activity.switchContent(new SettingTransOtherControl());


		}
	}

	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		
	}

	public void addItem(int resTitle, String key) {
		SettingSwicthBean swicthBean = new SettingSwicthBean();
		swicthBean.setResTitle(resTitle);
		swicthBean.setKey(key);
		
		beans.add(swicthBean);
	}
}
