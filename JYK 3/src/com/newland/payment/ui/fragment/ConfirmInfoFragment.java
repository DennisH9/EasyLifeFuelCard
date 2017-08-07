package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newland.base.util.TransUtils;
import com.newland.payment.R;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 信息确认界面
 *
 * @author CB
 * @date 2015-6-2 
 * @time 下午3:05:02
 */
@SuppressLint("ValidFragment")
public class ConfirmInfoFragment extends BaseFragment{

	/** 商户名称 */
//	@ViewInject(R.id.txt_merchant_name)
	private TextView txtMerchantName;
	/** 商户号 */
//	@ViewInject(R.id.txt_merchant_no)
	private TextView txtMerchantNo;
	/** 卡号 */
//	@ViewInject(R.id.txt_card_no)
	private TextView txtCardNo;
	/** 凭证号 */
//	@ViewInject(R.id.txt_voucher)
	private TextView txtVoucher;
	/** 授权码 */
//	@ViewInject(R.id.txt_auth_code)
	private TextView txtAuthCode;
	/** 参考码 */
//	@ViewInject(R.id.txt_ref_no)
	private TextView txtRefNo;
	/** 日期 */
//	@ViewInject(R.id.txt_date)
	private TextView txtDate;
	/** 金额 */
//	@ViewInject(R.id.txt_integer)
	private TextView txtAmount;
	
	private CommonBean<Water> bean;
	
	public ConfirmInfoFragment(CommonBean<Water> bean) {
		super(bean.getTimeOut());
		this.bean = bean;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.confirm_info_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	private void initView(View layout) {
		txtMerchantName = (TextView) layout.findViewById(R.id.txt_merchant_name);
		txtMerchantNo = (TextView) layout.findViewById(R.id.txt_merchant_no);
		txtCardNo = (TextView) layout.findViewById(R.id.txt_card_no);
		txtVoucher = (TextView) layout.findViewById(R.id.txt_voucher);
		txtAuthCode = (TextView) layout.findViewById(R.id.txt_auth_code);
		txtRefNo = (TextView) layout.findViewById(R.id.txt_ref_no);
		txtDate = (TextView) layout.findViewById(R.id.txt_date);
		txtAmount = (TextView) layout.findViewById(R.id.txt_integer);
		layout.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initClickEvent(v);
			}
		});

	}
	
	@Override
	protected void initData() {
		initView(mFragmentView);
		if (bean.getValue() != null) {
			Water water = getBean().getValue();
			LoggerUtils.d("hjh    :"+TransUtils.getPanByWater(water)+" "+water.getTrace()+" "+water.getReferNum()+"  "+FormatUtils.timeFormat(water.getDate(), water.getTime()));
			txtCardNo.setText(TransUtils.getPanByWater(water));
			txtVoucher.setText(water.getTrace());
//			txtAuthCode.setText(water.getAuthCode());
			txtRefNo.setText(water.getReferNum());
			txtDate.setText(FormatUtils.timeFormat(water.getDate(), water.getTime()));
			if (water.getAmount() != null) {
				
				String identify = FormatUtils.getCurrencyIdentify(water.getCurrency());
				String unit = FormatUtils.getCurrencyUnit(water.getCurrency());
				
				txtAmount.setText(identify+" "+FormatUtils.formatAmount(String.valueOf(getBean().getValue().getAmount()))+" "+unit);
			}
			if (getBean() != null) {
				if (getBean().getTitle() != null) {
					setTitle(getBean().getTitle());
				}
			}
		}
		
	}
	
//	@OnClick(R.id.btn_sure)
	@Override
	protected void initClickEvent(View view) {
		onSucess();
	}

	@Override
	protected CommonBean<Water> getBean() {
		return bean;
	}

	@Override
	protected void initEvent() {
		
	}

}
