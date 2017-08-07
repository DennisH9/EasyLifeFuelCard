package com.newland.payment.trans.impl;

import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.emv.EmvModule;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 操作员积分签到
 * @author lst
 * @date 20150522
 */
public class BonusLogin extends AbstractBaseTrans{
	
	private EmvModule emvModule;
	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int SWIP_CARD = 2;
		public final static int EMV_SIMPLE_PROCESS = 3;
		public final static int PACK_AND_COMM = 4;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	@Override
	protected void checkPower(){
		super.checkPower();
		checkSingIn = false; // 不检查签到状态
		checkWaterCount = false; // 不检查流水累计笔数上限
		checkSettlementStatus = false; // 不检查结算状态
		transcationManagerFlag = false; // 不开启事务
	}
	
	@Override
	protected int init() {
		int res = super.init();	
		transResultBean.setIsSucess(false);
		pubBean.setTransType(TransType.TRANS_CASHIER_LOGIN);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
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

	@AnnStep (stepIndex = Step.SWIP_CARD)
	public int step_SwipCard() {
		
		boolean result = stepProvider.swipCard(pubBean,ReadcardType.ICCARD
				| ReadcardType.RFCARD 
				| ReadcardType.SWIPE
				| ReadcardType.HAND_INPUT);
		
		if(!result){
			return FINISH;
		}

		switch (pubBean.getCardInputMode()) {
		case ReadcardType.SWIPE: // 磁条卡
		case ReadcardType.HAND_INPUT: // 手输卡号
			return Step.PACK_AND_COMM;
		
		case ReadcardType.ICCARD: // 接触式读卡
		case ReadcardType.RFCARD: // 非接式读卡
			return Step.EMV_SIMPLE_PROCESS;
		
		default:
			return FINISH;
		}
	}
	
	@AnnStep(stepIndex = Step.EMV_SIMPLE_PROCESS)
	public int step_EmvSimpleProcess() {
		
		boolean result = stepProvider.emvSimpleProcess(pubBean, new EmvApplication(this));
		
		if(!result){
			if(pubBean.isFallBack()){
				return Step.SWIP_CARD;
			}
			return FINISH;
		}
		//IC卡磁道加密处理,磁条卡磁道加密在K21完成
		pubBean.setTrackData2(dealTrack(pubBean.getCardInputMode(), pubBean.getTrackData2()));
		return Step.PACK_AND_COMM;
	}

	/** 组包 */
	@AnnStep(stepIndex = Step.PACK_AND_COMM)
	public int step_PackAndComm() {
		
		initPubBean();
		if ( StringUtils.isEmpty(pubBean.getPinBlock()) || 
				"0000000000000000".equals(pubBean.getPinBlock()) ){
			pubBean.setInputMode(pubBean.getInputMode()+"2");
		} else {
			pubBean.setInputMode(pubBean.getInputMode()+"1");
		}
		pubBean.setSecctrlInfo(getSecctrlInfo(pubBean));
		
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		pubBean.setIsoField60(isoField60);
		
		iso8583.initPack();
		
		iso8583.setField(0, pubBean.getMessageID());
		/** 刷卡交易不上送主帐号 */
		if (pubBean.getCardInputMode()!=ReadcardType.SWIPE) {
			iso8583.setField(2, pubBean.getPan());
		}
		if (!StringUtils.isEmpty(pubBean.getExpDate())) {
			iso8583.setField(14, pubBean.getExpDate());
		}
		iso8583.setField(22, pubBean.getInputMode());
		if (!StringUtils.isEmpty(pubBean.getTrackData2())) {
			iso8583.setField(35, pubBean.getTrackData2());
		}
		if (!StringUtils.isEmpty(pubBean.getTrackData3())) {
			iso8583.setField(36, pubBean.getTrackData3());
		}
		iso8583.setField(41, pubBean.getPosID());
		iso8583.setField(42, pubBean.getShopID());
		if ('1' == pubBean.getInputMode().charAt(2)
				|| !StringUtils.isEmpty(pubBean.getTrackData2())
				|| !StringUtils.isEmpty(pubBean.getTrackData3())) {
			iso8583.setField(53, pubBean.getSecctrlInfo());
		}
		iso8583.setField(60, pubBean.getIsoField60().getString());
		
		int resCode = dealPackAndComm(false, false, true);
		if (STEP_CONTINUE != resCode){
			transResultBean.setIsSucess(false);
			//连接出错
			if (STEP_FINISH == resCode){
				return TransConst.STEP_FINAL;
			}
			return resCode;
		}else {
			transResultBean.setIsSucess(true);
			transResultBean.setContent(getText(R.string.common_operator_sign_in_score_sucs));
		}
		
		return Step.TRANS_RESULT;
	}
	
	
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult(){
		transResultBean.setTitle(pubBean.getTransName());
		transResultBean = showUITransResult(transResultBean);
		return FINISH;
	}
}
