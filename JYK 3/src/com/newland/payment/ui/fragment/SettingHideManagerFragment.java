
package com.newland.payment.ui.fragment;


import android.app.Dialog;
import android.newland.os.NlBuild;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.newland.base.CommonThread;
import com.newland.base.util.AndroidTools;
import com.newland.base.util.MessageUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.emv.EmvAppModule;
import com.newland.payment.R;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.dao.impl.UserDaoImpl;
import com.newland.payment.mvc.model.User;
import com.newland.payment.mvc.service.impl.UserServiceImpl;
import com.newland.pos.sdk.bean.BaseBean;

//import com.newland.mtype.Module;
//import com.newland.mtype.ModuleType;
//import com.newland.mtype.module.common.pin.K21Pininput;
//import com.newland.pos.sdk.device.DeviceController;
//import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 厂商管理员登入
 *
 * @author linst
 * @date 2015-5-22
 * @time 
 */
public class SettingHideManagerFragment extends BaseFragment{
	

	/**
	 * EMV库版本
	 */
	private String emvVersion;
//	/**
//	 * k21固件版本
//	 */
//	private String k21FirmVersion;
//	/**
//	 * k21指令版本
//	 */
//	private String k21AppVersion;
//	/**
//	 * 硬件信息
//	 */
//	private String hardInfo;
	
	/**
	 * 安卓系统版本
	 */
	private String androidVersion;
	
	private Dialog dialog;
	
	public SettingHideManagerFragment()
	{
		super(0l);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.shop_manager_login_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		setTitle(R.string.shop_manager);	
	}
	
//	@OnClick({R.id.txt_reset_manager_password, R.id.txt_reset_pos_params,
//		R.id.txt_reset_flag, R.id.txt_more_version_flag
//		//R.id.txt_clear_all_key
//		})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_reset_manager_password) {//重置系统管理员密码
			dialog = MessageUtils.showCommonDialog(context, R.string.common_reset_manager_password, new OnClickListener() {

				@Override
				public void onClick(View v) {
					new CommonThread(new ThreadCallBack() {
						int result = -1;

						@Override
						public void onMain() {
							if (result == 1) {
								ToastUtils.show(context, R.string.reset_sys_password_scs);
							} else {
								ToastUtils.show(context, R.string.reset_sys_password_fail);

							}
						}

						@Override
						public void onBackGround() {
							User user = new User();
							UserServiceImpl impl = new UserServiceImpl(context);
							UserDaoImpl daoImpl = new UserDaoImpl(context);
							user = daoImpl.findByUserNoAndUserType("99", 3);

							result = impl.updateManagerPassword(user.getPassword(), "00000000");
						}
					}).start();
				}
			});

		} else if (i == R.id.txt_reset_pos_params) {//初始化pos参数
			dialog = MessageUtils.showCommonDialog(context, R.string.common_reset_pos_params, new OnClickListener() {

				@Override
				public void onClick(View v) {
					new CommonThread(new ThreadCallBack() {

						@Override
						public void onMain() {
							ToastUtils.show(context, R.string.common_reset_pos_params_succ);

						}

						@Override
						public void onBackGround() {
							ParamsUtils.clean();
							SelfCheckFragment.initExtendParamsToSharedPreferences(context);
						}
					}).start();
				}
			});

		} else if (i == R.id.txt_reset_flag) {//清标志

			activity.switchContent(new ShopManagerResetFlagFragment());


		} else if (i == R.id.txt_more_version_flag) {// 显示更多版本信息
			dialog = MessageUtils.showCommonDialog(getActivity(),
					R.string.common_more_version_flag,
					getMoreVersion(),
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							dialog.dismiss();
						}
					}, null);


//		case R.id.txt_clear_all_key:
//			// 清除所有标志
//			MessageUtils.showCommonDialog(getActivity(),
//					R.string.clear_all_key,
//					new View.OnClickListener() {
//
//						@Override
//						public void onClick(View arg0) {
//							MainActivity.getInstance().showProgress();
//							new CommonThread(new ThreadCallBack() {
//								boolean result;
//								@Override
//								public void onMain() {
//									MainActivity.getInstance().hideProgress();
//									ToastUtils.show(context, result ? R.string.common_clear_succ : R.string.fail_clear);
//								}
//
//								@Override
//								public void onBackGround() {
//									SecurityModule securityModule = SecurityModule.getInstance();
//									securityModule.clearKey();
////									Module module = DeviceController.getInstance().getStandardModule(ModuleType.COMMON_PININPUT);
////									if (module != null) {
////										K21Pininput pinInput = (K21Pininput) module;
////										result = pinInput.deleteKeyStore();
////										LoggerUtils.i(getString(R.string.clear_all_key)+":"+result);
////									}
//								}
//							}).start();
//						}
//					});
//			break;
		}
	}

	private String getMoreVersion(){
		if (emvVersion == null) {
			emvVersion = EmvAppModule.getInstance().getEmvVersion();
		}
//		fangjt
//		if (k21FirmVersion == null) {
//			getK21Version();
//		}
		androidVersion = NlBuild.VERSION.NL_FIRMWARE;
		String content = "";
		content += "应用版本："+AndroidTools.getApplicationVersionName(getActivity())+"\r\n";
		content += "android系统版本："+androidVersion+"\r\n";
//		content += "k21固件版本："+k21FirmVersion+"\r\n";
//		content += "k21指令版本："+k21AppVersion+"\r\n";
		content += "EMV库版本："+emvVersion+"\r\n";
//		content += "硬件信息："+hardInfo+"\r\n";
		return content;
	}
	
//	private void getK21Version(){
//		try {
//			DeviceController deviceController = DeviceController.getInstance();
//			deviceController.connect();
//			hardInfo = deviceController.getDeviceInfo().getBootVersion();
//			k21FirmVersion = deviceController.getDeviceInfo().getFirmwareVer().trim();
//			k21AppVersion = deviceController.getDeviceInfo().getAppVer().trim();
//			LoggerUtils.d("硬件版本 = "+hardInfo);
//			LoggerUtils.d("固件版本 = "+k21FirmVersion);
//			LoggerUtils.d("指令集版本 = "+k21AppVersion);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		
	}
	@Override
	public void onFragmentHide() {
		if (dialog!=null && dialog.isShowing()){
			dialog.dismiss();
			dialog = null;
		}
		super.onFragmentHide();
	}

	
}
