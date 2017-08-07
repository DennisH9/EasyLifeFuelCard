package com.newland.payment.trans.impl.elecash;
import com.newland.base.util.ParamsUtils;
import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.pos.sdk.common.EmvConst;
import com.newland.pos.sdk.emv.EmvModule;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;


/**
 * 余额查询
 * @version 1.0
 * @author spy
 * @date 2015年5月22日
 * @time 下午2:49:29
 */
public final class ECBalanceQuery extends AbstractBaseTrans {
	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int SWIP_CARD = 3;
		public final static int EMV_INIT = 4;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	//检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	protected void checkPower(){
		super.checkPower();
		checkIsExistWater = false;
		checkSettlementStatus = false;
		checkWaterCount = false;
		
	}
	
	@Override
	protected int init() {
		int res = super.init();
		//设置交易类型,交易属性
		pubBean.setTransType(TransType.TRANS_EC_BALANCE);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		pubBean.setAmount(0L);//RF卡 startEmvRF 需要金额
		return res;
	}
	
	
	@Override
	protected void release() {
		super.release();
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_IC)){
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
			return Step.EMV_INIT;
			
		default:
			return FINISH;
		}

	}
	
	@AnnStep(stepIndex = Step.EMV_INIT)
	public int step_EmvInit() {
		EmvApplication emvApp =  new EmvApplication(this);
		EmvModule emvModule = emvApp.getEmvAppModule();
		
		int emvResult = 0;
		LoggerUtils.d("111 pubBean:" + pubBean);
		LoggerUtils.d("111 emvApp:" + emvApp);
		try{
			if (pubBean.getCardInputMode() == ReadcardType.ICCARD) {
				pubBean.setTransAttr(com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_PREDIGEST);
			} else {
				pubBean.setTransAttr(com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_PREDIGEST_RF);
			}
			emvResult = emvApp.initEmvApp(pubBean);
		}catch(Exception e){
			e.printStackTrace();
			
		}
		LoggerUtils.d("111 initEmvApp result:" + emvResult);

		
		byte[] balance = null;
		switch(pubBean.getCardInputMode()){
		case ReadcardType.ICCARD:
			if(emvResult == EmvConst.EMV_TRANS_EC_GOON_AMOUNT){
				balance = emvModule.getEmvData(0x9F79);
			}
			break;
		case ReadcardType.RFCARD:
			if(emvResult == EmvConst.EMV_TRANS_RF_GOON_AMOUNT){
				balance = emvModule.getEmvData(0x9F5D);
			}
			break;
		}
		if(balance != null){
			transResultBean.setIsSucess(true);
			
			FormatUtils.formatAmount(BytesUtils.bcdToString(balance));
			
			String amount = getText(R.string.common_elec_cash)
					+ getText(R.string.common_balance)
					+ getText(R.string.common_colon)
					+ FormatUtils.formatAmount(BytesUtils.bcdToString(balance));

			transResultBean.setContent(amount);
			
		} else {
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.trans_fail));
		}
		
		return Step.TRANS_RESULT;
	}
	
	/**结果显示*/
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult(){
	// 对业务处理结果进行判断
		transResultBean.setTitle(pubBean.getTransName());
		transResultBean = showUITransResult(transResultBean);
		if(transResultBean.getIsSucess()){
			return SUCC;
		}
		return FINISH;
	}
}
