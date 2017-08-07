package com.newland.payment.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.Const.DesMode;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.trans.bean.PubBean;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.bean.CardBean;
import com.newland.pos.sdk.card.CardModule;
import com.newland.pos.sdk.interfaces.CardListener;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 撤销读卡
 */
public class NewVoidSaleReadCardFragment extends BaseFragment implements CardListener {
//	@ViewInject(R.id.tv_voucher_no)
	private TextView voucherNo;
//	@ViewInject(R.id.tv_amount)
    private TextView amount;
//	@ViewInject(R.id.tv_card_no)
    private TextView cardNo;
    
    private CardBean bean;
	private Handler handler;
	private String content;
	private Dialog dialog;
	private List<Integer> supportMode;
	private PubBean pubBean;
	private static CardModule cardModule;

	private final static int CARD_READ_COMFIRM = 0x0A;
	
	public NewVoidSaleReadCardFragment(PubBean pubBean ,long timeOut) {
		super(0);
		this.pubBean = pubBean;
	}
	public NewVoidSaleReadCardFragment(PubBean pubBean) {
		super(0);
		this.pubBean = pubBean;
	}
	public static NewVoidSaleReadCardFragment newInstance(CardBean bean, PubBean pubBean) {
		NewVoidSaleReadCardFragment fragment;
		if (bean.getTimeOut() != null) {
			fragment = new NewVoidSaleReadCardFragment(pubBean,Long.valueOf(bean.getTimeOut()));
		}
		else {
			fragment = new NewVoidSaleReadCardFragment(pubBean);
		}

		fragment.bean = bean;
		return fragment;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	mFragmentView = inflater.inflate(R.layout.layout_void_consume_readcard, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        return mFragmentView;
    }

	private void initView() {
		voucherNo = (TextView)mFragmentView.findViewById(R.id.tv_voucher_no);
		amount = (TextView)mFragmentView.findViewById(R.id.tv_amount);
		cardNo = (TextView)mFragmentView.findViewById(R.id.tv_card_no);
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
		initView();
		handler = getCardInputHandler();
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
		amount.setText(FormatUtils.formatAmount(String.valueOf(pubBean.getOldAmount())));
		voucherNo.setText(pubBean.getOldTrace());
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
	
	private Handler getCardInputHandler(){
		return new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				
				case CARD_READ_COMFIRM:
					if (bean.getIsComfrim()) {
						onCardComfrim();
						} else {
						onSucess();
					}
					break;
				default:
					break;
				}
			}
		};
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
