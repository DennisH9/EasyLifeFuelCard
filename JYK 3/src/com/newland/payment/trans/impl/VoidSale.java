package com.newland.payment.trans.impl;

import com.newland.base.util.ParamsUtils;
import com.newland.emv.EmvAppModule;
import com.newland.emv.EmvApplication;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransConst.TransStatus;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.PubBean.EmvTransResult;
import com.newland.payment.trans.bean.ThirdInvokeBean;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.pos.sdk.bean.MessageTipBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

public class VoidSale extends AbstractBaseTrans {

	private Water oldWater = null;
	
	private boolean isSwipeCard = false;

	private boolean isInputPin = false;
	
	private ThirdInvokeBean thirdInvokeBean = null;
	private class Step {
		public final static int TRANS_START = 1;
		public final static int INPUT_TRACENO = 2;
		public final static int SWIP_CARD = 3;
		public final static int EMV_SIMPLE_PROCESS = 4;
		public final static int INPUT_PIN = 5;
		public final static int PRE_ONLINE_PROCESS = 6;
		public final static int PACK_AND_COMM = 7;
		public final static int APPEND_WATER = 9;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}

	public VoidSale(ThirdInvokeBean thirdInvokeBean) {
		this(TransType.TRANS_VOID_SALE);
		this.thirdInvokeBean = thirdInvokeBean;
	}

	public VoidSale(int transType){
		
	}	
	
	public VoidSale() {
		// TODO Auto-generated constructor stub
	}

	// 检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	protected void checkPower() {
		super.checkPower();
		checkManagerPassword = false;
	}

	@Override
	protected int init() {
		int res = super.init();
		// 设置交易类型,交易属性
		pubBean.setTransType(TransType.TRANS_VOID_SALE);
		pubBean.setTransName(getTitle(pubBean.getTransType()));

		if (ParamsUtils.getBoolean(
				ParamsConst.PARAMS_KEY_TRANS_VOID_SWIPE)) {
			isSwipeCard = true;
		} else {
			isSwipeCard = false;
		}

		if (ParamsUtils.getBoolean(
				ParamsConst.PARAMS_KEY_IS_INPUT_TRANS_VOID)) {
			isInputPin = true;
		} else {
			isInputPin = false;
		}
		
		if(this.thirdInvokeBean != null){
			LoggerUtils.d("111 thirdinvokeBean:" + thirdInvokeBean.toString());
			
			pubBean.setTraceNo(thirdInvokeBean.getOriVoucherNo());
			
		}
		
		return res;
	}


	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		return Step.INPUT_TRACENO;
	}
	
	/**
	 * 请输入凭证号(流水号)
	 * 
	 * @return
	 */
	@AnnStep(stepIndex = Step.INPUT_TRACENO)
	public int step_InputTraceNo() {
		oldWater = stepProvider.inputOldTraceNo(pubBean, TransType.TRANS_SALE);
		if (oldWater == null){
			return FINISH;
		} else {
			return Step.SWIP_CARD;
		}
	}

	/** 刷卡 */
	@AnnStep(stepIndex = Step.SWIP_CARD)
	public int step_SwipCard() {

		if (!isSwipeCard) {
			pubBean.setInputMode("01");
			pubBean.setPan(oldWater.getPan());
			return Step.INPUT_PIN;
		}
		
		boolean result = false;
		pubBean.setOldAmount(oldWater.getAmount());
		result = stepProvider.swipCard(pubBean, ReadcardType.SWIPE
				| ReadcardType.ICCARD | ReadcardType.RFCARD);

		if (!result) {
			pubBean.setResponseCode("EC");
			pubBean.setMessage("SWIP_CARD异常");
			return FINISH;
		}

		switch (pubBean.getCardInputMode()) {
		case ReadcardType.SWIPE: // 磁条卡
			return Step.INPUT_PIN;
			
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
			pubBean.setResponseCode("EC");
			pubBean.setMessage("EMV_SIMPLE_PROCESS异常");
			return FINISH;
		}
		
		return Step.INPUT_PIN;
	}

	/** 输入密码 */
	@AnnStep(stepIndex = Step.INPUT_PIN)
	public int step_InputPin() {
		if (!isInputPin) {
			pubBean.setPinBlock(null);
			return Step.PRE_ONLINE_PROCESS;
		}
		boolean result = stepProvider.inputPin(pubBean);
		if(!result){
			pubBean.setResponseCode("EC");
			pubBean.setMessage("INPUT_PIN异常");
			return FINISH;
		}
		return Step.PRE_ONLINE_PROCESS;
	}

	/** 脚本上送、冲正 */
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

		LoggerUtils.d("111  p5_PackAndComm");
		/** 数据组织到pubBean中 */
		initPubBean();
		
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		pubBean.setIsoField60(isoField60);

		/** 用pubbean中数据，组8583数据 */
		iso8583.initPack();
		try {
			iso8583.setField(0, pubBean.getMessageID());
			/** 刷卡交易不上送主帐号 */
			if (pubBean.getCardInputMode() != ReadcardType.SWIPE) {
				iso8583.setField(2, pubBean.getPan());
			}
			iso8583.setField(3, pubBean.getProcessCode());
			iso8583.setField(4, pubBean.getAmountField());

			iso8583.setField(11, pubBean.getTraceNo());
			if (!StringUtils.isEmpty(pubBean.getExpDate())) {
				iso8583.setField(14, pubBean.getExpDate());
			}

			iso8583.setField(22, pubBean.getInputMode());
			if (!StringUtils.isEmpty(pubBean.getCardSerialNo())) {
				iso8583.setField(23, pubBean.getCardSerialNo());
			}
			iso8583.setField(25, pubBean.getServerCode());
			if ('1' == pubBean.getInputMode().charAt(2)) {
				iso8583.setField(26, String.format("%02d",12));
			}
			if (!StringUtils.isEmpty(pubBean.getTrackData2())) {
				iso8583.setField(35, pubBean.getTrackData2());
			}
			if (!StringUtils.isEmpty(pubBean.getTrackData3())) {
				iso8583.setField(36, pubBean.getTrackData3());
			}
			iso8583.setField(37, pubBean.getOldRefnum());
			if (!StringUtils.isEmpty(pubBean.getOldAuthCode())) {
				iso8583.setField(38, pubBean.getOldAuthCode());
			}

			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			iso8583.setField(49, pubBean.getCurrency());
			if ('1' == pubBean.getInputMode().charAt(2)) {
				iso8583.setField(52, pubBean.getPinBlock());
			}
			if ('1' == pubBean.getInputMode().charAt(2)
					|| !StringUtils.isEmpty(pubBean.getTrackData2())
					|| !StringUtils.isEmpty(pubBean.getTrackData3())) {
				iso8583.setField(53, pubBean.getSecctrlInfo());
			}

			iso8583.setField(60, pubBean.getIsoField60().getString());
			iso8583.setField(61, pubBean.getIsoField61());
			iso8583.setField(64, "00");

		} catch (Exception e) {
			LoggerUtils.e("组8583包异常");
			e.printStackTrace();
			pubBean.setEmvTransResult(EmvTransResult.FAIL);
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.common_pack));
			return Step.TRANS_RESULT;
		}

		int resCode = dealPackAndComm(true, true, true);

		if (resCode != STEP_CONTINUE) {
			//连接出错
			if (STEP_FINISH == resCode){
				return TransConst.STEP_FINAL;
			}
			return resCode;
		}

		return Step.APPEND_WATER;
	}

	/** 追加流水 */
	@AnnStep(stepIndex = Step.APPEND_WATER)
	public int step_AppendWater() {
		// 对业务处理结果进行判断
		try {
			Water water = getWater(pubBean);
			if (com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_PREDIGEST == water.getTransAttr()
				|| com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_PREDIGEST_RF == water.getTransAttr()){
				EmvAppModule.getInstance().setEmvAdditionInfo(water, null);
			}
			
			addWater(water);
			transResultBean.setWater(water);
			// 删除冲正流水
			
			if(ParamsUtils.getBoolean(ParamsConst.PARAMS_IS_REVERSE_TEST)){
				MessageTipBean tipBean = new MessageTipBean();
				tipBean.setTitle("警告，冲正测试 ！！!");
				tipBean.setContent("(该功能为测试人员使用测试，生产请勿使用)\r\n"
						+ "确认键测试冲正，按取消键继续...");
				tipBean.setCancelable(true);
				showUIMessageTip(tipBean);
				if(tipBean.getResult() == true){
					return FINISH;
				}
			}
			
			reverseWaterService.delete();
			//更新原交易流水
			oldWater.setTransStatus(TransStatus.REV);
			updateWater(oldWater); 
			// 更新结算数据
			changeSettle(pubBean);

		} catch (Exception e) {
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.trans_fail));
		}

		return Step.TRANS_RESULT;
	}

	/** 结果显示、打印处理 */
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult() {
		//启动电子签名
		if (transResultBean.getIsSucess()){
			doElecSign(transResultBean.getWater());
		}	
		transResultBean.setTitle(pubBean.getTransName());
		transResultBean = showUITransResult(transResultBean);

		if(transResultBean.getIsSucess()){
			//电子签名上送,离线上送
			dealSendAfterTrade();
			//TODO 交易结果集上送 ， 上送失败则凭 pos签购单人工处理
//			if (doSendInfo(pubBean) == false) {
//				//showToast("交易上送失败则凭 pos签购单人工处理");
//			} 
			return SUCC;
		}
		return FINISH;
	}
}
