package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.base.CommonThread;
import com.newland.base.util.CommunicationUtils;
import com.newland.base.util.FileUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.SoundUtils;
import com.newland.base.util.ToastUtils;
import com.newland.emv.EmvAppModule;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.tools.AnswerCodeHelper;
import com.newland.payment.common.tools.BankCodeHelper;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.service.impl.UserServiceImpl;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.view.CheckView;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.device.PrintModule;
import com.newland.pos.sdk.device.PrintModule.PRNSTATUS;
import com.newland.pos.sdk.emv.EmvModule;
import com.newland.pos.sdk.print.PrintUtils;
import com.newland.pos.sdk.util.LoggerUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化自检界面
 * 
 * @author CB
 * @date 2015-5-14
 * @time 上午11:26:15
 */
public class SelfCheckFragment  extends BaseFragment {

//	@ViewInject(R.id.cv_communication_port)
	private CheckView cvCommunicationPort;
//	@ViewInject(R.id.cv_ic_card)
	private CheckView cvIcCard;
//	@ViewInject(R.id.cv_keyboard)
	private CheckView cvKeyboard;
//	@ViewInject(R.id.cv_print)
	private CheckView cvPrint;

	private List<CheckView> checkViews;
	private CheckView checkView;
	//private int checkIndex;
	private InnerObject checkObject;
	private CommonThread checkThread;
	
	private List<Integer> initInfos;

//	DeviceController deviceController = DeviceController.getInstance();fangjt
	/**
	 * 检测结果： 0-异常 1-正常 2-缺纸
	 */
	private int checkResult;

	public SelfCheckFragment() {
		super(0);
	}
	
	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.launch_activity, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	private void initView() {
		cvCommunicationPort = (CheckView)mFragmentView.findViewById(R.id.cv_communication_port);
		cvIcCard = (CheckView)mFragmentView.findViewById(R.id.cv_ic_card);
		cvKeyboard = (CheckView)mFragmentView.findViewById(R.id.cv_keyboard);
		cvPrint = (CheckView)mFragmentView.findViewById(R.id.cv_print);
		mFragmentView.findViewById(R.id.txt_retry).setOnClickListener(clickListener);
		mFragmentView.findViewById(R.id.txt_skip).setOnClickListener(clickListener);
	}
	
	@Override
	protected void initData() {
		setTitle(null);
		initView();
		
		//设置超时时间
		App.FRAGMENT_TIME = ParamsUtils.getLong(ParamsConst.PARAMS_KEY_CONFIG_FRAGMENT_TIMEOUT, 60);

		initInfos = new ArrayList<Integer>();

		cvPrint.setContent(R.string.test_print);
		cvCommunicationPort.setContent(R.string.test_communication_port);
		cvKeyboard.setContent(R.string.test_keyboard);
		cvIcCard.setContent(R.string.test_ic_card);

		initInfos.add(R.string.test_print);
		initInfos.add(R.string.test_communication_port);
		initInfos.add(R.string.test_keyboard);
		initInfos.add(R.string.test_ic_card);

		checkViews = new ArrayList<CheckView>();
		checkViews.add(cvPrint);
		checkViews.add(cvCommunicationPort);
		checkViews.add(cvKeyboard);
		checkViews.add(cvIcCard);

		//checkIndex = 0;
		checkObject = new InnerObject();
		
		checkInit();

	}
	
	private class InnerObject{
		private int checkIndex = 0;
		private boolean isRetry = false;
	}

//	@OnClick({R.id.txt_retry, R.id.txt_skip})
	@Override
	protected void initClickEvent(View view) {
		int i1 = view.getId();
		if (i1 == R.id.txt_retry) {//执行自检时需先销毁之前的线程，否则，连续重复的两次自检导致线程重复，应用崩溃。
			finishInit(false);
			synchronized (checkObject) {
				checkObject.isRetry = true;
				checkObject.checkIndex = 0;
				for (int i = 0; i < checkViews.size(); i++) {
					checkViews.get(i).setNormalStyle();
					checkViews.get(i).setContent(initInfos.get(i));
				}
			}

			checkInit();

		} else if (i1 == R.id.txt_skip) {
			finishInit(true);

		}
	}

	
	@Override
	protected void initEvent() {
		
	}

	private void checkInit() {
		
		if (checkThread == null) {
			
			checkThread = new CommonThread(false, 100, true,
					new ThreadCallBack() {

						@Override
						public void onMain() {
							synchronized (checkObject) {
								if (checkObject.isRetry) {
									checkObject.isRetry = false;
									return;
								}
								checkView = checkViews.get(checkObject.checkIndex);
								if (checkObject.checkIndex == 0) {
									switch (checkResult) {
									case 0:
										checkView.setErrorStyle();
										break;
									case 1:
										checkView.setSuccessStyle();
										break;
									case 2:
										checkView.setErrorStyleWithMsg(getResources()
														.getString(R.string.printer_status_outof_paper));
										break;
									}
								} else {
									checkView.setSuccessStyle();
								}
								if (checkObject.checkIndex < checkViews.size() - 1) {
									checkObject.checkIndex++;
								} else {
									// 这里面不能耗时,对象现在是一直锁着的
									finishInit(false);
								}
							}
						}

						@Override
						public void onBackGround() {
							synchronized (checkObject) {
								if (checkObject.isRetry) {
									checkObject.isRetry = false;
								}
							}
							LoggerUtils.i("check index:"+ checkObject.checkIndex);

							try {
								switch (checkObject.checkIndex) {
								case 0:
									/** 打印机检测 */
									checkResult = checkPrintStatus();
									break;

								case 1:
									// if
									// (ParamsUtils.getBoolean(ParamsConst.EMV_INIT_IS_FIRST,
									// true)) {
									boolean result = ParamsUtils.getBoolean(
											ParamsConst.PARAMS_KEY_IS_FIRST,
											true);
									if (result) {
										try {
											// EMV初始化
//											deviceController.connect();fangjt
											EmvModule emvModule = EmvAppModule
													.getInstance();
											emvModule.initEmvParams();
											
											ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_AID_DOWN, true);
											ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CAPK_DOWN, true);
											ParamsUtils.setInt(ParamsTrans.PARAMS_EMV_TRANS_SERIAL, 1);
											// ParamsUtils.setBoolean(ParamsConst.EMV_INIT_IS_FIRST,
											// false);
											// 参数初始化
											writeExtendData(context);
											initExtendParamsToSharedPreferences(context);
											ParamsUtils.setBoolean(ParamsConst.PARAMS_KEY_IS_FIRST,false);

											// 读取图片
											loadLogoImg(context);
											
										} catch (Exception e) {
											e.printStackTrace();
											checkResult = 0;
											break;
										}

									}
									// 是否打开日志
									LoggerUtils.configPrint(ParamsUtils
											.getBoolean(
													ParamsConst.PARAMS_KEY_CONFIG_IS_DEBUG,
													false));

									// 读取应答码信息
									AnswerCodeHelper.getAnswerCodeDate(context);

									// 读取银行码信息
									BankCodeHelper.getBankCodeDate(context);

									// 读取打印模板
									PrintUtils.load(context, Const.FileConst.PRINT);
									
									//加载声音资源
									SoundUtils.getInstance().load(context);
									
									checkResult = 1;
									break;
								default:
									/** 不做处理 */
									break;
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		}
		
		checkThread.start();
		
	}

	/**
	 * 检查打印机是否缺纸
	 * 
	 * @return
	 */
	private int checkPrintStatus() {
		try {
//			deviceController.connect();fangjt
			PrintModule printModule = PrintModule.getInstance();
			PRNSTATUS status = printModule.getPrinterStatus();
			if (status == PRNSTATUS.PRN_STATUS_OK) {
				return 1;
			}
			if (status == PRNSTATUS.PRN_STATUS_NOPAPER) {
				return 2;
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private void writeExtendData(Context context) {

		// 第一次启动模拟拷贝assets目录资源到sd卡
		if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_FIRST)) {
			try {
				
				AssetManager am = context.getAssets();
				
				File file = FileUtils.createFile(Const.PathConst.APPS_DATA,
						Const.FileConst.PARAMS);
				InputStream is = am.open(Const.FileConst.PARAMS);
				if (is != null) {
					FileUtils.copyFileByInputStream(is, file);
					ParamsUtils.setBoolean(ParamsConst.PARAMS_KEY_IS_FIRST,true);
				}
				
				file = FileUtils.createFile(Const.PathConst.APPS_DATA,
						Const.FileConst.BINA);
				is = am.open(Const.FileConst.BINA);
				if (is != null) {
					FileUtils.copyFileByInputStream(is, file);
				}
				
				file = FileUtils.createFile(Const.PathConst.APPS_DATA,
						Const.FileConst.BINB);
				is = am.open(Const.FileConst.BINB);
				if (is != null) {
					FileUtils.copyFileByInputStream(is, file);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 装载asset下三个LOGO图片资源
	 */
	private void loadLogoImg(Context context) {
		File fileLogoSmall = FileUtils.createFile(Const.PathConst.APPS_DATA,
				Const.FileConst.LOGO_IMG_SMALL);
		File fileLogoNormal = FileUtils.createFile(Const.PathConst.APPS_DATA,
				Const.FileConst.LOGO_IMG_NORMAL);
		File fileLogoLarge = FileUtils.createFile(Const.PathConst.APPS_DATA,
				Const.FileConst.LOGO_IMG_LARGE);
		try {
			AssetManager am = context.getAssets();
			InputStream isLogoSmall = am.open(Const.FileConst.LOGO_IMG_SMALL);
			if (isLogoSmall != null) {
				FileUtils.copyFileByInputStream(isLogoSmall, fileLogoSmall);				
			}	
			InputStream isLogoNormal = am.open(Const.FileConst.LOGO_IMG_NORMAL);
			if (isLogoNormal != null) {
				FileUtils.copyFileByInputStream(isLogoNormal, fileLogoNormal);				
			}
			InputStream isLogoLarge = am.open(Const.FileConst.LOGO_IMG_LARGE);
			if (isLogoLarge != null) {
				FileUtils.copyFileByInputStream(isLogoLarge, fileLogoLarge);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 初始化外部参数数据
	 */
	public static void initExtendParamsToSharedPreferences(Context context) {
		String strTitle = null;
		String tempString = null;
		Map<String, String> map = new HashMap<String, String>();

		try {
			LoggerUtils.d(Const.PathConst.SDCARD_PATH + Const.FileConst.PARAMS);
			Reader reader = new FileReader(Const.PathConst.SDCARD_PATH
					+ Const.FileConst.PARAMS);

			BufferedReader bufferedReader = new BufferedReader(reader);
			String keyString = "";
			String valueString = "";
			while ((tempString = bufferedReader.readLine()) != null) {
				if (tempString.charAt(0) == '[') {
					strTitle = tempString.substring(1, tempString.indexOf(']'));
					strTitle = strTitle.equals("") ? "" : strTitle + "_";
				} else if (tempString != null && tempString.length() > 0
						&& tempString.charAt(0) != '#') {

					keyString = tempString
							.substring(0, tempString.indexOf('=')).trim();
					valueString = tempString.substring(
							tempString.indexOf('=') + 1).trim();
					map.put(strTitle + keyString, valueString);
					LoggerUtils.i("key:" + strTitle + keyString + ",value:"
							+ valueString);
				}
			}

			ParamsUtils.save(map);
			bufferedReader.close();
			
			/** 同步商户号、终端号、主密钥索引 */
			String terminalId = ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_POSID);
			String merchantId = ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_MERCHANTID);
			String merchantName = ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_MERCHANTNAME);
			ParamsUtils.setShopId(merchantId);
			ParamsUtils.setPosId(terminalId);
			ParamsUtils.setTMKIndex(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_MAIN_KEY_INDEX));
			EmvModule emvModule = EmvAppModule.getInstance();
			if (!emvModule.emvSetTerminalId(terminalId)){
				ToastUtils.showOnUIThread("EMV设置终端号失败");
			}
			if (!emvModule.emvSetMerchantId(merchantId)){
				ToastUtils.showOnUIThread("EMV设置商户号失败");
			}
			if (!emvModule.emvSetMerchantName(merchantName)){
				ToastUtils.showOnUIThread("EMV设置商户名称失败");
			}
			
			//同步电子现金开关到AID参数
			emvModule.emvSetSupportEc(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_TRANS_EC, true));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 检测是否完成初始化，完成则进入主界面
	 */
	private void finishInit(final boolean isSkip) {
		LoggerUtils.i("finishInit");
		if (checkThread != null) {
			checkThread.stop();
			checkThread = null;
		}
		
		new CommonThread(new ThreadCallBack() {
			
			@Override
			public void onMain() {
				LoggerUtils.i("onMain");
				if (!isSkip) {

					for (CheckView checkView : checkViews) {
						if (!checkView.isSuccess()) {
							return;
						}
					}
				}
				// 启动下一个界面
				try {
					activity.switchContent(new LoginFragment());
					activity.getSupportFragmentManager().
						beginTransaction().
						remove(SelfCheckFragment.this).
						commitAllowingStateLoss();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
			
			@Override
			public void onBackGround() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}

	@Override
	protected boolean onBackKeyDown() {
		App.getInstance().exit();
		return true;
	}

	@Override
	public BaseBean getBean() {
		return null;
	}
	
	/**
	 * 第三方调用收单初始化
	 */

	public void thirdInvokeCheckInit(Context context) {

		App.mainMenuData.checkAll();
		new UserServiceImpl(context);
		LoggerUtils.d("第三方调用收单初始化!!!");
		try {

			boolean result = ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_FIRST, true);
			if (result) {
				try {
					LoggerUtils.d("第三方调用收单初始化 第一次开启");
					// EMV初始化
					EmvModule emvModule = EmvAppModule
							.getInstance();
					emvModule.initEmvParams();
					
					ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_AID_DOWN, true);
					ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CAPK_DOWN, true);
					ParamsUtils.setInt(ParamsTrans.PARAMS_EMV_TRANS_SERIAL, 1);
					// ParamsUtils.setBoolean(ParamsConst.EMV_INIT_IS_FIRST,
					// false);
					// 参数初始化
					writeExtendData(context);
					initExtendParamsToSharedPreferences(context);
					ParamsUtils.setBoolean(ParamsConst.PARAMS_KEY_IS_FIRST,false);

					// 读取图片
					loadLogoImg(context);					
					
				} catch (Exception e) {
					e.printStackTrace();
					
					
				}

			}
			// 是否打开日志
			LoggerUtils.configPrint(ParamsUtils
					.getBoolean(
							ParamsConst.PARAMS_KEY_CONFIG_IS_DEBUG,
							false));

			// 读取应答码信息
			AnswerCodeHelper.getAnswerCodeDate(context);

			// 读取银行码信息
			BankCodeHelper.getBankCodeDate(context);

			// 读取打印模板
			PrintUtils.load(context, Const.FileConst.PRINT);
			
			//加载声音资源
			SoundUtils.getInstance().load(context);	
			
			//初始化通讯开关
			CommunicationUtils.commSwitchInit(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE), context);
			
			checkResult = 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			initClickEvent(v);
		}
	};
}
