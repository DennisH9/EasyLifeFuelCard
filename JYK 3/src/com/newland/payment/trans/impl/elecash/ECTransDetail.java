package com.newland.payment.trans.impl.elecash;

import java.util.List;

import com.newland.base.util.ParamsUtils;
import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.pos.sdk.bean.PbocDetailBean;
import com.newland.pos.sdk.emv.EmvCoreOperator.ECSelectMode;

/**
 * 明细查询
 * 
 * @version 1.0
 * @author spy
 * @date 2015年5月22日
 * @time 下午2:49:29
 */
public final class ECTransDetail extends AbstractBaseTrans {
	List<PbocDetailBean> pbocDetails = null;

	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int SWIP_CARD = 3;
		public final static int EMV_SHOW_LOG = 4;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	// 检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	protected void checkPower() {
		super.checkPower();
		checkIsExistWater = false;
		checkSettlementStatus = false;
		checkWaterCount = false;
	}

	@Override
	protected int init() {
		int res = super.init();
		// 设置交易类型,交易属性
		//pubBean.setTransType(TransType.TRANS_BALANCE);
		pubBean.setTransName(getText(R.string.check_detail));
		return res;
	}

	@Override
	protected void release() {
		pbocDetails = null;
		super.release();
	}

	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_TransStart(){

		if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_IC)) {
			showToast(getText(R.string.card_unsupport_ic_card));
			return FINISH;
		}
		
		return Step.SWIP_CARD;
	}

	/** 刷卡*/
	@AnnStep(stepIndex = Step.SWIP_CARD)
	public int step_SwipCard() {
		
			boolean result = false;
			result = stepProvider.swipCard(pubBean, ReadcardType.ICCARD | ReadcardType.RFCARD);
			
			if(!result){
				return FINISH;
			}

		switch (pubBean.getCardInputMode()) {
		case ReadcardType.ICCARD: // 接触式读卡
		case ReadcardType.RFCARD: // 非接式读卡
			return Step.EMV_SHOW_LOG;
			
		default:
			return FINISH;
		}
	}
	


	@AnnStep(stepIndex =  Step.EMV_SHOW_LOG)
	public int step_ShowPbocLog(){
		
		EmvApplication emvApp = new EmvApplication(this);
		pubBean.setEcSelectMode(ECSelectMode.PATH_EC);
		pbocDetails = emvApp.emvGetPbocLog(pubBean);
		
		if(pbocDetails == null ){
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.trans_fail));
			return Step.TRANS_RESULT;
		}
		if( pbocDetails.size() == 0){
			transResultBean.setIsSucess(true);
			transResultBean.setContent(getText(R.string.record_not_exist));
			return Step.TRANS_RESULT;
		}
		
		CommonBean<List<PbocDetailBean>> comBean = new CommonBean<List<PbocDetailBean>>();
		comBean.setTitle(pubBean.getTransName());
		comBean.setValue(pbocDetails);
		
		showUIPbocLog(comBean);
		
		switch (comBean.getStepResult()) {
		default:
			return SUCC;
		}
	}
	
	/** 结果显示*/
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult() {
		transResultBean.setTitle(pubBean.getTransName());
		transResultBean = showUITransResult(transResultBean);
		if(transResultBean.getIsSucess()){
			return SUCC;
		}
		return FINISH;
	}
}
