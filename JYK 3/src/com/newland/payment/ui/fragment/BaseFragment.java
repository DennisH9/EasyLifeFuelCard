package com.newland.payment.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.lidroid.xutils.ViewUtils;
import com.newland.base.util.AndroidTools;
import com.newland.base.util.MessageUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.listener.KeyDownListener;
import com.newland.payment.ui.listener.onTimeOutListener;
import com.newland.payment.ui.view.SettingEditView;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.common.TransConst.StepResult;
import com.newland.pos.sdk.util.LoggerUtils;
/**
 * 基础类
 * 继承该类需将mFragmentView = inflater.inflate(R.layout.xxx, null);方法写在super.onCreateView(inflater, container, savedInstanceState);前面
 * @ClassName: BaseYYFragment
 * @author jby
 * @date 2014年9月11日 下午3:10:37
 *
 */
public abstract class BaseFragment extends Fragment implements onTimeOutListener{

	protected Context context;
	protected MainActivity activity;
	protected View mFragmentView;
	private boolean isShowStatus = true;
	private String title;
	
	/** 是否已经加载数据 **/
	protected boolean hasDataSetuped = false;
	
	private Long outTime;

	public BaseFragment() {
		this.outTime = App.FRAGMENT_TIME;
	}
	
	/***
	 * 有默认超时时间的初始化
	 * @param outTime 单位（秒）,0-关闭
	 */
	public BaseFragment(long outTime) {
		this.outTime = outTime;
	}

	/**
	 * 该方法调用顺序优于onCreate
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName() + "->setUserVisibleHint[isVisibleToUser:"+isVisibleToUser
					+ ",this.isVisible():" + this.isVisible() +"]");
		}
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName()+"-> onCreate");
		}
		this.context = getActivity();
		this.activity = (MainActivity)getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName()+"-> onCreateView");
		}
		ViewUtils.inject(this,mFragmentView);
		
		//立即加载，只执行于此
		rushLoad();
		
		activity.setKeyDownListener(new KeyDownListener() {
			
			@Override
			public boolean onHome() {
				// HOME键处理
				final Fragment topFragment = AndroidTools.getTopFragment(activity);
				if (topFragment != null) {
					return ((BaseFragment)topFragment).doClickHomeEvent();
				}else{
					return doClickHomeEvent();
				}
			}
			
			@Override
			public boolean onBack() {
				// HOME键处理
				final Fragment topFragment = AndroidTools.getTopFragment(activity);
				if (topFragment != null) {
					return ((BaseFragment)topFragment).doClickBackEvent();
				}else{
					return doClickBackEvent();
				}
			}
		});
		
		mFragmentView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_UP)
					MainActivity.getInstance().resetProgress();
				return true;
			}
		});
		/**
		 * 设置标题栏上面返回按钮事件
		 */
		activity.setTitleBarGoBackEvent(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final Fragment topFragment = AndroidTools.getTopFragment(activity);
				boolean result = false;
				if (topFragment != null) {
					result = ((BaseFragment)topFragment).doClickBackEvent();
				}else{
					result = doClickBackEvent();
				}
				if (result) {
					activity.backFragment();
				}
			}
		});
		return super.onCreateView(inflater, container, savedInstanceState);
		
	}

	protected boolean doClickBackEvent() {
		final Fragment topFragment = AndroidTools.getTopFragment(activity);
		if (topFragment != null) {

			LoggerUtils.d("111 doClickBackEvent 栈顶界面："+topFragment.getClass().getName());
			if (((BaseFragment)topFragment).onBackKeyDown()) {
				return false;
			}
			if(topFragment instanceof MainFragment) {
				/**
				 *  首页菜单返回特殊处理 
				 */
				List<Fragment> fragments = AndroidTools.getFragments(activity);
				int num = 0;
				for (int i = 0; i < fragments.size(); i++) {
					if (fragments.get(i) instanceof MainFragment) {
						num++;
					}
				}
				if (num > 1) {
					return true;
				} else {
					activity.setMainStyle();
					activity.commonDialog = MessageUtils.showCommonDialog(
							activity, 
							activity.getResources().getString(R.string.common_exit_app), 
							new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									// 确认
									App.getInstance().exit();
									dismissCommonDialog();
								}
							}, 
							new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									// 取消
									dismissCommonDialog();
								}
							});
				}
			} else if (topFragment instanceof BaseSettingFragment) {
				return checkSettingError(topFragment);
				
			}else {
				if(((BaseFragment)topFragment).getBean()!=null) {
					/** fragment为交易界面 */
					LoggerUtils.w("getBean()!=null， bean = "+((BaseFragment)topFragment).getBean().getClass().getName());
					activity.commonDialog = MessageUtils.showCommonDialog(
							activity, 
							activity.getResources().getString(R.string.common_cancel_trans_tip_msg), 
							new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									// 确认
									onTransCancel();
									onBack();
									dismissCommonDialog();
								}
							}, 
							new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									// 取消
									dismissCommonDialog();
								}
							});
					return false;
				} else {
					LoggerUtils.w("getBean()==null");
		       		/** fragment为非交易界面时情况 */
					return true;
				}
			}
		}
		return false;
	}
	
	// 处理Home事件响应
	protected boolean doClickHomeEvent(){
		Fragment topFragment = AndroidTools.getTopFragment(activity);
		if(((BaseFragment)topFragment).getBean()!=null) {
			// 交易界面
			if(topFragment instanceof BillFragment ){
				return false;
			} else if (topFragment instanceof InputPinFragment ||
					topFragment instanceof InputPinOfflineFragment){
				if (App.SCREEN_TYPE != Const.ScreenType.IM_81) {
					return false;
				} 
			}
			/** fragment为交易界面 */
			LoggerUtils.w("getBean()!=null， bean = "+getBean().getClass().getName());
			activity.commonDialog = MessageUtils.showCommonDialog(
					activity, 
					activity.getResources().getString(R.string.common_cancel_trans_tip_msg), 
					new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// 确认
							onTransCancel();
							onBack();
							dismissCommonDialog();
						}
					}, 
					new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// 取消
							dismissCommonDialog();
						}
					});
			return false;
		}  else {
			boolean result = false;
			if (topFragment instanceof BaseSettingFragment) {
				result = checkSettingError(topFragment);
				
				if (!result) {
					return false;
				}
			}
			
			LoggerUtils.w("getBean()==null");
			if(topFragment instanceof MainFragment ||
					topFragment instanceof LoginFragment ) {
				activity.commonDialog = MessageUtils.showCommonDialog(
						activity, 
						activity.getResources().getString(R.string.common_exit_app), 
						new View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// 确认
								App.getInstance().exit();
								dismissCommonDialog();
							}
						}, 
						new View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// 取消
								dismissCommonDialog();
							}
						});
			} else if (topFragment instanceof BaseSettingFragment){
				activity.commonDialog = MessageUtils.showCommonDialog(
						activity, 
						activity.getResources().getString(R.string.exit_setting), 
						new View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// 确认
								activity.returnLogin(false);
							}
						}, 
						new View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
							}
						});
			} else if (topFragment instanceof LockFragment ||
					topFragment instanceof SelfCheckFragment){
				// 非交易界面，屏蔽HOME键不做处理
				return false;
			} else {
				activity.returnFirstMainMenu();
			}
			
		}
		return false;
	}
	
	private void dismissCommonDialog(){
		activity.dismissDialog();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName()+"-> onActivityCreated");
		}
		super.onActivityCreated(savedInstanceState);
	}
	/**
	 * 初始化数据
	 */
	protected abstract void initData();
    /**
     * 初始化点击事件
     * @return 返回"是否执行中" true为执行中,false为未执行中
     */
    protected abstract void initClickEvent(View view);
    
	/**
	 * 初始化事件
	 */
	protected abstract void initEvent();

    
    /**
     * 获取Bean对象
     * @return
     */
    protected abstract BaseBean getBean();
    
    public boolean isHasBean(){
    	return this.getBean()!=null;
    }
    
	/**
	 * 立即
	 */
	protected void rushLoad(){
		
		initData();
		initEvent();
	}

	/**
	 * 拦截返回事件，返回true-拦截
	 * @return
	 */
	protected boolean onBackKeyDown(){
		return false;
	}

	/**
	 * 是否取消交易点击确认
	 */
	protected void onTransCancel(){};
	
	/**
	 * 
	 * @Title: findViewById
	 * @param @param id
	 * @param @return    设定文件
	 * @return View    返回类型
	 * @throws
	 */
	public View findViewById (int id){
		return mFragmentView.findViewById(id);

	}
	
	/**
	 * Fragment从栈中销毁的时候回调
	 */
	@Override
	public void onDestroyView() {
		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName()+"-> onDestroyView");
		}
		super.onDestroyView();
	}
	
	private void stepGoOn() {
		if (getBean() != null) {
			LoggerUtils.d("stepGoOn :" + getBean().getClass());
			synchronized (getBean().getWaitObj()) {
				getBean().getWaitObj().notify();
			}
		}
	}
	
	/**
	 * 业务处理结束：成功
	 */
	public void onSucess(){
		LoggerUtils.d("1111BaseFragment 0");
		if (getBean() != null) {
			LoggerUtils.d("1111BaseFragment 1");
			getBean().setStepResult(StepResult.SUCCESS);
			LoggerUtils.d("1111BaseFragment 2");
			stepGoOn();
			LoggerUtils.d("1111BaseFragment 3");
		}
		LoggerUtils.d("1111BaseFragment 4");
	}
	
	/**
	 * 业务处理结束:失败
	 */
	public void onFail(){
		if (getBean() != null) {
			getBean().setStepResult(StepResult.FAIL);
			stepGoOn();
		}
	}
	
	/**
	 * 业务处理结束:返回
	 */
	public void onBack(){
		LoggerUtils.d("2222 BaseFragment 0");
		if (getBean() != null) {
			LoggerUtils.d("2222 BaseFragment 1");
			getBean().setStepResult(StepResult.BACK);
			LoggerUtils.d("2222 BaseFragment 2");
			stepGoOn();
			LoggerUtils.d("2222 BaseFragment 3");
		}
		LoggerUtils.d("2222 BaseFragment 4");
	}
	
	/**
	 * 业务处理结束：超时
	 */
	public void onTimeOut(){
		if (getBean() != null) {
			getBean().setStepResult(StepResult.TIME_OUT);
			stepGoOn();
		}
	}
	
	/**
	 * 记录fragment是否处于展示状态
	 * 由应用维护该专题
	 */
	private boolean isShowing;
	
	public boolean isShowing(){
		return isShowing;
	}
	
	/**
	 * fragment被覆盖时调用
	 */
	public void onFragmentHide(){
		LoggerUtils.v(getClass().getSimpleName()+"-> onFragmentHide");
		isShowing = false;
		activity.closeTimeOut();
		activity.dismissDialog();
	}
	/**
	 * fragment展示在界面上时调用
	 */
	public void onFragmentShow(){
		LoggerUtils.v(getClass().getSimpleName()+"-> onFragmentShow");

		if (title != null) {
			activity.showTitle();
			activity.setTitle(title);
	
			if (getString(R.string.common_main).equals(title)) {
				activity.setMainStyle();
			} else if ( getString(R.string.common_lock_terminal).equals(title)
					|| getString(R.string.common_operator_manage).equals(title)
					|| getString(R.string.sliding_menu_system_settings).equals(title)) {
				activity.setLoginStyle();
			} else {
				activity.setExceptMainStyle();
			}
		} else {
			if (this instanceof LoginFragment ||
//					this instanceof WaitingFragment ||
					this instanceof SelfCheckFragment) {
				// 全屏情况	
				activity.hideTitle();
			}
				
		}
		isShowing = true;
		if(this instanceof MainFragment){
			//主界面会有一个实例一值存在,导致那个界面超时时间不会刷新
//			outTime = App.FRAGMENT_TIME;
			outTime = 0L;
		}
		if (outTime > 0) {
			activity.showTimeOut();
			activity.startTimeOut(outTime);
		} else {
			activity.hideTimeOut();
			activity.closeTimeOut();
		}
		
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
	}
	

	@Override
	public void onAttach(Activity activity) {
		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName()+"-> onAttach");
		}
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName()+"-> onDestroy");
		}
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName()+"-> onDetach");
		}
		super.onDetach();
	}

	@Override
	public void onPause() {
		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName()+"-> onPause");
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName()+"-> onResume");
		}
		super.onResume();
	}

	@Override
	public void onStart() {
		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName()+"-> onStart");
		}
		super.onStart();
	}

	@Override
	public void onStop() {
		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName()+"-> onStop");
		}
		super.onStop();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		if (isShowStatus) {
			LoggerUtils.v(getClass().getSimpleName()+"-> onViewCreated");
		}
		super.onViewCreated(view, savedInstanceState);
	}

	public void setTitle(int resTitle) {
		title = getString(resTitle);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	private boolean checkSettingError(Fragment topFragment) {
		BaseSettingFragment settingFragment = (BaseSettingFragment)topFragment;
		for (SettingEditView settingEditView : settingFragment.settingEditViews) {
			LoggerUtils.i(settingEditView.getTitle() + " check:" + settingEditView.isCheck());
			if (!settingEditView.isCheck()) {
				if (settingEditView.disposeCheck() == false) {
					return false;
				}
			} 
			
			if (settingEditView.isShwoSystemKeyboard()) {
				LoggerUtils.i("isShwoSystemKeyboard:"+settingEditView.isShwoSystemKeyboard());
				settingEditView.hideSystemKeyboard();
			}
		}
		return true;
	}
}
