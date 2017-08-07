package com.newland.payment.trans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst.DownloadEmvParamType;
import com.newland.payment.common.TransConst.TransDeal;
import com.newland.payment.trans.impl.ElecSignSend;
import com.newland.payment.trans.impl.Login;
import com.newland.payment.trans.impl.Settle;
import com.newland.payment.trans.impl.offline.SendOffline;
import com.newland.payment.trans.manage.impl.CardBInCUpdate;
import com.newland.payment.trans.manage.impl.CardBinBUpdate;
import com.newland.payment.trans.manage.impl.EmvBlackListDownload;
import com.newland.payment.trans.manage.impl.EmvParamsDownload;
import com.newland.payment.trans.manage.impl.ParamsTransmit;
import com.newland.payment.trans.manage.impl.RfParamsDownload;
import com.newland.payment.trans.manage.impl.StatusSend;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.util.LoggerUtils;

import android.app.Activity;
import android.content.Context;

/**
 * 处理要求
 * @version 1.0
 * @author spy
 * @date 2015年6月6日
 * @time 上午10:46:34
 */
public class RequireHandler {

	private TransController transController = null;//new TransController(activity);
	
	/**待处理列表*/
	private List<AbstractBaseTrans> transList = Collections.synchronizedList(new ArrayList<AbstractBaseTrans>());
	
	/**处理标记*/
	private boolean isHandling  = false;
	
	/**签到次数标记*/
	private int reSignCount = 0;
	
	/**不处理次数*/
	private int skipCount = 0;
	
	/**同步锁,每次使用时间应该很短,需在主线程调用*/
	private final Object syncObject = new Object();
	
	
	private RequireHandler(){
		Context context = MainActivity.getInstance();
		transController = new TransController(context);
	}
	
	private static class InnerClass{
		private static final RequireHandler INSTANCE = new RequireHandler();
	}
	
	public static RequireHandler getInstance(){
		return InnerClass.INSTANCE;
	}
	
	/**
	 * 请求处理,内部使用同步方法
	 */
	public void request(){
		LoggerUtils.d("dd 执行->RequireHandler->request");
		synchronized (syncObject) {
			if(isHandling){
				LoggerUtils.d("dd request 有线程正在执行,退出");
				return;
			}
			isHandling = true;
		}
		
		
		if(skipCount > 0){
			LoggerUtils.d("dd request 本次不执行,退出");
			skipCount = 0;
			done();
			return;
		}
		doHandle();
	}
	
	/**
	 * 
	 */
	private void doHandle(){
		new Thread() {
			@Override
			public void run() {
				preHandle();
				if (transList != null && transList.size() > 0) {
					MainActivity.getInstance().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							MainActivity.getInstance().showProgress(false);
						}
					});
				}
				handle();
			}
		}.start();
		
	}
	
	/**
	 * 收集标记
	 */
	public void preHandle(){
		do{
			boolean flag = false;
			String currentKey = "";
			AbstractBaseTrans trans = null;
			
			transList.clear();
			//结算中断
			if(!MainActivity.getInstance().isThirdInvoke())
			{	
				//结算中断
				currentKey = ParamsTrans.PARAMS_IS_SETTLT_HALT;
				flag = ParamsUtils.getBoolean(currentKey, false);
				if (flag){
					LoggerUtils.d("dd preHandle->结算中断");
					trans = new Settle(TransDeal.SPECIAL);
					transList.add(trans);
				}
			}

			//参数传递
			currentKey = ParamsTrans.PARAMS_IS_PARAM_DOWN;
			flag = ParamsUtils.getBoolean(currentKey, false);
			if(flag){
				LoggerUtils.d("dd preHandle->参数传递");
				trans = new ParamsTransmit();
				transList.add(trans);
			}
			
			//状态上送
			currentKey = ParamsTrans.PARAMS_IS_STATUS_SEND;
			flag = ParamsUtils.getBoolean(currentKey, false);
			if(flag){
				LoggerUtils.d("dd preHandle->状态上送");
				trans = new StatusSend();
				transList.add(trans);
			}
			
			//重新签到
			currentKey = ParamsTrans.PARAMS_IS_RESIGN;
			flag = ParamsUtils.getBoolean(currentKey, false);
			if(flag){
				LoggerUtils.d("dd preHandle->重新签到");
				reSignCount = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_REVERSAL_RESEEND_TIMES, 3);
				LoggerUtils.d("dd 签到重发次数：" + reSignCount);
				trans = new Login();
				transList.add(trans);
			}			
			
			//IC卡参数、公钥
			if (ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_IC)){
				currentKey = ParamsTrans.PARAMS_IS_AID_DOWN;
				flag = ParamsUtils.getBoolean(currentKey, false);
				if(flag){
					LoggerUtils.d("dd preHandle->AID下载");
					trans = new EmvParamsDownload(DownloadEmvParamType.AID);
					transList.add(trans);
				}
				currentKey = ParamsTrans.PARAMS_IS_CAPK_DOWN;
				flag = ParamsUtils.getBoolean(currentKey, false);
				if(flag){
					LoggerUtils.d("dd preHandle->CAPK下载");
					trans = new EmvParamsDownload(DownloadEmvParamType.CAPK);
					transList.add(trans);
				}
			}
			//黑名单下载
			currentKey = ParamsTrans.PARAMS_IS_BALACKLIST_DOWN;
			flag = ParamsUtils.getBoolean(currentKey, false);
			if(flag){
				LoggerUtils.d("dd preHandle->黑名单下载");
				trans = new EmvBlackListDownload();
				transList.add(trans);
			}
			
			//TMS参数下载
			currentKey = ParamsTrans.PARAMS_IS_TMS_PARAM_DOWN;
			flag = ParamsUtils.getBoolean(currentKey, false);
			if(flag){
				trans = null;
				LoggerUtils.d("dd preHandle->Tms参数传递");
				//ParamsUtils.setBoolean(currentKey, false);
				
			}

			
			//离线自动上送
			int offlineUnsend = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_OFFLINE_UNSEND, 0);
			int elecSignUnsend = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_ELECSIGN_UNSEND, 0);
			int autoSend = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_MAX_OFFSEND_NUM, 10);
			LoggerUtils.d("dd 离线笔数：" + offlineUnsend + "电子签名笔数:" + elecSignUnsend + "自动上送笔数:" + autoSend);
			if (offlineUnsend + elecSignUnsend >= autoSend){
				LoggerUtils.d("dd preHandle->离线自动上送");
				trans = new SendOffline(TransDeal.SPECIAL);
				transList.add(trans);
				trans = new ElecSignSend(TransDeal.SPECIAL);
				transList.add(trans);
			}
			
			//非接业务参数下载
			currentKey = ParamsTrans.PARAMS_IS_RF_PARAM_DOWN;
			flag = ParamsUtils.getBoolean(currentKey, false);
			if(flag){
				LoggerUtils.d("dd preHandle->非接业务参数下载");
				trans = new RfParamsDownload();
				transList.add(trans);
			}
			
			//Bin表B更新
			currentKey = ParamsTrans.PARAMS_IS_CARDBIN_B_UPDATE;
			flag = ParamsUtils.getBoolean(currentKey, false);
			if(flag){
				LoggerUtils.d("dd preHandle->Bin表B更新");
				trans = new CardBinBUpdate();
				transList.add(trans);
			}
			
			//Bin表C更新
			currentKey = ParamsTrans.PARAMS_IS_CARDBIN_C_DOWN;
			flag = ParamsUtils.getBoolean(currentKey, false);
			if(flag){
				LoggerUtils.d("dd preHandle->Bin表C更新");
				trans = new CardBInCUpdate();
				transList.add(trans);
			}
			
		}while(false);
	}
	
	/**
	 * 处理要求
	 */
	private void handle(){
		LoggerUtils.d("dd 执行handle");
		
		if(transList == null){
			LoggerUtils.d("dd handle transList为null,退出");
			done();
			return;
		}
		
		if(transList.size() == 0){
			LoggerUtils.d("dd handle transList为空,退出");
			done();
			return;
		}
		
		Activity activity = MainActivity.getInstance();
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try{
					transController.start(transList.get(0), listener);
				}catch(Exception e){
					e.printStackTrace();
					listener.fail("start exception");
				}
			}
		});	
	
	}
	
	private TransResultListener listener = new TransResultListener() {
		
		@Override
		public void succ() {
			LoggerUtils.d("dd 执行成功");
			if(MainActivity.getInstance().isThirdInvoke())
			{
				done();
				return;
//				MainActivity.getInstance().finish();
			}
			transList.remove(0);
			preHandle();
			handle();
		}
		
		@Override
		public void fail(String message) {
			LoggerUtils.d("111 执行失败");
			AbstractBaseTrans trans = null;
			if(transList.size() > 0){
				 trans = transList.get(0);
			}else{
				if(trans instanceof Login){
					if(reSignCount > 0){
						LoggerUtils.d("dd 签到执行失败");
						reSignCount--;
						handle();
						return;
					} else {
						ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_RESIGN, false);
					}
				}				
			}
			
			if(!MainActivity.getInstance().isThirdInvoke())
			{
				skipCount++;
			}
			
			if(transList.size() > 0)
			{
				transList.remove(0);
			}
			if(MainActivity.getInstance().isThirdInvoke())
			{
				done();
				return;
//				MainActivity.getInstance().finish();
			}			
			
			handle();

		}
	};
	
	/**
	 * 处理完成
	 */
	private void done(){
		LoggerUtils.d("dd done-------");
		MainActivity.getInstance().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				MainActivity.getInstance().hideProgress();
			}
		});
		synchronized (syncObject) {
			LoggerUtils.d("dd done-置false");
			isHandling = false;
		}
	}
	
	public List<AbstractBaseTrans> getTransList() {
		return transList;
	}

	public boolean isHandling() {
		return isHandling;
	}

	public void setHandling(boolean isHandling) {
		this.isHandling = isHandling;
	}
}
