package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const.PrintStyleConstEnum;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransType;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.trans.bean.PrintBean;
import com.newland.payment.trans.bean.TransResultBean;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 账单界面
 * 
 * @author CB
 * @time 2015-4-21 上午9:24:23
 */
@SuppressLint({ "ValidFragment", "InflateParams" })
public class BillFragment extends BaseFragment {

	/** 商户名称 */
//	@ViewInject(R.id.txt_merchant_name)
	TextView txtMerchantName;
	/** 商户号 */
//	@ViewInject(R.id.txt_merchant_no)
	TextView txtMerchantNo;
//	@ViewInject(R.id.txt_pos_id)
	TextView txtPosId;
	/** 卡号 */
//	@ViewInject(R.id.txt_card_no)
	TextView txtCardNo;
	
	/** 凭证号 */
//	@ViewInject(R.id.txt_voucher)
	TextView txtVoucher;
	/** 授权码 */
//	@ViewInject(R.id.txt_auth_code)
	TextView txtAuthCode;
	/** 参考码 */
//	@ViewInject(R.id.txt_ref_no)
	TextView txtRefNo;
	/** 日期 */
//	@ViewInject(R.id.txt_date)
	TextView txtDate;
	/** 金额 */
//	@ViewInject(R.id.txt_integer)
	TextView txtAmount;
	/** 电子现金title*/
//	@ViewInject(R.id.txt_title_ec_percher)
	private TextView txtTitleECPurcher;
	/** 电子现金余额 */
//	@ViewInject(R.id.txt_ec_percher)
	private TextView txtECPurcher;
	/** 结果图标 */
//	@ViewInject(R.id.tv_trans_result_logo)
	private ImageView txtResultLogo;
	/** 结果信息 */
//	@ViewInject(R.id.tv_trans_result_content)
	private TextView txtResultText;
	/** 结果信息 */
//	@ViewInject(R.id.tv_trans_result)
	private TextView txtResult;
	/** 返回 */
//	@ViewInject(R.id.btn_result_return_main)
	private Button BtnReturnMain;
	private Button BtnPrintDetail;
	private String strMerchantName;
	private String strMerchantNo;
	private String strPosId;
	private String strVoucher;
	private String strAuthCode;
	private String strRefNo;
	private String strDate;
	private String strAmount;
	private int returnSecond;
	private TransResultBean bean;
	private boolean isNeedBackAfter5Second = true;
	
	Handler handler = new Handler(MainActivity.getInstance().getMainLooper()){
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x01:
				BtnReturnMain.setText(String.valueOf("确定（"+returnSecond+"）"));
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	private BillFragment(){
		super(0);
	}

	public static BillFragment newInstance(TransResultBean transResultBean) {
		BillFragment billFragment = new BillFragment();
		billFragment.bean = transResultBean;
		return billFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

//		if (isHasWater()) {
//			mFragmentView = inflater.inflate(R.layout.bill_fragment, null);
//		} else {
			mFragmentView = inflater.inflate(R.layout.show_trans_state_fragment, null);
//		}
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	private void initView() {
		txtMerchantName = (TextView) mFragmentView.findViewById(R.id.txt_merchant_name);
		txtMerchantNo = (TextView) mFragmentView.findViewById(R.id.txt_merchant_no);
		txtPosId = (TextView) mFragmentView.findViewById(R.id.txt_pos_id);
		txtCardNo = (TextView) mFragmentView.findViewById(R.id.txt_card_no);
		txtVoucher = (TextView) mFragmentView.findViewById(R.id.txt_voucher);
		txtAuthCode = (TextView) mFragmentView.findViewById(R.id.txt_auth_code);
		txtRefNo = (TextView) mFragmentView.findViewById(R.id.txt_ref_no);
		txtDate = (TextView) mFragmentView.findViewById(R.id.txt_date);
		txtAmount = (TextView) mFragmentView.findViewById(R.id.txt_integer);
		txtResultLogo = (ImageView) mFragmentView.findViewById(R.id.tv_trans_result_logo);
		txtResultText = (TextView) mFragmentView.findViewById(R.id.tv_trans_result_content);
		txtResult = (TextView) mFragmentView.findViewById(R.id.tv_trans_result);
		BtnReturnMain = (Button) mFragmentView.findViewById(R.id.btn_result_return_main);
		BtnPrintDetail = (Button) mFragmentView.findViewById(R.id.btn_print_detail);
	}

	@Override
	protected void initData() {
		initView();
		if (getBean() != null && getBean().getTitle() != null) {
				setTitle(getBean().getTitle());
		}
		if (isHasWater()) {
			new CommonThread(new ThreadCallBack() {

				@Override
				public void onMain() {
					
//					BtnReturnMain.setEnabled(false);
//					BtnReturnMain.setBackgroundResource(R.drawable.commom_disenabled);
//
//					txtMerchantNo.setText(strMerchantNo);
//					txtMerchantName.setText(strMerchantName);
//					txtPosId.setText(strPosId);
				}

				@Override
				public void onBackGround() {
					strMerchantName = ParamsUtils.getString( 
							ParamsConst.PARAMS_KEY_BASE_MERCHANTNAME);
					strMerchantNo = ParamsUtils.getShopId();
					strPosId = ParamsUtils.getPosId();
				}
			}).start();
			Water water = getBean().getWater();
			strVoucher = water.getTrace();
			strAuthCode = water.getAuthCode();
			strRefNo = water.getReferNum(); 
			strDate = FormatUtils.timeFormat(water.getDate(), water.getTime());
			txtResultLogo.setBackgroundResource(R.drawable.h_print_receicp);
//			if (water.getEcBalance() == null) {
//				txtECPurcher.setVisibility(View.GONE);
//				txtTitleECPurcher.setVisibility(View.GONE);
//			}
//			else {
//				txtECPurcher.setText(FormatUtils.formatAmount(String.valueOf(water.getEcBalance())));
//			}
			
//			if (water.getAmount() != null) {
//
//				String identify = FormatUtils.getCurrencyIdentify(water.getCurrency());
//
//				strAmount = identify+" "+FormatUtils.formatAmount(String.valueOf(getBean().getWater().getAmount()));
//
//			}
//			//设置卡号
//			txtCardNo.setText(TransUtils.getPanByWater(water));
//			txtVoucher.setText(strVoucher);
//			txtAuthCode.setText(strAuthCode);
//			txtRefNo.setText(strRefNo);
//			txtDate.setText(strDate);
//			txtAmount.setText(strAmount);
//			if (App.SCREEN_TYPE == ScreenType.IM_91) {
//				txtResultLogo.setVisibility(View.GONE);
//			}
//			txtResultText.setText(R.string.bill_printing_defuat_tip);
			// 开始打印

			printWater();
			txtResult.setText(getString(R.string.bill_result_print_success));
		} 
		else {
			BtnReturnMain.setVisibility(View.VISIBLE);
			// 如果交易失败
			if (getBean().getIsSucess() == null || !getBean().getIsSucess()) {
				txtResultLogo.setBackgroundResource(R.drawable.h_trans_fail);
				if(StringUtils.isNullOrEmpty(getBean().getContent())){
					txtResultText.setVisibility(View.GONE);
				}else{
					txtResultText.setText(bean.getContent());
				}
			} else {
				txtResultLogo.setBackgroundResource(R.drawable.h_trans_succes);
				if(StringUtils.isNullOrEmpty(getBean().getContent())){
					txtResult.setText(getString(R.string.bill_result_success));
					txtResultText.setVisibility(View.GONE);
				}else{
					txtResult.setText(getString(R.string.trans_succse));
					txtResultText.setText(bean.getContent());
					if(bean.getTransType() != -1){
						switch (bean.getTransType()){
							case TransType.TRANS_SALE:
								break;
							case TransType.TRANS_BALANCE:
								txtResult.setText(bean.getContent());
								txtResultText.setVisibility(View.GONE);
								break;
							case TransType.TRANS_REFUND:
								break;
							case TransType.TRANS_VOID_SALE:
								break;
							case TransType.TRANS_SETTLE:
								txtResult.setText("结算成功,对账平");
								txtResultText.setVisibility(View.GONE);
								BtnPrintDetail.setVisibility(View.VISIBLE);
								isNeedBackAfter5Second = false;
								break;
						}
					}
				}
			}

			if(isNeedBackAfter5Second){
				backAfter5Second();
			}
		}
	}
	
	/**
	 * 锁住线程
	 * @param
	 */
	private void lockPrintThread(PrintBean printBean) {
		try {
			synchronized(printBean.getWaitObj()) {
				printBean.getWaitObj().wait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/** 开启打印 */
	private void printWater(){
		new Thread(){
			public void run(){
				
				PrintBean printBean = new PrintBean();
				printBean.setPrintType(PrintStyleConstEnum.PRINT_WATER);
				printBean.setWater(getBean().getWater());
				
				final PrintFragment fragment = PrintFragment.newInstance(printBean);
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						activity.switchContent(fragment);	
					}
				});
				
				lockPrintThread(printBean);
				
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						BtnReturnMain.setEnabled(true);
//						BtnReturnMain.setBackgroundResource(R.drawable.common_btn_blue);
						BtnReturnMain.setVisibility(View.VISIBLE);
						txtResultText.setText(R.string.bill_cut_receipt);
						backAfter5Second();
					}
				});
			}
		}.start();
	}

	
	@Override
	protected void initClickEvent(View view) {

	}

	@Override
	protected void initEvent() {

		// 返回主菜单事件
		BtnReturnMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onSucess();
			}
		});
		BtnPrintDetail.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  onBack();
			  }
		  }
		);
	}

	/**
	 * 5秒后退出
	 */
	public void backAfter5Second() {
		new CommonThread(new ThreadCallBack() {
			
			@Override
			public void onMain() {
				onSucess();				
			}
			
			@Override
			public void onBackGround() {
				for (returnSecond = 5; returnSecond > 0; returnSecond--) {
					try {		
						handler.sendEmptyMessage(0x01);
						Thread.sleep(1000);
						
					} catch (Exception e) {
						
					}
				}
				
			}
		}).start();
	}
	
	@Override
	protected boolean onBackKeyDown() {
		return true;
	}
	
	private Boolean isHasWater() {
		if (getBean() != null) {
			if (getBean().getWater() != null) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	
	@Override
	public void onFragmentHide() {
		super.onFragmentHide();
		activity.setExceptMainStyle();
	}

	@Override
	public void onFragmentShow() {
		super.onFragmentShow();
		activity.setLoginStyle();
	}

	@Override
	protected TransResultBean getBean() {
		return bean;
	}
}
