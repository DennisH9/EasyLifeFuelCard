package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.newland.base.util.TransUtils;
import com.newland.payment.R;
import com.newland.payment.common.TransConst.SendStatus;
import com.newland.payment.common.TransConst.TransStatus;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.trans.bean.TransResultBean;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;


/**
 * 查询交易明细子界面
 * 
 * @author CB
 * @time 2015-4-21 上午9:24:23
 */
@SuppressLint({ "ValidFragment", "InflateParams" })
public class TransSelectListItemFragment extends BaseFragment {

	/** 商户名称 */
//	@ViewInject(R.id.txt_merchant_name)
	TextView txtMerchantName;
	/** 商户号 */
//	@ViewInject(R.id.txt_merchant_no)
	TextView txtMerchantNo;
	/** 终端号 */
//	@ViewInject(R.id.txt_pos_id)
	TextView txtPosNo;
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
	/** 交易类型 */
//	@ViewInject(R.id.txt_trans_type)
	TextView txtTransType;
	
	/** 撤销 */
//	@ViewInject(R.id.txt_repeal)
	private TextView txtRepeal;
	/** 结果图标 */
//	@ViewInject(R.id.tv_trans_result_logo)
	private TextView txtResultLogo;
	/** 结果信息 */
//	@ViewInject(R.id.tv_trans_result_content)
	private TextView txtResultText;
	/** 返回 */
//	@ViewInject(R.id.btn_result_return_main)
	private Button BtnReturnMain;
	private String strMerchantName;
	private String strMerchantNo;
	private String strPosNo;
	private String strVoucher;
	private String strAuthCode;
	private String strRefNo;
	private String strDate;
	private String strAmount;
	private String strTransType;
	private String strTransStatus;

	private Water water;

	

	public TransSelectListItemFragment(Water water, String strMerchantName,
			String strMerchantNo, String strPosNo) {
		this.strMerchantName = strMerchantName;
		this.strMerchantNo = strMerchantNo;
		this.strPosNo = strPosNo;
		this.water = water;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			mFragmentView = inflater.inflate(
					R.layout.trans_select_list_item_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {

		/** 商户名称 */
		txtMerchantName = (TextView)mFragmentView.findViewById(R.id.txt_merchant_name);
		/** 商户号 */
		txtMerchantNo = (TextView)mFragmentView.findViewById(R.id.txt_merchant_no);
		/** 终端号 */
		txtPosNo = (TextView)mFragmentView.findViewById(R.id.txt_pos_id);
		/** 卡号 */
		txtCardNo = (TextView)mFragmentView.findViewById(R.id.txt_card_no);
		/** 凭证号 */
		txtVoucher = (TextView)mFragmentView.findViewById(R.id.txt_voucher);
		/** 授权码 */
		txtAuthCode = (TextView)mFragmentView.findViewById(R.id.txt_auth_code);
		/** 参考码 */
		txtRefNo = (TextView)mFragmentView.findViewById(R.id.txt_ref_no);
		/** 日期 */
		txtDate = (TextView)mFragmentView.findViewById(R.id.txt_date);
		/** 金额 */
		txtAmount = (TextView)mFragmentView.findViewById(R.id.txt_integer);
		/** 交易类型 */
		txtTransType = (TextView)mFragmentView.findViewById(R.id.txt_trans_type);



		txtMerchantNo.setText(strMerchantNo);
		txtMerchantName.setText(strMerchantName);
		txtPosNo.setText(strPosNo);

		if (water != null) {
			strVoucher = water.getTrace();
			strAuthCode = water.getAuthCode();
			strRefNo = water.getReferNum(); 
			strDate = FormatUtils.timeFormat(water.getDate(), water.getTime());
			
			if (water.getAmount() != null) {
				
				String identify = FormatUtils.getCurrencyIdentify(water.getCurrency());
				String unit = FormatUtils.getCurrencyUnit(water.getCurrency());
				
				strAmount = identify+" "+FormatUtils.formatAmount(String.valueOf(water.getAmount()))+" "+unit;
				
			}
			strTransType = TransUtils.getTransType(water.getTransType())[0];
			strTransStatus = "";
			switch (water.getTransStatus()){
			case TransStatus.REV:
				strTransStatus = "已撤";
				break;
			case TransStatus.ADJUST:
			case TransStatus.SEND_AND_ADJ:
				strTransStatus = "已调";
				break;
			default:
				break;
			}
			if (water.getOffSendFlag() >= SendStatus.SUCC){
				strTransStatus += "已送";
			}
		} 

		txtCardNo.setText(TransUtils.getPanByWater(water));
		txtVoucher.setText(strVoucher);
		txtAuthCode.setText(strAuthCode);
		txtRefNo.setText(strRefNo);
		txtDate.setText(strDate);
		LoggerUtils.i("strAmount:"+strAmount);
		txtAmount.setText(strAmount);
		if (water != null) {
			String type = "";
			if (strTransType != null) {
				type +=strTransType;
			}
			txtTransType.setText(type);
			if (!StringUtils.isNullOrEmpty(strTransStatus)) {
				type += "【" + strTransStatus  + "】" ;
			}
		}
		

	}

	@Override
	protected void initClickEvent(View view) {

	}

	@Override
	protected void initEvent() {

	}

	@Override
	public TransResultBean getBean() {
		return null;
	}

}
