package com.newland.payment.trans.impl.elecash;

import com.newland.base.util.ParamsUtils;
import com.newland.emv.EmvAppModule;
import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.InputInfoBean;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 脱机退货
 * @version 1.0
 * @author spy
 * @date 2015年5月16日
 * @time 上午11:08:12
 */
public final class ECRefund extends AbstractBaseTrans {
	
	
	private String traceNo = null;
	private String batchNo = null;
	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int SWIP_CARD = 2;
		public final static int EMV_SIMPLE_PROCESS = 3;
		public final static int INPUT_AMOUNT = 6;
		public final static int PRE_ONLINE_PROCESS = 7;
		public final static int PACK_AND_COMM = 8;
		public final static int APPEND_WATER = 9;
		
		public final static int INPUT_OLD_TER_NO = 10;
		public final static int INPUT_OLD_TRACE_NO = 11;
		public final static int INPUT_OLD_BATCH_NO = 12;
		public final static int INPUT_OLD_TRANS_DATE = 13;
		
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	public ECRefund(){
	}
	
	
	// 检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	@Override
	protected void checkPower() {
		super.checkPower();
		checkManagerPassword = true;
	}

	@Override
	protected int init() {
		int res = super.init();
		transResultBean.setIsSucess(false);
		pubBean.setTransType(TransType.TRANS_EMV_REFUND);
		pubBean.setTransName(getTitle(pubBean.getTransType()));

		pubBean.setAmount(0L);
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
	
	@AnnStep(stepIndex = Step.SWIP_CARD)
	public int step_SwipCard() {
		
		boolean result = false;

		result = stepProvider.swipCard(pubBean,  ReadcardType.ICCARD | ReadcardType.RFCARD);
		
		if(!result){
			return FINISH;
		}

		switch (pubBean.getCardInputMode()) {
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
			return FINISH;
		}
		
		return Step.INPUT_OLD_TER_NO;
	}
	
	/**
	 * 输入原终端号
	 * @return
	 */
	@AnnStep(stepIndex = Step.INPUT_OLD_TER_NO)
	public int step_InputOldTerminalNo(){
		InputInfoBean infoBean = new InputInfoBean();
		infoBean.setTitle(pubBean.getTransName());
		infoBean.setContent(getText(R.string.common_pls_input) + getText(R.string.common_old_ter_no));
		infoBean.setShortContent(getText(R.string.common_old_ter_no));
		infoBean.setMode(InputInfoBean.INPUT_MODE_ASCII);
		infoBean.setMaxLen(8);
		infoBean.setMinLen(8);
		infoBean.setTimeOut(60);
		infoBean.setEmptyFlag(false);
		
		infoBean = showUIInputInfo(infoBean);
		
		switch(infoBean.getStepResult()){
			
		case SUCCESS:
			//设置原终端号
			pubBean.setOldTerminalId(infoBean.getResult());
			pubBean.setIsoField62(infoBean.getResult());
			break;
			
		case BACK:
		case TIME_OUT:
		case FAIL:
		default:
			return FINISH;
		}
		
		return Step.INPUT_OLD_TRACE_NO;
	}
	
	/**
	 * 请输入凭证号(流水号)
	 * @return
	 */
	@AnnStep(stepIndex = Step.INPUT_OLD_TRACE_NO)
	public int step_InputOldTraceNo(){
		InputInfoBean infoBean = new InputInfoBean();
		infoBean.setTitle(pubBean.getTransName());
		infoBean.setContent(getText(R.string.common_pls_input) + getText(R.string.common_old_trace_no));
		infoBean.setShortContent(getText(R.string.common_old_trace_no));
		infoBean.setMode(InputInfoBean.INPUT_MODE_NUMBER);
		infoBean.setMaxLen(6);
		infoBean.setMinLen(1);
		infoBean = showUIInputInfo(infoBean);
		
		switch(infoBean.getStepResult()){
		case SUCCESS:
			//设置原凭证号
			traceNo = StringUtils.paddingString(infoBean.getResult(), 6, "0", 0);
			break;

		case BACK:
		case TIME_OUT:
		case FAIL:
		default:
			return FINISH;
		}
		return Step.INPUT_OLD_BATCH_NO;
	}
	
	/**
	 * 请输入原批次号
	 * @return
	 */
	@AnnStep(stepIndex = Step.INPUT_OLD_BATCH_NO)
	public int step_InputOldBatchNo(){
		InputInfoBean infoBean = new InputInfoBean();
		infoBean.setTitle(pubBean.getTransName());
		infoBean.setContent(getText(R.string.common_pls_input) + getText(R.string.common_old_batch_no));
		infoBean.setShortContent(getText(R.string.common_old_batch_no));
		infoBean.setMode(InputInfoBean.INPUT_MODE_NUMBER);
		infoBean.setMaxLen(6);
		infoBean.setMinLen(1);
		infoBean = showUIInputInfo(infoBean);
		
		switch(infoBean.getStepResult()){
		case SUCCESS:
			batchNo = StringUtils.paddingString(infoBean.getResult(), 6, "0", 0);
			break;

		case BACK:
		case TIME_OUT:
		case FAIL:
		default:
			return FINISH;
		}
		return Step.INPUT_OLD_TRANS_DATE;
	}
	
	/**
	 * 日期输入
	 * @return
	 */
	@AnnStep(stepIndex = Step.INPUT_OLD_TRANS_DATE)
	public int step_InputOldTransDate(){
		boolean result = stepProvider.inputOldTransDate(pubBean,false);
		if (!result) {
			return FINISH;
		}
		return Step.INPUT_AMOUNT;
	}
	
	/**
	 * 输入金额
	 * @return
	 */
	@AnnStep(stepIndex = Step.INPUT_AMOUNT)
	public int step_InputAmount(){
		boolean result = stepProvider.inputAmount(pubBean, true, true);
		if(!result){
			return FINISH;
		} /*else {
			//金额确认
			if (!confirmAmount(pubBean.getTransName(), pubBean.getAmount())){
				return Step.INPUT_AMOUNT;
			}
			//退货最大金额限制检查
			if (!checkRefundAmtLimit(pubBean.getTransName(), pubBean.getAmount())){
				return Step.INPUT_AMOUNT;
			}
		}*/
		return Step.PRE_ONLINE_PROCESS;
	}
	
	/**脚本上送、冲正*/
	@AnnStep(stepIndex = Step.PRE_ONLINE_PROCESS)
	public int step_PreOnlineProcess() {

		boolean result = stepProvider.preOnlineProcess(pubBean);
		if (!result) {
			return FINISH;
		}

		return Step.PACK_AND_COMM;
	}
	
	/** 打包、通讯、解包*/
	@AnnStep(stepIndex = Step.PACK_AND_COMM)
	public int step_PackAndComm() {
		
		/** 采集数据*/
		initPubBean();

		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		pubBean.setIsoField60(isoField60);

		pubBean.setIsoField61(batchNo + traceNo + pubBean.getOldDate());
		pubBean.setIsoField63("000");

		//组包
		packRefund(pubBean, 0);

		int resCode = dealPackAndComm(true, false, true);
		if (STEP_CONTINUE != resCode){
			//连接出错
			if (STEP_FINISH == resCode){
				return Step.TRANS_RESULT;
			}
			return resCode;
		}
		
		return Step.APPEND_WATER;
	}
	
	/** 追加流水*/
	@AnnStep(stepIndex = Step.APPEND_WATER)
	public int step_AppendWater(){
		try {
			Water water = getWater(pubBean);
			EmvAppModule.getInstance().setEmvAdditionInfo(water, null);
			// 保存交易流水
			addWater(water);
			transResultBean.setWater(water);
			// 删除冲正流水
			reverseWaterService.delete();

			// 更新结算数据
			changeSettle(pubBean);

		} catch (Exception e) {
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.trans_fail));
		}
		return Step.TRANS_RESULT;
	}
		
	/** 结果显示,打印*/
	@AnnStep(stepIndex =  Step.TRANS_RESULT)
	public int step_TransResult(){
		//启动电子签名
		if (transResultBean.getIsSucess()){
			doElecSign(transResultBean.getWater());
		}	
		transResultBean.setTitle(pubBean.getTransName());
		transResultBean = showUITransResult(transResultBean);

		if(transResultBean.getIsSucess()){
			//电子签名上送,离线上送
			dealSendAfterTrade();
			return SUCC;
		}
		return FINISH;
	}


}
