package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.newland.base.util.MessageUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const.PrintStyleConstEnum;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 参数打印
 * 
 * @author CB
 * @date 2015-5-20
 * @time 下午2:52:21
 */
public class SettingPrintParamsFragment extends BaseSettingFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(
				R.layout.setting_print_params_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
		setTitle(R.string.param_print);
	}

//	@OnClick({R.id.txt_print_shop_info, R.id.txt_print_trans_control,
//		R.id.txt_print_system_control, R.id.txt_print_communication,
//		R.id.txt_print_version, R.id.txt_print_emv,
//		R.id.txt_print_other})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_print_shop_info) {//商户信息
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.print_shop_info, new OnClickListener() {

				@Override
				public void onClick(View v) {

					activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_PARAM_MERCHANTINFO, null, null));

				}
			});


		} else if (i == R.id.txt_print_trans_control) {//交易控制
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.print_trans_control, new OnClickListener() {

				@Override
				public void onClick(View v) {

					activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_PARAM_TRANSCCTRL, null, null));

				}
			});


		} else if (i == R.id.txt_print_system_control) {//系统控制
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.print_system_control, new OnClickListener() {

				@Override
				public void onClick(View v) {
					activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_PARAM_SYSTEMCTRL, null, null));
				}
			});


		} else if (i == R.id.txt_print_communication) {//通讯参数
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.print_communication, new OnClickListener() {

				@Override
				public void onClick(View v) {

					activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_PARAM_COMM, null, null));

				}
			});


		} else if (i == R.id.txt_print_version) {//版本信息
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.print_version, new OnClickListener() {

				@Override
				public void onClick(View v) {

					activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_PARAM_VERSION, null, null));

				}
			});


		} else if (i == R.id.txt_print_emv) {//EMV参数
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.print_emv, new OnClickListener() {

				@Override
				public void onClick(View v) {

					activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_PARAMS_EMV, null, null));

				}
			});

		} else if (i == R.id.txt_print_other) {//其它
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.print_other_control, new OnClickListener() {

				@Override
				public void onClick(View v) {

					activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_PARAM_OTHER, null, null));

				}
			});


		} else {
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
