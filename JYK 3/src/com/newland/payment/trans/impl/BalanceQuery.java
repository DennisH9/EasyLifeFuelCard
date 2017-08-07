package com.newland.payment.trans.impl;

import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.PubBean.EmvTransResult;
import com.newland.payment.trans.bean.ThirdInvokeBean;
import com.newland.payment.trans.bean.field.Field_54;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.emv.EmvModule;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 余额查询
 * @version 1.0
 * @author spy
 * @date 2015年5月15日
 * @time 下午6:13:20
 */
public final class BalanceQuery extends AbstractBaseTrans {

	private int m_TransType = TransType.TRANS_BALANCE;
	
	private EmvApplication emvApp;
	
	private EmvModule emvModule;
	

	private class Step {
		public final static int TRANS_START = 1;
		public final static int SWIP_CARD = 2;
		public final static int EMV_INIT = 3;
		public final static int EMV_PROCESS = 4;
		public final static int INPUT_PIN = 5;
		public final static int PRE_ONLINE_PROCESS = 6;
		public final static int PACK_AND_COMM = 7;
		public final static int EMV_COMPLETE = 8;
		public final static int DISPLAY_BALANCE = 9;
		public final static int EMV_INIT_RF = 10;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	public BalanceQuery(int transType){
		m_TransType = transType;
	}
	
	
	public BalanceQuery(ThirdInvokeBean thirdInvokeBean) {
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void checkPower(){
		super.checkPower();
		this.checkWaterCount = false;
	}
	
	@Override
	protected int init() {
		int res = super.init();	
		//设置交易类型,交易属性
		pubBean.setTransType(m_TransType);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		transResultBean.setIsSucess(false);
		transResultBean.setTransType(m_TransType);
		pubBean.setAmount(0L);
		return res;
	}
	
	@Override
	protected void release() {
		if (emvModule!=null) {
			emvModule.emvSuspend(0);
			emvModule.emvRFSuspend(0);
			emvModule = null;
		}
		super.release();
	}

	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		return Step.SWIP_CARD;
	}
	
	@AnnStep(stepIndex = Step.SWIP_CARD)
	public int step_SwipCard() {

		boolean result = false;
		int inputMode = ReadcardType.RFCARD;
		
		result = stepProvider.swipCard(pubBean, inputMode);

		if (!result) {
			return FINISH;
		}

		switch (pubBean.getCardInputMode()) {
		case ReadcardType.SWIPE: // 磁条卡
			return Step.INPUT_PIN;
			
		case ReadcardType.ICCARD: // 接触式读卡
			return Step.EMV_INIT;
		case ReadcardType.RFCARD: // 非接式读卡
			return Step.EMV_INIT_RF;
			
		default:
			return FINISH;
		}
	}
	
	@AnnStep(stepIndex = Step.EMV_INIT_RF)
	public int step_EmvInitRf(){
		emvApp = new EmvApplication(this);
		emvModule = emvApp.getEmvAppModule();
		
		boolean result = stepProvider.emvSimpleProcess(pubBean, emvApp);
		
		if(!result){
			return FINISH;
		}
		pubBean.setIsoField55(emvModule.packField55(false));
		
		return Step.INPUT_PIN;
		
	}
	
	@AnnStep(stepIndex = Step.EMV_INIT)
	public int step_EmvInit(){
		emvApp = new EmvApplication(this);
		emvModule = emvApp.getEmvAppModule();

		boolean result = stepProvider.emvInit(pubBean, emvApp);

		if (!result) {
			if (pubBean.isFallBack()) {
				return Step.SWIP_CARD;
			}
			return FINISH;
		}

		return Step.EMV_PROCESS;
		
	}

	@AnnStep(stepIndex = Step.EMV_PROCESS)
	public int step_EmvProcess(){
		int emvResult = emvApp.processApp(pubBean);
		if(emvResult != 0) {
			return FINISH;
		}
		
		
		
		pubBean.setIsoField55(emvModule.packField55(false));
		
		if (emvApp.checkInputPin())
		{
			//跳过PIN输密
			return Step.PRE_ONLINE_PROCESS;
		} else {
			
			return Step.INPUT_PIN;	
		}
	}
	
	@AnnStep(stepIndex = Step.INPUT_PIN)
	public int step_InputPin(){
		boolean result = stepProvider.inputPin(pubBean);
		if(!result){
			return FINISH;
		}
		return Step.PRE_ONLINE_PROCESS;
	}	

	
	@AnnStep(stepIndex = Step.PRE_ONLINE_PROCESS)
	public int step_PreOnlineProcess() {

		boolean result = stepProvider.preOnlineProcess(pubBean);
		if (!result) {
			return FINISH;
		}

		return Step.PACK_AND_COMM;
	}
	
	@AnnStep(stepIndex = Step.PACK_AND_COMM)
	public int step_PackAndComm() {
		/** 数据组织到pubBean中 */
		initPubBean();

		pubBean.setSecctrlInfo(getSecctrlInfo(pubBean));
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		pubBean.setIsoField60(isoField60);

		pubBean.setCurrency(TransConst.CURRENCY_CODE);
		
		/** 用pubbean中数据，组8583数据 */
		iso8583.initPack();
		try {
			iso8583.setField(0, pubBean.getMessageID());
			/** 刷卡交易不上送主帐号 */
			if (pubBean.getCardInputMode()!=ReadcardType.SWIPE) {
				iso8583.setField(2, pubBean.getPan());
			}
			iso8583.setField(3, pubBean.getProcessCode());
			
			iso8583.setField(11, pubBean.getTraceNo());
			if (!StringUtils.isEmpty(pubBean.getExpDate())) {
				iso8583.setField(14, pubBean.getExpDate());
			}
			
			iso8583.setField(22, pubBean.getInputMode());
			if (!StringUtils.isEmpty(pubBean.getCardSerialNo())) {
				iso8583.setField(23, pubBean.getCardSerialNo());
			}
			iso8583.setField(25, pubBean.getServerCode());
			if ('1' == pubBean.getInputMode().charAt(2)){
				iso8583.setField(26, String.format("%02d", 12));
			}
			if (!StringUtils.isEmpty(pubBean.getTrackData2())) {
				iso8583.setField(35, pubBean.getTrackData2());
			}
			if (!StringUtils.isEmpty(pubBean.getTrackData3())) {
				iso8583.setField(36, pubBean.getTrackData3());
			}
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			iso8583.setField(49, pubBean.getCurrency());
			if ('1' == pubBean.getInputMode().charAt(2)) {
				iso8583.setField(52, pubBean.getPinBlock());
			}
			if ('1' == pubBean.getInputMode().charAt(2)
					|| !StringUtils.isEmpty(pubBean.getTrackData2())
					|| !StringUtils.isEmpty(pubBean.getTrackData3())){
				iso8583.setField(53, pubBean.getSecctrlInfo());
			}
			if (pubBean.getCardInputMode()==ReadcardType.ICCARD ||
					pubBean.getCardInputMode()==ReadcardType.RFCARD) {
				iso8583.setField(55, pubBean.getIsoField55());
			}
			iso8583.setField(60, pubBean.getIsoField60().getString());
			iso8583.setField(64, "00");

		} catch (Exception e) {
			LoggerUtils.e("组8583包异常");
			e.printStackTrace();
			pubBean.setEmvTransResult(EmvTransResult.FAIL);
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.common_pack));
			return Step.TRANS_RESULT;
		}
		int resCode=0;
		if (pubBean.getCardInputMode()==ReadcardType.ICCARD) {
			resCode = dealPackAndComm(true, false, false);
		} else {
			resCode = dealPackAndComm(true, false, true);
		}
		
		
		if(resCode != STEP_CONTINUE){
			//连接出错
			if (STEP_FINISH == resCode){
				return Step.TRANS_RESULT;
			}
			return resCode;
		}
		
		if (pubBean.getCardInputMode()==ReadcardType.ICCARD) {
			pubBean.setIsoField55(iso8583.getField(55));
			return Step.EMV_COMPLETE;
		} else {
			return Step.DISPLAY_BALANCE;
		}
	}	
	
	
	@AnnStep(stepIndex = Step.EMV_COMPLETE)
	public int step_EmvComplete(){
		//这边参数需要设置么
		int emvResult = emvApp.completeApp(pubBean, true, true);
		if(emvResult == 0){
			return Step.DISPLAY_BALANCE;
		} else {
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.trans_fail));
			return Step.TRANS_RESULT;
		}
	}
	
	@AnnStep(stepIndex = Step.DISPLAY_BALANCE)
	public int step_DisplayBalance(){
			
		try{
			String balance = iso8583.getField(54);
			Field_54 field54 = new Field_54(balance);
			LoggerUtils.d("余额为:" + balance);
			transResultBean.setIsSucess(true);
			//余额处理
			transResultBean.setContent(getText(R.string.balance) + "  " + field54.getBalanceAmouont());
			
		} catch (Exception e) {
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.result_response_exception) + e.getMessage());
		}
		return Step.TRANS_RESULT;
	}
	
	
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult(){
		transResultBean.setTitle(pubBean.getTransName());
		showUITransResult(transResultBean);
		//电子签名上送,离线上送
		dealSendAfterTrade();
		return FINISH;
	}


}
