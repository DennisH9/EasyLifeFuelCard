package com.newland.payment.trans.impl;

import com.newland.payment.R;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.TransStatus;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.trans.bean.PubBean.EmvTransResult;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.common.TransConst.StepResult;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

public class VoidPartialSale extends AbstractBaseTrans {

	private Water oldWater = null;
	private CommonBean<PubBean> commonBean = null;
	private class Step {
		public final static int TRANS_START = 1;
		public final static int PACK_AND_COMM = 2;
		public final static int APPEND_WATER = 3;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}

	public VoidPartialSale(CommonBean<PubBean> commonBean) {
		this.commonBean = commonBean;
	}
	
	// 检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	protected void checkPower() {
		super.checkPower();
		//checkManagerPassword = true;
		transcationManagerFlag = false;
	}
	
	@Override
	protected int init() {
		int res = super.init();
		commonBean.setResult(false);
		pubBean = commonBean.getValue();
		// 设置交易类型,交易属性
		if (TransType.TRANS_SALE == pubBean.getTransType()) {
			pubBean.setTransType(TransType.TRANS_VOID_SALE);
		} 
//		else if(TransType.TRANS_BONUS_ALLIANCE == pubBean.getTransType()) {
//			pubBean.setTransType(TransType.TRANS_VOID_BONUS_ALLIANCE);
//		} else if(TransType.TRANS_BONUS_IIS_SALE == pubBean.getTransType()) {
//			pubBean.setTransType(TransType.TRANS_VOID_BONUS_IIS_SALE);
//		}
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		return res;
	}

	@Override
	protected void release() {
		goOnStep(commonBean);
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		return Step.PACK_AND_COMM;
	}

	@AnnStep(stepIndex = Step.PACK_AND_COMM)
	public int step_PackAndComm() {
		
		WaterService waterService = new WaterServiceImpl(MainActivity.getInstance());
		oldWater = waterService.findByTrace(pubBean.getTraceNo());
		// 设置原交易信息到PubBean对象
		pubBean.setAmount(oldWater.getAmount());
		pubBean.setOldRefnum(oldWater.getReferNum());
		if (!StringUtils.isEmpty(oldWater.getAuthCode())) {
			pubBean.setOldAuthCode(oldWater.getAuthCode());
		}
		String isoField61 = oldWater.getBatchNum() + oldWater.getTrace();
		pubBean.setIsoField61(isoField61);
		//不刷卡,不输密
		pubBean.setInputMode("012");
		
		/** 数据组织到pubBean中 */
		initPubBean();	
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		pubBean.setIsoField60(isoField60);
		/** 用pubbean中数据，组8583数据 */
		iso8583.initPack();
		try {
			iso8583.setField(0, pubBean.getMessageID());
				iso8583.setField(2, pubBean.getPan());
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
			iso8583.setField(37, pubBean.getOldRefnum());
			if (StringUtils.isEmpty(pubBean.getOldAuthCode())) {
				iso8583.setField(38, pubBean.getOldAuthCode());
			}
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			iso8583.setField(49, pubBean.getCurrency());

			iso8583.setField(60, pubBean.getIsoField60().getString());
			iso8583.setField(61,pubBean.getIsoField61());
			iso8583.setField(64, "00");

		} catch (Exception e) {
			LoggerUtils.e("组8583包异常");
			e.printStackTrace();
			commonBean.setResult(false);
			commonBean.setStepResult(StepResult.FAIL);
			pubBean.setEmvTransResult(EmvTransResult.FAIL);
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.common_pack));
			return Step.TRANS_RESULT;
		}

		int resCode = dealPackAndComm(true, true, true);

		if (resCode != STEP_CONTINUE) {
			commonBean.setResult(false);
			commonBean.setStepResult(StepResult.FAIL);
			//连接出错
			if (STEP_FINISH == resCode){
				return Step.TRANS_RESULT;
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

			addWater(water);
			transResultBean.setWater(water);
			// 删除冲正流水
			reverseWaterService.delete();
			//更新原交易流水
			oldWater.setTransStatus(TransStatus.REV);
			updateWater(oldWater); 
			// 更新结算数据
			changeSettle(pubBean);
			
			commonBean.setResult(true);
			commonBean.setStepResult(StepResult.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			commonBean.setResult(false);
			commonBean.setStepResult(StepResult.FAIL);
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.trans_fail));
		}

		return Step.TRANS_RESULT;
	}

	/** 结果显示、打印处理 */
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult() {
		transResultBean.setTitle(pubBean.getTransName());
		transResultBean = showUITransResult(transResultBean);
		//离线上送
		doOfflineSend();
		if(transResultBean.getIsSucess()){
			return SUCC;
		}
		return FINISH;
	}
}
