package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.model.User;
import com.newland.payment.mvc.service.impl.UserServiceImpl;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.view.OperatorNoListDialog;
import com.newland.payment.ui.view.OperatorPasswordDialog;
import com.newland.payment.ui.view.OperatorPasswordDialog.InputEventListener;
import com.newland.pos.sdk.bean.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 柜员管理界面
 * 
 * @author linst
 * 
 */
@SuppressLint("ValidFragment")
public class OperatorManagementFragment extends BaseFragment {

	/** 主管改密 */
//	@ViewInject(R.id.btnMainChanggePwd)
	TextView btnMainChanggePwd;
	/** 增加 */
//	@ViewInject(R.id.btnOperatorAdd)
	TextView btnOperatorAdd;
	/** 删除 */
//	@ViewInject(R.id.btnOperatorDel)
	TextView btnOperatorDel;
	/** 查询 */
//	@ViewInject(R.id.btnOperatorSearch)
	TextView btnOperatorSearch;
	/** 操作员改密 */
//	@ViewInject(R.id.btnOperatorChangePwd)
	TextView btnOperatorChangePwd;
	
	public static Boolean flag = false;
	public static String passwordInput = null;
	private String oldPassword = null;
	private String newPassword = null;
	private int step = 0;
	private OperatorPasswordDialog dialogMain;
	private OperatorPasswordDialog dialogAddOperator;
	private OperatorPasswordDialog dialogDelOperator;
	private InputEventListener inputEventListener;
	
	UserServiceImpl serviceImpl = new UserServiceImpl(context);

	

	public OperatorManagementFragment(boolean isTimeOut) {
		
		super(isTimeOut ? App.FRAGMENT_TIME : 0l);
	}

	
	public static OperatorManagementFragment newInstance(boolean isTimeOut) {
		OperatorManagementFragment fragment = new OperatorManagementFragment(isTimeOut);
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.operator_management_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {

		setTitle(R.string.common_operator_manage);
		activity.setLoginStyle();

		new CommonThread(new ThreadCallBack() {
			
			@Override
			public void onMain() {
				//如果是主管登入
				if (flag) {
					btnOperatorChangePwd.setVisibility(View.INVISIBLE);
				}				
			}
			
			@Override
			public void onBackGround() {
				flag = "1".equals(ParamsUtils.getString(ParamsConst.PARAMS_MAIN_MANAGER_LOGIN_FLAG));
			}
		}).start();
		
	}

	
//	@OnClick({ R.id.btnMainChanggePwd, R.id.btnOperatorAdd,
//			R.id.btnOperatorDel, R.id.btnOperatorSearch,
//			R.id.btnOperatorChangePwd })
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.btnMainChanggePwd) {
			step = 0;
			inputEventListener = new InputEventListener() {

				@Override
				public void onConfirm(Dialog dialog, final String value) {

					switch (step) {
						case 0:

							new CommonThread(new ThreadCallBack() {
								int result;

								@Override
								public void onMain() {
									// 原密码正确
									if (result == 2) {
										step = 1;
										oldPassword = value;
										dialogMain.setMTitle(context
												.getString(R.string.pls_input_new_main_password));


									} else {
										ToastUtils.show(
												context,
												context.getString(R.string.input_old_password_err));
										dialogMain.dismiss();
										step = 0;
										oldPassword = null;
										newPassword = null;
									}
								}

								@Override
								public void onBackGround() {
									result = serviceImpl.checkLogin("00", value);

								}
							}).start();

							break;
						case 1:
							if (value.equals(oldPassword)) {
								ToastUtils.show(
										activity,
										context.getString(R.string.input_new_old_password_same));
								step = 1;
							} else {
								newPassword = value;
								dialogMain.setMTitle(context
										.getString(R.string.pls_input_new_main_password_again));
								;
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
											ToastUtils.show(
													activity,
													context.getString(R.string.cg_main_password_scs));
											oldPassword = null;
											newPassword = null;
											dialogMain.dismiss();
											step = 0;
										} else {
											ToastUtils.show(
													activity,
													context.getString(R.string.cg_main_password_fail));
											oldPassword = null;
											newPassword = null;
											dialogMain.dismiss();
											step = 0;
										}

									}

									@Override
									public void onBackGround() {
										result = serviceImpl.updateMasterPassword(
												oldPassword, newPassword);
									}
								}).start();


							} else {
								ToastUtils.show(
										activity,
										context.getString(R.string.input_new_password_difference));
								dialogMain.dismiss();
								step = 0;
								oldPassword = null;
								newPassword = null;
							}
							break;
						default:
							ToastUtils.show(
									activity,
									context.getString(R.string.input_old_password_err));
							dialogMain.dismiss();
							step = 0;
							oldPassword = null;
							newPassword = null;
							break;
					}
				}

				@Override
				public void onCancel() {

				}

			};
			dialogMain = new OperatorPasswordDialog(activity,
					R.style.swiping_dialog, 6,
					R.string.pls_input_old_main_password, inputEventListener);

			dialogMain.show();


			// 增加操作员
		} else if (i == R.id.btnOperatorAdd) {
			inputEventListener = new InputEventListener() {

				@Override
				public void onConfirm(Dialog dialog, final String value) {


					new CommonThread(new ThreadCallBack() {
						int result;

						@Override
						public void onMain() {

							if (result == 2) {
								dialogAddOperator.dismiss();
								activity.switchContent(OperatorDelAddFragment
										.newInstance(true));


							} else {
								dialogAddOperator.dismiss();
								ToastUtils.show(activity, context.getString(R.string.error_password));
							}
						}

						@Override
						public void onBackGround() {
							result = serviceImpl.checkLogin("00", value);
						}
					}).start();


				}

				@Override
				public void onCancel() {

				}

			};
			dialogAddOperator = new OperatorPasswordDialog(activity,
					R.style.swiping_dialog, 6,
					R.string.pls_input_main_password, inputEventListener);
			dialogAddOperator.show();


			// 删除操作员
		} else if (i == R.id.btnOperatorDel) {
			inputEventListener = new InputEventListener() {

				@Override
				public void onConfirm(Dialog dialog, final String value) {

					new CommonThread(new ThreadCallBack() {
						int result;

						@Override
						public void onMain() {

							if (result == 2) {
								dialogDelOperator.dismiss();

								activity.switchContent(OperatorDelAddFragment
										.newInstance(false));

							} else {
								dialogDelOperator.dismiss();
								ToastUtils.show(activity, context.getString(R.string.error_password));

							}
						}

						@Override
						public void onBackGround() {
							result = serviceImpl.checkLogin("00", value);
						}
					}).start();
				}

				@Override
				public void onCancel() {

				}

			};

			dialogDelOperator = new OperatorPasswordDialog(activity,
					R.style.swiping_dialog, 6,
					R.string.pls_input_main_password, inputEventListener);
			dialogDelOperator.show();


			// 查询操作员
		} else if (i == R.id.btnOperatorSearch) {
			final ArrayList<String> userNoList = new ArrayList<String>();

			new CommonThread(new ThreadCallBack() {
				List<User> users;

				@Override
				public void onMain() {
					if (users != null && users.size() > 0) {
						for (User user : users) {
							if (user != null) {
								userNoList.add("操作员号码：" + user.getUserNo());
							}
						}
					}
					OperatorNoListDialog dialog = new OperatorNoListDialog(context, userNoList);
					dialog.setTitle(R.string.operator_list);
					dialog.show();
				}

				@Override
				public void onBackGround() {
					users = serviceImpl.findAllUsers();
				}
			}).start();


			// 操作员改密
		} else if (i == R.id.btnOperatorChangePwd) {
			activity.switchContent(OperatorChangePwdFragment
					.newInstance());


		} else {
		}

	}
	
	@Override
	public void onFragmentHide() {
		if (dialogMain!=null && dialogMain.isShowing()){
			dialogMain.dismiss();
			dialogMain = null;
		}
		if (dialogAddOperator!=null && dialogAddOperator.isShowing()){
			dialogAddOperator.dismiss();
			dialogAddOperator = null;
		}
		if (dialogDelOperator!=null && dialogDelOperator.isShowing()){
			dialogDelOperator.dismiss();
			dialogDelOperator = null;
		}
		super.onFragmentHide();
	}


	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {

	}

}
