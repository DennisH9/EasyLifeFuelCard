package com.newland.payment.ui.fragment;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.base.CommonThread;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.service.impl.UserServiceImpl;
import com.newland.payment.ui.view.OperatorPasswordDialog;
import com.newland.payment.ui.view.OperatorPasswordDialog.InputEventListener;
import com.newland.pos.sdk.bean.BaseBean;


/**
 * 密码管理界面
 *
 * @author CB
 * @date 2015-5-22 
 * @time 上午10:16:44
 */
public class SettingPasswordManagerFragment extends BaseSettingFragment{
	
	private UserServiceImpl serviceImpl = new UserServiceImpl(activity);
	private String oldPassword;
	private String newPassword;
	private int step = 0;
	private OperatorPasswordDialog dialogSys;
	private OperatorPasswordDialog dialogSafe;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_password_manager_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		setTitle(R.string.setting_password);
	}
//	@OnClick({R.id.setting_sys_manager_password,R.id.setting_sys_safe_password})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.setting_sys_manager_password) {
			step = 0;

			dialogSys = new OperatorPasswordDialog(activity, R.style.swiping_dialog, 8, R.string.pls_input_old_sys_password, new InputEventListener() {

				@Override
				public void onConfirm(Dialog dialog, final String value) {

					switch (step) {
						case 0:

							new CommonThread(new ThreadCallBack() {
								int result;

								@Override
								public void onMain() {
									//原密码正确
									if (result == 3) {
										step = 1;
										oldPassword = value;
										dialogSys.setMTitle(context.getString(R.string.pls_input_new_sys_password));
										;
									} else {
										ToastUtils.show(context, context.getString(R.string.input_old_password_err));
										dialogSys.dismiss();
										step = 0;
										oldPassword = null;
										newPassword = null;

									}
								}

								@Override
								public void onBackGround() {
									result = serviceImpl.checkLogin("99", value);
								}
							}).start();

							break;
						case 1:
							if (value.equals(oldPassword)) {
								ToastUtils.show(activity, context.getString(R.string.input_new_old_password_same));
								step = 1;
							} else {
								newPassword = value;
								dialogSys.setMTitle(context.getString(R.string.pls_input_new_sys_password_again));
								step = 2;

							}
							break;
						case 2:
							if (newPassword.equals(value)) {

								new CommonThread(new ThreadCallBack() {
									int result;

									@Override
									public void onMain() {

										if (result == 1) {
											ToastUtils.show(activity, context.getString(R.string.cg_sys_password_scs));
										} else {
											ToastUtils.show(activity, context.getString(R.string.cg_sys_password_fail));

										}
									}

									@Override
									public void onBackGround() {

										result = serviceImpl.updateManagerPassword(oldPassword, newPassword);
									}
								}).start();

							} else {
								ToastUtils.show(activity, context.getString(R.string.input_new_password_difference));

							}
							dialogSys.dismiss();
							step = 0;
							oldPassword = null;
							newPassword = null;
							break;
						default:
							ToastUtils.show(activity, context.getString(R.string.input_old_password_err));
							dialogSys.dismiss();
							step = 0;
							oldPassword = null;
							newPassword = null;
							break;
					}
				}

				@Override
				public void onCancel() {

				}

			});
			dialogSys.show();


		} else if (i == R.id.setting_sys_safe_password) {
			step = 0;

			dialogSafe = new OperatorPasswordDialog(activity, R.style.swiping_dialog, 6, R.string.pls_input_old_safe_password, new InputEventListener() {

				@Override
				public void onConfirm(Dialog dialog, String value) {


					switch (step) {

						case 0:
							//输入安全密码正确

							if (ParamsUtils.getString(ParamsConst.PARAMS_SAFE_PASSWORD).equals(value)) {
								step = 1;
								oldPassword = value;
								dialogSafe.setMTitle(context.getString(R.string.pls_input_new_safe_password));
							} else {
								ToastUtils.show(context, context.getString(R.string.input_old_password_err));
								step = 0;
								dialogSafe.dismiss();
								oldPassword = null;
								newPassword = null;
							}
							break;
						case 1:
							if (value.equals(oldPassword)) {
								ToastUtils.show(activity, context.getString(R.string.input_new_old_password_same));
								step = 1;
							} else {
								newPassword = value;
								dialogSafe.setMTitle(context.getString(R.string.pls_input_new_safe_password_again));
								step = 2;

							}
							break;
						case 2:
							if (newPassword.equals(value)) {
								ToastUtils.show(activity, context.getString(R.string.cg_safe_password_scs));
								ParamsUtils.setString(ParamsConst.PARAMS_SAFE_PASSWORD, newPassword);

							} else {
								ToastUtils.show(activity, context.getString(R.string.input_new_password_difference));
							}
							step = 0;
							dialogSafe.dismiss();
							oldPassword = null;
							newPassword = null;
							break;
						default:
							ToastUtils.show(activity, context.getString(R.string.input_old_password_err));
							dialogSafe.dismiss();
							step = 0;
							oldPassword = null;
							newPassword = null;
							break;
					}
				}

				@Override
				public void onCancel() {

				}

			});
			dialogSafe.show();


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

	@Override
	public void onFragmentHide() {
		if (dialogSafe!=null && dialogSafe.isShowing()) {
			dialogSafe.dismiss();
			dialogSafe = null;
		}
		if (dialogSys!=null && dialogSys.isShowing()) {
			dialogSys.dismiss();
			dialogSys = null;
		}
		super.onFragmentHide();
	}

}
