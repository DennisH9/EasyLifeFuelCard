package com.newland.payment.trans.impl;
import java.io.IOException;
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
public final class JycMposLoad extends AbstractBaseTrans {
	
	private String mPosKey = null;
	// USB端接口
	private TransPort tport;
	boolean isruntime = true;
	private class Step {
		public final static int TRANS_START = 1;
		public final static int COMM_MPOS = 2;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	public JycMposLoad(ThirdInvokeBean thirdInvokeBean) {
		// TODO Auto-generated constructor stub
	}

	public JycMposLoad() {
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
		pubBean.setTransType(TransType.TRANS_LOAD_LOAD);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		
		return res;
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
			ToastUtils.showOnUIThread("串口打开失败");
			return FINISH;
		}
		
		try {
			//发送握手请求
			ret = tport.Write(new byte[]{0x01}, 0, 1,1000);
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
				ret = tport.Read(bufRec, 18, 3);
				LoggerUtils.d("read ret = "+ret);
				if(ret > 0){
					if(ret != 18) {
						transResultBean.setIsSucess(false);
						transResultBean.setContent("读取串口数据错误");
						return Step.TRANS_RESULT;
					}else{
						transResultBean.setIsSucess(true);
						transResultBean.setContent("母pos密钥灌装成功");
						mPosKey = BytesUtils.bytesToHex(bufRec).substring(4,36);
						LoggerUtils.d("母POS密钥数据11:" + mPosKey);
						//传输密钥灌装
						SecurityModule sm = SecurityModule.getInstance();
//						sm.loadMainKey(9, mPosKey);
						ParamsUtils.setString(ParamsConst.PARAMS_TRANSPORT_KEY,mPosKey);
						return Step.TRANS_RESULT;
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
