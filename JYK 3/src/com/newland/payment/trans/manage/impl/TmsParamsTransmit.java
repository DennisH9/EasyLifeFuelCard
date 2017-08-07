package com.newland.payment.trans.manage.impl;


import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.content.SharedPreferences;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.payment.trans.bean.field.ISOField62_TmsParams;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 *TMS参数下载
 */
public final class TmsParamsTransmit extends AbstractBaseTrans {
	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int SEND_STATUS = 2;
		public final static int SEND_END_PRAM = 3;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	@Override
	protected void checkPower() {
		super.checkPower();
		checkSingIn = false; // 不检查签到状态
		checkWaterCount = false; // 不检查流水累计笔数上限
		checkSettlementStatus = false; // 不检查结算状态
		checkCardExsit = false;
		transcationManagerFlag = false; // 不开启事务
	}
	
	@Override
	protected int init() {
		int res = super.init();
		transResultBean.setIsSucess(false);
		
		//设置交易类型,交易属性
		pubBean.setTransType(TransType.TRANS_PARAM_TRANSFER);
		pubBean.setTransName(getText(R.string.setting_other_download_tms_parameter));
		return res;
	}
	

	@Override
	protected void release() {
		super.release();
	}

	protected String[] getCommunitionTipMsg() {
		return new String[]{getText(R.string.setting_other_download_tms_parameter_download)};
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		return Step.SEND_STATUS;
	}
	
	/** 组包 
	 * @throws UnsupportedEncodingException 
	 * @throws NumberFormatException */
	@AnnStep(stepIndex = Step.SEND_STATUS)
	public int step_statusSend() throws NumberFormatException, UnsupportedEncodingException {
		
		initPubBean();
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		isoField60.setNetManCode("364");
		pubBean.setIsoField60(isoField60);	
		iso8583.initPack();
		iso8583.setField(0, pubBean.getMessageID());
		iso8583.setField(41, pubBean.getPosID());
		iso8583.setField(42, pubBean.getShopID());
		iso8583.setField(60, pubBean.getIsoField60().getString());
		
		int resCode = dealPackAndComm(false, false, true);
		if (STEP_CONTINUE != resCode){
			return resCode;
		}
		String field62 = iso8583.getField(62);
		SaveParam(field62);
		
		return Step.SEND_END_PRAM;
	}
	
	@AnnStep(stepIndex = Step.SEND_END_PRAM)
	public int step_sendEndParam() {
		
		initPubBean();
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		isoField60.setNetManCode("365");
		pubBean.setIsoField60(isoField60);
		iso8583.initPack();	
		iso8583.setField(0, pubBean.getMessageID());
		iso8583.setField(41, pubBean.getPosID());
		iso8583.setField(42, pubBean.getShopID());	
		iso8583.setField(60, pubBean.getIsoField60().getString());	
		int resCode = dealPackAndComm(false, false, true);
		if (STEP_CONTINUE != resCode){
			LoggerUtils.d("STEP_CONTINUE:" + STEP_CONTINUE);
			return resCode;
		}
		
		//这边说明参数下载完成了
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_TMS_PARAM_DOWN, false);
		transResultBean.setIsSucess(true);
		transResultBean.setContent(getText(R.string.result_tms_param_download_success));
		
		return Step.TRANS_RESULT;
	}
	
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult(){
		transResultBean.setTitle(pubBean.getTransName());
		showToast(transResultBean.getContent());
		if(transResultBean.getIsSucess()){
			return SUCC;
		}
		return FINISH;
	}
	
	private void SaveParam(String field62) throws UnsupportedEncodingException {

		SharedPreferences sharedPreferences= activity.getSharedPreferences("tmsParam", 
				Context.MODE_PRIVATE); 
		SharedPreferences.Editor editor = sharedPreferences.edit(); 
		
		String strField = StringUtils.hexToStr(field62);
		LoggerUtils.i("strField: " + strField);	
		ISOField62_TmsParams TmsParams = new ISOField62_TmsParams(strField);
		editor.putString("平台标识码", TmsParams.getPlatformId());
		editor.putString("下载内容标志", TmsParams.getDownContextSign()); 
		editor.putString("下载任务标识", TmsParams.getDownOccasion()); 
		editor.putString("限制日期", TmsParams.getLimitDate()); 
		editor.putString("应用版本号", TmsParams.getAppVersion()); 
		editor.putString("POS下载时机标志", TmsParams.getDownOccasion()); 
		editor.putString("TMS1电话号码之1", TmsParams.getTelephone1()); 
		editor.putString("TMS2电话号码之2", TmsParams.getTelephone2()); 
		editor.putString("TMS1的IP1和端口号1", TmsParams.getIpAndPort1()); 	
		editor.putString("TMS2的IP2和端口号2", TmsParams.getIpAndPort2()); 	
		editor.putString("TMS的GPRS参数", TmsParams.getGprsParam()); 
		editor.putString("TMS的CDMA接入方式", TmsParams.getCdmaParam()); 
		editor.putString("下载任务校验码", TmsParams.getCheckCode()); 
		editor.putString("自动重拨间隔时间", TmsParams.getRedialTime()); 
		editor.putString("任务提示信息", TmsParams.getTaskInfo()); 
		editor.putString("TPDU", TmsParams.getTpdu()); 
		editor.putString("下载开始日期时间", TmsParams.getDownStartDate()); 
		editor.putString("下载结束日期时间", TmsParams.getDownEndDate()); 
		editor.commit(); 
/*		
		SharedPreferences sharedPreferences = activity.getSharedPreferences("tmsParam", 
				Context.MODE_PRIVATE); 
		String name = sharedPreferences.getString("name", ""); 
		String habit = sharedPreferences.getString("habit", ""); 
		LoggerUtils.i("name: " + name);
		LoggerUtils.i("habit: " + habit);*/	
	}
}
