package com.newland.payment.trans.manage.impl;


import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.payment.trans.bean.field.ISOField62_Status;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 状态上送
 *
 * @author CB
 * @date 2015-5-20 
 * @time 下午4:58:13
 */
public class StatusSend extends AbstractBaseTrans{


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
		checkCardExsit = false;
		transcationManagerFlag = false; // 不开启事务
	}
	
	@Override
	protected int init() {
		int res = super.init();
		pubBean.setTransType(TransType.TRANS_STATUS_SEND);
		pubBean.setTransName(getText(R.string.setting_other_download_state_upload));
		return res;
	}

	@Override
	protected String[] getCommunitionTipMsg() {
		return new String[]{getText(R.string.setting_other_download_state_upload)};
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
			ISOField60 isoField60 = new ISOField60(pubBean.getTransType(), false);
			pubBean.setIsoField60(isoField60);	
			ISOField62_Status isoField62 = new ISOField62_Status();
			
			//键盘状态
			isoField62.setStatusKeyboard("1");
			//密码键盘状态
			isoField62.setStatusKeyboardPassword("1");
			//读卡器状态
			isoField62.setStatusCardReader("1");
			//打印机状态
			isoField62.setStatusPrint("1");
			//显示器状态
			isoField62.setStatusDisplay("1");
			
			//终端应用类型
			LoggerUtils.d("111 终端应用类型[" + ParamsUtils.getString(ParamsConst.PARAMS_KEY_POS_TYPE, "60") +"]");
			isoField62.setPosAppType(ParamsUtils.getString(ParamsConst.PARAMS_KEY_POS_TYPE, "60"));
			//超时时间
			LoggerUtils.d("111 超时时间[" + ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_OUT_TIME) + "]");
			isoField62.setTimeout(String.format("%02d",  ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_OUT_TIME)));
			//重试次数
			LoggerUtils.d("111 重连次数[" + ParamsUtils.getString(ParamsConst.PARAMS_KEY_RECONNECT_TIMES, "3") + "]");
			isoField62.setRetryTime(ParamsUtils.getString(ParamsConst.PARAMS_KEY_RECONNECT_TIMES, "3"));
			//三个交易电话号码之一
			LoggerUtils.d("111 电话号码1[" + StringUtils.fill( ParamsUtils.getString(ParamsConst.PARAMS_KEY_DIAL_PHONE1), " ", 14, false) + "]");
			isoField62.setTransPhone1(StringUtils.fill( ParamsUtils.getString(ParamsConst.PARAMS_KEY_DIAL_PHONE1), " ", 14, false));
			//三个交易电话号码之二
			LoggerUtils.d("111 电话号码2[" + StringUtils.fill( ParamsUtils.getString(ParamsConst.PARAMS_KEY_DIAL_PHONE2), " ", 14, false) + "]");
			isoField62.setTransPhone2(StringUtils.fill( ParamsUtils.getString(ParamsConst.PARAMS_KEY_DIAL_PHONE2), " ", 14, false));
			//三个交易电话号码之三
			LoggerUtils.d("111 电话号码3[" + StringUtils.fill( ParamsUtils.getString(ParamsConst.PARAMS_KEY_DIAL_PHONE3), " ", 14, false) + "]");
			isoField62.setTransPhone3(StringUtils.fill( ParamsUtils.getString(ParamsConst.PARAMS_KEY_DIAL_PHONE3), " ", 14, false));
			//一个管理电话号码
			LoggerUtils.d("111 管理电话[" + StringUtils.fill( ParamsUtils.getString(ParamsConst.PARAMS_KEY_MANAGE_PHONE), " ", 14, false) + "]");
			isoField62.setManagePhone(StringUtils.fill( ParamsUtils.getString(ParamsConst.PARAMS_KEY_MANAGE_PHONE), " ", 14, false));
			//是否支持小费 1 —支持， 0—不支持
			LoggerUtils.d("111 是否支持小费[" + ParamsUtils.getString(ParamsConst.PARAMS_KEY_IS_TIP) + "]");
			isoField62.setIsSupportTip(ParamsUtils.getString(ParamsConst.PARAMS_KEY_IS_TIP));
			//小费百分比
			LoggerUtils.d("111 小费百分比[" + String.format("%02d",  ParamsUtils.getInt(ParamsConst.PARAMS_KEY_BASE_TIPRATE)) + "]");
			isoField62.setTipPercent(String.format("%02d",  ParamsUtils.getInt(ParamsConst.PARAMS_KEY_BASE_TIPRATE)));
			//是否支持手工输入卡号  1 —支持， 0—不支持
			LoggerUtils.d("111 是否支持手输卡号[" + ParamsUtils.getString(ParamsConst.PARAMS_KEY_IS_CARD_INPUT) + "]");
			isoField62.setIsSupportHandInputCardNo(ParamsUtils.getString(ParamsConst.PARAMS_KEY_IS_CARD_INPUT));
			//结算后自动签退 N1 1 —自动， 0—不自动
			LoggerUtils.d("111 结算后自动签退[" + ParamsUtils.getString(ParamsConst.PARAMS_KEY_IS_AUTO_LOGOUT) + "]");
			isoField62.setIsAutoSignOut(ParamsUtils.getString(ParamsConst.PARAMS_KEY_IS_AUTO_LOGOUT));
			//交易重发次数
			LoggerUtils.d("111 交易重发次数[" + ParamsUtils.getString(ParamsConst.PARAMS_KEY_REVERSAL_RESEEND_TIMES) + "]");
			isoField62.setTransRetryTime(ParamsUtils.getString(ParamsConst.PARAMS_KEY_REVERSAL_RESEEND_TIMES));
			//主密钥
			LoggerUtils.d("111 主密钥索引[" + String.format("%d",  ParamsUtils.getInt(ParamsConst.PARAMS_KEY_HEAD_MAIN_KEY_INDEX)) + "]");
			isoField62.setMainKeyIndex(String.format("%d",  ParamsUtils.getInt(ParamsConst.PARAMS_KEY_HEAD_MAIN_KEY_INDEX)));
			LoggerUtils.d("111 自动上送笔数[" + String.format("%02d",  ParamsUtils.getInt(ParamsConst.PARAMS_KEY_MAX_OFFSEND_NUM)) + "]");
			isoField62.setAutoUploadTotal(String.format("%02d",  ParamsUtils.getInt(ParamsConst.PARAMS_KEY_MAX_OFFSEND_NUM)));
			LoggerUtils.d("111 POS拨通率[" + String.format("%04d%05d%03d", 126,1195,98) + "]");
			isoField62.setDialPercent(String.format("%04d%05d%03d", 126,1195,98));
			LoggerUtils.d("111 状态上送62域[" + isoField62.toString() + "]");
			pubBean.setIsoField62(isoField62.toString());
			
			iso8583.initPack();
			iso8583.setField(0, pubBean.getMessageID());
			iso8583.setField(41, pubBean.getPosID());
			iso8583.setField(42, pubBean.getShopID());
			iso8583.setField(60, pubBean.getIsoField60().getString());
			iso8583.setField(62, pubBean.getIsoField62());
			
			int resCode = dealPackAndComm(false, false, true);
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
		
		//这边说明参数下载完成了
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_STATUS_SEND, false);
		transResultBean.setIsSucess(true);
		transResultBean.setContent(getText(R.string.common_upload_success));
	
		return Step.TRANS_RESULT;
	}

	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_showResult(){
		
		showToast(transResultBean.getContent());
		if(transResultBean.getIsSucess()){
			return SUCC;
		}
		return FINISH;
	}
}
