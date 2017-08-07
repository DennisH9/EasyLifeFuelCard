package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.base.util.MessageUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.pos.sdk.bean.PasswordBean;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 输入密码界面
 * 
 * @author CB
 * @time 2015-4-20 下午4:14:26
 */
@SuppressLint({ "ValidFragment", "InflateParams" })
public class InputPinOfflineFragment extends BaseFragment {

	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	private KeyboardNumber keyBoardNumber;
	/** 金额 */
//	@ViewInject(R.id.tv_input_pin_amount)
	private TextView tvAmount;
	/** 金额图标 */
//	@ViewInject(R.id.iv_amount)
	private ImageView ivAmount;
	/** 卡号 */
//	@ViewInject(R.id.tv_input_pin_card)
	private TextView tvCard;
	/** 卡号图标 */
//	@ViewInject(R.id.iv_card)
	private ImageView ivCard;

	/** 界面提示内容 */
//	@ViewInject(R.id.input_tip_msg)
	private TextView input_tip_msg;
	/** 密码 */
//	@ViewInject(R.id.txt_password)
	private TextView txtPassword;
	
	private PasswordBean bean;
	
//	private DeviceController deviceController;fangjt
	/**
	 * 组合亮灯
	 */
/*	private LightType[] lisLightTypes = new LightType[]{LightType.BLUE_LIGHT,
			LightType.GREEN_LIGHT, LightType.YELLOW_LIGHT};*/
//	private IndicatorLight light;
	
//	private KeyBoard keyBoard;
//	private Handler handler;
	private StringBuffer buffer = new StringBuffer();
	private final String star = " * ";
	
	private InputPinOfflineFragment() {
		super(0);
	}
	
	public static InputPinOfflineFragment newInstance(PasswordBean passwordBean) {
		InputPinOfflineFragment fragment = new InputPinOfflineFragment();
		fragment.bean = passwordBean;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.input_pin_offline_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		
//		deviceController = DeviceController.getInstance();fangjt

		if (getBean().getTitle() != null) {
			setTitle(getBean().getTitle());
		}
		if (!StringUtils.isEmpty(bean.getContent())) {
			input_tip_msg.setText(bean.getContent());
		}
		
		if (App.SCREEN_TYPE == Const.ScreenType.IM_81) {
			//initIM81date();fangjt
		} else {
			initIM91date();
		}
	}
	
//	private void initIM81date(){
//		
//		if (getBean().getAmountContent() != 0) {
//			tvAmount.setText("金额    "+FormatUtils.formatAmount(String.valueOf(getBean().getAmountContent())));
//		} else {
//			tvAmount.setVisibility(View.GONE);
//			ivAmount.setVisibility(View.GONE);
//		}
//		if (getBean().getPan() != null) {
//			tvCard.setText("卡号    "+FormatUtils.formatCardNoWithStar(String.valueOf(getBean().getPan())));
//		} else { 
//			tvCard.setVisibility(View.GONE);
//			ivCard.setVisibility(View.GONE);
//		}
//		handler = getIm81PinInputHandler();
//		
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					
//					keyBoard = (KeyBoard) deviceController.getDevice().getStandardModule(ModuleType.COMMON_KEYBOARD);
//					keyBoard.readString(DispType.NORMAL, 
//							null, 
//							null, 
//							bean.getPinMinLen(), 
//							bean.getPinMaxLen(), 
//							bean.getTimeOut(), 
//							TimeUnit.SECONDS,  new DeviceEventListener<KeyBoardReadingEvent<String>>() {
//
//								@Override
//								public Handler getUIHandler() {
//									return null;
//								}
//
//								@Override
//								public void onEvent(KeyBoardReadingEvent<String> event, Handler arg1) {
//									if(event instanceof KeyBoardReadingEvent&&((KeyBoardReadingEvent<String>) event).isProcessing()){
//										String input = event.getSingleKeyWord();
//										LoggerUtils.i("输入："+input);
//										if ("\n".equals(input)) {
//											handler.sendEmptyMessage(MSG.CIPHER_BACKSPACE);
//										} else {
//											handler.sendEmptyMessage(MSG.CIPHER_ENTER);
//										}
//										return; 
//									}
//									if(event instanceof KeyBoardReadingEvent&&((KeyBoardReadingEvent<String>) event).isSuccess()){
//										LoggerUtils.i("按键输入为："+event.getWholeKeyWord());
//										handler.sendEmptyMessage(MSG.CIPHER_SUCCESS);
//										return;
//									}
//									if(event instanceof KeyBoardReadingEvent&&((KeyBoardReadingEvent<String>) event).isUserCanceled()){
//										LoggerUtils.i("按键输入为：Cancel");
//										handler.sendEmptyMessage(MSG.CIPHER_CANCEL);
//										return;
//									}
//								}
//							});
//					
//				} catch (Exception ex) {
//					LoggerUtils.e("输入失败!");
//					ex.printStackTrace();
//				}
//			}
//		}).start();
//		
//		
//	}
	
	
	private void initIM91date(){
//		light = (IndicatorLight)deviceController.getStandardModule(ModuleType.COMMON_INDICATOR_LIGHT);
//		light.turnOnLight(lisLightTypes);
		
		keyBoardNumber.setRandomNumber();
		keyBoardNumber.setEnterNotGone();
		if (bean.getAmountContent() != 0) {
			tvAmount.setText("金额    " + FormatUtils.formatAmount(String.valueOf(bean.getAmountContent())));
		} else {
			tvAmount.setVisibility(View.GONE);
//			ivAmount.setVisibility(View.GONE);
		}
		
		
		AbstractKeyBoardListener listener = new AbstractKeyBoardListener(){
			@Override
			public void onChangeText(String text) {
				super.onChangeText(text);
				txtPassword.setText(" *  *  *  *  *  *  *  *  *  *  *  * ".substring(0, text.length()*3));
				LoggerUtils.i("text:"+text);
			}
			@Override
			public void onEnter() {
				int pinLen = getValue().length();
				if (pinLen ==0 || 
						(pinLen>=bean.getPinMinLen() && pinLen<=bean.getPinMaxLen())){
					super.onEnter();
//					light.turnOffLight(lisLightTypes);
					LoggerUtils.i("onEnter pin 成功:"+getValue());
					bean.setPin(getValue());
					onSucess();
				}
			}
			/**
			 * 重写取消事件
			 */
			@Override
			public void onCancel() {
//				light.turnOffLight(lisLightTypes);
				onBack();
			}
		};
		
		listener.setMaxLength(bean.getPinMaxLen());
		keyBoardNumber.setKeyBoardListener(listener);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler getIm81PinInputHandler(){
		return new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG.CIPHER_BACKSPACE:
					LoggerUtils.d("CIPHER_BACKSPACE...");
					if (buffer.length() > 0) {
						buffer.delete(buffer.length() - star.length(), buffer.length());
					}
					activity.resetProgress();
					txtPassword.setText(buffer.toString());
					break;
					 
				case MSG.CIPHER_ENTER:
					LoggerUtils.d("CIPHER_BACKSPACE...");
					buffer.append(star);
					activity.resetProgress();
					txtPassword.setText(buffer.toString());
					break;
					
				case MSG.CIPHER_SUCCESS:
					LoggerUtils.d("CIPHER_SUCCESS 输PIN成功...:"+(String)msg.obj);
					bean.setPin((String)msg.obj);
					onSucess();
					break;
					
				case MSG.CIPHER_CANCEL:
					LoggerUtils.d("CIPHER_CANCEL 输PIN取消...");
					onBack();
					break;
					
				case MSG.CIPHER_TIMEOUT:
					LoggerUtils.d("CIPHER_TIMEOUT 输PIN超时...");
					ToastUtils.show(context, msg.obj);
					onTimeOut();
					break;
					
				case MSG.CIPHER_EXCEPTION:
					// 输PIN异常，取消交易
					LoggerUtils.d("CIPHER_EXCEPTION 输PIN异常...");
					onFail();
					break;
				default:
					break;
				}
			}
		};
	}
	
	private class MSG {
		private final static int CIPHER_BACKSPACE = 0x01;
		private final static int CIPHER_ENTER = 0x02;
		private final static int CIPHER_SUCCESS = 0x03;
		private final static int CIPHER_CANCEL = 0x04;
		private final static int CIPHER_TIMEOUT = 0x05;
		private final static int CIPHER_EXCEPTION = 0x06;
	}
	
	@Override
	protected boolean onBackKeyDown() {
		if (App.SCREEN_TYPE != Const.ScreenType.IM_81) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void onFragmentHide() {
		super.onFragmentHide();
		dismissTipDialog();
		if (App.SCREEN_TYPE != Const.ScreenType.IM_81) {
			activity.setExceptMainStyle();
		}
	}

	@Override
	public void onFragmentShow() {
		super.onFragmentShow();
		if (App.SCREEN_TYPE != Const.ScreenType.IM_81) {
			activity.setLoginStyle();
		}
	}
	

	@Override
	protected PasswordBean getBean() {
		return bean;
	}
	
	@Override
	protected void initClickEvent(View view) {
	}
	
	@Override
	protected void initEvent() {	
	}
	
	private void cancelPinInput(){
		new Thread(){
			public void run() {
				LoggerUtils.d("cancel pin input");
//				DeviceController.getInstance().reset();fangjt
				LoggerUtils.d("cancel pin input end");
			}
		}.start();
	}
	
	private Dialog tipDialog = null;
	private void dismissTipDialog(){
		if(tipDialog != null){
			tipDialog.dismiss();
			tipDialog = null;
		}
	}
	
	@Override
	protected boolean doClickBackEvent() {
		tipDialog = MessageUtils.showCommonDialog(
				activity, 
				activity.getResources().getString(R.string.common_cancel_trans_tip_msg), 
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// 确认
						cancelPinInput();
						dismissTipDialog();
					}
				}, 
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// 取消
						dismissTipDialog();
					}
				});
		return false;
	}
	
	@Override
	protected boolean doClickHomeEvent() {
		return doClickBackEvent();
	}

}
