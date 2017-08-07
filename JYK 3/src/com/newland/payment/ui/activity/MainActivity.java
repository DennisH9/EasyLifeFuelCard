package com.newland.payment.ui.activity;


import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import java.security.SecureRandom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.base.util.AndroidTools;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ViewUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.invoke.ThirdInvokeController;
import com.newland.payment.trans.invoke.listener.InvokeResultListener;
import com.newland.payment.ui.fragment.BaseFragment;
import com.newland.payment.ui.fragment.BaseSettingFragment;
import com.newland.payment.ui.fragment.LoginFragment;
import com.newland.payment.ui.fragment.MainFragment;
import com.newland.payment.ui.fragment.SelfCheckFragment;
import com.newland.payment.ui.listener.KeyDownListener;
import com.newland.payment.ui.view.OperatorPasswordDialog;
import com.newland.payment.ui.view.SystemStatusManager;
import com.newland.payment.ui.view.progressbar.TimeOutProgressBar;
import com.newland.payment.ui.view.progressbar.TimeOutProgressBarListener;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.PublicLibJNIService;
import com.newland.pos.sdk.util.StringUtils;
import com.newland.pos.sdk.util.TimeUtils;
import com.nineoldandroids.animation.ObjectAnimator;


/**
 * 收单主界面
 *
 * @author CB
 * @time 2015-5-6 上午9:40:01
 */
public class MainActivity extends BaseActivity {

	private static MainActivity mainActivity;
	public static AbstractBaseTrans TRANS;
	
	/** 标题 */
//	@ViewInject(R.id.rl_title)
	private ViewGroup rlTitle;
	/** 返回按钮 */
//	@ViewInject(R.id.iv_left)
	private ImageView ivLeft;
	/** 返回文字 */
//	@ViewInject(R.id.txt_back)
	private TextView backtext;
	/** 返回待机界面按钮 */
//	@ViewInject(R.id.iv_waiting)
	private ImageView ivWaiting;
	/** 标题 */
//	@ViewInject(R.id.txt_center)
	private TextView txtCenter;
	/** 标题右侧按钮 */
//	@ViewInject(R.id.iv_right)
	private ImageView ivRight;
	/** 超时进度条 */
//	@ViewInject(R.id.time_out_progress_bar)
	private TimeOutProgressBar timeOutProgressBar;

	/** 加载遮罩 */
//	@ViewInject(R.id.v_progress)
	private View vProgress;
	/** 加载遮罩 */
//	@ViewInject(R.id.progress)
	private ProgressBar progress;
	/** 加载遮罩 */
//	@ViewInject(R.id.txt_progress)
	private TextView txtProgress;
	
	/** 栈顶fragment */
	public Fragment lastFragment;
	/** 当前的fragment */
	public Fragment currentFragment;
	/** 是否锁屏 */
	@SuppressWarnings("unused")
	private boolean isLock = false;
	private KeyDownListener keyDownListener;
	
	private boolean isSecond = false;
	/**
	 * 交易类型（供第三方调用时存放）
	 */
	private int transType;
	private Intent intent;
	private String outOrderNo;
	
	public OperatorPasswordDialog operatorPasswordDialog;
	public Dialog commonDialog;
	
	private CommonThread queueThread;
	private List<ThreadCallBack> queueTasks;
	
	private int activateState = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		View view = View.inflate(this, R.layout.main_activity, null);  
//		setTheme(R.style.translucent);
		setTranslucentStatus();
		setContentView(view);
		super.onCreate(savedInstanceState);
		/**初始化设备控制器*/
		LoggerUtils.d("333 设备控制器初始化init");
//		DeviceController.getInstance().init(this);
		
		// APP 获取 HOME keyEvent的设定关键代码。
		getWindow().addFlags(3);
		getWindow().addFlags(5);
//		if(VERSION.SDK_INT > VERSION_CODES.JELLY_BEAN_MR1) {
////			getWindow().addFlags(WindowManager.LayoutParams.FLAG_HOME_KEY_EVENT+1);
//			getWindow().addFlags(3);
//		} else {
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_HOME_KEY_EVENT);
//		}
		if (Build.VERSION.SDK_INT >= 19) { // Android 4.4及其以上沉浸式效果
			int statusHeight = ViewUtils.getStatusHeight(this);
			View statusBar = findViewById(R.id.view_status_bar);
			LayoutParams params = statusBar.getLayoutParams();
			params.height = statusHeight;
			params.width = LayoutParams.MATCH_PARENT;
			statusBar.setLayoutParams(params);
		}
		init();
	}

	private void init() {
		rlTitle = (ViewGroup)findViewById(R.id.rl_title);
		ivLeft = (ImageView)findViewById(R.id.iv_left);
		backtext = (TextView)findViewById(R.id.txt_back);
		ivWaiting = (ImageView)findViewById(R.id.iv_waiting);
		txtCenter = (TextView)findViewById(R.id.txt_center);
		ivRight = (ImageView)findViewById(R.id.iv_right);
		timeOutProgressBar = (TimeOutProgressBar)findViewById(R.id.time_out_progress_bar);
		vProgress = findViewById(R.id.v_progress);
		progress = (ProgressBar)findViewById(R.id.progress);
		txtProgress = (TextView)findViewById(R.id.txt_progress);
		findViewById(R.id.iv_left).setOnClickListener(clickListener);
		findViewById(R.id.iv_waiting).setOnClickListener(clickListener);
	}
	
	
	/**
	 * 用来处理是事有第三方调用的事件
	 * @return 是否继续后面的业务（给登陆专用）
	 * <li>true: 不需要继续后面的业务 </li>
	 * <li>false: 继续后面的业务</li>
	 */
	public boolean doThirdInvoke() {
		if (transType > 0) {
			// 第三方启动
			//外部订单号
			if(!StringUtils.isNullOrEmpty(intent.getStringExtra("outOrderNo"))){
				this.outOrderNo = intent.getStringExtra("outOrderNo");
				LoggerUtils.d("lxb 外部订单号：" + this.outOrderNo);
			}
			new ThirdInvokeController().invoke(this, transType, intent, new InvokeResultListener() {
				@Override
				public void succ(Intent intent) {
					transType = 0;
					//0是成功，-1是失败
					setResult(0, intent);
					finish();
				}
				
				@Override
				public void fail(String message) {
					transType = 0;
					setResult(-1, intent);
					finish();
				}
			});
			return true;
		}
		return false;
	}
	
	/**
	 * 用来处理是事有第三方调用的事件（自动签到失败处理）
	 * @return 是否继续后面的业务（给登陆专用）
	 * <li>true: 不需要继续后面的业务 </li>
	 * <li>false: 继续后面的业务</li>
	 */
	public boolean doThirdInvokeLoginFail() {
		
			transType = 0;
			LoggerUtils.d("lxb 处理是事有第三方调用的事件（自动签到失败处理）");
			intent.putExtra("responseCode", "EC");
			intent.putExtra("message", "自动签到异常");
			intent.putExtra("transCode", 0);
			setResult(-1, intent);
			finish();
			LoggerUtils.d("lxb end");
			return true;

	}
	/**
	 * 是否第三方调用
	 * @return
	 */
	public boolean isThirdInvoke() {
		return transType>0;
	}
	
	@Override
	protected void onStart() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		LoggerUtils.e("当前登陆操作员："+(App.USER.getUserNo()==null?"  ":App.USER.getUserNo()));
		super.onStart();
		this.intent = getIntent();
//		Bundle bundle = intent.getExtras();
		LoggerUtils.d("第三方传入intent：" + intent.toString());
		this.transType = intent.getIntExtra("transType", -1);
		LoggerUtils.d("第三方传入交易类型：" + this.transType);
		this.outOrderNo = null;
		// 界面路由
		if(App.mainMenuData.MAIN_MENU_ITEMS.size() == 0 && !isThirdInvoke()){ // 未初始化
			returnLogin(false);
		} else {
			//第三方调用,除了第一次签到,其他不调用签到
			if ((StringUtils.isEmpty(App.USER.getUserNo()) || isSignOut()) && !isThirdInvoke()) {
				LoggerUtils.d("lxb  isThirdInvoke0000");
				returnLogin(false);
			} else {
				if (isThirdInvoke()){
					//TODO
					App.USER.setUserNo("01");
					
					if(App.mainMenuData.MAIN_MENU_ITEMS.size() == 0){
						LoggerUtils.d("lxb  isThirdInvoke1111");
						SelfCheckFragment fragment = new SelfCheckFragment();
						fragment.thirdInvokeCheckInit(context);	
						//switchContent(new SelfCheckFragment());
					}
					LoggerUtils.e("执行第三方调用");
					if(!isSecond)
					{
						if(!StringUtils.isNullOrEmpty(intent.getStringExtra("operatorNo"))){
							App.USER.setUserNo(intent.getStringExtra("operatorNo"));
						}	
						doThirdInvoke(); // 执行第三方调用
					}
					isSecond = true;
				} else {
					if (TimeUtils.getCurrentDate().equals(ParamsUtils.getString(ParamsConst.PARAMS_RUN_LOGIN_DATE)) == false
							|| !ParamsUtils.getBoolean(ParamsTrans.PARAMS_FLAG_SIGN)){
						// 退出应用再次进入，强制再次登陆
						LoggerUtils.d("lxb  isThirdInvoke222");
						returnLogin(false);
						
					}else {
						activity.returnFirstMainMenu();
					}
	
				}
			}
		}
		
		
		// TODO N900也需要设置默认输入法，系统暂不支持
		if (App.SCREEN_TYPE == Const.ScreenType.IM_81) {
			// 输入法切换
			InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
			List<InputMethodInfo> lists = imm.getInputMethodList();
			for (InputMethodInfo info:lists) {
//				if ("com.android.inputmethod.pinyin/.PinyinIME".equals(info.getId())) {
//					SettingsManager settingsManager = (SettingsManager) getSystemService(SETTINGS_MANAGER_SERVICE);
//					settingsManager.setDefaultInputMethod(info.getId());
//				}
			}
		}
	}
	
//	@Override
//	protected void onStop() {
//		LoggerUtils.e("MainActivity onStop ...");
//		hideTimeOut();

//		if (AbstractBaseTrans.TRANS_FRAGMENT_COUNT>0){
			// 交易取消
//			try {
//				AbstractBaseTrans.TRANS_FRAGMENT_COUNT = 0;
//				LoggerUtils.d("交易中途中断，关闭事务...");
//				DeviceController.getInstance().getDeviceTransationManager().endTransaction();
//			} catch (OpenTrasactionException e) {
//				e.printStackTrace();
//			}
//		}
		// 设备操作复位
//		DeviceController.getInstance().reset();
//		super.onStop();
//	}


	private boolean isSignOut(){
		if (TimeUtils.getCurrentDate().equals(ParamsUtils.getString(ParamsConst.PARAMS_RUN_LOGIN_DATE)) == false
				|| !ParamsUtils.getBoolean(ParamsTrans.PARAMS_FLAG_SIGN)){
			return true;
		}
		return false;
	}

	@Override
	protected void initData() {
		mainActivity = this;
		queueTasks = new ArrayList<ThreadCallBack>();
		if(queueThread != null){
			queueThread.stop();
			queueThread = null;
		}
		queueThread = new CommonThread(false, 100, true, new ThreadCallBack() {
			ThreadCallBack task = null;
			@Override
			public void onBackGround() {
				if (queueTasks.size() > 0) {
					try{
						task = queueTasks.get(0);
						task.onBackGround();
					}catch(Exception e){
						LoggerUtils.d("queueThread onBackGround", e);
					}
				}
			}
			@Override
			public void onMain() {
				if (task != null ) {
					try{
						task.onMain();
					}catch(Exception e){
						LoggerUtils.d("queueThread onMain", e);
					}
					queueTasks.remove(task);
					task = null;
				}
			}
		});
		queueThread.start();
	}
	
//	@OnClick({R.id.iv_left, R.id.iv_waiting})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.iv_left) {
			backFragment();

		} else if (i == R.id.iv_waiting) {
//			activity.switchContent(new WaitingFragment());
		}
	}

	@Override
	protected void initEvent() {
		
	}
	@Override
	protected void onDestroy() {
		if (queueThread != null) {
			queueThread.stop();
			queueThread = null;
		}
		closeTimeOut();
		super.onDestroy();
	}

	/**
	 * 添加队列任务
	 * @param threadCallBack
	 */
	public void addQueueTask(ThreadCallBack threadCallBack) {
		if (queueThread != null) {
			queueTasks.add(threadCallBack);
		}
	}
	public static MainActivity getInstance() {
		return mainActivity;
	}


	/**
	 * 登录界面样式
	 */
	public void setLoginStyle() {
		LoggerUtils.d("hjh     setLoginStyle111111");
		ivLeft.setVisibility(View.GONE);
		ivWaiting.setVisibility(View.GONE);
		backtext.setVisibility(View.GONE);
	}

	/**
	 * 非主界面样式
	 */
	public void setExceptMainStyle() {
		LoggerUtils.d("hjh     setExceptMainStyle111111");
		ivLeft.setVisibility(View.VISIBLE);
		ivWaiting.setVisibility(View.GONE);
		backtext.setVisibility(View.VISIBLE);
	}

	/**
	 * 主界面样式
	 */
	public void setMainStyle() {
		LoggerUtils.d("hjh     setMainStyle111111");
		ivLeft.setVisibility(View.GONE);
		ivWaiting.setVisibility(View.GONE);
		backtext.setVisibility(View.GONE);
	}

	/**
	 * 设置标题
	 */
	public void setTitle(int title) {
		setTitle(title, true);
	}

	/**
	 * 设置标题
	 */
	public void setTitle(String title) {
		setTitle(title, true);
	}

	public void setTitle(int title, boolean isAddBack) {

		setTitle(getString(title), isAddBack);
	}
	public void setTitle(String title, boolean isAddBack) {
		LoggerUtils.i("title:" + title);
		txtCenter.setText(title);
		showTitle();
	}
	/**
	 * 获取标题
	 */
	public String getMainTitle() {
		if (txtCenter.getText() != null) {
			return txtCenter.getText().toString();
		}
		else {
			return null;

		}
	}
	/**
	 * 设置标题栏上的返回图标的按键事件
	 */
	public void setTitleBarGoBackEvent(OnClickListener onClickListener){
		if(ivLeft!=null) {
			ivLeft.setOnClickListener(onClickListener);
		}
	}

	/**
	 * 锁定
	 */
	public void lock() {
		isLock = true;
	}
	/**
	 * 解锁
	 */
	public void unLock() {
		isLock = false; 
	}
	
	public void hideTitle() {
		rlTitle.setVisibility(View.GONE);
	}
	
	public void showTitle() {
		if (!rlTitle.isShown())
			rlTitle.setVisibility(View.VISIBLE);
	}
		
	/**
	 * 设置进度条
	 * @param time 单位（秒）
	 * @param
	 */
	public void startTimeOut(long time){
		startTimeOut(time, new TimeOutProgressBarListener() {
			
			@Override
			public void timeout() {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						BaseFragment fragment = (BaseFragment) AndroidTools.getTopFragment(mainActivity);
						if (fragment == null) {
							return;
						}
						if (fragment.isHasBean()) {
							fragment.onTimeOut();
						} else if (fragment instanceof LoginFragment ) {
							return;
						}else if (fragment instanceof MainFragment) {
//							activity.switchContent(new WaitingFragment());
						} else if (fragment instanceof BaseSettingFragment) {
							returnSettingMain();
							hideTimeOut();
						} else {
							returnMainMenu();
						}
					}
				});
			}
		});
	}
	
	/**
	 * 启动进度条
	 * @param time 单位（秒）
	 * @param listener
	 */
	public void startTimeOut(long time, TimeOutProgressBarListener listener){
		LoggerUtils.i("start timeOut :" + time);
		
		if (time < 1) {
			hideTimeOut();
		} else {
			timeOutProgressBar.setTimeOut(time * 1000, listener);
			timeOutProgressBar.start();
		}
	}

	/**
	 * 重置进度条
	 */
	public void resetProgress() {
		timeOutProgressBar.resume();
		timeOutProgressBar.resetProgress();
	}
	/**
	 * 暂停进度条
	 */
	public void pauseProgress() {
		timeOutProgressBar.pause();
	}
	
	public void closeTimeOut() {

		LoggerUtils.i("close timeOut");
		timeOutProgressBar.close();
	}
	
	public void hideTimeOut() {
		LoggerUtils.i("hide timeOut");
		if (timeOutProgressBar != null &&
				timeOutProgressBar.getVisibility()==View.VISIBLE) {
			timeOutProgressBar.pause();
			timeOutProgressBar.close();
			timeOutProgressBar.setVisibility(View.GONE);
		}
	}
	
	public void showTimeOut() {
		LoggerUtils.i("show timeOut");
		if (timeOutProgressBar.getVisibility() != View.VISIBLE) {

			timeOutProgressBar.resume();
			timeOutProgressBar.resetProgress();
			timeOutProgressBar.setVisibility(View.VISIBLE);
			ObjectAnimator animator = ObjectAnimator.ofFloat(timeOutProgressBar,
					"translationY", -10, 0).setDuration(500);
			animator.start();
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LoggerUtils.e("Main onKeyDown");
		resetProgress();
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			if (keyDownListener != null) {
				return keyDownListener.onHome();
			}
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			boolean result = false;
			if (keyDownListener != null) {
				result = keyDownListener.onBack();
			}
			
			if (result) {
				backFragment();
			}
		}
		return false;
	}

	/**
	 * 设置返回监听
	 * @param
	 */
	public void setKeyDownListener(KeyDownListener keyDownListener) {
		this.keyDownListener = keyDownListener;
	}

	/**
	 * 显示加载
	 */
	public void showProgress() {
		showProgress(true,null);
	}
	/**
	 * 显示加载
	 */
	public void showProgress(boolean isShowIcon) {
		showProgress(isShowIcon,null);
	}
	/**
	 * 显示加载
	 */
	public void showProgress(boolean isShowIcon, Integer resText) {
		vProgress.setVisibility(View.VISIBLE);
		if (resText != null) {
			txtProgress.setText(resText);
		}
		progress.setVisibility(isShowIcon ? View.VISIBLE : View.GONE);
	}
	
	/**
	 * 隐藏加载
	 */
	public void hideProgress() {
		vProgress.setVisibility(View.GONE);
	}
	
	/**
	 * 销毁界面提示框
	 */
	public void dismissDialog() {
		
		if (commonDialog != null && commonDialog.isShowing()) {
			commonDialog.dismiss();
			commonDialog = null;
		}	
		if (operatorPasswordDialog!=null && operatorPasswordDialog.isShowing()) {
			operatorPasswordDialog.dismiss();
			operatorPasswordDialog = null;
		}	
	}
	
	public int getTransType() {
		return transType;
	}
	
	public void setTransType(int transType) {
		this.transType = transType;
	}


	public int getActivateState() {
		return activateState;
	}


	public void setActivateState(int activateState) {
		this.activateState = activateState;
	}


	public String getOutOrderNo() {
		return outOrderNo;
	}


	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}
	
	/**
	 * 设置沉浸式效果
	 * @author ZhengWx
	 * @date 2015年4月18日 上午10:49:52
	 * @since 1.0
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void setTranslucentStatus() {
		if (Build.VERSION.SDK_INT >= 19) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(0);
	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			initClickEvent(v);
		}
	};
}
