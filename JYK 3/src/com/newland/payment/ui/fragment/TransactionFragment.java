package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.newland.payment.R;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 交易查询主界面
 *
 * @author CB
 * @time 2015-4-22 上午8:41:57
 */
public class TransactionFragment extends BaseFragment{

	/** 输入框 */
//	@ViewInject(R.id.et_content)
	EditText etContent;
	/** 消费总计 */
//	@ViewInject(R.id.txt_expense_total) private TextView txtExpenseTotal;
//	/** 消费笔数 */
//	@ViewInject(R.id.txt_expense_count) private TextView txtExpenseCount;
//	/** 消费金额 */
//	@ViewInject(R.id.txt_expense_money) private TextView txtExpenseMoney;
//	/** 退货总计 */
//	@ViewInject(R.id.txt_returns_total) private TextView txtReturnsTotal;
//	/** 退货笔数 */
//	@ViewInject(R.id.txt_returns_count) private TextView txtReturnsCount;
//	/** 退货金额 */
//	@ViewInject(R.id.txt_returns_money) private TextView txtReturnsMoney;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.transaction_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		setTitle(R.string.sliding_menu_check_transaction);
		
		//强制不弹出系统输入法
		etContent.setInputType(InputType.TYPE_NULL);
	}

	@Override
	protected void initClickEvent(View view) {
		
	}

	@Override
	protected void initEvent() {
		etContent.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					activity.switchContent(new TransVoucherSearchFragment());
				}
			}
		});
	}

	@Override
	public BaseBean getBean() {
		return null;
	}
	
}
