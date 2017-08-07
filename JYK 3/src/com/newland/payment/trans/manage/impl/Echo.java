package com.newland.payment.trans.manage.impl;

import com.newland.payment.R;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.field.ISOField60;

/**
 * 回响测试
 *
 * @author CB
 * @date 2015-5-20 
 * @time 下午4:58:13
 */
public class Echo extends AbstractBaseTrans{

	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int SEND_STATUS = 2;
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
		
		pubBean.setTransType(TransType.TRANS_ECHO);
		pubBean.setTransName(getText(R.string.setting_other_download_echo_test));
		return res;
	}
	
	@Override
	protected String[] getCommunitionTipMsg() {
		return new String[]{getText(R.string.setting_other_download_echo_testing)};
	}

	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		return Step.SEND_STATUS;
	}
	
	/** 组包 */
	@AnnStep(stepIndex = Step.SEND_STATUS)
	public int step_packIsoData() {
		
		try {
			initManagePubBean();
			ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
			pubBean.setIsoField60(isoField60);
			
			iso8583.initPack();
			iso8583.setField(0, pubBean.getMessageID());
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			iso8583.setField(60, pubBean.getIsoField60().getString());		
			int resCode = dealPackAndComm(false, false, false);
			if (resCode != STEP_CONTINUE){
				if (resCode == STEP_FINISH){
					return Step.TRANS_RESULT;
				}
				return resCode;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return FINISH;
		}
		
		transResultBean.setIsSucess(true);
		transResultBean.setContent(getText(R.string.common_test_success));

		return Step.TRANS_RESULT;
	}
	

	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_showResult(){
		
		transResultBean = showUITransResult(transResultBean);
		if(transResultBean.getIsSucess()){
			return SUCC;
		}
		return FINISH;
	}
}
