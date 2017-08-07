package com.newland.payment.trans.impl;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.Const.DesMode;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.ThirdInvokeBean;
import com.newland.payment.trans.bean.field.ISOField60;
import com.newland.payment.trans.bean.field.ISOField62_WorkKey;
import com.newland.payment.ui.fragment.BlankFragment;
import com.newland.pos.sdk.device.Device;
import com.newland.pos.sdk.security.SecurityModule;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.TimeUtils;


/**
 *签到
 */
public final class Login extends AbstractBaseTrans {

	private class Step {
		public final static int TRANS_START = 1;
		public final static int PACK_AND_COMM = 2;
		public final static int SINGIN_DEAL = 3;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	public Login(ThirdInvokeBean thirdInvokeBean) {
		// TODO Auto-generated constructor stub
	}

	public Login() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void checkPower(){
		super.checkPower();
		checkSingIn = false; // 不检查签到状态
		checkWaterCount = false; // 不检查流水累计笔数上限
		checkSettlementStatus = false; // 不检查结算状态
		checkCardExsit = false;//交易结束不检卡
	}
	
	@Override
	protected int init() {
		int res = super.init();
		transResultBean.setIsSucess(false);
		//先置成未签到
		ParamsUtils.setBoolean(ParamsTrans.PARAMS_FLAG_SIGN, false);
		pubBean.setTransType(TransType.TRANS_LOGIN);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		return res;
	}
	
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		transSwitchContent(BlankFragment.newInstance());
		return Step.PACK_AND_COMM;
	}

	@Override
	protected String[] getCommunitionTipMsg() {
		return new String[]{getText(R.string.comm_sign_waitting)};
	}

	@AnnStep(stepIndex = Step.PACK_AND_COMM)
	public int step_PackAndComm() {
		
		initPubBean();
		ISOField60 isoField60 = new ISOField60();
		isoField60.setFuncCode(getF601FuncCode(pubBean.getTransType()));
		if("1".equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_ENCRYPT_MODE))){
			isoField60.setNetManCode("001");
		} else{
			if("1".equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_ENCPYTRACK))){
				isoField60.setNetManCode("004");
			}
			else{
				isoField60.setNetManCode("003");	
			}
		}
		pubBean.setIsoField60(isoField60);
		
		//入网证号 + 硬件序列号
		String tmpField62 = Device.getCertNo() + Device.getSN();
		LoggerUtils.d("111 入网证号：[" + Device.getCertNo() + "]");
		LoggerUtils.d("111 硬件序列号：[" + Device.getSN() + "]");
		pubBean.setIsoField62("Sequence No" + String.format("%d", tmpField62.length()) + tmpField62);
		
		iso8583.initPack();
		
		iso8583.setField(0, pubBean.getMessageID());
		iso8583.setField(11, pubBean.getTraceNo());
		
		iso8583.setField(41, pubBean.getPosID());
		iso8583.setField(42, pubBean.getShopID());
		iso8583.setField(60, pubBean.getIsoField60().getString());
		 
//		iso8583.setField(62, pubBean.getIsoField62());
//		iso8583.setField(63, pubBean.getCurrentOperNo() + " ");
		
		int resCode = dealPackAndComm(false, false, true);
		if (STEP_CONTINUE != resCode){
			return Step.TRANS_RESULT;
			/*
			//连接出错
			if (STEP_FINISH == resCode){
				return Step.TRANS_RESULT;
			}
			return resCode;
			*/
		}
		return Step.SINGIN_DEAL;
	}
	
	/** 安装密钥,更新时间,更新批次号*/
	@AnnStep(stepIndex = Step.SINGIN_DEAL)
	public int step_LoadAndUpdate(){
		try {
			String merchantName = iso8583.getField(43).trim();
			String workKey = iso8583.getField(62);
//			String workKey = "00DD600F71D757FBACDD600F71D757FBAC0CD7DC4903A8B88B016E66ACAF9D9AFB7C6E66ACAF9D9AFB7CADC67D8473BF2F0602AB7EE2B8674D4AB8AB7EE2B8674D4AB800962B60AA556E65";
			//TODO  下发索引先不处理
			ISOField62_WorkKey field62 = new ISOField62_WorkKey(workKey);
			if(field62.isErr()){
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.sign_in_key_len_error));
				return Step.TRANS_RESULT;
			}
			LoggerUtils.i("PIK:" + field62.getPIK() + " PIKChe:"
					+ field62.getPIKCheck());
			LoggerUtils.i("MAK:" + field62.getMAK() + " MAKChe:"
					+ field62.getMAKCheck());
			LoggerUtils.i("TMK:" + field62.getTMK() + " TMKChe:"
					+ field62.getTMKCheck());
			//传输秘钥
			String transferKey = ParamsUtils.getString(ParamsConst.PARAMS_TRANSPORT_KEY);

			SecurityModule sm = SecurityModule.getInstance();
			LoggerUtils.d("dd 当前传输秘钥：" + transferKey);
			LoggerUtils.d("dd 当前主密钥索引：" + ParamsUtils.getTMkIndex());		
			sm.setCurrentMainKeyIndex(ParamsUtils.getTMkIndex());
			//有下发主密钥密文
			if (field62.getTMK() != null) {
				String tmk = sm.softundes3(transferKey, field62.getTMK());
				LoggerUtils.d("hjh    主密钥明文tmk ："+ tmk);
				String tmkcheck = sm.softdes3(tmk, "0000000000000000");
				if(!tmkcheck.substring(0, 16).equals(field62.getTMKCheck())){
					transResultBean.setIsSucess(false);
					transResultBean.setContent(getText(R.string.sign_in_load_tmk_fail));
					return Step.TRANS_RESULT;
				}
				sm.loadMainKey(ParamsUtils.getTMkIndex(), tmk);
//				sm.updatemainkey(9,ParamsUtils.getTMkIndex(),field62.getTMK());
			}
			try{
				if (field62.getPIK() != null) {
					sm.loadWorkKey(Const.WorkKeyType.PIN, field62.getPIK(),
							field62.getPIKCheck());
				}
				
			}catch(Exception e){
				e.printStackTrace();
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.sign_in_load_pin_fail));
				return Step.TRANS_RESULT;
			}
			
			try{
				if (field62.getMAK() != null) {
					sm.loadWorkKey(Const.WorkKeyType.MAC, field62.getMAK(),
							field62.getMAKCheck());
				}
				
			}catch(Exception e){
				e.printStackTrace();
				transResultBean.setIsSucess(false);
				transResultBean.setContent(getText(R.string.sign_in_load_mac_fail));
				return Step.TRANS_RESULT;
			}
			if (DesMode.DES3.equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_ENCRYPT_MODE))
					&& Const.YES.equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_ENCPYTRACK))){
				
				try{
					if (field62.getTDK() != null) {
						sm.loadWorkKey(Const.WorkKeyType.TDK, field62.getTDK(),
								field62.getTDKCheck());
					} else {
						//后台没有返回磁道密钥,会导致刷卡失败-20150728,lld
						LoggerUtils.d("后台没有返回磁道密钥->将磁道加密开关关闭");
						ParamsUtils.setString(ParamsConst.PARAMS_KEY_BASE_ENCPYTRACK, Const.NO);
					}
					
				}catch(Exception e){
					e.printStackTrace();
					transResultBean.setIsSucess(false);
					transResultBean.setContent(getText(R.string.sign_in_load_tdk_fail));
					return Step.TRANS_RESULT;
				}
			}
			
			
			String time = iso8583.getField(12);
			String date = iso8583.getField(13);
			String batchNo = iso8583.getField(60).substring(2, 8);

			LoggerUtils.i("time:" + time + " date:" + date);
			LoggerUtils.i("batchNo:" + batchNo);
			int year = TimeUtils.getCurrentYear();
			LoggerUtils.i("current year:" + year);
			
			TimeUtils.updateSystemTime(date, time, year);

			ParamsUtils.setString( 
					ParamsConst.PARAMS_KEY_BASE_BATCHNO, batchNo);
			
			// 设置签到状态
			ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_RESIGN, false);
			ParamsUtils.setBoolean(ParamsTrans.PARAMS_FLAG_SIGN, true);
			ParamsUtils.setString(ParamsConst.PARAMS_RUN_LOGIN_DATE, date);
			ParamsUtils.setString(ParamsConst.PARAMS_KEY_BASE_MERCHANTNAME,merchantName);
			transResultBean.setIsSucess(true);
			transResultBean.setContent(getText(R.string.sign_in_trans_succse));
		} catch (Exception e) {
			e.printStackTrace();
			transResultBean.setIsSucess(false);
			transResultBean.setContent(getText(R.string.sign_in_trans_fail));
		}
		return Step.TRANS_RESULT;
	}
	
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult(){	
		transResultBean.setTitle(pubBean.getTransName());
		//transResultBean = showUITransResult(transResultBean);
//		if (transResultBean.getContent() != null){
//			showToast(transResultBean.getContent());
//		}
		transResultBean = showUITransResult(transResultBean);
		if(transResultBean.getIsSucess()){
			return SUCC;
		}
		return FINISH;
	}


}
