package com.newland.payment.trans.manage.impl;

import java.io.UnsupportedEncodingException;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.service.impl.CardBinCServiceImpl;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

public class CardBInCUpdate extends AbstractBaseTrans{

	private String field62 = null;
	private int count = 0;
	private CardBinCServiceImpl cardBinService;
	
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
		cardBinService = new CardBinCServiceImpl(activity);
		transResultBean.setIsSucess(false);
		
		//设置交易类型,交易属性
		pubBean.setTransType(TransType.TRANS_PARAM_TRANSFER);
		pubBean.setTransName(getText(R.string.setting_other_download_bin_c_update));
		return res;
	}
	

	@Override
	protected void release() {
		super.release();
	}

	protected String[] getCommunitionTipMsg() {
		return new String[]{getText(R.string.setting_downloading_bin_c)};
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		return Step.SEND_STATUS;
	}
	
	
	@AnnStep(stepIndex = Step.SEND_STATUS)
	public int step_statusSend() throws NumberFormatException, UnsupportedEncodingException {
		
		initPubBean();
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		isoField60.setNetManCode("398");
		pubBean.setIsoField60(isoField60);

		StringBuffer sbCardbin = new StringBuffer();
		String falg;
		boolean downFlag = true;
		while(downFlag) {
			String countStr = String.format("%03d", count);
			LoggerUtils.i("countStr: " + countStr);
			pubBean.setIsoField62(countStr);
			
			iso8583.initPack();
			iso8583.setField(0, pubBean.getMessageID());
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			iso8583.setField(60, pubBean.getIsoField60().getString());
			iso8583.setField(62, pubBean.getIsoField62());
			
			int resCode = dealPackAndComm(false, false, true);
			if (STEP_CONTINUE != resCode){
				LoggerUtils.d("STEP_CONTINUE:" + STEP_CONTINUE);
				return resCode;
			}
				
			field62 = iso8583.getField(62);
			LoggerUtils.d("field62: " + field62);
			falg = field62.substring(0, 2);
			if(falg.equals("31") || falg.equals("32")){
				sbCardbin.append(field62.substring(8));
			}
			
			if (falg.equals("31")){
				downFlag = false;	
			} else if(falg.equals("32")){
				downFlag = true;
			} else { //其他情况认为是成功的
				cardBinService.deleteAll();
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CARDBIN_C_DOWN, false);
				transResultBean.setIsSucess(true);
				transResultBean.setContent(getText(R.string.result_blackList_download_succse));
				return Step.TRANS_RESULT;
			}
			count = Integer.valueOf(StringUtils.hexToStr(field62.substring(2, 8))) + 1;
			
			if(falg.equals("31")){
				cardBinService.deleteAll();
				if(!SaveCardBin(sbCardbin.toString())){
					showToast(getText(R.string.result_bin_c_update_fail));
					return FINISH;
				}
			}			
		}
		return Step.SEND_END_PRAM;
	}

	private boolean SaveCardBin(String field62) throws UnsupportedEncodingException {
		
		String strField = StringUtils.hexToStr(field62);
		LoggerUtils.i("strField: " + strField);
		int currentIndex = 0;
		String cardBin;
		int dataLen;
		
		dataLen = strField.length();
		if(dataLen % 8 != 0){
			return false;
		}
		
		for(int i = 0; i < dataLen / 8; i++){
			cardBin = strField.substring(currentIndex, currentIndex+8);
			currentIndex += 8;
			if(-1 == cardBinService.addCardBin(cardBin)){
				return false;
			}
		}
		return true;
	}
	
	@AnnStep(stepIndex = Step.SEND_END_PRAM)
	public int step_sendEndParam() {
		//pubBean.setTransType(TransType.TRANS_PARAM_TRANSFER);
		initPubBean();
		
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		isoField60.setNetManCode("399");
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
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CARDBIN_C_DOWN, false);
		transResultBean.setIsSucess(true);
		transResultBean.setContent(getText(R.string.result_bin_c_update_succse));
		
		return Step.TRANS_RESULT;
	}
	
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult(){
		LoggerUtils.d("count:" + count);
		transResultBean.setTitle(pubBean.getTransName());
		//transResultBean = showUITransResult(transResultBean);
		showToast(transResultBean.getContent());
		if(transResultBean.getIsSucess()){
			return SUCC;
		}
		return FINISH;
	}

}
