package com.newland.payment.trans.impl;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.common.Const;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.InputInfoBean;
import com.newland.payment.trans.bean.ThirdInvokeBean;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.fragment.MposLoadFragment;
import com.newland.pos.sdk.security.SecurityModule;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.ISO8583;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;
import com.newland.usb.TransPort;
import com.newland.usb.USBPortImpl;


/**
 *母pos密钥导入
 */
public final class JycMposDown extends AbstractBaseTrans {
	
	private String mPosKey = null;
	private String mPosTransNo = null;
	private String mPosSn = null;
	// USB端接口
	private TransPort tport;
	boolean isruntime = true;
	private class Step {
		public final static int TRANS_START = 1;
		public final static int COMM_MPOS = 2;
		public final static int COMM_MPOS_BEGIN = 3;
		public final static int PACK_AND_COMM = 4;
		public final static int COMM_MPOS_END = 5;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	public JycMposDown(ThirdInvokeBean thirdInvokeBean) {
		// TODO Auto-generated constructor stub
	}

	public JycMposDown() {
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
		pubBean.setTransType(TransType.TRANS_MPOS_DOWN);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		
		return res;
	}
	
	@Override
	protected void release() {

		super.release();
	}
	
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		
		return Step.COMM_MPOS;
	}
	
	@AnnStep(stepIndex = Step.COMM_MPOS)
	public int step_CommMpos() {
		
		InputInfoBean baseBean = new InputInfoBean();
		baseBean.setTitle(getTitle(pubBean.getTransType()));
		MposLoadFragment mposDown = MposLoadFragment.newInstance(baseBean);
		transSwitchContent(mposDown);

		/**
		 * MPOS---N900
		 */
		tport = new USBPortImpl(activity);
		LoggerUtils.d("MposDown init");

		byte[] bufRec = new byte[64];
		int ret = 0;
		if(!tport.Open()){
			LoggerUtils.d("MposDown 串口打开失败");
			ToastUtils.showOnUIThread("串口打开失败");
			return FINISH;
		}
		
		try {
			//发送握手请求
			ret = tport.Write(new byte[]{0x03}, 0, 1,1000);
			if(ret < 0) {
				transResultBean.setIsSucess(false);
				transResultBean.setContent("数据发送失败");
				return Step.TRANS_RESULT;
			}
			
			isruntime = false;
			LoggerUtils.d("计时器启动");
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					isruntime = true;
				}
			}, 2000);
			
			while(!isruntime){
				ret = tport.Read(bufRec, 36, 3);
				LoggerUtils.d("read ret = "+ret);
				if(ret > 0){
					if(ret != 36) {
						transResultBean.setIsSucess(false);
						transResultBean.setContent("读取串口数据错误");
						return Step.TRANS_RESULT;
					}else{
						
//						transResultBean.setIsSucess(true);
//						transResultBean.setContent("母pos密钥灌装成功");
						mPosKey = BytesUtils.bytesToHex(bufRec).substring(4,36);
						mPosSn = BytesUtils.bytesToHex(bufRec).substring(36,60);
						mPosTransNo = BytesUtils.bytesToHex(bufRec).substring(60,72);
						LoggerUtils.d("母POS密钥数据:" + mPosKey);
						LoggerUtils.d("母POS终端序列号:" + mPosSn);
						LoggerUtils.d("母POS终端流水号:" + mPosTransNo);
						
						return Step.COMM_MPOS_BEGIN;
					}
				}
			}
			
			transResultBean.setIsSucess(false);
			transResultBean.setContent("读取串口数据超时");
			return Step.TRANS_RESULT;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return Step.TRANS_RESULT;
	}
	@AnnStep(stepIndex = Step.COMM_MPOS_BEGIN)
	public int step_CommMposBegin() {
		
//		try {
//			mposIso8583.loadXmlFile(Const.FileConst.MPOSCUPS8583);
//		} catch (Exception e) {
//			LoggerUtils.e("初始化 iso8583失败");
//			e.printStackTrace();
//		}
		return Step.PACK_AND_COMM;
	}

	@Override
	protected String[] getCommunitionTipMsg() {
		return new String[]{"母pos密钥下载..."};
	}

	@AnnStep(stepIndex = Step.PACK_AND_COMM)
	public int step_PackAndComm() throws UnsupportedEncodingException {
		initPubBean();
		
		//入网证号 + 硬件序列号
		mposIso8583.initPack();
		
		mposIso8583.setField(0, pubBean.getMessageID());
		mposIso8583.setField(3, pubBean.getProcessCode());
		mposIso8583.setField(11, StringUtils.hexToStr(mPosTransNo));
		
		mposIso8583.setField(41, "NEWLAND");
		mposIso8583.setField(42, "SP50");
		 
		mposIso8583.setField(43, "12"+mPosSn);
		int resCode = dealPackAndComm3(false, false, false);
		if (STEP_CONTINUE != resCode){
			return Step.TRANS_RESULT;
		}
		
		/**
		 * 返回码判断
		 */
		String responseCode = pubBean.getResponseCode();
		if(!responseCode.equals("00")){
			transResultBean.setIsSucess(false);
			if(responseCode.equals("01")){
				transResultBean.setContent("01 无效母pos");
			}else if(responseCode.equals("A7")){
				transResultBean.setContent("01  密钥转加密失败");
			}else{
				transResultBean.setContent(responseCode+" 未知返回码");
			}
			return Step.TRANS_RESULT;
		}
		
		return Step.COMM_MPOS_END;
	}
	
	/** N900-----MPOS
	 * @throws Exception */
	@AnnStep(stepIndex = Step.COMM_MPOS_END)
	public int step_LoadEnd() throws Exception{
		
		/**
		 * 接收数据处理
		 */
		String filed62 = mposIso8583.getField(62);
		String mPosEncKek = filed62.substring(6, 38);
		String checkValue = filed62.substring(38, 54);

		SecurityModule sm = SecurityModule.getInstance();
		String mPosKek = sm.softundes3(mPosKey, mPosEncKek);

		if(!checkValue.equals(sm.softdes3(mPosKek, "0000000000000000").substring(0,16))){
			transResultBean.setIsSucess(false);
			transResultBean.setContent("母pos校验错误");
			return Step.TRANS_RESULT;
		}
		
		/**
		 * MPOS---N900
		 */
		tport = new USBPortImpl(activity);
		LoggerUtils.d("MposDown end");

		byte[] bufSend = new byte[64];
		int ret = 0;
		
		if(!tport.Open()){
			ToastUtils.showOnUIThread("串口打开失败");
			return FINISH;
		}
		
		System.arraycopy(new byte[]{0x02,0x00}, 0, bufSend,0, 2);
		bufSend[5] = 0x02;
		//TODO 测试
//		System.arraycopy(BytesUtils.hexStringToBytes("1234567890ABCDEF1234567890ABCDEF"), 0, bufSend,2, 16);
		System.arraycopy(BytesUtils.hexStringToBytes(mPosKek), 0, bufSend,2, 16);
		try {
			//发送握手请求
			ret = tport.Write(bufSend, 0, 18,1000);
			if(ret < 0) {
				transResultBean.setIsSucess(false);
				transResultBean.setContent("数据发送失败");
				return Step.TRANS_RESULT;
			}
			
			
			transResultBean.setIsSucess(true);
			transResultBean.setContent("下发成功，母pos上查看结果");
			return Step.TRANS_RESULT;
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return Step.TRANS_RESULT;
	}
	
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult(){
		tport.Clear();
		try {
			tport.Close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		transResultBean.setTitle(pubBean.getTransName());
		transResultBean = showUITransResult(transResultBean);

		if(transResultBean.getIsSucess()){
			pubBean.setResponseCode("00");
			return SUCC;
		}
		
		return FINISH;
	}

}
