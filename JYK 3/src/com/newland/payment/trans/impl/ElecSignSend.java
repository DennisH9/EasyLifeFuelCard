package com.newland.payment.trans.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.Const.ElecSignType;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.EmvStatus;
import com.newland.payment.common.TransConst.TransDeal;
import com.newland.payment.common.TransType;
import com.newland.payment.common.tools.AnswerCodeHelper;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.PrintBean;
import com.newland.payment.trans.bean.CommunicationBean.CommunicationFailReason;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.trans.bean.field.Field_55_Signature;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.payment.ui.activity.App;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 电子签名上送
 * @author linld
 * @date 2015-06-05
 */
public final class ElecSignSend extends AbstractBaseTrans {

	private class Step {
		public final static int TRANS_START = 1;
		public final static int ELEC_SEND = 2;
		public final static int ELEC_SEND_SETTLE = 3;
	}
	
	private WaterService waterService = null;
	private Water water;
	
	private int maxTimes = 3;
	private int failTimes = 0;	// 上送失败次数
	private int connectTimes = 0; // 连接失败次数
	
	private final int STEP_QUIT = -10;
	private boolean isFirstFail = true;
	
	private int count = 0;
	private int waterSum = 0;
	private int sendType = ElecSignType.ONLINE_OFFLINE_UNSEND;
	
	private CommonBean<Integer> commonBean;
	private int dealType = TransDeal.NORMAL;
	
	public ElecSignSend(int dealType){
		this.commonBean = new CommonBean<Integer>();
		this.sendType = ElecSignType.ONLINE_OFFLINE_UNSEND;
		this.dealType = dealType;
	}
	
	public ElecSignSend(CommonBean<Integer> commonBean){
		this.commonBean = commonBean;
		this.sendType = commonBean.getValue();
		LoggerUtils.d("dd 电子签名sendType:" + sendType);
	}
	
	@Override
	protected void checkPower(){
		super.checkPower();
		checkCardExsit = false;
		checkSettlementStatus = false;
		checkWaterCount = false;
		transcationManagerFlag = false; // 不开启事务
	}
	
	@Override
	protected int init() {
		int res = super.init();
		transResultBean.setIsSucess(false);		
		commonBean.setResult(false);
		return res;
	}
	
	
	@Override
	protected void release() {
		if (dealType == TransDeal.SPECIAL){
			super.release();
		}
		goOnStep(commonBean);
	}
	
	@Override
	protected String[] getCommunitionTipMsg() {
		return new String[]{String.format("正在上送电子签名\r\n第[%d]笔\r\n请稍候",count++)};
	}


	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		
		//电子签名开关检查
		if (!ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_ELECSIGN, false)){
			commonBean.setResult(true);
			return SUCC;
		}
		
		waterService = new WaterServiceImpl(activity);
		waterSum = waterService.getWaterCount();
		LoggerUtils.d("waterSum:" + waterSum);
		if (waterSum <= 0){
			commonBean.setResult(true);
			return SUCC;
		}
		
		if (sendType == ElecSignType.SETTLE_SEND){
			return Step.ELEC_SEND_SETTLE;
		}else{
			return Step.ELEC_SEND;
		}		
		
	}
	
	private int elecSignSend(Water water, String fileName){
		pubBean = new PubBean();
		
		initPubBean();
	
		pubBean.setMessageID("0820");
		if(!StringUtils.isEmpty(water.getPan())){
			pubBean.setPan(water.getPan());
		}
		pubBean.setAmount(water.getAmount());
		pubBean.setTraceNo(water.getTrace());
		if (!StringUtils.isEmpty(water.getSettleDate())){
			pubBean.setSettleDate(water.getSettleDate());
		}
		if (!StringUtils.isEmpty(water.getReferNum())){
			pubBean.setSystemRefNum(water.getReferNum());
		}
	
		Field_55_Signature field55 = new Field_55_Signature(water);
LoggerUtils.d("dd 电子签名上送55域:" + BytesUtils.bytesToHex(field55.pack()));		
		pubBean.setIsoField55(BytesUtils.bytesToHex(field55.pack()));
		
		ISOField60 isoField60 = new ISOField60();
		isoField60.setFuncCode("07");
		isoField60.setBatchNum(water.getBatchNum());
		isoField60.setNetManCode("800");
		pubBean.setIsoField60(isoField60);
		
		byte[] data = null;
		FileInputStream ins = null;
		try {
			File file = new File(fileName);
			long len = file.length();
			//超出长度不处理
			if(len > Const.SIGNATURE_MAX_LEN){
				LoggerUtils.d("signature file out of length!");
				return FINISH;
			}
			LoggerUtils.d("signature len:" + len);
			data = new byte[(int) len];
			ins = new FileInputStream(file);
			//while??
			int readLen = ins.read(data);
			if(readLen != len){
				LoggerUtils.d("signature file read length not equals!");
				return FINISH;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return FINISH;
		} finally{
			if(ins != null){
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ins = null;
			}
		}
		
		iso8583.initPack();
		
		iso8583.setField(0, pubBean.getMessageID());
		if(!StringUtils.isEmpty(pubBean.getPan())){
			iso8583.setField(2, pubBean.getPan());
		}
		iso8583.setField(4, pubBean.getAmountField());
		
		iso8583.setField(11, pubBean.getTraceNo());
		if(!StringUtils.isEmpty(pubBean.getSettleDate())
			&& !"0000".equals(pubBean.getSettleDate())){
			iso8583.setField(15, pubBean.getSettleDate());
		}
		
		if (!StringUtils.isEmpty(pubBean.getSystemRefNum())){
			iso8583.setField(37,pubBean.getSystemRefNum());
		}
		
		iso8583.setField(41, pubBean.getPosID());
		iso8583.setField(42, pubBean.getShopID());
		
		iso8583.setField(55, pubBean.getIsoField55());
		
		iso8583.setField(60, pubBean.getIsoField60().getString());
		//不存到PubBean中
		iso8583.setField(62, BytesUtils.bytesToHex(data));
		iso8583.setField(64, "00");
		
		//响应码后面处理
		int resCode = packAndComm();
		switch (resCode){
		case STEP_CONTINUE:
			break;
		case STEP_CANCEL: //取消
		case STEP_FINISH: //连接出错
			return resCode;
		default:
			return FINISH;
		}
		
		String responseCode = iso8583.getField(39);
		LoggerUtils.d("dd 上送应答码："+responseCode);
		if("00".equals(responseCode)
			||"94".equals(responseCode)){
			//成功
			return SUCC;
		} else {
			showToast(getText(R.string.common_respon)+
					responseCode+"\r\n"
					+ AnswerCodeHelper.getAnswerCodeCN(responseCode));
			//后台拒绝,结算时,退出打单
			return STEP_QUIT;
		}		
	}
	
	@AnnStep(stepIndex = Step.ELEC_SEND)
	public int step_ElecSignSend(){
		int waterOnlineHalt = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_ELECSEND_ONLINE_HALT, 1);
		int waterOfflineHalt = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_ELECSEND_OFFLINE_HALT, 1);
		int num = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_ELECSIGN_UNSEND, 0);
		LoggerUtils.d("dd 电子签名未上送笔数：" + num );
		if (num <= 0){
			commonBean.setResult(true);
			return SUCC;
		}
		
		//从上次送的地方开始继续送
		int waterHalt = 1;
		if (ElecSignType.ONLINE == sendType){
			waterHalt = waterOnlineHalt;
		}else if(ElecSignType.OFFLINE == sendType){
			waterHalt = waterOfflineHalt;
		}else {
			//离线联机都送
			waterHalt = Math.min(waterOnlineHalt, waterOfflineHalt);
		}
		
		int currentTrace = Integer.parseInt(ParamsUtils.getString( 
				ParamsConst.PARAMS_KEY_BASE_TRACENO));
		
		count = 1;
		for (; waterHalt <= waterSum; waterHalt++){
			water = waterService.findById(waterHalt);
			if (water == null){
				LoggerUtils.d("dd 取不到流水:"+ waterHalt);
				continue;
			}
			
			if (!water.getSignatureFlag()){
				LoggerUtils.d("dd 没有签名标志:" + water.getTrace());
				continue;
			}
						
			if (ElecSignType.ONLINE == sendType){
				//上送联机类,则过滤离线交易的签名
				if(EmvStatus.EMV_STATUS_OFFLINE_SUCC == water.getEmvStatus()){
					LoggerUtils.d("dd 当前上送联机签名,过滤离线流水:" + water.getTrace());
					continue;
				}
				
				//联机类签名,当前交易不上送
				int waterTrace =  Integer.parseInt(water.getTrace());
				LoggerUtils.d("dd 当前Trace：" + currentTrace + "流水Trace:" + waterTrace);
				if (currentTrace - 1 == waterTrace){
					LoggerUtils.d("当前签名下笔上送:" + water.getTrace());
					if (isFirstFail){
						isFirstFail = false;
						LoggerUtils.d("dd -记录中断位置" + waterHalt);
						ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_ONLINE_HALT, waterHalt);
					}
					continue;
				}
				
			}else if (ElecSignType.OFFLINE == sendType){
				//上送离线类,则过滤联机交易的签名
				if (EmvStatus.EMV_STATUS_OFFLINE_SUCC != water.getEmvStatus()){
					LoggerUtils.d("dd 当前上送离线签名,过滤联机流水:" + water.getTrace());
					continue;
				}
			}
			
			File file = new File(App.SIGNATURE_JBIG_DIR + water.getTrace());
			if (!file.exists()){
				LoggerUtils.d("dd 签名文件不存在" + App.SIGNATURE_JBIG_DIR + water.getTrace());
				continue;
			}
			LoggerUtils.d("fileName:" + file.getAbsolutePath());
			int resCode = elecSignSend(water, file.getAbsolutePath());
				
			if (SUCC != resCode){
				LoggerUtils.d("dd 电子签名上送失败");
				//转存为失败图片
				File failFile = new File(file.getParent(), file.getName()+ Const.SIGNATURE_FAIL_FLAG );
				LoggerUtils.d("Fail File:" + failFile.getAbsoluteFile());
				if (failFile.exists()){
					failFile.delete();
				}
				file.renameTo(failFile);
				//电子签名未上送次数减1
				delElecSignUnSendNum();
				//按取消退出
				if (resCode == STEP_CANCEL){
					break;
				}else{
					continue;
				}
			}
			
			LoggerUtils.d("dd 电子签名上送成功");
			//电子签名未上送次数减1
			delElecSignUnSendNum();
			//删除对应jbig
			file.delete();
			showToast(getText(R.string.settle_send_succ));
					
		}
		if (isFirstFail){
			isFirstFail = false;
			//更新当前流水的位置
			if (ElecSignType.ONLINE == sendType){
				ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_ONLINE_HALT, waterHalt);
			}else if(ElecSignType.OFFLINE == sendType){
				ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_OFFLINE_HALT, waterHalt);
			}else {
				ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_ONLINE_HALT, waterHalt);
				ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_OFFLINE_HALT, waterHalt);
			}
		
		}
		commonBean.setResult(true);
		return SUCC;
	}
	
	@AnnStep(stepIndex = Step.ELEC_SEND_SETTLE)
	public int step_ElecSignSendSettle(){
		failTimes = ParamsUtils.getInt(ParamsTrans.PARAMS_TIMES_ELECSIGN_HAVE_SEND, 0);
		maxTimes =  ParamsUtils.getInt(ParamsConst.PARAMS_KEY_SIGN_RESEND_TIMES, 3);
		
		while(failTimes < maxTimes){ //此处不加=,因检测中心系统bug?
			
			int waterHalt = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_ELECSEND_SETTLE_HALT, 1);
			connectTimes = 0;
			isFirstFail = true;
			count = 1;
			LoggerUtils.d("dd 签名上送中断:" + waterHalt + "流水总数：" + waterSum);
			for (; waterHalt <= waterSum; waterHalt++){
				water = waterService.findById(waterHalt);
				if (water == null){
					continue;
				}
				
				if (!water.getSignatureFlag()){
					continue;
				}
				
				File file = new File(App.SIGNATURE_JBIG_DIR + water.getTrace());
				if (!file.exists()){
					file = new File(App.SIGNATURE_JBIG_DIR + water.getTrace() + Const.SIGNATURE_FAIL_FLAG);
					if (!file.exists()){
						continue;
					}
				}
				
				LoggerUtils.d("fileName:" + file.getAbsolutePath());
				
				int resCode = elecSignSend(water, file.getAbsolutePath());
				switch(resCode){
				case SUCC:
					LoggerUtils.d("dd 电子签名上送成功:" + water.getTrace());
					// 删除对应jbig
					file.delete();
					showToast(getText(R.string.settle_send_succ));
					break;
					
				case STEP_FINISH: //连接失败
					connectTimes ++;
					if (connectTimes > maxTimes){
						commonBean.setResult(false);
						return FINISH;
					}else{
						waterHalt--;
						count--;
						continue;
					}
				case STEP_CANCEL: //按取消退出
					commonBean.setResult(false);
					return FINISH;
				default: //失败
					if (isFirstFail){
						isFirstFail = false;
						LoggerUtils.d("dd 记录电子签名上送失败位置:" + waterHalt);
						ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_SETTLE_HALT, waterHalt);
					}
					LoggerUtils.d("555 电子签名失败次数:" + failTimes + ",最大次数-1:" + (maxTimes-1) + ",resCode=" + resCode);
					if (failTimes >= maxTimes - 1 || resCode == STEP_QUIT){
						LoggerUtils.d("555 电子签名上送失败重打印..before");
						//打印失败流水
						//这里应该阻塞打印，不然，后面直接开始打印结算单会失败，因为此时这笔签购单还没打完
						PrintBean bean = new PrintBean();
						bean.setPrintMessage("打印上送电子签名失败签购单");
						bean.setPrintType(Const.PrintStyleConstEnum.PRINT_WATER);
						bean.setWater(water);
							
						bean =  showUIPrintFragment(bean);
						if (!bean.getPrintResult()) {
							LoggerUtils.d("111 打印上送电子签名失败签购单。。。失败！！！");
						}


						// 删除对应jbig图片
						file.delete();
						continue;
					}
					break;
				}
				
			}//end 遍历流水
			
			if (isFirstFail){ //过程中没失败
				isFirstFail = false;
				ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_SETTLE_HALT, waterSum + 1);
				break;
			}else{
				failTimes++;
			} 
		}
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSIGN_UNSEND, 0);
		ParamsUtils.setInt(ParamsTrans.PARAMS_TIMES_ELECSIGN_HAVE_SEND, 0);
		//上送位置覆位
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_ONLINE_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_OFFLINE_HALT, 1);
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_ELECSEND_SETTLE_HALT, 1);
		commonBean.setResult(true);
		return SUCC;
	}
	
	@SuppressWarnings("incomplete-switch")
	private int packAndComm(){
		
		String requst = null;
		try{
			requst = iso8583.pack();
			// 替换64域
			requst = replaceMac(requst);
		}catch (Exception e) {
			e.printStackTrace();
			showToast(getText(R.string.error_pack_exception)+e.getMessage());
			return TransConst.STEP_FINAL;
		}
		// 流水号增1
		addTraceNo();
		
		/** 通讯 */
		communicationBean = initCommunicationBean();
		communicationBean.setContent(getCommunitionTipMsg());
		communicationBean.setData(requst);
		communicationBean = showUICommunication(communicationBean);
		
		switch(communicationBean.getStepResult()) {
		case BACK:
			// 提示冲正取消，交易停止
			showToast(getText(R.string.result_trans_cancel));
//			if (CommunicationFailReason.CONNECT_FAIL == communicationBean.getReason()){
//				//连接出错,如冲正,批上送,脚本通知等交易，可过滤处理
//				return STEP_FINISH; 
//			}
//			return TransConst.STEP_FINAL;
			return STEP_CANCEL;
				
		case FAIL:
			//网络连接异常, 重试
			showToast(getText(R.string.result_network_offline));
			if (CommunicationFailReason.CONNECT_FAIL == communicationBean.getReason()){
				//连接出错,如冲正,批上送,脚本通知等交易，可过滤处理
				return STEP_FINISH; 
			}
			return FINISH;
			
		case TIME_OUT:
			showToast(getText(R.string.result_network_offline));
			return FINISH;
			
		case SUCCESS:
			
			try {
				iso8583.initPack();
				iso8583.unpack(communicationBean.getData());
				int resCode = checkRespons(iso8583, pubBean);
				switch (resCode) {
				case 0:
					// 正常情况
					break;
				case 1:
					String data = communicationBean.getData();
					String resMac = data.substring(data.length()-16);
					String mac = getMac(data.substring(0, data.length()-16));
					if (!resMac.equals(mac)) {
						// MAC校验不对
						showToast(getText(R.string.response_check_fail_mac_error));
						return FINISH;
					} 
					break;
				default:
					// 校验返回数据其他情况
					showToast(getErrorInfo(resCode));
					return FINISH;
				}
				
			}catch(Exception e){
				e.printStackTrace();
				//应答数据处理异常，重试
				return FINISH;
			}
		}
		
		return STEP_CONTINUE;
	}
	
	
}
