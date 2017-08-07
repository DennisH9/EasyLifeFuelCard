package com.newland.payment.trans.impl;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.field.ISOField60;

public class LogOut extends AbstractBaseTrans{

	private class Step {
		public final static int TRANS_START = 1;
		public final static int PACK_AND_COMM = 2;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	private CommonBean<Integer> commonBean = null;
	
	public LogOut(){
		commonBean = null;
	}
	
	public LogOut(CommonBean<Integer> commonBean){
		this.commonBean = commonBean;
		
	}
	
	@Override
	protected void checkPower(){
		super.checkPower();
		checkIsExistWater = true;
		transcationManagerFlag = false; // 不开启事务
	}
	
	@Override
	protected void release() {
		if (commonBean == null){
			super.release();
		}
	}
	
	@Override
	protected int init() {
		int res = super.init();
		transResultBean.setIsSucess(false);
			
		pubBean.setTransType(TransType.TRANS_LOGOUT);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		return res;
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		return Step.PACK_AND_COMM;
	}


	@AnnStep(stepIndex = Step.PACK_AND_COMM)
	public int step_PackAndComm() {
		
		initPubBean();
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		pubBean.setIsoField60(isoField60);
		
		iso8583.initPack();
		iso8583.setField(0, pubBean.getMessageID());
		iso8583.setField(11, pubBean.getTraceNo());
		iso8583.setField(41, pubBean.getPosID());
		iso8583.setField(42, pubBean.getShopID());
		iso8583.setField(60, pubBean.getIsoField60().getString());


		int resCode = dealPackAndComm(false, false, true);
		if (STEP_CONTINUE != resCode){
			//连接出错
			if (STEP_FINISH == resCode){
				return Step.TRANS_RESULT;
			}
			return resCode;
		}
		transResultBean.setIsSucess(true);
		transResultBean.setContent(getText(R.string.common_sign_out_susc));
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_FLAG_SIGN, false);
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
