package com.newland.payment.trans.impl;

import com.newland.payment.R;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.ThirdInvokeBean;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.fragment.TransSelectListFragment;
import com.newland.pos.sdk.bean.MessageTipBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

public class Reprint extends AbstractBaseTrans {

	private Water oldWater = null;
	
	private ThirdInvokeBean thirdInvokeBean = null;
	private class Step {
		public final static int TRANS_START = 1;
		public final static int INPUT_TRACENO = 2;
		public final static int SHOW_WATER = 3;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}

	public Reprint(ThirdInvokeBean thirdInvokeBean) {
		this(TransType.TRANS_VOID_SALE);
		this.thirdInvokeBean = thirdInvokeBean;
	}

	public Reprint(int transType){
		
	}	
	
	public Reprint() {
		// TODO Auto-generated constructor stub
	}

	// 检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	protected void checkPower() {
		super.checkPower();
		checkManagerPassword = false;
		checkWaterCount = false;
		checkIsExistWater = false;
		checkSettlementStatus = false;
		checkSingIn = false;
	}

	@Override
	protected int init() {
		int res = super.init();
		// 设置交易类型,交易属性
		pubBean.setTransType(TransType.TRANS_NULL);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
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
//		if(this.thirdInvokeBean != null){
//			pubBean.setTraceNo(thirdInvokeBean.getTraceNo());
//			WaterServiceImpl impl = new WaterServiceImpl(MainActivity.getInstance());
//			LoggerUtils.d("hjh   TraceNo :"+pubBean.getTraceNo());
//			oldWater = impl.findByTrace(StringUtils.fill(pubBean.getTraceNo(), "0", 6, true));
//		}

		oldWater = stepProvider.inputOldTraceNo(pubBean,TransType.TRANS_REPRINT);
		if (oldWater == null){
			return FINISH;
		} else {
			return Step.SHOW_WATER;
		}
	}

	@AnnStep(stepIndex = Step.SHOW_WATER)
	public int step_showWater() {
		TransSelectListFragment transSelectListFragment = TransSelectListFragment.newInstance(oldWater,true,thirdInvokeBean);
		MainActivity.getInstance().switchContent(transSelectListFragment);
		try {
			synchronized(thirdInvokeBean) {
				thirdInvokeBean.wait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Step.TRANS_RESULT;
	}

	
	/** 结果显示、打印处理 */
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult() {
		return FINISH;
	}
}
