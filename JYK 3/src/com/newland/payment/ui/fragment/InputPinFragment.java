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
import android.widget.TableLayout;
import android.widget.TextView;

import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.trans.bean.TransStepBeanException;
import com.newland.payment.ui.activity.App;
import com.newland.pos.sdk.bean.PasswordBean;
import com.newland.pos.sdk.device.Device;
import com.newland.pos.sdk.security.SecurityModule;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.PublicLibJNIService;
import com.newland.pos.sdk.util.StringUtils;


/**
 * 输入密码界面
 * 
 * @author CB
 * @time 2015-4-20 下午4:14:26
 */
@SuppressLint({ "ValidFragment", "InflateParams" })
public class InputPinFragment extends BaseFragment {

	private int nPinLen = 0;
	/** 密码键盘 */
//	@ViewInject(R.id.keyboard_layout)
	private TableLayout keyboard_layout;
	/** 键盘按键 */
//	@ViewInject(R.id.iv_0)
	private ImageView iv0;
	/** 键盘按键 */
//	@ViewInject(R.id.iv_1)
	private ImageView iv1;
	/** 键盘按键 */
//	@ViewInject(R.id.iv_2)
	private ImageView iv2;
	/** 键盘按键 */
//	@ViewInject(R.id.iv_3)
	private ImageView iv3;
	/** 键盘按键 */
//	@ViewInject(R.id.iv_4)
	private ImageView iv4;
	/** 键盘按键 */
//	@ViewInject(R.id.iv_5)
	private ImageView iv5;
	/** 键盘按键 */
//	@ViewInject(R.id.iv_6)
	private ImageView iv6;
	/** 键盘按键 */
//	@ViewInject(R.id.iv_7)
	private ImageView iv7;
	/** 键盘按键 */
//	@ViewInject(R.id.iv_8)
	private ImageView iv8;
	/** 键盘按键 */
//	@ViewInject(R.id.iv_9)
	private ImageView iv9;
	
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
	
	private PasswordBean bean;
	
	TextView tvTitle;
	/** 界面提示内容 */
//	@ViewInject(R.id.input_tip_msg)
	private TextView input_tip_msg;
	/** 密码 */
//	@ViewInject(R.id.txt_password)
	private TextView txtPassword;
	
	private Handler handler;
	private StringBuffer buffer = new StringBuffer();
	
	/** 图片资源 */
	public final int[] resImages = { R.drawable.keyboard_0,
			R.drawable.keyboard_1, R.drawable.keyboard_2, R.drawable.keyboard_3,
			R.drawable.keyboard_4, R.drawable.keyboard_5, R.drawable.keyboard_6,
			R.drawable.keyboard_7, R.drawable.keyboard_8, R.drawable.keyboard_9 };

	/** 按键坐标值 */
	public static byte[] coordinate;
	/*NEW----功能键（键值+坐标）*/
	public static byte[] func_key;
	
	
	/** 随机按键坐标值 */
	public byte[] coordinateReadom;
	
	static {
		int x0 = 0, x1 = 135, x2 = 270, x3 = 405, x4 = 540, y0 = 385, y1 = 510, y2 = 637, y3 = 763, y4 = 888;
		/** 数字键*/
		int[] cooridnateInt = new int[] { 
				x0, y0, x1, y1, 
				x1, y0, x2, y1, 
				x2, y0, x3, y1, 
				//x3, y0, x4, y1,

				x0, y1, x1, y2, 
				x1, y1, x2, y2, 
				x2, y1, x3, y2, 
				//x3, y1, x4, y2,

				x0, y2, x1, y3, 
				x1, y2, x2, y3, 
				x2, y2, x3, y3,

				//x0, y3, x1, y4,
				x1, y3, x2, y4, 
				//x2, y3, x3, y4,

				//x3, y2, x4, y4, 
				};
		
		coordinate = new byte[cooridnateInt.length * 2];
		for (int i = 0, j = 0; i < cooridnateInt.length; i++, j++) {
			coordinate[j] = (byte) ((cooridnateInt[i]) & 0xff);
			j++;
			coordinate[j] = (byte) ((cooridnateInt[i] >> 8) & 0xff);
		}
		
		/** 功能键*/
		int[] func_keyInt = new int[] { 
				x3, y0, x4, y1,		//取消键
				x3, y1, x4, y2,		//退格键
				x3, y2, x4, y4, 	//确认键
				};
		
		func_key = new byte[12 + func_keyInt.length * 2];
		for (int i = 0, j = 0; i < func_keyInt.length; j++) {
			if ((j>=0)&&(j<=3) || (j>=12)&&(j<=15) || (j>=24)&&(j<=27) ) {
				if (j == 0) {
					func_key[j] = (byte)0x1b; 		//取消键
				} else if (j == 12) {
					func_key[j] = (byte)0x0a;		//退格键
				} else if (j == 24) {
					func_key[j] = (byte)0x0d;		//确认键
				} else {
					func_key[j] = (byte)0x00;
				}
			} else {
				func_key[j] = (byte) ((func_keyInt[i]) & 0xff);
				j++;
				func_key[j] = (byte) ((func_keyInt[i] >> 8) & 0xff);
				i++;
			}
		}	
	}
	/*NEW*/
//	static {
//		int x0 = 0, x1 = 135, x2 = 270, x3 = 405, x4 = 540, y0 = 385, y1 = 510, y2 = 637, y3 = 763, y4 = 888;
//		int[] func_keyInt = new int[] { 
//				x3, y0, x4, y1,		//取消键
//				x3, y1, x4, y2,		//退格键
//				x3, y2, x4, y4, 	//确认键
//				};
//		
//		func_key = new byte[12 + func_keyInt.length * 2];
//		for (int i = 0, j = 0; i < func_keyInt.length; j++) {
//			if ((j>=0)&&(j<=3) || (j>=12)&&(j<=15) || (j>=24)&&(j<=27) ) {
//				if (j == 0) {
//					func_key[j] = (byte)0x1b; 		//取消键
//				} else if (j == 12) {
//					func_key[j] = (byte)0x0a;		//退格键
//				} else if (j == 24) {
//					func_key[j] = (byte)0x0d;		//确认键
//				} else {
//					func_key[j] = (byte)0x00;
//				}
//			} else {
//				func_key[j] = (byte) ((func_keyInt[i]) & 0xff);
//				j++;
//				func_key[j] = (byte) ((func_keyInt[i] >> 8) & 0xff);
//				i++;
//			}
//		}	
//	}

	private InputPinFragment() {
		super(0);
	}

	public static InputPinFragment newInstance(PasswordBean passwordBean) {
		InputPinFragment fragment = new InputPinFragment();
		fragment.bean = passwordBean;
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LoggerUtils.d("input_pin_fragment.onCreate");
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.input_pin_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		
		
		
		return mFragmentView;
	}

	@Override
	protected void initData() {
//		deviceController = DeviceController.getInstance();fangjt
		
		handler = getPinInputHandler();
		if (getBean().getTitle() != null) {
			setTitle(getBean().getTitle());
		}
		if (!StringUtils.isEmpty(bean.getContent())) {
			input_tip_msg.setText(bean.getContent());
		}
		
		if (App.SCREEN_TYPE == Const.ScreenType.IM_81) {
			initIM81date();
		} else {
			initIM91date();
		}
		
		try {
			checkBean();
		} catch (TransStepBeanException e) {
			e.printStackTrace();
			return;
		} 
		
		startPinInput();
		

	}

	private void initIM91date() {
		try {
			if (getBean().getAmountContent() != 0) {
				tvAmount.setText("金额    "+FormatUtils.formatAmount(String.valueOf(getBean().getAmountContent())));
			} else {
				tvAmount.setVisibility(View.GONE);
			}
//			try {fangjt
//				deviceController.connect();
//			} catch (Exception e) {
//				e.printStackTrace();
//				//return;
//			}
	
			StringBuilder sb = null;
			
			/*NEW*/
			coordinateReadom = new byte[10];
			int nRet = PublicLibJNIService.jnivpptpinit(coordinate, func_key, coordinateReadom);
			LoggerUtils.d("jnivpptpinit_____nRet = " + nRet);
			if (nRet == 0) {
				LoggerUtils.d("jnivpptpinit_____SUCC");
			} else {
				LoggerUtils.d("jnivpptpinit_____FAIL");
			}

			sb = new StringBuilder();
			
			byte[] numserial=new byte[10];
			int d=0;
			for (int i = 0; i < coordinateReadom.length; i++) {
				/*if (i == 3 || i == 7 || i == 11 || i == 13 || i == 14) {
					//continue;
				}*/
				numserial[d]=(byte) (coordinateReadom[i]&0x0f);
				
				sb.append(numserial[d]);
				d++;
			}
			
			//处理图片
			
			ImageView[] buttons = new ImageView[]{
					iv1,iv2,iv3,iv4,iv5,iv6,iv7,iv8,iv9,iv0
			};
			
			LoggerUtils.i("sb.toString():"+sb.toString());
			
			for (int i = 0; i < buttons.length; i++) {
				buttons[i].setImageResource(resImages[Integer.valueOf(sb.toString().charAt(i)+"")]);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = handler.obtainMessage(MSG.CIPHER_EXCEPTION);
			msg.obj = App.getInstance().getApplicationContext().getResources().getString(R.string.cipher_exception)+e.getMessage();
			msg.sendToTarget();
		}
	}

	private void initIM81date(){
		if (App.SCREEN_TYPE == Const.ScreenType.IM_81) {
			
			if (getBean().getAmountContent() != 0) {
				tvAmount.setText("金额    "+FormatUtils.formatAmount(String.valueOf(getBean().getAmountContent())));
			} else {
				tvAmount.setVisibility(View.GONE);
				ivAmount.setVisibility(View.GONE);
			}
			if (getBean().getPan() != null) {
				tvCard.setText("卡号    "+FormatUtils.formatCardNoWithStar(String.valueOf(getBean().getPan())));
			} else { 
				tvCard.setVisibility(View.GONE);
				ivCard.setVisibility(View.GONE);
			}
		} 

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
	protected void initClickEvent(View view) {	
		
	}

	@Override
	protected void onTransCancel() {
//		if (App.SCREEN_TYPE != Const.ScreenType.IM_81) {
//			light.turnOffLight(lisLightTypes);
//		}
	}

	@Override
	protected void initEvent() { 
		
	}
	
	private final String star = " * ";
	
	@SuppressLint("HandlerLeak")
	private Handler getPinInputHandler(){
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
					LoggerUtils.d("CIPHER_ENTER...");
					buffer.append(star);
					activity.resetProgress();
					txtPassword.setText(buffer.toString());
					break;
					
				case MSG.CIPHER_SUCCESS:
					LoggerUtils.d("CIPHER_SUCCESS 输PIN成功...");
					if (App.SCREEN_TYPE != Const.ScreenType.IM_81) {
						// N900输PIN完成密码键盘异常
						keyboard_layout.setVisibility(View.INVISIBLE);
						//light.turnOffLight(lisLightTypes);
					}
					bean.setPin((String)msg.obj);
					onSucess();
					break;
					
				case MSG.CIPHER_CANCEL:
					LoggerUtils.d("CIPHER_CANCEL 输PIN取消...");
					ToastUtils.show(context, msg.obj);
					//	light.turnOffLight(lisLightTypes);
					//}
					onBack();
					break;
					
				case MSG.CIPHER_TIMEOUT:
					LoggerUtils.d("CIPHER_TIMEOUT 输PIN超时...");
					ToastUtils.show(context, msg.obj);
					onTimeOut();
					break;
					
				case MSG.CIPHER_EXCEPTION:
					// 输PIN异常，取消交易
					onFail();
					break;
				default:
					break;
				}
			}
		};
	}
	
	private final String devicesTimeOutCode = "7"; // 设备超时原因码
	
	/** 启动输密 */
	private void startPinInput(){
		
		/*设置主密钥索引*/
		SecurityModule securityModule = SecurityModule.getInstance();
		securityModule.setCurrentMainKeyIndex(ParamsUtils.getTMkIndex());
		
		
		int nMode = 0;
		byte[] pan = new byte[20];
		switch (bean.getPinInputMode()) {
		case PINTYPE_WITHOUT_PAN:	// 卡号不参与计算 
			nMode = 1;
			break;
		case PINTYPE_WITH_PAN:		// 卡号参与计算 
			nMode = 0;
			pan = bean.getPan().getBytes();
			break;
		default:
			return;
		}
		
		int nRet = PublicLibJNIService.jnigetpin(nMode, pan, pinMaxLen, pinMinLen);
		
		LoggerUtils.d("jnigetpin_____nRet = " + nRet);
		if (nRet != 0) {
			LoggerUtils.d("jnigetpin_____FAIL");
			return;
		}
		
		new Thread(){
			public void run(){
				try {
					int[] nStatus= new int[1];
					byte[] pinblock = new byte[8];
					nPinLen = 0;
					
					while(true) {
						int nRet = PublicLibJNIService.jnigetpinresult(pinblock, nStatus);
						LoggerUtils.d("jnigetpinresult_____nRet = " + nRet);

						if (nRet == 0) {
							if (nStatus[0] == PINSTATUS.SEC_VPP_KEY_PIN) {
								Device.Beep(1);
								if (nPinLen < pinMaxLen) {
									nPinLen++;
								}
								handler.post( new Runnable() {
									@Override
									public void run() {
										txtPassword.setText(StringUtils.fill("", star, nPinLen, false));
									}
								});
							}
							else if (nStatus[0] == PINSTATUS.SEC_VPP_KEY_BACKSPACE) {
								Device.Beep(1);
								if (nPinLen > 0) {
									nPinLen--;
								}
								handler.post( new Runnable() {
									@Override
									public void run() {
										txtPassword.setText(StringUtils.fill("", star, nPinLen, false));
									}
								});
							}
							else if (nStatus[0] == PINSTATUS.SEC_VPP_KEY_CLEAR) {
								Device.Beep(1);
								nPinLen = 0;
								handler.post( new Runnable() {
									@Override
									public void run() {
										txtPassword.setText("");
									}
								});
							}
							else if (nStatus[0] == PINSTATUS.SEC_VPP_KEY_ENTER) {
								Device.Beep(1);
								Message msg = handler.obtainMessage(MSG.CIPHER_SUCCESS);
								msg.obj = BytesUtils.bytesToHex(pinblock);
								LoggerUtils.d("pin密文 = "+BytesUtils.bytesToHex(pinblock));
								msg.sendToTarget();
								break;
							}
							else if (nStatus[0] == PINSTATUS.SEC_VPP_KEY_ESC) {
								Device.Beep(1);
								Message msg = handler.obtainMessage(MSG.CIPHER_CANCEL);
								msg.obj = App.getInstance().getApplicationContext().getResources().getString(R.string.cipher_cancel);
								msg.sendToTarget();
								break;
							}
							else if (nStatus[0] == PINSTATUS.SEC_VPP_KEY_NULL) {
								continue;
							}
						} else {
							Device.Beep(1);
							Message msg = handler.obtainMessage(MSG.CIPHER_CANCEL);
							msg.obj = App.getInstance().getApplicationContext().getResources().getString(R.string.cipher_cancel);
							msg.sendToTarget();
							break;
						}

						
					}
						

					
					
					/*
					SecurityModule securityModule = SecurityModule.getInstance();
					securityModule.setCurrentMainKeyIndex(ParamsUtils.getTMkIndex());
					K21Pininput pinInput = getPinInputModule();
					pinInput.startStandardPinInput(null, 
							new WorkingKey(securityModule.getPINIndex()),
							KeyManageType.MKSK,  
							accountInputType, 
							acctSymbol, 
							pinMaxLen, 
							getPinLengthRange(pinMaxLen), 
							new byte[]{'F','F','F','F','F','F','F','F','F','F'},
							PinConfirmType.ENABLE_ENTER_COMMANG, 
							(long)timeOut, 
							TimeUnit.SECONDS, 
							null, 
							null, 
							inputListener);
							*/
				} catch(Exception e) {
					String msg1 = e.getMessage().trim();
					LoggerUtils.e("输pin异常 ："+msg1);
					String code = msg1.substring(msg1.length()-1);
					if (devicesTimeOutCode.equals(code)) {
						Message msg = handler.obtainMessage(MSG.CIPHER_TIMEOUT);
						msg.obj = App.getInstance().getApplicationContext().getResources().getString(R.string.cipher_timeout);
						msg.sendToTarget();
					} else {
						Message msg = handler.obtainMessage(MSG.CIPHER_EXCEPTION);
						msg.obj = App.getInstance().getApplicationContext().getResources().getString(R.string.cipher_exception)+code;
						msg.sendToTarget();
					}
				}
			}
		}.start();
	}

	
	//private AccountInputType accountInputType = null;
	//private String acctSymbol = "";
	private int pinMaxLen;
	private int pinMinLen;
	//private int timeOut;
	private void checkBean() throws TransStepBeanException{
		if (bean==null) {
			throw new TransStepBeanException("输密bean对象为空，输密执行异常...");
		}
		
		switch (bean.getPinInputMode()) {
		/** 卡号不参与计算 */
		case PINTYPE_WITHOUT_PAN:
			
			//accountInputType = AccountInputType.UNUSE_ACCOUNT;
			//不带主帐户加密 = 带主帐户加密+pan全0
			//accountInputType = AccountInputType.USE_ACCOUNT;
			//acctSymbol = StringUtils.fill("000", "0", 20, false);
			//LoggerUtils.d("111 不带主账户加密acctSymbol:" + acctSymbol);
			break;
		
		/** 卡号参与计算 */
		case PINTYPE_WITH_PAN:
			
			String pan = bean.getPan();
			if (StringUtils.isEmpty(pan)) {
				throw new TransStepBeanException("输密模式PINTYPE_WITH_PAN下 PAN 不可为空，输密执行异常...");
			}
			//accountInputType = AccountInputType.USE_ACCOUNT;
			//acctSymbol = ISOUtils.padright(bean.getPan(), 20, 'F');
			
			break;
		default:
			throw new TransStepBeanException("输密模式"+bean.getPinInputMode()+"暂不支持，输密执行异常...");
		}
		
		if (bean.getPinMaxLen()>12 || bean.getPinMaxLen()<4) {
			throw new TransStepBeanException("设置输密长度超过合法范围4-12，输密执行异常...");
		} else {
			pinMaxLen = bean.getPinMaxLen();
			pinMinLen = bean.getPinMinLen();
		}
		
		//timeOut = bean.getTimeOut()==null?30:bean.getTimeOut();
		
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
	public PasswordBean getBean() {
		return bean;
	}
	
	private class MSG {
		private final static int CIPHER_BACKSPACE = 0x01;
		private final static int CIPHER_ENTER = 0x02;
		private final static int CIPHER_SUCCESS = 0x03;
		private final static int CIPHER_CANCEL = 0x04;
		private final static int CIPHER_TIMEOUT = 0x05;
		private final static int CIPHER_EXCEPTION = 0x06;
	}
	
	private class PINSTATUS {
		private final static int SEC_VPP_KEY_PIN = 0x00;
		private final static int SEC_VPP_KEY_BACKSPACE = 0x01;
		private final static int SEC_VPP_KEY_CLEAR = 0x02;
		private final static int SEC_VPP_KEY_ENTER = 0x03;
		private final static int SEC_VPP_KEY_ESC = 0x04;
		private final static int SEC_VPP_KEY_NULL = 0x05;
	}

	@Override
	public void onTimeOut() {
		super.onTimeOut();
	}


	private Dialog tipDialog = null;
	private void dismissTipDialog(){
		if(tipDialog != null){
			tipDialog.dismiss();
			tipDialog = null;
		}
	}
	
}
