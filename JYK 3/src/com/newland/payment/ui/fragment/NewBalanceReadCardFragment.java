package com.newland.payment.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.Const.DesMode;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.bean.CardBean;
import com.newland.pos.sdk.card.CardModule;
import com.newland.pos.sdk.interfaces.CardListener;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 余额查询读卡
 */
public class NewBalanceReadCardFragment extends BaseFragment implements CardListener {
    
    private CardBean bean;
	private List<Integer> supportMode;
	private static CardModule cardModule;

	public NewBalanceReadCardFragment(long timeOut) {
		super(0);
	}
	public NewBalanceReadCardFragment() {
		super(0);
	}
	public static NewBalanceReadCardFragment newInstance(CardBean bean) {
		NewBalanceReadCardFragment fragment;
		if (bean.getTimeOut() != null) {
			fragment = new NewBalanceReadCardFragment(Long.valueOf(bean.getTimeOut()));
		}
		else {
			fragment = new NewBalanceReadCardFragment();
		}

		fragment.bean = bean;
		return fragment;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	mFragmentView = inflater.inflate(R.layout.layout_balance_readcard, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return mFragmentView;
    }

	@Override
	public void ToastShow(Object info) {
		ToastUtils.show(context, info);
	}

	@Override
	public void onCardBack() {
		LoggerUtils.d("onCardBack Start!!!");
		onBack();
	}

	@Override
	public void onCardComfrim() {
		LoggerUtils.d("onCardComfrim Start!!!");
		onSucess();
	}

	@Override
	public void onCardFail() {
		LoggerUtils.d("onCardFail Start!!!");
		onFail();
	}

	@Override
	public void onCardSucess() {
		LoggerUtils.d("onCardSucess Start!!!");
		onSucess();
	}

	@Override
	public void onCardTimeOut() {
		LoggerUtils.d("onCardTimeOut Start!!!");
		onTimeOut();		
	}

	@Override
	public void onRefreshDate() {
		refreshDate();
	}

	@Override
	protected void initData() {
		bean.setIsPosSupportIc(ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_SUPPORT_IC, true));
		cardModule = new CardModule(bean,this);
		cardModule.initData();
		CardModule.setTMkIndex(ParamsUtils.getTMkIndex());
		if(DesMode.DES3.equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_ENCRYPT_MODE))){
			CardModule.setEncryptMode(DesMode.DES3);
		}
		else{
			CardModule.setEncryptMode(DesMode.DES);
		}
		if(Const.YES.equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_ENCPYTRACK))){
			CardModule.setIs_encrypt_track(Const.YES);
		}
		else{
			CardModule.setIs_encrypt_track(Const.NO);
		}
		setTitle(bean.getTitle());
//		InputUtils.setMaxLength(etCardNo, 19);
		refreshDate();
//		etCardNo.setTextKeepState(etCardNo.getText().toString());
	}

	@Override
	protected void initClickEvent(View view) {
		
	}

	@Override
	protected void initEvent() {
		cardModule.startCheckCard();		
	}

	@Override
	protected BaseBean getBean() {
		return bean;
	}
	
	@Override
	protected boolean doClickBackEvent() {
		cancelCardReader();
		return false;
	}
	
	@Override
	public void onPause() {
		cardModule.cancelCheckCard();
		super.onPause();
	}
	
	private void refreshDate(){
		supportMode = new ArrayList<Integer>();
		int readCardType = bean.getInputMode();
		if ((readCardType&ReadcardType.SWIPE) == ReadcardType.SWIPE) {
			supportMode.add(ReadcardType.SWIPE);
		}
		if ((readCardType&ReadcardType.ICCARD) == ReadcardType.ICCARD) {
			supportMode.add(ReadcardType.ICCARD);
		}
		if ((readCardType&ReadcardType.RFCARD) == ReadcardType.RFCARD) {
			supportMode.add(ReadcardType.RFCARD);
		}
	}
	
	/**
	 * 判断是否 IC卡
	 */
	protected boolean isIcCard(String serviceCode){
		if (StringUtils.isEmpty(serviceCode)) {
			return false;
		}
		if(serviceCode.startsWith("2") || serviceCode.startsWith("6")){
			return true;
		}
		return false;
	}
	
	private void cancelCardReader(){
		/** 取消检卡 */
		new Thread(){
			public void run() {
				LoggerUtils.d("111 cancel reader card");
//				DeviceController.getInstance().reset();
				cardModule.cancelCheckCard();
				LoggerUtils.d("111 cancel reader card end");
				onBack();
			}
		}.start();
	}
	
	@Override
	protected boolean doClickHomeEvent() {
		return doClickBackEvent();
	}
}
