package com.newland.payment.trans.manage.impl;


import java.io.UnsupportedEncodingException;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.service.BlackCardService;
import com.newland.payment.mvc.service.impl.BlackCardServiceImpl;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 *卡bin黑名单下载
 */
public final class EmvBlackListDownload extends AbstractBaseTrans {

	private String field62 = null;
	private int count = 0;
	private boolean isDelParams = true;
	private BlackCardService blackCardService;
	
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
		blackCardService = new BlackCardServiceImpl(activity);
		transResultBean.setIsSucess(false);
		
		//设置交易类型,交易属性
		pubBean.setTransType(TransType.TRANS_PARAM_TRANSFER);
		pubBean.setTransName(getText(R.string.setting_other_download_blacklist));
		return res;
	}
	

	@Override
	protected void release() {
		super.release();
	}

	protected String[] getCommunitionTipMsg() {
		return new String[]{getText(R.string.setting_other_download_ic_blacklist_download)};
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
		isoField60.setNetManCode("390");
		pubBean.setIsoField60(isoField60);

		boolean downFlag = true;
		while(downFlag) {
			String countStr = String.format("%03d", count);
			//LogUtil.i("countStr: " + countStr);
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
			if(isDelParams) {
				blackCardService.deleteAll();
				isDelParams = false;
			}
			field62 = iso8583.getField(62);
			//field62 = "323030343130313233343536373839303038363232323331353430383935353838303134313031323334353637383930";
			LoggerUtils.d("field62: " + field62);
			if (field62.startsWith("31")){
				downFlag = false;
			} else if(field62.startsWith("32")){
				downFlag = true;
			} else { //其他情况认为是成功的
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_BALACKLIST_DOWN, false);
				transResultBean.setIsSucess(true);
				transResultBean.setContent(getText(R.string.result_blackList_download_succse));
				return Step.TRANS_RESULT;
			}
			count = Integer.valueOf(StringUtils.hexToStr(field62.substring(2, 8))) + 1;
			if(SaveBlackList(field62.substring(8)) == false) {
				showToast(getText(R.string.result_blackList_download_fail));
				return FINISH;
			}
		}
		return Step.SEND_END_PRAM;
	}

	private boolean SaveBlackList(String field62) throws UnsupportedEncodingException {
		String strField = StringUtils.hexToStr(field62);
		LoggerUtils.i("strField: " + strField);
		int cardLen=0;
		int currentIndex = 0;
		String cardBin;
		for(currentIndex = 0; currentIndex < strField.length();) {
			cardLen = Integer.valueOf(strField.substring(currentIndex, currentIndex + 2));
			if(cardLen <=0 || cardLen > 14) {
				return false;
			} else {
				currentIndex += 2;
				cardBin = strField.substring(currentIndex, currentIndex + cardLen);
				if(-1 == blackCardService.addBlackCard(cardBin)) {
					return false; 
				}
				currentIndex += cardLen;
			}
		}
		return true;
	}
	
	@AnnStep(stepIndex = Step.SEND_END_PRAM)
	public int step_sendEndParam() {
		//pubBean.setTransType(TransType.TRANS_PARAM_TRANSFER);
		initPubBean();
		
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		isoField60.setNetManCode("391");
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
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_BALACKLIST_DOWN, false);
		transResultBean.setIsSucess(true);
		transResultBean.setContent(getText(R.string.result_blackList_download_succse));
		
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
