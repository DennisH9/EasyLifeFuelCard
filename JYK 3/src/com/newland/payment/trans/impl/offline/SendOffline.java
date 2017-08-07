package com.newland.payment.trans.impl.offline;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.EmvStatus;
import com.newland.payment.common.TransConst.SendStatus;
import com.newland.payment.common.TransConst.TransDeal;
import com.newland.payment.common.TransType;
import com.newland.payment.common.tools.AnswerCodeHelper;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.CommunicationBean;
import com.newland.payment.trans.bean.CommunicationBean.CommunicationFailReason;
import com.newland.payment.trans.bean.PubBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 离线上送
 * @author linld
 * @date 2015-05-29
 */
public class SendOffline extends AbstractBaseTrans {

	private CommonBean<Integer> commonBean;
	private CommunicationBean communicationBean;
	private WaterService waterService = null;
	private Water water = null;
	private int unSendOfflineNum = 0;
	private int waterSum;
	private int maxTimes = 3;
	private int failTimes = 0;	// 上送失败次数
	private int connectTimes = 0; // 连接失败次数
	private int waterHalt = 0;
	private boolean isFirstFail = true;
	private int count = 0;
	private int dealType = TransDeal.NORMAL;
	
	public SendOffline(int dealType){
		this.commonBean = new CommonBean<Integer>();
		this.dealType = dealType;
	}
	
	public SendOffline(CommonBean<Integer> commonBean){
		this.commonBean = commonBean;
		this.dealType = TransDeal.NORMAL;
	}

// 检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	@Override
	protected void checkPower() {
		super.checkPower();
		checkWaterCount = false;
		checkSettlementStatus = false;
		checkManagerPassword = false;
		checkCardExsit = false;	
		transcationManagerFlag = false; // 不开启事务
	}
	
	@Override
	protected int init() {
		int res = super.init();
		isFirstFail = true;
		return res;
	}
	
	@Override
	protected String[] getCommunitionTipMsg() {
		return new String[]{String.format("正在上送离线交易\r\n第[%d]笔\r\n请稍候",count++)};
	}
	
	@AnnStep(stepIndex = 1)
	public int step_SendOffline() {
		unSendOfflineNum = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_OFFLINE_UNSEND, 0);
		LoggerUtils.d("111 离线未上送笔数:" + unSendOfflineNum);
		
		if (unSendOfflineNum <= 0){
			commonBean.setResult(true);
			return SUCC;
		}
		
		failTimes = ParamsUtils.getInt(ParamsTrans.PARAMS_TIMES_OFFLINE_HAVE_SEND, 0);
		maxTimes =  ParamsUtils.getInt(ParamsConst.PARAMS_KEY_OFFSEND_RESEND_TIMES, 3);
		waterService = new WaterServiceImpl(activity);
		waterSum = waterService.getWaterCount();
		
		connectTimes = 0;
		
		while(failTimes <= maxTimes){
			count = 1;
			waterHalt = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_OFFLINE_SEND_HALT, 1);
			LoggerUtils.d("111 离线上送中断流水:" + waterHalt + "流水总数：" + waterSum);
			for (;waterHalt <= waterSum; waterHalt++){
				water = waterService.findById(waterHalt);
				if (null == water){
					continue;
				}
				
				switch (water.getTransType()){
				case TransType.TRANS_EC_PURCHASE:
					break;
				case TransType.TRANS_SALE:  //此处是考虑,目前很少见的借贷记脱机消费
					if (EmvStatus.EMV_STATUS_OFFLINE_SUCC != water.getEmvStatus()){
						continue;
					}
					LoggerUtils.d("dd 消费交易-脱机成功-进行离线上送");
					break;
				default:
					continue;				
				}
				LoggerUtils.d("111 当前记录位置：" + waterHalt);
				LoggerUtils.d("111 当笔流水号：" + water.getTrace());
				LoggerUtils.d("111 上送状态(次数)：" + water.getOffSendFlag());
				LoggerUtils.d("111 最大重发次数:" + maxTimes);
				if (water.getOffSendFlag() > maxTimes){
					LoggerUtils.d("111 当笔不送.");
					continue;
				}
				
				pubBean = new PubBean();
				/** 打包*/
				switch (water.getTransType()){
				case TransType.TRANS_EC_PURCHASE:
				case TransType.TRANS_SALE:
					packSaleOffline(water, 0);
					break;
				default:
					continue;
				}
				
				int resCode = packAndComm(); //成功以及被拒时修改sendFlag
		LoggerUtils.d("111 SendOffline---packAndComm->resCode:" + resCode);		
				switch (resCode){
				case STEP_FINISH: //连接失败,当前交易继续尝试连接三次
					connectTimes++;
					if (connectTimes > maxTimes){
						commonBean.setResult(false);
						return FINISH;
					}else{
		LoggerUtils.d("111 连接失败-本笔流水重连");				
						waterHalt --;
						count--;
						continue;
					}
				case STEP_CANCEL: //取消,退出上送,退出交易
					commonBean.setResult(false);
					return FINISH;
				case STEP_CONTINUE: //成功,继续下一笔
					break;
				default: //上送失败
					LoggerUtils.d("111 离线上送->失败！");
					
					if (water.getOffSendFlag() < maxTimes){
						water.setOffSendFlag(water.getOffSendFlag() + 1);
					}else if (water.getOffSendFlag() == SendStatus.DECLINE){
						//未上送笔数减一
						delOfflineUnSendNum();
					}else{
						water.setOffSendFlag(SendStatus.FAIL);
						//未上送笔数减一
						delOfflineUnSendNum();
					}
					
					waterService.updateWater(water);
					
					//记录失败中断
					if (isFirstFail && water.getOffSendFlag() <= maxTimes){
						isFirstFail = false;
						LoggerUtils.d("111 记录离线上送失败位置:" + waterHalt);
						ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_OFFLINE_SEND_HALT, waterHalt);
					}
					
					continue;
				} //end switch
				
				LoggerUtils.d("111 离线上送->成功！");
				showToast(getText(R.string.settle_send_succ));
				
				//修改上送标志
				water.setOffSendFlag(SendStatus.SUCC);
				//更新系统参考号
				water.setReferNum(pubBean.getSystemRefNum());
				water.setInterOrg(pubBean.getInternationOrg());
				//小额代授权方式
				if (water.getAddition().startsWith("02", 16)){
					//保存原来手输的授权码
					water.setOldAuthCode(water.getAuthCode());
					//更新后台返回的授权码
					water.setAuthCode(pubBean.getAuthCode());
				}
				/**
				 * 保存电子签名上送,信息
				 */
				water.setSettleDate(pubBean.getSettleDate());
				water.setIisCode(pubBean.getIsoField44().getIisCode());
				water.setAcqCode(pubBean.getIsoField44().getAcqCode());
				if (pubBean.getIsoField63().length() >= 23){
					water.setIisInfo(pubBean.getIsoField63().substring(3, 23));
				}
				if (pubBean.getIsoField63().length() >= 43){
					water.setCupInfo(pubBean.getIsoField63().substring(23, 43));
				}
				if (pubBean.getIsoField63().length() >= 63){
					water.setMerAcqInfo(pubBean.getIsoField63().substring(43, 63));
				}
				
				waterService.updateWater(water);
				
				//未上送笔数减一
				delOfflineUnSendNum();
			
			} //end for
			unSendOfflineNum = ParamsUtils.getInt(ParamsTrans.PARAMS_NUM_OFFLINE_UNSEND);
			LoggerUtils.d("111 离线上送,遍历流水后,剩下未上送成功的脱机交易笔数:" + unSendOfflineNum);
			if (unSendOfflineNum > 0){ //还有未上送流水,说明过程中有失败
				failTimes++;
				ParamsUtils.setInt(ParamsTrans.PARAMS_TIMES_OFFLINE_HAVE_SEND, failTimes);
				LoggerUtils.d("111 失败第"+failTimes+"次");
			} else {
				ParamsUtils.setInt(ParamsTrans.PARAMS_TIMES_OFFLINE_HAVE_SEND, 0);
				//将上送位置移到最后一笔流水位置
				ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_OFFLINE_SEND_HALT, waterSum + 1);
				commonBean.setResult(true);
				return SUCC;
			}

		} //end while
		
		ParamsUtils.setInt(ParamsTrans.PARAMS_TIMES_OFFLINE_HAVE_SEND, 0);
		//将上送位置移到最后一笔流水位置
		ParamsUtils.setInt(ParamsTrans.PARAMS_NUM_OFFLINE_SEND_HALT, waterSum + 1);
		commonBean.setResult(true);
		return SUCC;
	}

	
	@Override
	protected void release() {
		if (TransDeal.SPECIAL == dealType){
			super.release();
		}
		goOnStep(commonBean);	
	}
	
	
	/**
	 * 打包、通讯,回包检查,成功及被拒时修改上送状态
	 * @return
	 */
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
		communicationBean.setData(requst);
		communicationBean.setContent(getCommunitionTipMsg());
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
		LoggerUtils.d("111 通讯失败,连接失败！");		
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
				String responseCode = iso8583.getField(39);
				LoggerUtils.d("111 上送应答码："+responseCode);
				if("00".equals(responseCode) 
					|| "94".equals(responseCode)){
					//成功,退出外面处理
					if (!StringUtils.isNullOrEmpty(pubBean.getIsoField63())){
						pubBean.setInternationOrg(pubBean.getIsoField63().substring(0, 3));
					}
					water.setOffSendFlag(SendStatus.SUCC);
					return STEP_CONTINUE;
				} else {
					water.setOffSendFlag(SendStatus.DECLINE);
					showToast(getText(R.string.common_respon)+
							responseCode+"\r\n"+
							 AnswerCodeHelper.getAnswerCodeCN(responseCode));
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
