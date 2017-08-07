package com.newland.payment.trans.manage.impl;

import java.io.UnsupportedEncodingException;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;



public class RfParamsDownload extends AbstractBaseTrans{

	private String field62 = null;
	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int SEND_STATUS = 2;
		public final static int ANALYSIS_PARAM = 3;
		public final static int SEND_END_PRAM = 4;
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
		pubBean.setTransName(getText(R.string.setting_download_rf_params));
		return res;
	}
	

	@Override
	protected void release() {
		super.release();
	}

	protected String[] getCommunitionTipMsg() {
		return new String[]{getText(R.string.setting_downloading_rf_params)};
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		return Step.SEND_STATUS;
	}
	
	/** 组包 */
	@AnnStep(stepIndex = Step.SEND_STATUS)
	public int step_statusSend() throws NumberFormatException, UnsupportedEncodingException {
		
		initPubBean();
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		isoField60.setNetManCode("394");
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
		
		field62 = iso8583.getField(62);
		field62 = StringUtils.hexToStr(field62);
		field62 = field62.substring(2, field62.length());
		if(field62 == null || field62.length() <= 0){
			showToast(getText(R.string.result_blackList_download_fail));
			return FINISH;
		}
		return Step.ANALYSIS_PARAM;
	}

	@AnnStep(stepIndex = Step.ANALYSIS_PARAM)
	public int step_analysis() {
		
		String param[] = field62.split("FF");
		LoggerUtils.d("field62: " + field62);
		String value = "";
		String key ="";
		for(int i = 0; i < param.length; i++){
			value = param[i].substring(7, param[i].length());	
			LoggerUtils.d("value: " + value);
			if(param[i].startsWith("805D")){//非接交易通道开关
				key = ParamsConst.PARAMS_KEY_QPBOC_PRIORITY;			
			}else if(param[i].startsWith("803A")){//闪卡当笔重刷处理时间
				value = value.replaceFirst("^0*", "");
				key = ParamsConst.PARAMS_KEY_FLASH_CARD_RESWIP_TIME_OUT;
			}else if(param[i].startsWith("803C")){//闪卡记录可处理时间
				value = value.replaceFirst("^0*", "");
				key = ParamsConst.PARAMS_KEY_FLASH_CARD_CAN_DEAL_TIME_OUT;
			}else if(param[i].startsWith("8058")){//非接快速业务（QPS）免密限额
				value = value.replaceFirst("^0*", "");
				key = ParamsConst.PARAMS_KEY_QPS_NO_PSW_LIMIT;
			}else if(param[i].startsWith("8054")){//非接快速业务标识
				key = ParamsConst.PARAMS_KEY_QPS_IS_NO_PSW;
			}else if(param[i].startsWith("8055")){//BIN表A标识
				key = ParamsConst.PARAMS_KEY_QPS_CARD_BIN_A;
			}else if(param[i].startsWith("8056")){//BIN表标识
				key = ParamsConst.PARAMS_KEY_QPS_CARD_BIN_B;
			}else if(param[i].startsWith("8057")){//CDCVM标识
				key = ParamsConst.PARAMS_KEY_QPS_IS_CDCVM;
			}else if(param[i].startsWith("8059")){//免签限额
				value = value.replaceFirst("^0*", "");
				key = ParamsConst.PARAMS_KEY_QPS_NO_SINGNATURE_LIMIT;
			}else if(param[i].startsWith("805A")){//免签标识
				key = ParamsConst.PARAMS_KEY_QPS_IS_NO_SINGNATURE;
			}
			ParamsUtils.setString(key,value);	
		}
		return Step.SEND_END_PRAM;
	}
	
	
	
	@AnnStep(stepIndex = Step.SEND_END_PRAM)
	public int step_sendEndParam() {
		//pubBean.setTransType(TransType.TRANS_PARAM_TRANSFER);
		initPubBean();
		
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		isoField60.setNetManCode("395");
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
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_RF_PARAM_DOWN, false);
		transResultBean.setIsSucess(true);
		transResultBean.setContent(getText(R.string.result_rf_param_download_sucess));
		
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

}
