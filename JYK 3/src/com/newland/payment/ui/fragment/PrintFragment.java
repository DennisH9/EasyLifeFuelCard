package com.newland.payment.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.base.util.MessageUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
//import com.newland.mtype.module.common.printer.PrinterResult;
//import com.newland.mtype.module.common.printer.PrinterStatus;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.Const.PrintStyleConst;
import com.newland.payment.common.Const.PrintStyleConstEnum;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.PrintType.PrintWaterType;
import com.newland.payment.common.TransConst.EmvStatus;
import com.newland.payment.common.TransConst.SendStatus;
import com.newland.payment.common.TransConst.TransStatus;
import com.newland.payment.common.TransType;
import com.newland.payment.common.tools.PrintModelUtils;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.dao.impl.WaterDaoImpl;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.printer.template.model.PrintAllWaterModel;
import com.newland.payment.printer.template.model.PrintInfoModel;
import com.newland.payment.printer.template.model.PrintSettleModel;
import com.newland.payment.printer.template.model.PrintSysParamModel;
import com.newland.payment.printer.template.model.PrintTotalModel;
import com.newland.payment.trans.bean.PrintBean;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.device.PrintModule;
import com.newland.pos.sdk.device.PrintModule.PRNSTATUS;
import com.newland.pos.sdk.print.PrintUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 打印界面
 * 
 * @author lst
 * @date 20150608
 */
public class PrintFragment extends BaseFragment {
	private Dialog progressCountDialog;
	private TextView tvTips;
	private View rootView;
	private Dialog dialog;
	private AlertDialog alertDialog;
	private PrintBean bean;
	private String tipContent;
	private PrintStyleConstEnum printType;
	private Water mWater;
	/** 签购单剩余打印张数*/
	private int printVoucherCount = 0;
	/** 进度条显示信息*/
	private String tipMessage = null;
/*	*//** 是否有后续打印，用于打印签购单时，结果返回*//*
	private boolean result;*/
	/** 判断是否需要显示打印中进度条*/
	private boolean isShow = true;
	/** 判断是否继续等待*/
	private boolean isWait = true;
	
	/** 获取打印下一联消息的handle*/
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(MainActivity.getInstance().getMainLooper()) {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x01:

				AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_Transparent));
				View view = LayoutInflater.from(MainActivity.getInstance()).inflate(R.layout.dialog_confirm, null);
				TextView title = (TextView) view.findViewById(R.id.tv_title);
				TextView content = (TextView) view.findViewById(R.id.tv_content);
				Button confrim = (Button) view.findViewById(R.id.btn_confirm);
				builder.setView(view);
				title.setText("提示信息");
				content.setText("点击\"确定\"继续打印下一联");
				alertDialog  = builder.create();
				alertDialog.setCanceledOnTouchOutside(false);
				alertDialog.setCancelable(false);
				confrim.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						new Thread(new Runnable() {

							@Override
							public void run() {
								isWait = false;
								alertDialog.dismiss();
								//打印第二联后的签购单
								printVoucherAfterFirst();
							}
						}).start();
					}
				});
				alertDialog.show();
				break;
			default:
				break;
			}
		}
	};
	
	public static PrintFragment newInstance(PrintBean bean) {
		PrintFragment fragment = new PrintFragment();
		fragment.bean = bean;
		return fragment;
	}

	public static PrintFragment newInstance(PrintStyleConstEnum type,
			String tip,
			Water water) {
		PrintFragment fragment = new PrintFragment();
		fragment.bean = null;
		fragment.tipContent = tip;
		fragment.printType = type;
		fragment.mWater = water;
		return fragment;
	}
	
	public PrintFragment() {
		super(0L);
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.empty_view, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
 
	@Override
	protected void initData() {
		printVoucherCount = 0;
		if (bean != null) {
			printType = bean.getPrintType();
			tipContent = bean.getPrintMessage();
			mWater = bean.getWater();
		}
		if (StringUtils.isEmpty(tipContent)) {
			tipContent = (String) activity.getText(R.string.bill_printing_defuat_tip);
		} 
		
		switch (printType) {
		// 打印结算单
		case PRINT_SETTLE:
			new Thread(){
				public void run(){
					String settleData = getPrintSettleData(context);
					if (settleData != null) {
						print(settleData);
					}
					else {
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								isShow = false;
								ToastUtils.show(context, R.string.no_settle);
								printEnd(true);				
							}
						});
					}
					
				}
			}.start();

			break;
		// 打印明细
		case PRINT_ALL_WATER:
			new Thread(){
				public void run(){
					printTransDetail(activity);
				}
			}.start();

			break;
		// 打印失败明细
		case PRINT_FAIL_WATER:
			new Thread(){
				public void run(){
					printFTransDetail(activity);
				}
			}.start();
			break;
		// 打印汇总
		case PRINT_TOTAL:
			new Thread(){
				public void run(){
					print(getPrintTotalData(context));
				}
			}.start();

			break;
		// 打印签购单
		case PRINT_WATER:
			
			if (bean != null) {
				mWater = bean.getWater();
			}					
			printVoucherCount = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_BASE_PRTCOUNT);
			printVouchers();		
			
			break;
		// 打印商户信息
		case PRINT_PARAM_MERCHANTINFO:
			new Thread(){
				public void run(){
					print(getPrintMerchantInfoData(context));
				}
			}.start();
			break;
		// 打印版本信息
		case PRINT_PARAM_VERSION:
			new Thread(){
				public void run(){
					print(getPrintVertionInfoData(context));
				}
			}.start();
			break;
		//  打印交易参数
		case PRINT_PARAM_TRANSCCTRL:
			new Thread(){
				public void run(){
					LoggerUtils.d("***********************这里开始print方法");
					print(getTransControlData(context));
				}
			}.start();
			break;	
		// 打印系统参数
		case PRINT_PARAM_SYSTEMCTRL:
			new Thread(){
				public void run(){
					print(getSysControlData(context));
				}
			}.start();
			break;	
			// 打印其他设置
		case PRINT_PARAM_OTHER:
			new Thread(){
				public void run(){
					print(getOtherControlData(context));
				}
			}.start();
			break;
			// 打印通讯设置
		case PRINT_PARAM_COMM:
			new Thread(){
				public void run(){
					print(getCommunicateControlData(context));
				}
			}.start();
			break;	
		case PRINT_PARAMS_EMV:
			//打印emv
			new Thread(){
				public void run(){
					print(PrintModelUtils.getEMVParamsData());
				}
			}.start();
			break;
		default:
			break;

		}
		
		
	}
	@Override
	protected void initClickEvent(View view) {

	}
	
	@Override
	protected void initEvent() {

	}
	
	@Override
	public void onFragmentHide() {
		dismissDialog();
		super.onFragmentHide();
	}

	@Override
	protected PrintBean getBean() {
		return bean;
	}

	
	/**
	 * 打印结束
	 */
	private void printEnd(boolean printResult){
		// 界面跳转
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.backFragment();
			}
		});
		// 设置打印结果
		if (bean != null) {
			bean.setPrintResult(printResult);
			onSucess();
		}
		
	}
	

	/**
	 * 打印签购单
	 * @param activity
	 * @param data
	 */
	private void printVoucher(final String data) {
		final PrintModule printModule = PrintModule.getInstance();
		PRNSTATUS status = printModule.getPrinterStatus();
		int result = 0;
		
		if (status == PRNSTATUS.PRN_STATUS_OK) {
				if (isShow) {
					showCountProgress(tipContent);
				}
				result = printModule.printScript(data);
			checkPrintVoucherStatus(result, printModule, data);
				
		} else if (status == PRNSTATUS.PRN_STATUS_BUSY) {
				showPrintToast(context.getString(R.string.error_device_busy));
				unLockData(data, false);;
		} else if (status == PRNSTATUS.PRN_STATUS_NOPAPER) {
				
				
				
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						dialog = MessageUtils.showCommonDialog(activity, activity
								.getString(R.string.error_print_lose_paper),
								R.string.reprint, R.string.common_cancel,
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										// 重新打印
										dialog.dismiss();
										new Thread(){
											public void run(){
												printVoucher(data);
											}
										}.start();

									}
								}, new OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog.dismiss();
										showPrintToast("取消打印");
										printEnd(false);
									}
								});
					}
				});
		} else if (status == PRNSTATUS.PRN_STATUS_OVERHEAT) {
				showPrintToast(context.getString(R.string.error_print_hot));
				unLockData(data, false);;
		} else {
				showPrintToast(context.getString(R.string.error_print_noreason));
				unLockData(data, false);;

			}

			

	}
	
	/**
	 * 校验打印签购单状态
	 * @param result
	 * @param data
	 */
	private void checkPrintVoucherStatus(int result, PrintModule printModule, final String data) {
		
		if (result != 0) {
			PRNSTATUS status = printModule.getPrinterStatus();
			if (status == PRNSTATUS.PRN_STATUS_OK) {
				LoggerUtils.d("打印数据为空");
				showPrintToast("无打印数据");
				unLockData(data, false);
				return;
			}
			else if (status == PRNSTATUS.PRN_STATUS_BUSY) {
				tipMessage = activity.getString(R.string.error_device_busy);
			}
			else if (status == PRNSTATUS.PRN_STATUS_NOPAPER) {
				tipMessage = activity.getString(R.string.error_print_lose_paper);
			}
			else if (status == PRNSTATUS.PRN_STATUS_OVERHEAT) {
				tipMessage = activity.getString(R.string.error_print_hot);
			}
			else if (status == PRNSTATUS.PRN_STATUS_VOLERR) {
				tipMessage = activity.getString(R.string.error_print_volerr);
			}
			else if (status == PRNSTATUS.PRN_STATUS_OTHER) {
				tipMessage = activity.getString(R.string.error_print_other);
			}

			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// 提示打印错误信息
					dialog = MessageUtils.showCommonDialog(activity, tipMessage,
							R.string.reprint, R.string.common_cancel,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
									// 界面跳转
									new Thread(){
										public void run(){
											printVoucher(data);
										}
									}.start();
									
								}
							}, new OnClickListener() {

								@Override
								public void onClick(View v) {
									activity.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											dialog.dismiss();
											showPrintToast("取消打印");
											unLockData(data, false);
										}
									});
								}
							});
				}
			});

		} else {
			// 成功打印结束
			
			unLockData(data, true);

		}
	
	}
	
	
	/**
	 * 非签购单
	 * @param activity
	 * @param data
	 */
	private void print( final String data) {
		final PrintModule printModule = PrintModule.getInstance();
		PRNSTATUS status = printModule.getPrinterStatus();
		int result = 0;
		if (data == null) {
			isShow = false;
			showPrintToast(context.getString(R.string.error_print_data));
			printEnd(true);
		}
			
		if (status == PRNSTATUS.PRN_STATUS_OK) {
				if (isShow) {
					showCountProgress(tipContent);
				}
				result = printModule.printScript(data);
			checkPrintStatus(result, printModule, data);
				
		} else if (status == PRNSTATUS.PRN_STATUS_BUSY) {
				showPrintToast(context.getString(R.string.error_device_busy));
				printEnd(false);
		} else if (status == PRNSTATUS.PRN_STATUS_NOPAPER) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog = MessageUtils.showCommonDialog(activity, activity
								.getString(R.string.error_print_lose_paper),
								R.string.reprint, R.string.common_cancel,
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										// 重新打印
										dialog.dismiss();
										new Thread(){
											public void run(){
												print(data);
											}
										}.start();

									}
								}, new OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog.dismiss();
										showPrintToast("取消打印");
										printEnd(false);
									}
								});
					}
				});
		} else if (status == PRNSTATUS.PRN_STATUS_OVERHEAT) {
				showPrintToast(context.getString(R.string.error_print_hot));
				printEnd(false);
		} else {
				showPrintToast(context.getString(R.string.error_print_noreason));
				printEnd(false);

			}


	}
	/**
	 * 校验非签购单打印状态
	 * @param result
	 * @param data
	 */
	private void checkPrintStatus(int result, PrintModule printModule, final String data) {
		
		if (result != 0) {
			PRNSTATUS status = printModule.getPrinterStatus();
			if (status == PRNSTATUS.PRN_STATUS_OK) {
				LoggerUtils.d("打印数据为空");
				showPrintToast("无打印数据");
				unLockData(data, false);
				return;
			}
			else if (status == PRNSTATUS.PRN_STATUS_BUSY) {
				tipMessage = activity.getString(R.string.error_device_busy);
			}
			else if (status == PRNSTATUS.PRN_STATUS_NOPAPER) {
				tipMessage = activity.getString(R.string.error_print_lose_paper);
			}
			else if (status == PRNSTATUS.PRN_STATUS_OVERHEAT) {
				tipMessage = activity.getString(R.string.error_print_hot);
			}
			else if (status == PRNSTATUS.PRN_STATUS_VOLERR) {
				tipMessage = activity.getString(R.string.error_print_volerr);
			}
			else if (status == PRNSTATUS.PRN_STATUS_OTHER) {
				tipMessage = activity.getString(R.string.error_print_other);
			}

			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// 提示打印错误信息
					dialog = MessageUtils.showCommonDialog(activity, tipMessage,
							R.string.reprint, R.string.common_cancel,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
									// 界面跳转
									new Thread(){
										public void run(){
											print(data);
										}
									}.start();
									
								}
							}, new OnClickListener() {

								@Override
								public void onClick(View v) {
									activity.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											dialog.dismiss();
											showPrintToast("取消打印");
											printEnd(false);
										}
									});
								}
							});
				}
			});

		} 
		else {
			// 成功打印结束			
			printEnd(true);			
		}
	
	}

	
	/**
	 * 签购单打印前，锁住打印线程
	 * @param data
	 */
	private void lockData(String data) {
		try {
			synchronized(data) {
				data.wait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 签购单打印后，解锁打印线程
	 * @param data
	 * @param printResult 是否还继续打印
	 */
	protected void unLockData(String data, boolean printResult) {
		try {
			synchronized(data) {
//				result = printResult;
				data.notify();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showPrintToast(final String tip) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ToastUtils.show(activity, tip);
			}
		});
	}


	
	/**
	 * 将流水转化成不带英文打印的指令数据
	 * 
	 * @param water
	 *            流水对象
	 * @param type
	 *            标识打印商户联、银行联、客户联
	 * @param isReprint
	 *            是否重打印
	 * @return
	 */
	public String getPrintWaterData(Water water, PrintWaterType type,
			boolean isReprint) {
		//判断打印的签购单类型
		String printStyle = null;
		if ("0".equals(ParamsUtils.getString(
				ParamsConst.PARAMS_KEY_IS_PRINT_SIGN_ORDER_ENGLISH))){
			if (0 == ParamsUtils.getInt(
				ParamsConst.PARAMS_KEY_SIGN_ORDER_FONT) || 1 == ParamsUtils.getInt(
						ParamsConst.PARAMS_KEY_SIGN_ORDER_FONT)) {
				printStyle = Const.PrintStyleConst.PRINT_WATER_WITHOUT_ENGLISH;
			}
			else {
				printStyle = Const.PrintStyleConst.PRINT_WATER_WITHOUT_ENGLISH_SMALL;
			}
		} else {
			if (0 == ParamsUtils.getInt(
					ParamsConst.PARAMS_KEY_SIGN_ORDER_FONT) || 1 == ParamsUtils.getInt(
							ParamsConst.PARAMS_KEY_SIGN_ORDER_FONT)) {
					printStyle = Const.PrintStyleConst.PRINT_WATER_WITH_ENGLISH;
				}
			else {
				printStyle = Const.PrintStyleConst.PRINT_WATER_WITH_ENGLISH_SMALL;
			}
		}
		
		return PrintUtils.getPrintDataEndLine(printStyle, 
				PrintModelUtils.waterToPrinterModel(water, type, isReprint));
	}

	/**
	 * 将流水转化成带英文打印的指令数据
	 * 
	 * @param water
	 *            流水对象
	 * @param type
	 *            标识打印商户联、银行联、客户联
	 * @param isReprint
	 *            是否重打印
	 * @return
	 */
	public String getPrintWaterWithEnglishData(Water water, PrintWaterType type,
			boolean isReprint) {
		
		return PrintUtils.getPrintDataEndLine(PrintStyleConst.PRINT_WATER_WITH_ENGLISH, 
				PrintModelUtils.waterToPrinterModel(water, type, isReprint));
	}

	/**
	 * 获取打印交易控制数据
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static String getTransControlData(Context context) {
	
		PrintSysParamModel model = PrintModelUtils.getTransControlModel(context);
		return PrintUtils.getPrintDataEndLine(PrintStyleConst.PRINT_PARAM_TRANSCCTRL, model);
	}

	/**
	 * 获取打印系统控制数据
	 * 
	 * @param
	 * @param
	 * @return
	 */
	private String getSysControlData(Context context) {
		PrintSysParamModel model = PrintModelUtils.getSysControlModel(context);

		return PrintUtils.getPrintDataEndLine(PrintStyleConst.PRINT_PARAM_SYSTEMCTRL, model);
	}

	
	
	/**
	 * 获取打印其他控制数据
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static String getOtherControlData(Context context) {
		PrintSysParamModel model = PrintModelUtils.getOtherControlModel(context);

		return PrintUtils.getPrintDataEndLine(PrintStyleConst.PRINT_PARAM_OTHER, model);
	}

	/**
	 * 获取打印通讯参数数据
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static String getCommunicateControlData(Context context) {
		PrintSysParamModel model = PrintModelUtils.getCommunicateControlModel(context);

		return PrintUtils.getPrintDataEndLine(PrintStyleConst.PRINT_PARAM_COMM, model);
	}

	/**
	 * 获取打印结算总计单数据
	 * 
	 * @param
	 * @param
	 * @return
	 */
	private String getPrintSettleData(Context context) {
		
		if (bean == null) {
			return ParamsUtils.getString(ParamsConst.PARAMS_OLD_SETTLEMENT);

		}else {
			PrintSettleModel model = PrintModelUtils.getPrintSettleModel(context);
			model.setIsReprint("true");
			ParamsUtils.setString(ParamsConst.PARAMS_OLD_SETTLEMENT, 
					PrintUtils.getPrintDataEndLine(PrintStyleConst.PRINT_SETTLE, model));
			
			model.setIsReprint(null);
			return PrintUtils.getPrintDataEndLine(PrintStyleConst.PRINT_SETTLE, model);
		}
		
	}

	/**
	 * 获取打印交易汇总数据
	 * 
	 * @param
	 * 
	 * @return
	 */
	private String getPrintTotalData(Context context) {
		PrintTotalModel model = PrintModelUtils.getPrintTotalModel(context);

		return PrintUtils.getPrintDataEndLine(PrintStyleConst.PRINT_TOTAL, model);
	}

	/**
	 * 获取打印商户信息数据
	 * 
	 * @param
	 * 
	 * @return
	 */
	public static String getPrintMerchantInfoData(Context context) {
		PrintInfoModel model = PrintModelUtils.getPrintMerchantInfoModel(context);

		return PrintUtils.getPrintDataEndLine(PrintStyleConst.PRINT_PARAM_MERCHANTINFO, model);
	}

	/**
	 * 获取打印版本信息数据
	 * 
	 * @param
	 * 
	 * @return
	 */
	public static String getPrintVertionInfoData(Context context) {
		PrintInfoModel model = PrintModelUtils.getPrintVertionInfoModel(context);

		return PrintUtils.getPrintDataEndLine(PrintStyleConst.PRINT_PARAM_VERSION, model);
	}


	/**
	 * 获取打印失败流水明细数据
	 * 
	 * @param
	 * @return
	 */
	public static String getPrintFailWaterData(Boolean top, Boolean end,
			Water water, int isUnSusc) {
		PrintAllWaterModel model = PrintModelUtils.getPrintFailWaterModel(top,
				end, water, isUnSusc);

		return PrintUtils.getPrintData(PrintStyleConst.PRINT_FAIL_WATER, model);
	}

	/**
	 * 获取打印交易明细模板数据
	 * 
	 * @param
	 * @return
	 */
	public static String getPrintAllWaterData(Boolean title, Boolean end,
			Water wate) {
		PrintAllWaterModel model = PrintModelUtils.getPrintAllWaterDate(title,
				end, wate);

		return PrintUtils.getPrintData(PrintStyleConst.PRINT_ALL_WATER, model);
	}

	/**
	 * 完整的打印签购单方法
	 * @param context
	 * @param water
	 */
	private void printVouchers()
	{
		new CommonThread(new ThreadCallBack() {
			
			@Override
			public void onMain() {
				
				if (printVoucherCount != 0) {
					handler.sendEmptyMessage(0x01);
				}
				else {
					printEnd(true);
				}
				
			}
			
			@Override
			public void onBackGround() {
				//打印首张签购单
				printSingleVoucher(context, mWater,
						PrintWaterType.MERCHANT);
				printVoucherCount --;
			}
		}).start();
	}
	
	
	/**
	 * 打印单条签购单
	 * @param context
	 * @param water
	 * @param type
	 */
	private String printSingleVoucher(Context context,
			Water water, PrintWaterType type) {
		boolean isRprint = false;		
		if (bean == null) {
			isRprint = true;
		}
		// 设备开始打印
		final String printCmdData = getPrintWaterData(water, type, isRprint);
		new Thread(){
			public void run(){
				printVoucher(printCmdData);
			}
		}.start();
		
		lockData(printCmdData); // 锁住线程等待，打印结果
		return printCmdData;
	}
	
	/**
	 * 打印交易明细单
	 * @param mActivity
	 */
	private void printTransDetail(Activity mActivity){
		
		String printCmdDate = null;

		WaterDaoImpl waterDaoImpl = new WaterDaoImpl(mActivity);
		List<Water> waterList = waterDaoImpl.findAll();

		StringBuffer printBufferTop = new StringBuffer();
		StringBuffer printBufferItem = new StringBuffer();
		StringBuffer printBufferBottom = new StringBuffer();
		String printAllFlag = ParamsUtils
				.getString(ParamsConst.PARAMS_KEY_PRINT_ALL_TRANS_DETAIL);

		if (waterList != null && waterList.size() != 0) {
			// 打印交易开头
			printCmdDate = getPrintAllWaterData(true,
					false, null);
			printBufferTop.append(printCmdDate);
		}
 
		for (Water water : waterList) {
			if (Const.NO.equals(printAllFlag)) {
					// 处理不不要打印的交易类型
				if (water.getTransStatus() != null
						&& water.getTransType() != null) {
					if (water.getTransStatus() != TransStatus.REV
							&& water.getTransStatus() != TransStatus.SEND_AND_ADJ
							&& water.getTransType() != TransType.TRANS_VOID_SALE
							&& water.getTransType() != TransType.TRANS_VOID_AUTHSALE
							&& water.getTransType() != TransType.TRANS_EC_VOID_LOAD_CASH
							&& water.getTransType() != TransType.TRANS_PREAUTH
							&& water.getTransType() != TransType.TRANS_VOID_PREAUTH
							&& water.getTransType() != TransType.TRANS_EC_LOAD_NOT_BIND
							&& water.getTransType() != TransType.TRANS_EC_LOAD) {
						printCmdDate = getPrintAllWaterData(false, false,
										water);
						printBufferItem.append(printCmdDate);
					}
				}
			} else {
				if (water.getTransStatus() != TransStatus.REV && water.getTransStatus() != TransStatus.SEND_AND_ADJ ) {
					printCmdDate = getPrintAllWaterData(
							false, false, water);
					printBufferItem.append(printCmdDate);
				}
				
			}
		}
		if (printBufferItem != null && printBufferItem.length() != 0) {
			
			// 获取明细单尾部
			printCmdDate = getPrintAllWaterData(false,
					true, null);
			printBufferBottom.append(printCmdDate);
			printBufferTop.append(printBufferItem.toString());
			printBufferTop.append(printBufferBottom.toString());
	
		}
		else {
			showPrintToast(context.getString(R.string.no_transdetail));
			printEnd(true);
			return;
		}
		LoggerUtils.d(printBufferTop.toString());
	    print(printBufferTop.toString());
	}

	/**
	 *  打印失败流水明细单
	 * @param mActivity
	 */
	public void printFTransDetail(final Activity mActivity) {
		String printCmdDate = null;
		WaterDaoImpl impl = new WaterDaoImpl(mActivity);
		List<Water> waterList = impl.findAll();
		List<Water> waterFailList = new ArrayList<Water>();
		List<Water> waterDeliedList = new ArrayList<Water>();
		Boolean isHasFailWater = false;
		Boolean isHasDeliedWater = false;
		StringBuffer printBuffer = new StringBuffer();
		StringBuffer printBufferFail = new StringBuffer();
		StringBuffer printBufferDelied = new StringBuffer();

		//构造上送失败与上送被拒流水列表
		if (waterList != null) {
			for (Water water : waterList) {
				if (water.getOffSendFlag() == SendStatus.FAIL) {
					waterFailList.add(water);
				}
				if (water.getOffSendFlag() ==  SendStatus.DECLINE) {
					waterDeliedList.add(water);
				}
			}
		}
		printBufferFail = getFwaterBuffer(waterFailList);
		printBufferDelied = getFwaterBuffer(waterDeliedList);

		//判断需要打印的流水数据是否为空
		if (printBufferFail != null && printBufferFail.length() != 0) {
			isHasFailWater = true;
		}
		if (printBufferDelied != null && printBufferDelied.length() != 0) {
			isHasDeliedWater = true;
		}
		
		if (isHasFailWater) {
			// 获取交易开头
			printCmdDate = getPrintFailWaterData(true, false,
					null, SendStatus.FAIL);
			printBuffer.append(printCmdDate);
		
			// 获取流水数据
			printBuffer.append(printBufferFail.toString());
			// 获取明细单尾部
			printCmdDate = getPrintFailWaterData(false, true,
					null, -1);
			printBuffer.append(printCmdDate);

		}
		
		if (isHasDeliedWater) {
			// 获取交易开头
			printCmdDate = getPrintFailWaterData(true, false,
					null,SendStatus.DECLINE);
			printBuffer.append(printCmdDate);		
			// 获取流水数据
			printBuffer.append(printBufferDelied.toString());
			// 获取明细单尾部
			printCmdDate = getPrintFailWaterData(false, true,
					null, -1);
			printBuffer.append(printCmdDate);
			
		}
		if (printBuffer == null || printBuffer.length() == 0) {
			showPrintToast(context.getString(R.string.no_fail_waters));
			printEnd(true);
			isShow = false;
			return;
		}
		print(printBuffer.toString());
		
	}

	/**
	 * 获取失败流水单的中间流水数据（不包括开头与结尾）
	 * @param waterList 失败流水列表
	 * @return
	 */
	private  StringBuffer getFwaterBuffer(List<Water> waterList) {
		if (waterList == null || waterList.size() ==0) {
			return null;
		}
		String printAllFlag = ParamsUtils
				.getString(ParamsConst.PARAMS_KEY_PRINT_ALL_TRANS_DETAIL);
		String printCmdDate = null;
		StringBuffer printBuffer = new StringBuffer();
		for (Water water : waterList) {
			if ("0".equals(printAllFlag)) {
				// 处理不不要打印的交易类型
				if (water.getEmvStatus() != null
						&& water.getTransType() != null) {
					if (water.getTransType() == TransType.TRANS_SALE
							|| water.getTransType() == TransType.TRANS_EC_PURCHASE) {
						
							printCmdDate = getPrintFailWaterData(
									false, false, water, -1);
							printBuffer.append(printCmdDate);
						
					}
					if (water.getTransType() == TransType.TRANS_SALE
							&& water.getTransType() == TransType.TRANS_EC_PURCHASE) {
						if (water.getEmvStatus() != EmvStatus.EMV_STATUS_OFFLINE_SUCC) {
							
						}else {
							printCmdDate = getPrintFailWaterData(
									false, false, water, -1);
							printBuffer.append(printCmdDate);
						}
					}
				}
			} else {
				printCmdDate = getPrintFailWaterData(false,
						false, water, -1);
				if (printCmdDate != null) {
					printBuffer.append(printCmdDate);
					isShow = false;
				}
			}
		}
		return printBuffer;
	}
	
	
	/**
	 * 创建进度显示条
	 */
	private void initCountProgress() {
		if (null == progressCountDialog) {
			progressCountDialog = new Dialog(activity, R.style.progress_dialog);
			progressCountDialog.setCancelable(false);
			rootView = LayoutInflater.from(activity).inflate(
					R.layout.progress_contain_cutdown_text_tip, null);
			tvTips = (TextView) rootView.findViewById(R.id.tv_dialog_tips);

			progressCountDialog.setContentView(rootView);
			// APP 获取 HOME keyEvent的设定关键代码。
			progressCountDialog.getWindow().addFlags(3);
			progressCountDialog.getWindow().addFlags(5);
//			if (VERSION.SDK_INT > VERSION_CODES.JELLY_BEAN_MR1) {
//				progressCountDialog.getWindow().addFlags(3);
//			} else {
//				progressCountDialog.getWindow().addFlags(
//						WindowManager.LayoutParams.FLAG_HOME_KEY_EVENT);
//			}

			progressCountDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dg, int keyCode,
						KeyEvent event) {
					return false;
				}
			});
		}
	}
	
	/**
	 * 显示进度显示条
	 * @param msg
	 */
	private void showCountProgress(final String msg) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (activity.isFinishing()) {
					return;
				}
				if (null == progressCountDialog) {
					initCountProgress();
				}
				if (progressCountDialog != null
						&& !progressCountDialog.isShowing()) {
					if (!StringUtils.isEmpty(msg))
						tvTips.setText(msg);
					progressCountDialog.show();
				} else if (null != progressCountDialog
						&& progressCountDialog.isShowing()) {
					if (!StringUtils.isEmpty(msg))
						tvTips.setText(msg);
				}
			}
		});
	}
	
	/**
	 * 关闭进度显示条
	 */
	private void dismissDialog() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (null != progressCountDialog
						&& progressCountDialog.isShowing()) {
					progressCountDialog.dismiss();
					progressCountDialog = null;
				}
			}
		});
	}
	
	/**
	 * 非商户联打印
	 */
	private void printVoucherAfterFirst(){
		if ( printVoucherCount > 1) {
			printSingleVoucher(context, mWater,
					PrintWaterType.BANK);
			printVoucherCount --;
			handler.sendEmptyMessage(0x01);
			}				
		else {
			printSingleVoucher(context, mWater,
					PrintWaterType.CUSTOMER);
			printEnd(true);
		}
	}
	
	/**
	 * 休眠30秒后打印并发送打印下一联
	 */
	private void sleepHalfMiniter() {
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					for (int i = 0; i < 60; i++) {
						if (isWait) {
							Thread.sleep(500);
						}
						else {
							isWait = true;
							return;
						}			 
					}	
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					printVoucherAfterFirst();
				}
							
			}
		});
		thread.start();
		
	}
	
}
