package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.base.util.AndroidTools;
import com.newland.base.util.InputUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.base.util.ViewUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.service.UserService;
import com.newland.payment.mvc.service.impl.UserServiceImpl;
import com.newland.payment.trans.TransController;
import com.newland.payment.trans.impl.Login;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.TimeUtils;

/**
 * 登录界面
 * 
 * @author CB
 * @time 2015-4-23 上午8:37:48
 */
public class LoginFragment extends BaseFragment {

	/** 登录按钮 */
//	@ViewInject(R.id.txt_login)
	private TextView txtLogin;
	/** 记住用户名 */
//	@ViewInject(R.id.iv_rememeber)
	private ImageView ivRemember;
	/** 用户名 */ 
//	@ViewInject(R.id.et_user)
	private EditText etUser;
	/** 密码 */
//	@ViewInject(R.id.et_password)
	private EditText etPassword;
	/** 总布局 */
//	@ViewInject(R.id.v_main)
	private View vMain;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	private KeyboardNumber keyBoardNumber;
	/** 时间 */
//	@ViewInject(R.id.txt_time)
	private TextView txtTime;
	/** 日期 */
//	@ViewInject(R.id.txt_date)
	private TextView txtDate;
	/** 星期 */
//	@ViewInject(R.id.txt_week)
	private TextView txtWeek;
	/** 版本号 */
//	@ViewInject(R.id.txt_version)
	private TextView txtVersion;

	/** 是否记住用户名 */
	private boolean isRemember = false;
	private AbstractKeyBoardListener keyBoardListener;
	private int userNoMaxSize = Const.USER_NO_SIZE;
	private int passwordMaxSize = 999;
	private UserService userServiceImpl;
	private Boolean mainManagerLoginFlag = false;
	private String userLogin =null;
	private CommonThread timeThread;
	

	public LoginFragment(boolean isManageLogin) {
		super(0);
	}
	
	public LoginFragment() {
		this(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.login_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	private void initView() {
		txtLogin = (TextView) mFragmentView.findViewById(R.id.txt_login);
		ivRemember = (ImageView) mFragmentView.findViewById(R.id.iv_rememeber);
		etUser = (EditText) mFragmentView.findViewById(R.id.et_user);
		etPassword = (EditText) mFragmentView.findViewById(R.id.et_password);
		vMain =  mFragmentView.findViewById(R.id.v_main);
		keyBoardNumber = (KeyboardNumber) mFragmentView.findViewById(R.id.key_board_number);
		txtDate = (TextView) mFragmentView.findViewById(R.id.txt_date);
		txtVersion = (TextView) mFragmentView.findViewById(R.id.txt_version);
	}

	@Override
	protected void initData() {
		activity.lock();
		initView();
		etUser.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
		etUser.setHint(R.string.input_user_number);
		etPassword.setHint(R.string.input_password);
		etUser.setTextIsSelectable(true);
		etPassword.setTextIsSelectable(true);

		userServiceImpl = new UserServiceImpl(context);
		keyBoardNumber.setTelephoneKeyDisplay(true);

		if (App.SCREEN_TYPE == Const.ScreenType.IM_81) {
			timeThread = new CommonThread(false, 60000,new ThreadCallBack() {
				
				@Override
				public void onMain() {
					txtTime.setText(TimeUtils.getCurrentTime("HH:mm"));
				}
				
				@Override
				public void onBackGround() {
					
				}
			});
			txtDate.setText(TimeUtils.getTimeNow("MM-dd"));
			txtWeek.setText(TimeUtils.getTimeNow("E"));
			txtVersion.setText(AndroidTools.getApplicationVersionName(context));
		}
	}

//	@OnClick({ R.id.txt_login, R.id.iv_rememeber, R.id.v_main, R.id.et_user,
//		R.id.et_password })
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_login) {
			if (ViewUtils.isFastClick()) {
				return;
			}
			login();

		} else if (i == R.id.iv_rememeber) {
			new CommonThread(new ThreadCallBack() {

				@Override
				public void onMain() {
					if (isRemember) {
						ivRemember.setImageResource(R.drawable.h_login_remebered);
					} else {
						ivRemember.setImageResource(R.color.transparent);
					}
				}

				@Override
				public void onBackGround() {
					isRemember = !isRemember;
					ParamsUtils.setBoolean(ParamsConst.PARAMS_IS_REMEMBER, isRemember);
				}
			}).start();

		} else if (i == R.id.v_main) {
			keyBoardNumber.setVisibility(View.GONE);

		} else if (i == R.id.et_user) {
			ViewUtils.setFocus(etUser);
			keyBoardNumber.setVisibility(View.VISIBLE);

		} else if (i == R.id.et_password) {
			ViewUtils.setFocus(etPassword);
			keyBoardNumber.setVisibility(View.VISIBLE);

		}
	}

	@Override
	protected boolean onBackKeyDown() {
		App.getInstance().exit();
		return true;
	}
	
	@Override
	protected void initEvent() {

		keyBoardListener = new AbstractKeyBoardListener(etPassword,InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD) {

			@Override
			public void onEnter() {
				if (etPassword.getText().length() == 0) {
					etPassword.setFocusable(true);
					etPassword.setFocusableInTouchMode(true);
					etPassword.requestFocus();
				} else {
					login();
				}
			}
			@Override
			public void onCancel() {
				keyBoardNumber.setVisibility(View.GONE);
				super.onCancel();
			}

		};

		keyBoardNumber.setKeyBoardListener(keyBoardListener);

		etUser.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {
					keyBoardNumber.setVisibility(View.VISIBLE);
					keyBoardListener.setValue(etUser.getText().toString());
					keyBoardListener.setMaxLength(userNoMaxSize);
					keyBoardListener.setTargetView(etUser);
				}
			}
		});
		
		etPassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					keyBoardNumber.setVisibility(View.VISIBLE);
					keyBoardListener.setValue(etPassword.getText().toString());
					keyBoardListener.setTargetView(etPassword);
					refreshInputMaxLen();
				}
			}
		});
	}
	
	private void refreshInputMaxLen(){
		new CommonThread(new ThreadCallBack() {

			@Override
			public void onMain() {

				keyBoardListener.setMaxLength(passwordMaxSize);
				if (passwordMaxSize < etPassword.length()) {
					etPassword.setText("");
					
				}
				InputUtils.setMaxLength(etPassword, passwordMaxSize);
				if (passwordMaxSize == 0) {
					if (etUser.getText().length() == 0) {
						ToastUtils.show(context,
								R.string.login_error_not_user_no);
					} else {
						ToastUtils.show(context,
								R.string.login_error_not_user);
					}
				}    
			}

			@Override
			public void onBackGround() {
				// 设置最大输入字数
				passwordMaxSize = userServiceImpl
						.getPasswdLenByUserNo(etUser.getText()
								.toString());
			}
		}).start();
	}

	@Override
	public BaseBean getBean() {
		return null;
	}

	/**
	 * 登陆
	 */
	public void login() {
		userLogin = etUser.getText().toString();
		if(isRemember &&
				!userLogin.equals("99") &&
					!userLogin.equals("00")) {
			ParamsUtils.setString(ParamsConst.PARAMS_REMEMMBER_NO, userLogin);
		} else {
			etUser.setText(null);
		}
 
		new CommonThread(new ThreadCallBack() {
			private int result = -1;
			@Override
			public void onMain() {
				switch (result) {
				case -1:
					// -1-操作员号不存在
					ToastUtils.show(context, R.string.operator_no_contain);
					break;
				case 0:
					// 0-密码错误
					ToastUtils.show(context, R.string.error_password);
					keyBoardNumber.setVisibility(View.VISIBLE);
					break;
				case 1:
					// 1-一般操作员登陆
					App.USER.setUserNo(userLogin);
					App.USER.setPassword(etPassword.getText().toString());
					LoggerUtils.e("一般操作员登陆:"+App.USER.getUserNo());
					//登陆成功先验证主菜单再执行跳转
					activity.showProgress();
					new CommonThread(new ThreadCallBack() {
						
						@Override
						public void onMain() {
							// 判断终端是否已签到，若未签到先签到
							if (TimeUtils.getCurrentDate().equals(ParamsUtils.getString(ParamsConst.PARAMS_RUN_LOGIN_DATE)) == false
								|| !ParamsUtils.getBoolean(ParamsTrans.PARAMS_FLAG_SIGN)) {
								TransController transController = new TransController(activity);
								transController.start(new Login());
							//	activity.hideProgress();
							} else {
								new CommonThread(new ThreadCallBack() {
									
									@Override
									public void onMain() {
							//			activity.hideProgress();
										if (activity.isThirdInvoke()) {
											activity.doThirdInvoke();
										} else {
											LoggerUtils.i(App.mainMenuData.MAIN_MENU_ITEMS.toString());
											activity.returnFirstMainMenu();//switchContent(new MainFragment(R.string.common_main, App.mainMenuData.MAIN_MENU_ITEMS));
										}
									}
									
									@Override
									public void onBackGround() {
										ParamsUtils.setBoolean(ParamsConst.PARAMS_MAIN_MANAGER_LOGIN_FLAG, false);
									}
								}).start();
								
							}
						}
						
						@Override
						public void onBackGround() {
							App.mainMenuData.checkAll();
							LoggerUtils.i("Login check :"+App.mainMenuData.MAIN_MENU_ITEMS.toString());
						}
					}).start();
					
					break;
				case 2:
					new CommonThread(new ThreadCallBack() {
						 
						@Override
						public void onMain() {
							activity.switchContent(OperatorManagementFragment.newInstance(false));
						}
						
						@Override
						public void onBackGround() {
							ParamsUtils.setBoolean(ParamsConst.PARAMS_MAIN_MANAGER_LOGIN_FLAG, true);
						}
					}).start();
					break;
				case 3:
					// 3-系统管理员登陆
					activity.switchContent(new SystemManageFragment());
					break;
				case 4:
					// 4-产商管理员登陆
					activity.switchContent(new SettingHideManagerFragment());
					break;
				}
				// 清除密码	

				etPassword.setFocusable(true);
				etPassword.setFocusableInTouchMode(true);
				etPassword.requestFocus();
				etPassword.setText("");
				keyBoardListener.setValue("");

				// 解锁
				MainActivity.getInstance().unLock();
			}
 
			@Override
			public void onBackGround() {
				
					result = userServiceImpl.checkLogin(userLogin, etPassword
							.getText().toString());
			}
		}).start();

	}
	
	
	public Boolean getMainManagerLoginFlag() {
		return mainManagerLoginFlag;
	}
	public void setMainManagerLoginFlag(Boolean mainManagerLoginFlag) {
		this.mainManagerLoginFlag = mainManagerLoginFlag;
	}

	@Override
	public void onFragmentShow() {
		activity.hideTitle();
		activity.hideTimeOut();
		isRemember = ParamsUtils.getBoolean(ParamsConst.PARAMS_IS_REMEMBER);
		String userNoRemebered = ParamsUtils.getString(ParamsConst.PARAMS_REMEMMBER_NO);
		if(isRemember)
		{ 
			ivRemember.setImageResource(R.drawable.h_login_remebered);
			etUser.setText(userNoRemebered);
			etPassword.setFocusable(true);
			etPassword.setFocusableInTouchMode(true);
			etPassword.requestFocus();
			refreshInputMaxLen();
		}
		else {
			ivRemember.setImageResource(R.color.transparent);
		}
		if (timeThread != null) {
			timeThread.start();
		}
		super.onFragmentShow();
	}

	@Override
	public void onFragmentHide() {
		if (timeThread != null) {
			timeThread.pause();
		}
		activity.hideProgress();
		super.onFragmentHide();
	}

	
	@Override
	public void onDestroy() {
		if (timeThread != null) {
			timeThread.stop();
		}
		super.onDestroy();
	}

}
