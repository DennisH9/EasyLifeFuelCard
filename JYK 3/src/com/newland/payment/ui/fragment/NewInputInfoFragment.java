package com.newland.payment.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.newland.base.util.InputUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.trans.bean.InputInfoBean;
import com.newland.payment.ui.view.NumberKeyboardView;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 输入凭证号
 */
public class NewInputInfoFragment extends BaseFragment implements OnClickListener {
//	@ViewInject(R.id.et_voucher_no)
	private EditText voucherNo; // 凭证号
	
//	@ViewInject(R.id.tv_input_tip)
	private TextView inputTip;
	
//	@ViewInject(R.id.btn_next)
	private Button next;        // 下一步
	
//	@ViewInject(R.id.kb_num)
	private NumberKeyboardView numKeyboard; // 数字键盘
	
	private InputInfoBean bean;
	/**输入最大长度*/
	private int maxLen;
	/**最小长度*/
	private int minLen;
	/**输入模式*/
	private int mode;
	/**允许空标记*/
	private boolean emptyFlag;
	private String result;
	
	public NewInputInfoFragment(long timeOut) {
		super(timeOut);
	}

	public static NewInputInfoFragment newInstance(InputInfoBean infoBean) {
		NewInputInfoFragment fragment = new NewInputInfoFragment(infoBean.getTimeOut());
		fragment.bean = infoBean;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.new_input_info_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	private void initView() {
		voucherNo = (EditText) mFragmentView.findViewById(R.id.et_voucher_no);
		inputTip = (TextView) mFragmentView.findViewById(R.id.tv_input_tip);
		next = (Button) mFragmentView.findViewById(R.id.btn_next);
		numKeyboard = (NumberKeyboardView) mFragmentView.findViewById(R.id.kb_num);

	}

	@Override
	protected void initData() {
		setTitle(bean.getTitle());
		initView();
		maxLen = bean.getMaxLen();
		minLen = bean.getMinLen();
		mode = bean.getMode();
		emptyFlag = bean.getEmptyFlag();
		
		inputTip.setText(bean.getShortContent());
		voucherNo.setHint(bean.getContent());
		voucherNo.setSelection(0);
		voucherNo.setText(bean.getResult());
		InputUtils.setMaxLength(voucherNo, maxLen);
		InputFilter[] filters = {new InputFilter.LengthFilter(maxLen)};
		voucherNo.setFilters(filters);
		numKeyboard.setInputEdittext(voucherNo);
		next.setOnClickListener(this);
	}

	@Override
	protected void initClickEvent(View view) {
	}

	@Override
	protected void initEvent() {
	}

	@Override
	public void onSucess() {
		doEndThing();
		super.onSucess();
	}
	
	@Override
	public void onBack() {
		doEndThing();
		super.onBack();
	};
	
	@Override
	public void onTimeOut() {
		doEndThing();
		super.onTimeOut();
	}
	
	@Override
	public BaseBean getBean() {
		return this.bean;
	}
	
	private void doEndThing(){
		InputMethodManager imm = (InputMethodManager)voucherNo.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive()){
			imm.hideSoftInputFromWindow(voucherNo.getWindowToken(), 0);
		}
		voucherNo.clearFocus();
	}

	@Override
	public void onClick(View v) {
		result = voucherNo.getText().toString();
		if (!StringUtils.isEmpty(result) && StringUtils.length(result) < minLen) {
			LoggerUtils.d("result.length() < minLen");
			if(mode != InputInfoBean.INPUT_MODE_PASSWD){
				ToastUtils.show(context, getString(R.string.common_input_not_enough));
			}
			return;
		}
		bean.setResult(result);
		onSucess();
	}
	
}
