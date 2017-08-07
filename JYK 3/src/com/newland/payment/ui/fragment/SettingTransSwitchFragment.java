package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.base.util.ParamsUtils;
import com.newland.emv.EmvAppModule;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.bean.SettingSwicthBean;
import com.newland.payment.ui.listener.BooleanValueChangeListener;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.emv.EmvModule;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易开关设置
 *
 * @author CB
 * @date 2015-5-13 
 * @time 下午7:22:51
 */
public class SettingTransSwitchFragment extends BaseSettingFragment{
	
	private final List<SettingSwicthBean> beans = new ArrayList<SettingSwicthBean>();
	private int resTitle;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_trans_switch_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
		setTitle(R.string.setting_trans_switch);
	}
	
//	@OnClick({R.id.txt_setting_trans_switch_tradition,
//		R.id.txt_setting_trans_switch_ec})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_setting_trans_switch_tradition) {//传统类交易开关
			resTitle = R.string.setting_trans_switch_tradition;
			beans.clear();
			//消费
			addItem(R.string.common_consumption, ParamsConst.PARAMS_KEY_TRANS_SALE);
			//消费撤销
			addItem(R.string.common_revocation, ParamsConst.PARAMS_KEY_TRANS_VOID);
			//预授权
			addItem(R.string.common_pre_author, ParamsConst.PARAMS_KEY_TRANS_PREAUTH);
			//余额查询
			addItem(R.string.common_check_balance, ParamsConst.PARAMS_KEY_TRANS_BALANCE);
			//预授权完成请求
			addItem(R.string.common_pre_author_finish, ParamsConst.PARAMS_KEY_TRANS_AUTHSALE);
			//预授权完成通知
			addItem(R.string.common_pre_author_finish_notice, ParamsConst.PARAMS_KEY_TRANS_AUTHSALEOFF);
			//预授权撤销
			addItem(R.string.common_pre_author_revocation, ParamsConst.PARAMS_KEY_TRANS_VOIDPREAUTH);
			//预授权完成撤销
			addItem(R.string.common_pre_author_finish_revocation, ParamsConst.PARAMS_KEY_TRANS_VOIDAUTHSALE);
			//离线结算
			addItem(R.string.common_offline_settlement, ParamsConst.PARAMS_KEY_TRANS_SETTLEOFF);
			//结算调整
			addItem(R.string.common_settlement_adjust, ParamsConst.PARAMS_KEY_TRANS_ADJUSTOFF);
			//退货
			addItem(R.string.common_return_goods, ParamsConst.PARAMS_KEY_TRANS_REFUND);

		} else if (i == R.id.txt_setting_trans_switch_ec) {//电子现金类开关
			resTitle = R.string.setting_trans_switch_ec;
			beans.clear();
			//接触式电子现金
			addItem(R.string.common_contack_elec_cash, ParamsConst.PARAMS_KEY_TRANS_EC, new BooleanValueChangeListener() {
				@Override
				public void onChange(boolean value) {
					//同步电子现金开关到AID参数
					EmvModule emvModule = EmvAppModule.getInstance();
					emvModule.emvSetSupportEc(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_TRANS_EC, value));
				}
			});
			//快速支付
			addItem(R.string.common_trans_ecnotuchsale, ParamsConst.PARAMS_KEY_TRANS_QPBOC);
			//指定账户圈存
			addItem(R.string.trans_ecpointload, ParamsConst.PARAMS_KEY_TRANS_ECPOINTLOAD);
			//非指定账户圈存
			addItem(R.string.trans_ecnopointload, ParamsConst.PARAMS_KEY_TRANS_ECNOPOINTLOAD);
			//电子现金现金充值
			addItem(R.string.trans_ecmoneyload, ParamsConst.PARAMS_KEY_TRANS_ECMONEYLOAD);
			//电子现金重置撤销
			addItem(R.string.trans_ecvoidmoneyload, ParamsConst.PARAMS_KEY_TRANS_ECVOIDMONEYLOAD);
			//电子现金脱机退货
			addItem(R.string.trans_ecrefund, ParamsConst.PARAMS_KEY_TRANS_ECREFUND);


		}

		activity.switchContent(new SettingSwitchListFragment(resTitle, beans));
	}

	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		
	}

	private void addItem(int resTitle, String key) {
		addItem(resTitle, key, null);
	}
	private void addItem(int resTitle, String key, BooleanValueChangeListener listener) {
		SettingSwicthBean swicthBean = new SettingSwicthBean();
		swicthBean.setResTitle(resTitle);
		swicthBean.setKey(key);
		swicthBean.setBooleanValueChangeListener(listener);
		beans.add(swicthBean);
	}
}
