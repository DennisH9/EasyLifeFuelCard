package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.base.util.MessageUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.Const.PrintStyleConstEnum;
import com.newland.payment.common.tools.PrintModelUtils;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.printer.template.model.PrintTotalModel;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 交易汇总界面
 * 
 * @author CB
 * @date 2015-5-16
 * @time 上午10:12:57
 */
public class TransTotalListFragment extends BaseFragment {

	/** 内借记卡金额 */
//	@ViewInject(R.id.txt_inside_debit_amount)
	TextView txtInsideDebitAmount;
	/** 内借记卡笔数 */
//	@ViewInject(R.id.txt_inside_debit_num)
	TextView txtInsideDebitNum;
	/** 外借记卡笔数 */
//	@ViewInject(R.id.txt_outside_debit_amount)
	TextView txtOutsideDebitAmount;
	/** 外借记卡笔数 */
//	@ViewInject(R.id.txt_outside_debit_num)
	TextView txtOutsideDebitNum;
	/** 内贷记卡金额 */
//	@ViewInject(R.id.txt_inside_credit_amount)
	TextView txtInsideCreditAmount;
	/** 内贷记卡笔数 */
//	@ViewInject(R.id.txt_inside_credit_num)
	TextView txtInsideCreditNum;
	/** 外贷记卡金额 */
//	@ViewInject(R.id.txt_outside_credit_amount)
	TextView txtOutsideCreditAmount;
	/** 外贷记卡笔数 */
//	@ViewInject(R.id.txt_outside_credit_num)
	TextView txtOutsideCreditNum;
	/** 打印按钮 */
//	@ViewInject(R.id.btn_print)
	Button btnPrint;

	/** 内借记卡金额 */
	private String insideDebitAmount;
	/** 内借记卡笔数 */
	private String insideDebitNum;
	/** 外借记卡金额 */
	private String outsideDebitAmount;
	/** 外借记卡笔数 */
	private String outsideDebitNum;
	/** 内贷记卡金额 */
	private String insideCreditAmount;
	/** 内贷记卡笔数 */
	private String insideCreditNum;
	/** 外贷记卡金额 */
	private String outsideCreditAmount;
	/** 外贷记卡笔数 */
	private String outsideCreditNum;
	/** 存储汇总信息的model */
	private PrintTotalModel model;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.trans_total_list_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {

		setTitle(R.string.common_search_consumption_all);

		new CommonThread(new ThreadCallBack() {

			@Override
			public void onMain() {
				txtInsideDebitAmount.setText(insideDebitAmount);
				txtInsideDebitNum.setText(insideDebitNum);
				txtOutsideDebitAmount.setText(outsideDebitAmount);
				txtOutsideDebitNum.setText(outsideDebitNum);
				txtInsideCreditAmount.setText(insideCreditAmount);
				txtInsideCreditNum.setText(insideCreditNum);
				txtOutsideCreditAmount.setText(outsideCreditAmount);
				txtOutsideCreditNum.setText(outsideCreditNum);

			}

			@Override
			public void onBackGround() {

				model = PrintModelUtils.getPrintTotalModel(context);
				/** 内借记卡金额 */
				insideDebitAmount = model.getDebitAmt_nk();
				/** 内借记卡笔数 */
				insideDebitNum = model.getDebitNum_nk();
				/** 外借记卡金额 */
				outsideDebitAmount = model.getDebitAmt_wk();
				/** 外借记卡笔数 */
				outsideDebitNum = model.getDebitNum_wk();
				/** 内贷记卡金额 */
				insideCreditAmount = model.getCreditAmt_nk();
				/** 内贷记卡笔数 */
				insideCreditNum = model.getCreditNum_nk();
				/** 外贷记卡金额 */
				outsideCreditAmount = model.getCreditAmt_wk();
				/** 外贷记卡笔数 */
				outsideCreditNum = model.getCreditNum_wk();
			}
		}).start();
	}

	@Override
	protected void initClickEvent(View view) {

	}

	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {

		btnPrint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MessageUtils.showCommonDialog(activity, context.getString(R.string.is_prn_total),
						R.string.common_sure, R.string.common_cancel,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								activity.switchContent(PrintFragment.newInstance(PrintStyleConstEnum.PRINT_TOTAL, null, null));					

								
							}
						}, new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								
							}
						});


			}

		});

	}

	/**
	 * 获取金额总计
	 * 
	 * @param keys
	 * @return
	 */
	public long getTotalAmount(String... keys) {

		long value = 0;

		for (int i = 0; i < keys.length; i++) {
			value += ParamsUtils.getLong(keys[i]);
		}

		return value;

	}

	/**
	 * 获取笔数总计
	 * 
	 * @param keys
	 * @return
	 */
	public int getTotalCount(String... keys) {

		int value = 0;

		for (int i = 0; i < keys.length; i++) {
			value += ParamsUtils.getLong(keys[i]);
		}

		return value;

	}

}
