package com.newland.payment.trans;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst.TransDeal;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.impl.JycMposDown;
import com.newland.payment.trans.impl.JycMposLoad;
import com.newland.payment.trans.impl.Login;
import com.newland.payment.trans.impl.Settle;
import com.newland.payment.trans.invoke.ThirdInvokeController;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.bean.MessageTipBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.TimeUtils;


/**
 * 交易流程控制器
 * @author linchunhui
 * @date 2015-05-07
 * @time 18:00:00
 *
 */
public class TransController {
//	private final static int SHOW_MAIN_MENU = 0x01;
	
	private List<Method> methodTask = new ArrayList<Method>();
	
	private List<Method> methodList = new ArrayList<Method>();
	
	private WaterService waterService;
	
	private TransResultListener listener;

	public TransController(Context context) {
		waterService = new WaterServiceImpl(context);
	}
	
	private static boolean isRequest = false;
	
	private static boolean isCheckRequest = true;
	
	
	/**
	 * 获取下一步操作的函数
	 * @param nextStep
	 * @return
	 * @throws NoStepMethodException
	 */
	private Method getNextMethod(int nextStep) throws NoStepMethodException {
		for (Method method : methodList) {
			AnnStep ann = method.getAnnotation(AnnStep.class);
			if (ann.stepIndex() == nextStep) {
				return method;
			}
		}
		throw new NoStepMethodException("无此步骤的方法:" + nextStep);
	}
	
	public void start(final AbstractBaseTrans trans, final int stepIndex) {
		if (stepIndex < 1) {
			return;
		}
		
		methodList.clear();
		Class<?> clazz = trans.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		
		for (Method method : methods) {
			AnnStep step = method.getAnnotation(AnnStep.class);
			if (step != null) {
				methodList.add(method);
			}
		}
		
		new Thread() {
			@Override
			public void run() {
				// 1. 验证权限
				LoggerUtils.d("111 thread:"+Thread.currentThread().getId());
				trans.checkPower();
				boolean isNeedLogin = false;
				if(trans instanceof JycMposDown || trans instanceof JycMposLoad){
					isCheckRequest = false;
				}
				if(isCheckRequest && MainActivity.getInstance().isThirdInvoke()){
					RequireHandler.getInstance().preHandle();
					if ( RequireHandler.getInstance().getTransList() != null 
							&& RequireHandler.getInstance().getTransList().size() > 0){
							//EMV参数下载判断
							isRequest = true;
					}else{
						isRequest = false;
					}					
				}else{
					isRequest = false;
				}
				
				if (trans.checkSingIn) {
					// 判断系统是否签到，以及签到是否超时
					if (TimeUtils.getCurrentDate().equals(ParamsUtils.getString(ParamsConst.PARAMS_RUN_LOGIN_DATE)) == false
							|| !ParamsUtils.getBoolean(ParamsTrans.PARAMS_FLAG_SIGN)) {
//						MessageTipBean tipBean = new MessageTipBean();
//						tipBean.setTitle(trans.getText(R.string.common_tip_title));
//						tipBean.setContent("终端未签到，请先签到");
//						tipBean.setCancelable(false);
//						trans.showUIMessageTip(tipBean);
//						trans.backToTransMenu();
//						endThirdInvoke(-1);
//						return;
						
						isNeedLogin = true;
					}
				}
				if (trans.checkWaterCount) {
					LoggerUtils.d("dd 流水笔数超限---------：" + trans.getClass());
					// 判断流水是否已满
					int c = waterService.getWaterCount();
					
					File dir = new File(App.SIGNATURE_BMP_DIR);
					int signalNum = dir.listFiles() == null ? 0 : dir.listFiles().length;

					if ( signalNum >= ParamsUtils.getInt(ParamsConst.PARAMS_KEY_MAX_ELEC_COUNT) ||
							c >= ParamsUtils.getInt(ParamsConst.PARAMS_KEY_MAX_TRANS_COUNT, 500)) {
						// 提示笔数超限
						MessageTipBean tipBean = new MessageTipBean();
						tipBean.setTitle(trans.getText(R.string.common_tip_title));
						tipBean.setContent(trans.getText(R.string.settle_max_count_status_tip));
						tipBean.setCancelable(false);
						trans.showUIMessageTip(tipBean);
						trans.backToTransMenu();
						endThirdInvoke(-1);
						return;
					}
				}
				if (trans.checkSettlementStatus) {
					if (ParamsUtils.getBoolean(
							ParamsTrans.PARAMS_IS_SETTLT_HALT, false)) {
						// 结算中断,重新结算
						MessageTipBean tipBean = new MessageTipBean();
						tipBean.setTitle(trans.getText(R.string.common_tip_title));
						tipBean.setContent(trans.getText(R.string.settle_halt));
						tipBean.setCancelable(false);
						trans.showUIMessageTip(tipBean);
						trans.backToTransMenu();
						endThirdInvoke(-1);
						return;
					}
				}
				if (trans.checkIsExistWater){
					/** 数据库有流水存在，提示先结算 */
					int c = waterService.getWaterCount();
					if (c > 0) {
						MessageTipBean tipBean = new MessageTipBean();
						tipBean.setTitle(trans.getText(R.string.common_tip_title));
						tipBean.setContent(trans.getText(R.string.settle_exist_count_status_tip));
						tipBean.setCancelable(false);
						trans.showUIMessageTip(tipBean);
						trans.backToTransMenu();
						endThirdInvoke(-1);
						return;
					}
				}
				if (trans.checkManagerPassword
						&&(MainActivity.getInstance().isThirdInvoke() == false  || !isRequest)){
					if (ParamsUtils.getBoolean(
							ParamsConst.PARAMS_KEY_IS_ADMIN_PASSWORD)) {
						CommonBean<Integer> commonBean = new CommonBean<Integer>();
						commonBean.setValue(Integer.valueOf(R.string.pls_input_main_password));
						commonBean = trans.showUIOperatorPasswordInput(commonBean);
						
						if(!commonBean.getResult()){
							trans.backToTransMenu();
							endThirdInvoke(-1);
							return;
						}
					}
				}
				
				// 2. 初始化交易
				int ret = trans.init();
				if (ret<0){
					trans.showToast(trans.getText(R.string.error_device_busy));
					endThirdInvoke(ret);
					return;
				}
				
				// 3. 执行业务
				if(MainActivity.getInstance().isThirdInvoke() == false){
					//非第三方调用执行业务
					ret = doStep(stepIndex, trans);
				}else{
					if(trans instanceof Login){
						isNeedLogin = true;
					}
					if(MainActivity.getInstance().getTransType() == TransType.TRANS_LOGIN){
						LoggerUtils.d("111 签到" );
						ret = doStep(stepIndex, trans);
					}else if(isNeedLogin){
						//第三方其他交易做自动签到
						if(trans instanceof Login){
							LoggerUtils.d("111 自动签到" );
							ret = doStep(stepIndex, trans);
						}else{
							//自动签到后，不需要调用dostep执行交易。在release中会再次调用第三方调用。
							LoggerUtils.d("111 创建自动签到" );
							new ThirdInvokeController().invokeLogin(null);
							return;								
						}
					}else if(RequireHandler.getInstance().isHandling()){
						LoggerUtils.d("111 参数下载");
						ret = doStep(stepIndex, trans);
						if(ret != 0){
							isCheckRequest = false;
						}
					}else{
						if(MainActivity.getInstance().getTransType() == TransType.TRANS_SETTLE){
							if(ParamsUtils.getBoolean(ParamsTrans.PARAMS_IS_SETTLT_HALT, false)){
								LoggerUtils.d("111 未完成结算" );
								Settle st = new Settle();
								st = (Settle) trans;
								st.setDealType(TransDeal.SPECIAL);
								ret = doStep(stepIndex, st);							
							}else{
								LoggerUtils.d("111 结算" );
								ret = doStep(stepIndex, trans);
							}
						}else{
							if (isRequest){
								LoggerUtils.d("111 开始参数下载");
								RequireHandler.getInstance().request();
								return;
							}else{
								LoggerUtils.d("111 第三方非管理类交易");
								ret = doStep(stepIndex, trans);
								isCheckRequest = true;
								isRequest = false;
							}
						}
						
					}					
				}
				// 4. 释放资源
				trans.release();
				LoggerUtils.d("111 release:" + trans.getClass());
				LoggerUtils.d("111 执行结果res:" + ret);
				endThirdInvoke(ret);					

			}
		}.start();
	}
	
	/**
	 * 第三方调用结果回调
	 * @param res
	 */
	private void endThirdInvoke(int res){
		LoggerUtils.d("dd endThirdInvoke listener==null:" + (listener==null));
		LoggerUtils.d("dd endThirdInvoke->res:" + res);
		if(listener != null){
			switch (res) {
			case 0: //succ
				listener.succ();
				break;
			case -1:
				listener.fail("交易未完成");
				break;
			default:
				listener.fail("交易失败");
				break;
			}
		}
	}
	
	private int doStep(int stepIndex, AbstractBaseTrans trans) {
		int curStep = stepIndex;
		try {
			while (curStep > 0) {
				Method method = getNextMethod(curStep);
				
				Object obj = null;
				obj = method.invoke(trans);
				LoggerUtils.e("hjh    current step = "+curStep);
				methodTask.add(0, method);
				
				try {
					curStep = Integer.valueOf(obj.toString());
				} catch(Exception ex) {
					throw new Exception("step [" + curStep + "] 方法没有返回正确的步骤数据");
				}
				if (curStep == AbstractBaseTrans.FINISH) {
					// 失败
					return -1;
//					if (this.listener != null)
//						this.listener.fail("交易失败");
				} else if (curStep == AbstractBaseTrans.SUCC) {
					// 成功
					return 0;
//					if (this.listener != null) 
//						this.listener.succ();
					
				} else if (curStep < 0) {
					while(methodTask.size() > 0 && curStep != 0) {
						methodTask.remove(0);
						curStep++;
					}
					if (methodTask.size() == 0) {
						curStep = 0;
					} else {
						try {
							AnnStep annStep = methodTask.get(0).getAnnotation(AnnStep.class);
							curStep = annStep.stepIndex();
							methodTask.remove(0);
						}catch(Exception e){
							this.listener.fail(e.getMessage());
							throw new Exception("执行步骤 [" + curStep + "] 异常");
						}
					}
				}
			}
			return 1;
		} catch (NoStepMethodException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 标准入口
	 * @param trans
	 */
	public void start(final AbstractBaseTrans trans) {
		this.start(trans, 1);
	}
	
	public void start(final AbstractBaseTrans trans, TransResultListener listener) {
		this.listener = listener;
		this.start(trans);
	}
}
