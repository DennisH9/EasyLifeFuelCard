package com.newland.payment.service;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.newland.base.util.FileUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.aidl.IPaymentService;
import com.newland.payment.aidl.SimpleWater;
import com.newland.payment.common.Const;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.PrintType.PrintWaterType;
import com.newland.payment.common.tools.PrintModelUtils;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.ui.activity.App;
import com.newland.pos.sdk.device.PrintModule;
import com.newland.pos.sdk.device.PrintModule.PRNSTATUS;
import com.newland.pos.sdk.print.PrintUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

public class PaymentService extends Service {

	private Context context = PaymentService.this;
	 int result = 0;


	@Override
	public IBinder onBind(Intent arg0) {
		return new PaymentInfServiceStub();
	}
	
	private class PaymentInfServiceStub extends IPaymentService.Stub {

		@Override
		public boolean setParam(String key, String value)
				throws RemoteException {
			LoggerUtils.d("lxb -setParam");
			if (isRightfulCalling(key,value)) {
				// TODO 设置参数
				//
				Map<String, String> map = new HashMap<String, String>();
				ParamsUtils.setString(key, value);
				LoggerUtils.i("AIDL -商户号");					
				 if (key.equals("BASE_MERCHANTID")){
					 if(!StringUtils.isDigital(value) || value.length() !=15) {
						 return false;
					 }else{
						 return App.paramsManager.setMerchantNumber(value);
					 }	 
				 }
				if(key.equals("BASE_POSID")){
					LoggerUtils.i("AIDL -终端号");		
					 if(!StringUtils.isDigital(value) || value.length() !=8) {
						 return false;
					 }else{
						 return App.paramsManager.setTerminalNumber(value);
					 }	 
				}
					
				if(key.equals("BASE_MERCHANTNAME")){
					LoggerUtils.i("AIDL --商户名称");	
					if(StringUtils.isNullOrEmpty(value)){
						return false;
					}else{
						if(value.length()>40){
							value = value.substring(0, 40);
						}
						map.put(key, value);
						return  App.paramsManager.commit(map)?true:false;
					}
				}
				if(key.equals("BASE_BUSINESSCODE") || key.equals("BASE_BUSINESSNO") 
						                    || key.equals("BASE_BUSINESSACCOUNT")){
					LoggerUtils.i("AIDL --业务子编码||业务商户号||对账号");	
					if(StringUtils.isNullOrEmpty(value)){
						return false;
					}else{
						map.put(key, value);
						return  App.paramsManager.commit(map)?true:false;
					}
				}
				
				if(key.equals("BASE_TRACENO") || key.equals("BASE_BATCHNO")){
					LoggerUtils.i("AIDL --批次号||流水号");	
					if(StringUtils.isDigital(value) || value.length() != 6){
						return false;
					}else{
						map.put(key, value);
					return  App.paramsManager.commit(map)?true:false;
					}
				}	
				if(key.equals("COMMUNICATION_OUT_TIME")){
					LoggerUtils.i("AIDL --COMMUNICATION_OUT_TIME");
					if(StringUtils.isDigital(value) || value.length() > 2){
						return false;
					}else{
						map.put(key, value);
					return  App.paramsManager.commit(map)?true:false;
					}
				}
				if(key.equals("COMMUNICATION_TYPE")){
					LoggerUtils.i("AIDL --COMMUNICATION_TYPE");
					if("012".indexOf(value)<0){
						return false;
					}else{
						map.put(key, value);
					return  App.paramsManager.commit(map)?true:false;
					}
				}
				if(key.equals("GPRS_MODE") || key.equals("WIFI_MODE")||key.equals("IS_SUPPORT_ELECSIGN")){
					LoggerUtils.i("AIDL --长短连接||电子签名标识");
					if("01".indexOf(value)<0){
						return false;
					}else{
						map.put(key, value);
					return  App.paramsManager.commit(map)?true:false;
				}

			}
				if(key.equals("GPRS_APN")||key.equals("GPRS_APN")||key.equals("GPRS_SERVERIP1")
					||key.equals("GPRS_SERVERIP2")||key.equals("GPRS_PORT1")||key.equals("GPRS_PORT2")
					||key.equals("GPRS_TPDU")){
					LoggerUtils.i("AIDL --GPRS类");
					if(StringUtils.isNullOrEmpty(key)){
						return false;
					}else{
						map.put(key, value);
					return  App.paramsManager.commit(map)?true:false;
				}

			}
			
				if(key.equals("WIFI_APN")||key.equals("WIFI_APN")||key.equals("WIFI_SERVERIP1")
						||key.equals("WIFI_SERVERIP2")||key.equals("WIFI_PORT1")||key.equals("WIFI_PORT2")
						||key.equals("WIFI_TPDU")){
					LoggerUtils.i("AIDL --WIFI类");
						if(StringUtils.isNullOrEmpty(key)){
							return false;
						}else{
							map.put(key, value);
						return  App.paramsManager.commit(map)?true:false;
					}

				}
				
				if(key.equals("BASE_PRTCOUNT")){
					LoggerUtils.i("AIDL --打印张数");
					if("12".indexOf(value)<0){
						return false;
					}else{
						map.put(key, value);
					return  App.paramsManager.commit(map)?true:false;
					}
				}
			}
			return false;
		}
		
		@Override
		public String getParam(String key) throws RemoteException {
			if (isRightfulCalling()) {
				// TODO 获取参数
				
				String value = App.paramsManager.query(key);
				LoggerUtils.i("Preferences get->key:"+key+", value:"+value);
				return value;
			}
			return null;
		}


		@Override
		public int rePrint(String traceNo, String transTime)
				throws RemoteException {
			if (isRightfulCalling()) {
				// TODO 查询流水，重打印
				//printVouchers
				
				if(!StringUtils.isDigital(traceNo) ){
					return -8;
				}
				return printVouchers(traceNo,transTime);
			}
			return -8;
		}

		/**
		 * 判断调用者是否合法
		 * @return
		 */
		private boolean isRightfulCalling(){
			String callingApp = context.getPackageManager().
					getNameForUid(Binder.getCallingUid());
			LoggerUtils.d("lxb callingApp:" + callingApp);
			return true;
		}
		private boolean isRightfulCalling(String key, String value){
			String callingApp = context.getPackageManager().
					getNameForUid(Binder.getCallingUid());
			LoggerUtils.d("lxb callingApp:" + key+ "  "+value);
			return true;
		}

		/**
		 * 完整的打印签购单方法
		 * @param
		 * @param
		 */
		private int printVouchers(String traceNo,String transTime)
		{
			final Water water ;
			WaterServiceImpl impl = new WaterServiceImpl(context);
			//water = impl.findByTraceBatch(traceNo,transTime);	
			water = impl.findByTrace(traceNo);
			
			if(water == null){
				return 8;
			}

			PrintUtils.load(context, Const.FileConst.PRINT);
			result = printSingleVoucher(context, water,PrintWaterType.CUSTOMER);
	
			return result;
		}
		/**
		 * 打印签购单
		 * @param
		 * @param data
		 */
		private int printVoucher(final String data) {
			final PrintModule printModule = PrintModule.getInstance();
			PRNSTATUS status = printModule.getPrinterStatus();
			int result = 0;
			if (status == PRNSTATUS.PRN_STATUS_OK) {
				result = printModule.printScript(data);
				unLockData(data, false);
		        if(result != 0){
		        	return  -7;
		        }	
			   return 0;     
			} else {
				unLockData(data, false);
				return 0-status.ordinal();
			}
		}
		
		 
		/**
		 * 打印单条签购单
		 * @param context
		 * @param water
		 * @param type
		 */
		private int printSingleVoucher(Context context,
				Water water, PrintWaterType type) {
			boolean isRprint = true;	
			
			// 设备开始打印
			final String printCmdData = getPrintWaterData(water, type, isRprint);
			new Thread(){
				public void run(){
					result = printVoucher(printCmdData);
				}
			}.start();
			
			lockData(printCmdData); // 锁住线程等待，打印结果
			return result;
		}
		
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
//					result = printResult;
					data.notify();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public int findWater(SimpleWater water, String voucherNo, String transTime) throws RemoteException {
			
			if (voucherNo==null) {
				return -1;
			}
			String time = transTime.substring(8);
			LoggerUtils.d("voucherNo = "+voucherNo+", time = "+time);
			WaterService waterService = new WaterServiceImpl(context);
			Water water2=waterService.findByTrace(voucherNo, time);
			if (water2==null) {
				LoggerUtils.d("lxb 未找到对应流水号:" + voucherNo);
				return 0;
			} else {
				LoggerUtils.d("lxb water2 pan = "+water2.getPan());
			}
			SimpleWater.waterToSimpleWater(water2, water);
			
			return 1;
		}

		@Override
		public int findWaterByOutOrderNo(SimpleWater water, String outOrderNo)
				throws RemoteException {
			// TODO Auto-generated method stub
			if (outOrderNo==null) {
				return -1;
			}

			WaterService waterService = new WaterServiceImpl(context);
			Water water2=waterService.findByOutOrderNo(outOrderNo);
			if (water2==null) {
				LoggerUtils.d("lxb 未找到对外部订单号:" + outOrderNo);
				return 0;
			} else {
				LoggerUtils.d("lxb water2 pan = "+water2.getPan());
			}
			SimpleWater.waterToSimpleWater(water2, water);
			
			return 1;
		}

		@Override
		public int rePrintSettle() throws RemoteException {
			// TODO Auto-generated method stub
			LoggerUtils.d("lxb Into rePrintSettle");
			/**
			 * 装载assetLOGO图片资源
			 */
			{
				File fileLogoLarge = FileUtils.createFile(Const.PathConst.APPS_DATA,
						Const.FileConst.LOGO_IMG_LARGE);
				try {
					AssetManager am = context.getAssets();
					InputStream isLogoLarge = am.open(Const.FileConst.LOGO_IMG_LARGE);
					if (isLogoLarge != null) {
						FileUtils.copyFileByInputStream(isLogoLarge, fileLogoLarge);				
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
				
			
			// 设备开始打印
			LoggerUtils.d("lxb 设备开始打印111");

			String printData = ParamsUtils.getString(ParamsConst.PARAMS_OLD_SETTLEMENT);
			
			LoggerUtils.d("lxb 设备开始打印222:" + printData);
			
			if (printData != null) {
				return printVoucher(printData);
			}
			else {
				return 8;
			}
			
		}
	
	}
}
