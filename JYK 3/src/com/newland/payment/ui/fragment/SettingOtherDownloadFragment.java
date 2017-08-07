package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.newland.base.util.MessageUtils;
import com.newland.payment.R;
import com.newland.payment.common.TransConst;
import com.newland.payment.trans.TransController;
import com.newland.payment.trans.manage.impl.CardBInCUpdate;
import com.newland.payment.trans.manage.impl.CardBinBUpdate;
import com.newland.payment.trans.manage.impl.Echo;
import com.newland.payment.trans.manage.impl.EmvBlackListDownload;
import com.newland.payment.trans.manage.impl.EmvParamsDownload;
import com.newland.payment.trans.manage.impl.ParamsTransmit;
import com.newland.payment.trans.manage.impl.RfParamsDownload;
import com.newland.payment.trans.manage.impl.StatusSend;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 下载功能界面
 *
 * @author CB
 * @date 2015-5-15 
 * @time 下午5:14:51
 */
public class SettingOtherDownloadFragment extends BaseSettingFragment{

	/** 回响测试 */
//	@ViewInject(R.id.txt_setting_other_download_echo_test) TextView txtSettingOtherDownloadEchoTest;
//	/** 参数传递 */
//	@ViewInject(R.id.txt_setting_other_download_parameter_pass) TextView txtSettingOtherDownloadParameterPassing;
//	/** IC卡公钥下载 */
//	@ViewInject(R.id.txt_setting_other_download_ic_public_key) TextView txtSettingOtherDownloadIcPublicKey;
//	/** IC卡参数下载 */
//	@ViewInject(R.id.txt_setting_other_download_ic_param) TextView txtSettingOtherDownloadIcParam;
//	/** 黑名单下载 */
//	@ViewInject(R.id.txt_setting_other_download_blacklist) TextView txtSettingOtherDownloadBlacklist;
//	/** 状态上传 */
//	@ViewInject(R.id.txt_setting_other_download_state_upload) TextView txtSettingOtherDownloadStateUpload;
//	/** 非接业务参数下载*/
//	@ViewInject(R.id.txt_setting_other_download_rf_param) TextView txtSettingOtherDownloadRfparamDownload;
//	/** BIN表B更新*/
//	@ViewInject(R.id.txt_setting_other_download_bin_b_update) TextView txtSettingOtherDownloadBinBUpdate;
//	/** BIN表C更新*/
//	@ViewInject(R.id.txt_setting_other_download_bin_c_update) TextView txtSettingOtherDownloadBinCUpdate;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_other_download_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		setTitle(R.string.setting_other_down_load);
	}

//	@OnClick({
//		R.id.txt_setting_other_download_echo_test,
//		R.id.txt_setting_other_download_parameter_pass,
//		R.id.txt_setting_other_download_ic_public_key,
//		R.id.txt_setting_other_download_ic_param,
//		R.id.txt_setting_other_download_blacklist,
//		R.id.txt_setting_other_download_state_upload,
//		R.id.txt_setting_other_download_rf_param,
//		R.id.txt_setting_other_download_bin_b_update,
//		R.id.txt_setting_other_download_bin_c_update
//	})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_setting_other_download_echo_test) {//回响测试
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.setting_other_download_echo_test, new OnClickListener() {

				@Override
				public void onClick(View v) {
					TransController controller = new TransController(context);
					controller.start(new Echo());
				}
			});

		} else if (i == R.id.txt_setting_other_download_parameter_pass) {//参数传递
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.setting_other_download_parameter_pass, new OnClickListener() {

				@Override
				public void onClick(View v) {
					TransController controller = new TransController(context);
					controller.start(new ParamsTransmit());
				}
			});

		} else if (i == R.id.txt_setting_other_download_ic_public_key) {//IC卡公钥下载
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.setting_other_download_ic_public_key, new OnClickListener() {
				@Override
				public void onClick(View v) {
					TransController controller = new TransController(context);
					controller.start(new EmvParamsDownload(TransConst.DownloadEmvParamType.CAPK));
				}
			});

		} else if (i == R.id.txt_setting_other_download_ic_param) {//IC卡参数下载
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.setting_other_download_ic_param, new OnClickListener() {
				@Override
				public void onClick(View v) {
					TransController controller = new TransController(context);
					controller.start(new EmvParamsDownload(TransConst.DownloadEmvParamType.AID));
				}
			});

		} else if (i == R.id.txt_setting_other_download_blacklist) {//黑名单下载
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.setting_other_download_blacklist, new OnClickListener() {
				@Override
				public void onClick(View v) {
					TransController controller = new TransController(context);
					controller.start(new EmvBlackListDownload());
				}
			});

		} else if (i == R.id.txt_setting_other_download_state_upload) {//状态上传
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.setting_other_download_state_upload, new OnClickListener() {

				@Override
				public void onClick(View v) {
					TransController controller = new TransController(context);
					controller.start(new StatusSend());
				}
			});

		} else if (i == R.id.txt_setting_other_download_rf_param) {//非接参数下载
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.setting_download_rf_params, new OnClickListener() {

				@Override
				public void onClick(View v) {
					TransController controller = new TransController(context);
					controller.start(new RfParamsDownload());
				}
			});

		} else if (i == R.id.txt_setting_other_download_bin_b_update) {//Bin B下载
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.setting_other_download_bin_b_update, new OnClickListener() {

				@Override
				public void onClick(View v) {
					TransController controller = new TransController(context);
					controller.start(new CardBinBUpdate());
				}
			});

		} else if (i == R.id.txt_setting_other_download_bin_c_update) {//Bin C下载
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.setting_other_download_bin_c_update, new OnClickListener() {

				@Override
				public void onClick(View v) {
					TransController controller = new TransController(context);
					controller.start(new CardBInCUpdate());
				}
			});

		}
	}

	@Override
	protected BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		
	}

}
