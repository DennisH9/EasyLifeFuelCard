package com.newland.payment.trans.impl;

import android.annotation.SuppressLint;

import com.newland.base.util.ParamsUtils;
import com.newland.base.util.TransUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.Const.ElecSignType;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.AccountStatus;
import com.newland.payment.common.TransConst.EmvStatus;
import com.newland.payment.common.TransConst.SendStatus;
import com.newland.payment.common.TransConst.TransDeal;
import com.newland.payment.common.TransType;
import com.newland.payment.common.tools.AnswerCodeHelper;
import com.newland.payment.mvc.model.EmvFailWater;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.EmvFailWaterService;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.EmvFailWaterServiceImpl;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.PrintBean;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.trans.bean.ThirdInvokeBean;
import com.newland.payment.trans.bean.field.Field48_BatchMagOnline;
import com.newland.payment.trans.bean.field.Field48_Settle;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.payment.trans.bean.field.ISOField62_EmvBatchUp;
import com.newland.pos.sdk.bean.MessageTipBean;
import com.newland.pos.sdk.common.TransConst.TransAttr;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;


/**
 * 结算
 * @version 1.0
 * @author linld
 * @date 2015-05-25
 */
public final class Settle extends AbstractBaseTrans {
	
	private Water water = null;
	private WaterService waterService = null;
	private EmvFailWaterService failWaterService = null;
	private	int maxResend ;
	private int waterSum;
	private int failWaterSum;
	private boolean isFirstFail = true;
	private final int CHECK_CN = 1;
	private final int CHECK_EN = 2;
	private final int CHECK_FAIL = -1;
	private String keyBatchHlat = "";
	private int connectFail = 0; //连接失败次数
	private int step = 0;
	private ISOField60 isoField60 = null;
	private ISOField62_EmvBatchUp isoField62  = null;
	private int batchUpNum  = 0;
	private class Step {
		public final static int TRANS_START = 1;
		public final static int SETTLE = 2;
		public final static int BATCH_UP = 3;
		public final static int PRINT_SETTLE = 4;
		public final static int PRINT_DETAIL = 5;
		public final static int CLEAR_SETTLE = 6;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	private int dealType = TransDeal.NORMAL;
	
	//中断自动结算调用
	public Settle(int dealType){
		this.dealType = dealType;
	}
	
	public Settle(){
		this.dealType = TransDeal.NORMAL;
	}
	public Settle(ThirdInvokeBean thirdInvokeBean) {
		// TODO Auto-generated constructor stub
	}

	//检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	@Override
	protected void checkPower(){
		super.checkPower();
		checkWaterCount = false; // 不检查流水累计笔数上限
		checkSettlementStatus = false; // 不检查结算状态
		transcationManagerFlag = false; // 不开启事务
	}
	
	@Override
	protected int init() {
		int res = super.init();	
		transResultBean.setIsSucess(false);
		transResultBean.setTransType(TransType.TRANS_SETTLE);
		pubBean.setTransType(TransType.TRANS_SETTLE);
		pubBean.setTransAttr(com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_MAG);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		
		waterService = new WaterServiceImpl(activity);
		maxResend = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_REVERSAL_RESEEND_TIMES);
		waterSum = waterService.getWaterCount();
		failWaterService = new EmvFailWaterServiceImpl(activity);
		failWaterSum = failWaterService.getCount();
		return res;
	}
	
	@Override
	protected void release() {
		super.release();
		water = null;
		waterService = null;
	}

	/** 检查结算中断步骤*/
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		if (dealType == TransDeal.SPECIAL){
			MessageTipBean messageTipBean = new MessageTipBean();
			messageTipBean.setTitle("提示");
			messageTipBean.setContent("上次结算未完成,是否继续结算");
			messageTipBean.setCancelable(true);
			messageTipBean.setTimeOut(60);
			messageTipBean = showUIMessageTip(messageTipBean);
			if (!messageTipBean.getResult()){
				return FINISH;
			}
		}
		
		step = Step.SETTLE;
		 //正常结算
		if (!ParamsUtils.getBoolean(ParamsTrans.PARAMS_IS_SETTLT_HALT, false)){
			LoggerUtils.d("dd 正常结算");
			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_MAG_OFFLINE_HALT, 1);
			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_EMV_OFFLINE_SUCC_HALT, 1);
			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_MAG_ONLINE_HALT, 1);
			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_INFORM_HALT, 1);
			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_EMV_ONLINE_SUCC_HALT, 1);
			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_EMV_OFFLINE_FAIL_HALT, 1);
			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_EMV_ONLINE_SUCC_ARPC_ERR_HALT, 1);		

			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, 0);
		}else{ 
		//结算中断
			LoggerUtils.d("dd 结算中断");
			if (ParamsUtils.getBoolean(ParamsTrans.PARAMS_IS_BATCHUP_HALT, false)){
				step = Step.BATCH_UP;
			}else if (ParamsUtils.getBoolean(ParamsTrans.PARAMS_IS_PRINT_SETTLE_HALT, false)){
				step = Step.PRINT_SETTLE;
			}else if (ParamsUtils.getBoolean(ParamsTrans.PARAMS_IS_PRINT_ALLWATER_HALT, false)){
				step = Step.PRINT_DETAIL;
			}else if (ParamsUtils.getBoolean(ParamsTrans.PARAMS_IS_CLEAR_SETTLT_HLAT, false)){
				step = Step.CLEAR_SETTLE;
			}
		}
		
		return step;
	}

	/** 联机结算*/
	@AnnStep(stepIndex = Step.SETTLE)
	public int step_Settle(){
		
		// 脚本执行结果上送
		if(!doScriptAdvise()){
			return FINISH;
		}
		
		// 执行冲正
		if (!doReverseWater()){
			LoggerUtils.e("冲正失败，界面返回主菜单");
			return FINISH;
		}
		
		//离线上送

		if (!doOfflineSend()){
			return FINISH;
		}

		//电子签名上送,(包括未上送及上送失败的签名)
		if (!doElecSignSend(ElecSignType.SETTLE_SEND)){
			LoggerUtils.d("555 结算电子签名上送返回失败");
			return FINISH;
		}
		
		/**
		 * 采集数据
		 */
		initPubBean();
		ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), pubBean.isFallBack());
		pubBean.setIsoField60(isoField60);
		
		Field48_Settle field48_Settle = new Field48_Settle();
		pubBean.setIsoField48(field48_Settle.getString());
		LoggerUtils.d("33333   "+field48_Settle.getString());
		pubBean.setCurrency(TransConst.CURRENCY_CODE);
		
		/**
		 * 组包
		 */
		iso8583.initPack();
		iso8583.setField(0, pubBean.getMessageID());
		iso8583.setField(11, pubBean.getTraceNo());
		iso8583.setField(41, pubBean.getPosID());
		iso8583.setField(42, pubBean.getShopID());
		iso8583.setField(48, pubBean.getIsoField48());
		iso8583.setField(49, pubBean.getCurrency());
		iso8583.setField(60, pubBean.getIsoField60().getString());
		iso8583.setField(63, pubBean.getCurrentOperNo() + " ");
		int resCode = dealPackAndComm(false, false, false);
		if (STEP_CONTINUE != resCode){
			//连接出错
			if (STEP_FINISH == resCode){
				return Step.TRANS_RESULT;
			}
			return resCode;
		}
		
		try{
			/**取对账应答码*/
			String field48 = iso8583.getField(48);
			if (null != field48){
				LoggerUtils.d("111 结算返回48域：[" + field48 + "]");
				String settleCode_N = field48.substring(30, 31);
				LoggerUtils.d("111 内卡对账应答码：" + settleCode_N);
				if (AccountStatus.EQUAL.equals(settleCode_N)
					|| AccountStatus.UNEQUAL.equals(settleCode_N)
					|| AccountStatus.ERROR.equals(settleCode_N)){
					ParamsUtils.setString(ParamsTrans.PARAMS_FLAG_CN_ACCOUNT_CHECKING, settleCode_N);
				}else{
					transResultBean.setIsSucess(false);
					transResultBean.setContent(getText(R.string.settle_error_settlecode_cn));
					return Step.TRANS_RESULT;
				}
				
				String settleCode_W = field48.substring(61, 62);
				LoggerUtils.d("111 外卡对账应答码：" + settleCode_W);
				if (AccountStatus.EQUAL.equals(settleCode_W)
					|| AccountStatus.UNEQUAL.equals(settleCode_W)
					|| AccountStatus.ERROR.equals(settleCode_W)){
					ParamsUtils.setString(ParamsTrans.PARAMS_FLAG_EN_ACCOUNT_CHECKING, settleCode_W);
				}else{
					transResultBean.setIsSucess(false);
					transResultBean.setContent(getText(R.string.settle_error_settlecode_en));
					return Step.TRANS_RESULT;
				}
				
				/**保存结算日期、时间*/
				ParamsUtils.setString(ParamsTrans.PARAMS_SETTLE_TIME, 
										pubBean.getDate()+pubBean.getTime());
				
			}
				
		}catch(Exception e){
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.settle_error) + e.getMessage());
			return Step.TRANS_RESULT;
		}

//		return Step.BATCH_UP;
		//先跳过批上送
		return Step.PRINT_SETTLE;
	}

	/** 批上送*/
	@AnnStep(stepIndex = Step.BATCH_UP)
	public int step_BatchUp(){
		//设置结算中断标志(整个结算的中断标志)
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_SETTLT_HALT, true);
		
		LoggerUtils.d("111 成功流水总笔数:" + waterSum + ", 失败流水总笔数:" + failWaterSum);
		if (waterSum + failWaterSum <= 0){
			return Step.PRINT_SETTLE;
		}
		
		//设置批上送中断
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_BATCHUP_HALT, true);
		batchUpNum = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_BATCHUP, 0);
		
		for (int sendTime = 0; sendTime <= maxResend; sendTime++){
			if (!AccountStatus.EQUAL.equals(ParamsUtils.getString(ParamsTrans.PARAMS_FLAG_CN_ACCOUNT_CHECKING))
				|| !AccountStatus.EQUAL.equals(ParamsUtils.getString(ParamsTrans.PARAMS_FLAG_EN_ACCOUNT_CHECKING))){
				/** 对账不平*/
				//上送磁条卡离线类交易
				if(!batchMagOffline()){
					transResultBean.setIsSucess(false);
					transResultBean.setContent(getText(R.string.comm_connect_fail));
					return Step.TRANS_RESULT;
				}
				//上送EMV成功的脱机交易
				if (!batchEmvOfflineSucc()){
					transResultBean.setIsSucess(false);
					transResultBean.setContent(getText(R.string.comm_connect_fail));
					return Step.TRANS_RESULT;
				}
				//上送磁条卡联机类交易
				if (!batchMagOnline()){
					transResultBean.setIsSucess(false);
					transResultBean.setContent(getText(R.string.comm_connect_fail));
					return Step.TRANS_RESULT;
				}
				//上送通知类交易
				if (!batchInform()){
					transResultBean.setIsSucess(false);
					transResultBean.setContent(getText(R.string.comm_connect_fail));
					return Step.TRANS_RESULT;
				}
			}
			
			/**不管对账平不平都要上送*/
			
			//EMV成功的联机交易
			if (!batchEmvOnlineSucc()){
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.comm_connect_fail));
				return Step.TRANS_RESULT;
			}
			//EMV失败的脱机交易
			if (!batchEmvOfflineFail()){
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.comm_connect_fail));
				return Step.TRANS_RESULT;
			}
			
			//交易承兑,但ARPC错的联机交易
			if (!batchEmvOnlineSuccArpcErr()){
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.comm_connect_fail));
				return Step.TRANS_RESULT;
			}
		}
		
		//批上送结束报文
		batchUpEnd();
			
		//将中断流水与上送笔数重置,修改批上送中断状态
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_BATCHUP_HALT, false);
		
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_MAG_OFFLINE_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_EMV_OFFLINE_SUCC_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_MAG_ONLINE_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_INFORM_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_EMV_ONLINE_SUCC_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_EMV_OFFLINE_FAIL_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_EMV_ONLINE_SUCC_ARPC_ERR_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, 0);
		
		return Step.PRINT_SETTLE;
	}
	
	/** 打印结算单*/
	@AnnStep(stepIndex = Step.PRINT_SETTLE)
	public int step_PrintSettle(){
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_PRINT_SETTLE_HALT, true);
		// 提示正在打印结算单		 
		PrintBean bean = new PrintBean();
		bean.setPrintMessage("打印结算单");
		bean.setPrintType(Const.PrintStyleConstEnum.PRINT_SETTLE);
		 
		bean =  showUIPrintFragment(bean);
		if (!bean.getPrintResult()){
			LoggerUtils.d("打印结算单失败");
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.print_error));
			return Step.TRANS_RESULT;
		}
		
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_PRINT_SETTLE_HALT, true);
		transResultBean.setIsSucess(true);
		transResultBean.setContent(getText(R.string.settle_succ));
		return Step.TRANS_RESULT ;
	}
	
	/** 打印明细,失败明细*/
	@AnnStep(stepIndex = Step.PRINT_DETAIL)
	public int step_PrintAllWater(){
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_PRINT_ALLWATER_HALT, true);
		if (Const.YES.equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_IS_HINT_PRINT_DETAIL))){
			// 提示打印明细
//			MessageTipBean tipBean = new MessageTipBean();
//			tipBean.setTitle(pubBean.getTransName());
//			tipBean.setContent(getText(R.string.settle_print_all_water));
//			tipBean.setCancelable(true);
//			tipBean = showUIMessageTip(tipBean);
//			if (tipBean.getResult()){
			LoggerUtils.d("结算->执行打印交易明细！");
			PrintBean bean = new PrintBean();
			bean.setPrintMessage("打印交易明细");
			bean.setPrintType(Const.PrintStyleConstEnum.PRINT_ALL_WATER);

			bean =  showUIPrintFragment(bean);
			if (!bean.getPrintResult()) {
				LoggerUtils.d("打印明细单失败");
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.print_error));
				return Step.TRANS_RESULT;
			}
		}

		//打印失败、被拒明细
		PrintBean bean = new PrintBean();
		bean.setPrintMessage("打印失败明细");
		bean.setPrintType(Const.PrintStyleConstEnum.PRINT_FAIL_WATER);
		
		bean =  showUIPrintFragment(bean);
		if (!bean.getPrintResult()) {
			LoggerUtils.d("打印失败明细失败");
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.print_error));
			return Step.TRANS_RESULT;
		}

		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_PRINT_ALLWATER_HALT, false);
		return Step.CLEAR_SETTLE;
	}
	
	/** 清除结算数据*/
	@AnnStep(stepIndex = Step.CLEAR_SETTLE)
	public int step_ClearSettle(){
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CLEAR_SETTLT_HLAT, true);	
		//清流水
		TransUtils.clearWater(activity);
		//批次号+1
		addBatchNo();

		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CLEAR_SETTLT_HLAT, false);
		//结算中断标志
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_SETTLT_HALT, false);
		return FINISH;
	}
	
	/** 结果显示*/
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_Result(){
		transResultBean.setTitle(pubBean.getTransName());
		boolean isSettleSuccess = transResultBean.getIsSucess();
		showUITransResult(transResultBean);
 		if(isSettleSuccess){
			if (ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_AUTO_LOGOUT, false)){
				//不做签退
//				doLogOut();
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_FLAG_SIGN, false);
			}
			LoggerUtils.d("hjh  transResultBean.getStepResult()"+ transResultBean.getStepResult());
			LoggerUtils.d("hjh  transResultBean.getStepResult()"+ (transResultBean.getStepResult() == com.newland.pos.sdk.common.TransConst.StepResult.BACK));
			if (transResultBean.getStepResult() == com.newland.pos.sdk.common.TransConst.StepResult.BACK){
				return Step.PRINT_DETAIL;
			}else{
				return Step.CLEAR_SETTLE;
			}
		}else{
			return FINISH;
		}

	}

	/**
	 * 对账不平时,区分内、外卡,并判断上送条件
	 * @param water
	 * @return
	 */
	private int checkCnEnSend(Water water){
		
		if (water.getBatchUpFlag() > maxResend){
			return CHECK_FAIL;
		}
		LoggerUtils.d("checkCnEnSend: water.getInterOrg()->" + water.getInterOrg());	
		if (!AccountStatus.EQUAL.equals(ParamsUtils.getString(ParamsTrans.PARAMS_FLAG_CN_ACCOUNT_CHECKING))
				&& water.getInterOrg().startsWith("CUP")){
			return CHECK_CN;
		}else if (!AccountStatus.EQUAL.equals(ParamsUtils.getString(ParamsTrans.PARAMS_FLAG_EN_ACCOUNT_CHECKING))
				&& !water.getInterOrg().startsWith("CUP")){
			return CHECK_EN;
		}else{
			return CHECK_FAIL;
		}
		
	}
	
	
	private boolean dealBatchUpFail(Water water, int waterHalt, String keyHalt){
		
		try{	
			int batchUpFlag = water.getBatchUpFlag();
			if (batchUpFlag < maxResend){
				batchUpFlag++;
			}else if (batchUpFlag == SendStatus.SUCC || batchUpFlag == SendStatus.DECLINE){
				batchUpNum++;
				ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, batchUpNum);
			}else{
				batchUpFlag = SendStatus.FAIL;
				batchUpNum++;
				ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, batchUpNum);
			}
			water.setBatchUpFlag(batchUpFlag);
			waterService.updateWater(water);
		}catch(Exception e){
			e.printStackTrace();
			LoggerUtils.d("批上送更新流水失败");
			return false;
		}
		
		if (water.getBatchUpFlag() <= maxResend){
			if (isFirstFail){
				isFirstFail = false;
				ParamsUtils.setInt(keyHalt, waterHalt);
			}
		}
		
		return true;
	}
	
	/**
	 * 更新上送标志,但没写入流水,放外面更新
	 * @param failWater
	 * @param waterHalt
	 * @param keyHalt
	 * @return
	 */
	private boolean dealFailWaterBatchUpFail(EmvFailWater failWater, int waterHalt, String keyHalt){
		
		try{	
			int batchUpNum = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_BATCHUP);
			int batchUpFlag = failWater.getBatchUpFlag();
			if (batchUpFlag < maxResend){
				batchUpFlag++;
			}else if (batchUpFlag == SendStatus.SUCC || batchUpFlag == SendStatus.DECLINE){
				batchUpNum++;
				ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, batchUpNum);
			}else{
				batchUpFlag = SendStatus.FAIL;
				batchUpNum++;
				ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, batchUpNum);
			}
			failWater.setBatchUpFlag(batchUpFlag);
		}catch(Exception e){
			e.printStackTrace();
			LoggerUtils.d("批上送更新流水失败");
			return false;
		}
		
		if (water.getBatchUpFlag() <= maxResend){
			if (isFirstFail){
				isFirstFail = false;
				ParamsUtils.setInt(keyHalt, waterHalt);
			}
		}
		
		return true;
	}
	
	private boolean batchMagOffline(){
	
//		keyBatchHlat = ParamsTrans.PARAMS_NUM_MAG_OFFLINE_HALT;
//		int waterHalt = ParamsUtils.getInt(keyBatchHlat);
//		LoggerUtils.d("dd batchMagOffline->begin:" + waterHalt);
//		isFirstFail = true;
//		connectFail = 0;
//		for (;waterHalt <= waterSum; waterHalt++){
//			water = waterService.findById(waterHalt);
//			if (null == water){
//				continue;
//			}
//			switch (water.getTransType()){
//			case TransType.TRANS_OFFLINE:
//			case TransType.TRANS_ADJUST:
//				int resCheck = checkCnEnSend(water);
//				if (resCheck == CHECK_FAIL){
//					continue;
//				}
//				
//				break;
//			default:
//				continue;
//			}
//			
//			// 提示对账不平,正在批上送
//			showUIProgressFragment(getText(R.string.settle_batch_unbalance));
//			pubBean = new PubBean();
//			
//			switch (water.getTransType()){
//			case TransType.TRANS_OFFLINE:
//				packOffline(water, 1);
//				break;
//			case TransType.TRANS_ADJUST:
//				packAdjust(water, 1);
//				break;
//			default:
//				continue;
//			}
//			
//			int resCode = dealPackAndComm(false, false, false);
//			if (STEP_CONTINUE != resCode){
//				//连接出错,当前交易重连三次,再失败则退出交易
//				if (STEP_FINISH == resCode){
//					connectFail++;
//					if (connectFail > 3){
//						return false;
//					} else {
//						waterHalt--;
//						continue;
//					}
//				}
//				dealBatchUpFail(water, waterHalt, keyBatchHlat);
//				continue;
//			}
//			
//			String respCode = iso8583.getField(39);
//			if (!"00".equals(respCode)){
//				displayFailResp(respCode);
//				water.setBatchUpFlag(SendStatus.DECLINE);
//				dealBatchUpFail(water, waterHalt, keyBatchHlat);
//				continue;
//			}
//			
//			water.setBatchUpFlag(SendStatus.SUCC);
//			waterService.updateWater(water);
//			batchUpNum ++;
//			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, batchUpNum);
//			//提示上送成功?
//			showToast(getText(R.string.settle_send_succ));
//			LoggerUtils.d("batchMagOffline->成功");
//		}
//		//如果上送过程没中断,将中断位置设为最后一笔流水
//		if (isFirstFail){
//			ParamsUtils.setInt(keyBatchHlat, waterSum);
//		}				
		return true;
	}
	
	private boolean batchEmvOfflineSucc(){
		keyBatchHlat = ParamsTrans.PARAMS_NUM_EMV_OFFLINE_SUCC_HALT;
		int waterHalt = ParamsUtils.getInt(keyBatchHlat);
		LoggerUtils.d("dd batchEmvOfflineSucc->begin:" + waterHalt);
		
		isFirstFail = true;
		connectFail = 0;
		for (;waterHalt <= waterSum; waterHalt++){
			water = waterService.findById(waterHalt);
			if (null == water){
				continue;
			}
		LoggerUtils.d("dd batchEmvOfflineSucc,emvSatus:" + water.getEmvStatus() + "transAttr:" + water.getTransAttr());	
			if ((TransAttr.ATTR_qPBOC == water.getTransAttr()
				|| TransAttr.ATTR_EMV_STANDARD_RF == water.getTransAttr()
				|| TransAttr.ATTR_EMV_STANDARD == water.getTransAttr())
				&& (EmvStatus.EMV_STATUS_OFFLINE_SUCC == water.getEmvStatus())){
				if (water.getBatchUpFlag() > maxResend){
					continue;
				}
				pubBean = new PubBean();
				//组包
				packSaleOffline(water, 1);
				
			}else{
				continue;
			}
			
			// 提示对账不平,正在批上送
			showUIProgressFragment(getText(R.string.settle_batch_unbalance));
			
			int resCode = dealPackAndComm(false, false, false);
			if (STEP_CONTINUE != resCode){
				//连接出错,当前交易重连三次,再失败则退出交易
				if (STEP_FINISH == resCode){
					connectFail++;
					if (connectFail > maxResend){
						return false;
					} else {
						waterHalt--;
						continue;
					}
				}
				dealBatchUpFail(water, waterHalt, keyBatchHlat);
				continue;
			}
			
			String respCode = iso8583.getField(39);
			if (!"00".equals(respCode)){
				displayFailResp(respCode);
				water.setBatchUpFlag(SendStatus.DECLINE);
				dealBatchUpFail(water, waterHalt, keyBatchHlat);
				continue;
			}
			
			water.setBatchUpFlag(SendStatus.SUCC);
			waterService.updateWater(water);
			batchUpNum ++;
			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, batchUpNum);
			//提示上送成功
			showToast(getText(R.string.settle_send_succ));
			LoggerUtils.d("batchEmvOfflineSucc->成功");
			
		}
		//如果上送过程没中断,将中断位置设为最后一笔流水
		if (isFirstFail){
			ParamsUtils.setInt(keyBatchHlat, waterSum);
		}		
		return true;
	}
	
	private boolean batchMagOnline(){
		
		keyBatchHlat = ParamsTrans.PARAMS_NUM_MAG_ONLINE_HALT;
		int waterHalt = ParamsUtils.getInt(keyBatchHlat);
		LoggerUtils.d("dd batchMagOnline->begin:" + waterHalt);
		
		Field48_BatchMagOnline field48 = new Field48_BatchMagOnline();
		
		int[] sendLoop = new int[8+1];
		int i = 0;
		int j = 0;
				
		isFirstFail = true;
		connectFail = 0;
		for (;waterHalt <= waterSum; waterHalt++){
			water = waterService.findById(waterHalt);
			if (null == water){
				continue;
			}
			switch (water.getTransType()){
			case TransType.TRANS_SALE:
			case TransType.TRANS_VOID_SALE:
			case TransType.TRANS_AUTHSALE:
			case TransType.TRANS_VOID_AUTHSALE:
//			case TransType.TRANS_INSTALMENT:
//			case TransType.TRANS_VOID_INSTALMENT:
//			case TransType.TRANS_BONUS_IIS_SALE:
//			case TransType.TRANS_BONUS_ALLIANCE:
//			case TransType.TRANS_VOID_BONUS_IIS_SALE:
//			case TransType.TRANS_VOID_BONUS_ALLIANCE:
//			case TransType.TRANS_APPOINTMENT_SALE:
//			case TransType.TRANS_VOID_APPOINTMENT_SALE:
//			case TransType.TRANS_PHONE_SALE:
//			case TransType.TRANS_VOID_PHONE_SALE:
//			case TransType.TRANS_PHONE_AUTHSALE:
//			case TransType.TRANS_VOID_PHONE_AUTHSALE:
//			case TransType.TRANS_ORDER_SALE:
//			case TransType.TRANS_VOID_ORDER_SALE:
//			case TransType.TRANS_ORDER_AUTHSALE:
//			case TransType.TRANS_VOID_ORDER_AUTHSALE:
//			case TransType.TRANS_MAG_LOAD_CASH:	
				if (water.getTransAttr() == TransAttr.ATTR_MAG
				|| water.getTransAttr() == TransAttr.ATTR_FALLBACK
				|| water.getTransAttr() == TransAttr.ATTR_EMV_PREDIGEST){
					/** 仅磁条卡交易*/
					int resCheck = checkCnEnSend(water);
					if (resCheck == CHECK_CN){
						field48.setCardType("00");
					}else if (resCheck == CHECK_EN){
						field48.setCardType("01");
					}else{
						break;
					}
					
					field48.setTrace(water.getTrace());
					field48.setPan(StringUtils.paddingString(water.getPan(), 20, "0", 0));
					field48.setAmount(String.format("%012d", water.getAmount()));
					field48.add();
					sendLoop[i++] = waterHalt;
				}
				break;
			default:
				break;
			}
			
			/**每8笔上送一次或者到底就送*/
			if (i == 8 || (waterHalt == waterSum && i > 0)){
								
				// 提示对账不平,正在批上送
				showToast(getText(R.string.settle_batch_unbalance));
				pubBean = new PubBean();
				/**采集数据*/
				pubBean.setTransType(TransType.TRANS_BATCHUP);
				initPubBean();
				ISOField60 isoField60 = new ISOField60();
				LoggerUtils.d("111 batchMagOnline.0 ->60域[" + isoField60.getString() + "]");
				isoField60.setFuncCode(getF601FuncCode(pubBean.getTransType()));
				LoggerUtils.d("111 batchMagOnline.1 ->60域[" + isoField60.getString() + "]");
				isoField60.setNetManCode("201");
				LoggerUtils.d("111 batchMagOnline.2 ->60域[" + isoField60.getString() + "]");
				pubBean.setIsoField60(isoField60);
				pubBean.setIsoField48(field48.getString());
				/**48域重值重置*/
				field48.setNum(0);
				field48.setField48("");
				
				/**组包*/
				iso8583.initPack();
				iso8583.setField(0, pubBean.getMessageID());
				iso8583.setField(11, pubBean.getTraceNo());
				iso8583.setField(41, pubBean.getPosID());
				iso8583.setField(42, pubBean.getShopID());
				iso8583.setField(48, pubBean.getIsoField48());
				iso8583.setField(60, pubBean.getIsoField60().getString());
				
				int resCode = dealPackAndComm(false, false, false);
				if (STEP_CONTINUE != resCode){
					//连接出错,当前交易重连三次,再失败则退出交易
					if (STEP_FINISH == resCode){
						connectFail++;
						if (connectFail > 3){
							
							return false;
						} else {
							waterHalt--;
							continue;
						}
					}
					for (j = 0; j < i; j++){
						water = waterService.findById(sendLoop[j]);
						dealBatchUpFail(water, sendLoop[j], keyBatchHlat);
					}
					i = 0;
					continue;
				}
				
				String respCode = iso8583.getField(39);
				if (!"00".equals(respCode)){
					displayFailResp(respCode);
					water.setBatchUpFlag(SendStatus.DECLINE);
					for (j = 0; j < i; j++){
						water = waterService.findById(sendLoop[j]);
						dealBatchUpFail(water, sendLoop[j], keyBatchHlat);
					}
					i = 0;
					continue;
				}
				
				for (j = 0; j < i; j++){
					water = waterService.findById(sendLoop[j]);
				water.setBatchUpFlag(SendStatus.SUCC);
				waterService.updateWater(water);
				batchUpNum ++;
				ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, batchUpNum);
				}
				i = 0;
				//提示上送成功
				showToast(getText(R.string.settle_send_succ));
				LoggerUtils.d("batchMagOnline->成功");
			}
		}
		//如果上送过程没中断,将中断位置设为最后一笔流水
		if (isFirstFail){
			ParamsUtils.setInt(keyBatchHlat, waterSum);
		}			
		return true;
	}
	
	private boolean batchInform(){
		keyBatchHlat = ParamsTrans.PARAMS_NUM_INFORM_HALT;
		int waterHalt = ParamsUtils.getInt(keyBatchHlat);
		LoggerUtils.d("dd batchInform->begin:" + waterHalt);
		
		isFirstFail = true;
		connectFail = 0;
		for (;waterHalt <= waterSum; waterHalt++){
			water = waterService.findById(waterHalt);
			if (null == water){
				continue;
			}
			/** 订购退货,订购预授权完成通知不再上送*/
			switch (water.getTransType()){
			case TransType.TRANS_EMV_REFUND:
			case TransType.TRANS_REFUND:
			case TransType.TRANS_AUTHSALEOFF:
				if (CHECK_FAIL == checkCnEnSend(water)){
					continue;
				}
				break;
			default:
				continue;
			}
			
			// 提示对账不平,正在批上送
			showUIProgressFragment(getText(R.string.settle_batch_unbalance));
			pubBean = new PubBean();
			
			switch (water.getTransType()){
			case TransType.TRANS_EMV_REFUND:
			case TransType.TRANS_REFUND:
				// 组包
				pubBean.setTransType(water.getTransType());
				initPubBean();
				LoggerUtils.d("555 退货类36域1[" + pubBean.getTrackData3() + "]");
				waterToPubBean(water, pubBean);
				isoField60 = new ISOField60(water.getTransType(), false);
				pubBean.setIsoField60(isoField60);
				if (water.getTransType() == TransType.TRANS_EMV_REFUND){
					if (pubBean.getOldTerminalId() != null){
						pubBean.setIsoField62(pubBean.getOldTerminalId());
					}
					pubBean.setIsoField61(water.getOldBatch() + water.getOldTrace() + water.getOldDate());
					
				} else {
					pubBean.setIsoField61("000000" + "000000" + water.getOldDate());
				}
				pubBean.setIsoField63("000");
				
				LoggerUtils.d("555 退货类36域[" + pubBean.getTrackData3() + "]");
				//组包
				packRefund(pubBean, 1);
				
				break;
			case TransType.TRANS_AUTHSALEOFF:
				pubBean.setTransType(water.getTransType());
				initPubBean();
				waterToPubBean(water, pubBean);
				isoField60 = new ISOField60(water.getTransType(), false);
				pubBean.setIsoField60(isoField60);
				pubBean.setIsoField61("000000" + "000000" + water.getOldDate());
				//组包
				packAuthSaleOff(pubBean, 1);
				break;
			default:
				continue;
			}
			
			int resCode = dealPackAndComm(false, false, false);
			if (STEP_CONTINUE != resCode){
				//连接出错,当前交易重连三次,再失败则退出交易
				if (STEP_FINISH == resCode){
					connectFail++;
					if (connectFail > 3){
						return false;
					} else {
						waterHalt--;
						continue;
					}
				}
				dealBatchUpFail(water, waterHalt, keyBatchHlat);
				continue;
			}
			
			String respCode = iso8583.getField(39);
			if (!"00".equals(respCode)){
				displayFailResp(respCode);
				water.setBatchUpFlag(SendStatus.DECLINE);
				dealBatchUpFail(water, waterHalt, keyBatchHlat);
				LoggerUtils.d("batchInform->失败");
				continue;
			}
			
			water.setBatchUpFlag(SendStatus.SUCC);
			waterService.updateWater(water);
			batchUpNum ++;
			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, batchUpNum);
			// 提示上送成功
			showToast(getText(R.string.settle_send_succ));
			LoggerUtils.d("batchInform->成功");
		}
		//如果上送过程没中断,将中断位置设为最后一笔流水
		if (isFirstFail){
			ParamsUtils.setInt(keyBatchHlat, waterSum);
		}
		return true;
	}
	
	private boolean batchEmvOnlineSucc(){
		keyBatchHlat = ParamsTrans.PARAMS_NUM_EMV_ONLINE_SUCC_HALT;
		int waterHalt = ParamsUtils.getInt(keyBatchHlat);
		LoggerUtils.d("dd batchEmvOnlineSucc->begin:" + waterHalt + " waterSum:" + waterSum);
	
		isFirstFail = true;
		connectFail = 0;
		
		for (;waterHalt <= waterSum; waterHalt++){
			LoggerUtils.d("dd batchEmvOnlineSucc->findById:" + waterHalt);
			water = waterService.findById(waterHalt);
			if (null == water){
				continue;
			}
			LoggerUtils.d("dd batchEmvOnlineSucc->1:" + waterHalt);
			if ((com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_STANDARD_RF == water.getTransAttr()
				|| com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_STANDARD == water.getTransAttr())
				&& (EmvStatus.EMV_STATUS_ONLINE_SUCC == water.getEmvStatus())){
				LoggerUtils.d("dd batchEmvOnlineSucc->1.1:" + waterHalt);	
				/** 现金圈存与非指定账户圈存不上送*/
				if (TransType.TRANS_EC_LOAD == water.getTransType() 
					|| TransType.TRANS_EC_LOAD_NOT_BIND == water.getTransType()){
					continue;
				}
					
				if (water.getBatchUpFlag() > maxResend){
					continue;
				}
					
			}else{
				continue;
			}
			// 排除ARPC错,但仍然承兑的情况
			byte[] tvr = new byte[5];
			tvr = BytesUtils.hexToBytes(water.getTvr());
			if ((tvr[4] & 0x40) != 0){
				continue;
			}
			
			//提示正在批上送
			showUIProgressFragment(getText(R.string.settle_batch_normal));
			
			pubBean = new PubBean();
			// 组包
			pubBean.setTransType(TransType.TRANS_BATCHUP);
			initPubBean();
			waterToPubBean(water, pubBean);
			isoField60 = new ISOField60(TransType.TRANS_BATCHUP, false);
			if (AccountStatus.EQUAL.equals(ParamsUtils.getString(ParamsTrans.PARAMS_FLAG_CN_ACCOUNT_CHECKING))
				&& AccountStatus.EQUAL.equals(ParamsUtils.getString(ParamsTrans.PARAMS_FLAG_EN_ACCOUNT_CHECKING))){
				//对账平
				isoField60.setNetManCode("203");
			} else {
				//对账不平
				isoField60.setNetManCode("205");
			}
			isoField60.setTermCapacity("6");
			isoField60.setIcConditionCode("0");
			pubBean.setIsoField60(isoField60);
			//62域,用法6
			isoField62 = new ISOField62_EmvBatchUp();
			isoField62.setUse("61");
			if ("CUP".equals(pubBean.getInternationOrg())){
				isoField62.setCardClass("00");
			} else {
				isoField62.setCardClass("01");
			}
			isoField62.setRespCode("00");
			isoField62.setAuthAmt(pubBean.getAmountField());
			isoField62.setAuthCurrency(pubBean.getCurrency());
			pubBean.setIsoField62(isoField62.getStringUsage6());
				
			iso8583.initPack();
			iso8583.setField(0, pubBean.getMessageID());
			iso8583.setField(2, pubBean.getPan());
			iso8583.setField(4, pubBean.getAmountField());
			iso8583.setField(11, pubBean.getTraceNo());
			iso8583.setField(22, pubBean.getInputMode());
			if (!StringUtils.isEmpty(pubBean.getCardSerialNo())) {
				iso8583.setField(23, pubBean.getCardSerialNo());
			}
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			if (!StringUtils.isNullOrEmpty(pubBean.getIsoField55())){
				iso8583.setField(55, pubBean.getIsoField55());
			}
			iso8583.setField(60, pubBean.getIsoField60().getString());
			iso8583.setField(62, pubBean.getIsoField62());
										
			int resCode = dealPackAndComm(false, false, false);
			if (STEP_CONTINUE != resCode){
				//连接出错,当前交易重连三次,再失败则退出交易
				if (STEP_FINISH == resCode){
					connectFail++;
					if (connectFail > 3){
						return false;
					} else {
						waterHalt--;
						continue;
					}
				}
				dealBatchUpFail(water, waterHalt, keyBatchHlat);
				continue;
			}
			
			String respCode = iso8583.getField(39);
			if (!"00".equals(respCode)){
				displayFailResp(respCode);
				water.setBatchUpFlag(SendStatus.DECLINE);
				dealBatchUpFail(water, waterHalt, keyBatchHlat);
				continue;
			}
			
			water.setBatchUpFlag(SendStatus.SUCC);
			waterService.updateWater(water);
			batchUpNum ++;
			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, batchUpNum);
			// 提示上送成功
			showToast(getText(R.string.settle_send_succ));
		}
		//如果上送过程没中断,将中断位置设为最后一笔流水
		if (isFirstFail){
			ParamsUtils.setInt(keyBatchHlat, waterSum);
		}
		return true;
	}
	
	
	private boolean batchEmvOfflineFail(){
		keyBatchHlat = ParamsTrans.PARAMS_NUM_EMV_OFFLINE_FAIL_HALT;
		int waterHalt = ParamsUtils.getInt(keyBatchHlat);
		LoggerUtils.d("111 batchEmvOfflineFail->begin:" + waterHalt + ",failWaterSum:" + failWaterSum);
		
		EmvFailWater failWater = null;
		if (failWaterSum <= 0){
			LoggerUtils.d("111 failWaterSum <= 0，return true");
			return true;
		}
	
		isFirstFail = true;
		connectFail = 0;
		for (;waterHalt <= failWaterSum; waterHalt++){
			failWater = failWaterService.findEmvFailWater(waterHalt);
			if (null == failWater){
				LoggerUtils.d("111 null == failWater，continue");
				continue;
			}

			if (failWater.getBatchUpFlag() > maxResend){
				LoggerUtils.d("111 failWater.getBatchUpFlag() > maxResend，continue");
				continue;
			}
			
			LoggerUtils.d("111 batchEmvOfflineFail....开始打包上送");
			//提示正在批上送
			showUIProgressFragment(getText(R.string.settle_batch_normal));
			pubBean = new PubBean();
			// 组包			
			pubBean.setTransType(TransType.TRANS_BATCHUP);
			initPubBean();
			failWaterToPubBean(failWater, pubBean);
			isoField60 = new ISOField60(TransType.TRANS_BATCHUP, false);
			if (AccountStatus.EQUAL.equals(ParamsUtils.getString(ParamsTrans.PARAMS_FLAG_CN_ACCOUNT_CHECKING))
				&& AccountStatus.EQUAL.equals(ParamsUtils.getString(ParamsTrans.PARAMS_FLAG_EN_ACCOUNT_CHECKING))){
				//对账平
				isoField60.setNetManCode("204");
			} else {
				//对账不平
				isoField60.setNetManCode("206");
			}
			isoField60.setTermCapacity("6");
			isoField60.setIcConditionCode("0");
			pubBean.setIsoField60(isoField60);
			//62域,用法7,密文类型:AAC
			isoField62 = new ISOField62_EmvBatchUp();
			isoField62.setUse("71");
			if ("CUP".equals(pubBean.getInternationOrg())){
				isoField62.setCardClass("00");
			} else {
				isoField62.setCardClass("01");
			}
			LoggerUtils.d("111 EmvAuthResp:" + failWater.getEmvAuthResp());
			if ("5931".equals(failWater.getEmvAuthResp())
				|| "5A31".equals(failWater.getEmvAuthResp())){
				isoField62.setRespCode("11");
			} else if ("5933".equals(failWater.getEmvAuthResp())
				|| "5A33".equals(failWater.getEmvAuthResp())){
				isoField62.setRespCode("13");
			} else { //缺省填 11, ???
				isoField62.setRespCode("11");
			}
			isoField62.setAuthAmt(pubBean.getAmountField());
			isoField62.setAuthCurrency(pubBean.getCurrency());
			//AAC
			isoField62.setAcType("1");
			isoField62.setArpcRes("0");
	
			pubBean.setIsoField62(isoField62.getStringUsage7());
				
			iso8583.initPack();
			iso8583.setField(0, pubBean.getMessageID());
			iso8583.setField(2, pubBean.getPan());
			iso8583.setField(4, pubBean.getAmountField());
			iso8583.setField(11, pubBean.getTraceNo());
			iso8583.setField(22, pubBean.getInputMode());
			if (!StringUtils.isEmpty(pubBean.getCardSerialNo())) {
				iso8583.setField(23, pubBean.getCardSerialNo());
			}
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			if (!StringUtils.isNullOrEmpty(pubBean.getIsoField55())){
				iso8583.setField(55, pubBean.getIsoField55());
			}
			iso8583.setField(60, pubBean.getIsoField60().getString());
			iso8583.setField(62, pubBean.getIsoField62());
										
			int resCode = dealPackAndComm(false, false, false);
			if (STEP_CONTINUE != resCode){
				//连接出错,当前交易重连三次,再失败则退出交易
				if (STEP_FINISH == resCode){
					connectFail++;
					if (connectFail > 3){
						return false;
					} else {
						waterHalt--;
						continue;
					}
				}
				dealFailWaterBatchUpFail(failWater, waterHalt, keyBatchHlat);
				//这里需要更新流水,dealFailWaterBatchUpFail没处理
				failWaterService.updateEmvFailWater(failWater);
				continue;
			}
			
			String respCode = iso8583.getField(39);
			if (!"00".equals(respCode)){
				displayFailResp(respCode);
				failWater.setBatchUpFlag(SendStatus.DECLINE);
				dealFailWaterBatchUpFail(failWater, waterHalt, keyBatchHlat);
				//这里需要更新流水,dealFailWaterBatchUpFail没处理
				failWaterService.updateEmvFailWater(failWater);
				continue;
			}
			
			failWater.setBatchUpFlag(SendStatus.SUCC);
			failWaterService.updateEmvFailWater(failWater);
			batchUpNum ++;
			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, batchUpNum);
			// 提示上送成功
			showToast(getText(R.string.settle_send_succ));
		}
		//如果上送过程没中断,将中断位置设为最后一笔流水
		if (isFirstFail){
			ParamsUtils.setInt(keyBatchHlat, failWaterSum);
		}
		return true;
	}
	
	
	private boolean batchEmvOnlineSuccArpcErr(){
		keyBatchHlat = ParamsTrans.PARAMS_NUM_EMV_ONLINE_SUCC_ARPC_ERR_HALT;
		int waterHalt = ParamsUtils.getInt(keyBatchHlat);
		LoggerUtils.d("dd batchEmvOnlineSuccArpcErr->begin:" + waterHalt);
		
		isFirstFail = true;
		connectFail = 0;
		for (;waterHalt <= waterSum; waterHalt++){
			water = waterService.findById(waterHalt);
			if (null == water){
				continue;
			}
			if ((com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_STANDARD_RF == water.getTransAttr()
				|| com.newland.pos.sdk.common.TransConst.TransAttr.ATTR_EMV_STANDARD == water.getTransAttr())
				&& (EmvStatus.EMV_STATUS_ONLINE_SUCC == water.getEmvStatus())){
				
				/** 现金圈存与非指定账户圈存不上送*/
				if (TransType.TRANS_EC_LOAD == water.getTransType() 
					|| TransType.TRANS_EC_LOAD_NOT_BIND == water.getTransType()){
					continue;
				}
				
				if (water.getBatchUpFlag() > maxResend){
					continue;
				}
				
			}else{
				continue;
			}
			
			// ARPC错,但仍然承兑
			byte[] tvr = new byte[5];
			tvr = BytesUtils.hexToBytes(water.getTvr());
			if ((tvr[4] & 0x40) == 0){
				continue;
			}
			
			//提示正在批上送
			showUIProgressFragment(getText(R.string.settle_batch_normal));
			pubBean = new PubBean();
			// 组包
			pubBean.setTransType(TransType.TRANS_BATCHUP);
			initPubBean();
			waterToPubBean(water, pubBean);
			isoField60 = new ISOField60(TransType.TRANS_BATCHUP, false);
			if (AccountStatus.EQUAL.equals(ParamsUtils.getString(ParamsTrans.PARAMS_FLAG_CN_ACCOUNT_CHECKING))
				&& AccountStatus.EQUAL.equals(ParamsUtils.getString(ParamsTrans.PARAMS_FLAG_EN_ACCOUNT_CHECKING))){
				//对账平
				isoField60.setNetManCode("204");
			} else {
				//对账不平
				isoField60.setNetManCode("206");
			}
			isoField60.setTermCapacity("6");
			isoField60.setIcConditionCode("0");
			pubBean.setIsoField60(isoField60);
			//62域,用法7,密文类型:ARPC
			isoField62 = new ISOField62_EmvBatchUp();
			isoField62.setUse("71");
			if ("CUP".equals(pubBean.getInternationOrg())){
				isoField62.setCardClass("00");
			} else {
				isoField62.setCardClass("01");
			}
			isoField62.setRespCode("05");
			isoField62.setAuthAmt(pubBean.getAmountField());
			isoField62.setAuthCurrency(pubBean.getCurrency());
			//ARPC
			isoField62.setAcType("2");
			isoField62.setArpcRes("2");
			pubBean.setIsoField62(isoField62.getStringUsage7());
				
			iso8583.initPack();
			iso8583.setField(0, pubBean.getMessageID());
			iso8583.setField(2, pubBean.getPan());
			iso8583.setField(4, pubBean.getAmountField());
			iso8583.setField(11, pubBean.getTraceNo());
			iso8583.setField(22, pubBean.getInputMode());
			if (!StringUtils.isEmpty(pubBean.getCardSerialNo())) {
				iso8583.setField(23, pubBean.getCardSerialNo());
			}
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			if (!StringUtils.isNullOrEmpty(pubBean.getIsoField55())){
				iso8583.setField(55, pubBean.getIsoField55());
			}
			iso8583.setField(60, pubBean.getIsoField60().getString());
			iso8583.setField(62, pubBean.getIsoField62());
										
			int resCode = dealPackAndComm(false, false, false);
			if (STEP_CONTINUE != resCode){
				//连接出错,当前交易重连三次,再失败则退出交易
				if (STEP_FINISH == resCode){
					connectFail++;
					if (connectFail > 3){
						return false;
					} else {
						waterHalt--;
						continue;
					}
				}
				dealBatchUpFail(water, waterHalt, keyBatchHlat);
				continue;
			}
			
			String respCode = iso8583.getField(39);
			if (!"00".equals(respCode)){
				displayFailResp(respCode);
				water.setBatchUpFlag(SendStatus.DECLINE);
				dealBatchUpFail(water, waterHalt, keyBatchHlat);
				continue;
			}
			
			water.setBatchUpFlag(SendStatus.SUCC);
			waterService.updateWater(water);
			batchUpNum ++;
			ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_BATCHUP, batchUpNum);
			// 提示上送成功
			showToast(getText(R.string.settle_send_succ));
		}
		//如果上送过程没中断,将中断位置设为最后一笔流水
		if (isFirstFail){
			ParamsUtils.setInt(keyBatchHlat, waterSum);
		}
		return true;
	}
	
	private boolean batchUpEnd(){
		int batchNum = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_BATCHUP);
		
//		if (batchNum <= 0){
//			return true;
//		}
			
		//提示正在批上送
		showUIProgressFragment(getText(R.string.settle_batch_normal));
		pubBean = new PubBean();
		// 组包			
		pubBean.setTransType(TransType.TRANS_BATCHUP);
		initPubBean();
		isoField60 = new ISOField60(TransType.TRANS_BATCHUP, false);
		if (!AccountStatus.EQUAL.equals(ParamsUtils.getString(ParamsTrans.PARAMS_FLAG_CN_ACCOUNT_CHECKING))
			|| !AccountStatus.EQUAL.equals(ParamsUtils.getString(ParamsTrans.PARAMS_FLAG_EN_ACCOUNT_CHECKING))){
			//对账不平
			isoField60.setNetManCode("202");
		} else {
			//对账平
			isoField60.setNetManCode("207");
		}
		pubBean.setIsoField60(isoField60);
		
		iso8583.initPack();
		iso8583.setField(0, pubBean.getMessageID());
		iso8583.setField(11, pubBean.getTraceNo());
		iso8583.setField(41, pubBean.getPosID());
		iso8583.setField(42, pubBean.getShopID());
		iso8583.setField(48, String.format("%04d", batchNum));
		iso8583.setField(60, pubBean.getIsoField60().getString());
									
		int resCode = dealPackAndComm(false, false, false);
		if (STEP_CONTINUE != resCode){
			if (STEP_FINISH == resCode){
				return true;
			}
			return true;
		}
		
		String respCode = iso8583.getField(39);
		if (!"00".equals(respCode)){
			displayFailResp(respCode);;
		}
		
		// 提示上送成功
		showToast(getText(R.string.settle_send_succ));
		
		return true;
	}
	
	
	
	/**
	 * 批次号增加1
	 * @author linld
	 */
	@SuppressLint("DefaultLocale")
	private synchronized void addBatchNo(){
		String batchNo = ParamsUtils.getString( 
				ParamsConst.PARAMS_KEY_BASE_BATCHNO);
		LoggerUtils.e("batchNo = "+ batchNo);
		int currenNo = 1;
		try{
			currenNo = Integer.parseInt(batchNo);
		}catch(Exception e){
			e.printStackTrace();
		}
		int nextNo = (currenNo+1)%1000000;
		ParamsUtils.setString(
				ParamsConst.PARAMS_KEY_BASE_BATCHNO, 
				String.format("%06d", nextNo));
	}	
	
	private void displayFailResp(String responseCode){
		showToast(getText(R.string.common_respon) +	responseCode 
				+ "\r\n" 
				+  AnswerCodeHelper.getAnswerCodeCN(responseCode));
	}
	
	public int getDealType() {
		return dealType;
	}

	public void setDealType(int dealType) {
		this.dealType = dealType;
	}
	
}
