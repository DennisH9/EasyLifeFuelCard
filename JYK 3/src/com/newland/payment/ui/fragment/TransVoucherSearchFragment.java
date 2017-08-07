package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.base.util.InputUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 凭证号搜索界面
 *
 * @author CB
 * @time 2015-4-22 上午9:46:57
 */
@SuppressLint("ValidFragment")
public class TransVoucherSearchFragment extends BaseFragment{

	/** 输入框 */
//	@ViewInject(R.id.et_input_info_input)
	EditText etContent;
	/** 提示*/
//	@ViewInject(R.id.txt_input_info_content)
	TextView txtContent;
	/** 提示*/
//	@ViewInject(R.id.txt_short_content)
	TextView txtShortContent;
	/** 键盘*/
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.input_info_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		
		setTitle(R.string.common_search_by_evidence);
		txtContent.setText(R.string.voucher_input);
		txtShortContent.setText(R.string.common_voucher);
		keyboardNumber.setVisibility(View.VISIBLE);
		//强制不弹出系统输入法
		keyboardNumber.setEnterNotGone();
		
		InputUtils.alwaysHideKeyBoard(etContent, InputType.TYPE_CLASS_NUMBER);

		InputUtils.setMaxLength(etContent, 6);
	}

	@Override
	protected void initClickEvent(View view) {
	}

	@Override
	protected void initEvent() {
		
		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener(6,etContent){
			@Override
			public void onEnter() {
				
				new CommonThread(new ThreadCallBack() {
					Water water;
					@Override
					public void onMain() {
						if (water != null) {
							
							MainActivity.getInstance().switchContent(TransSelectListFragment.newInstance(water));
							
						}else {
							ToastUtils.show(context, R.string.error_voucher);
						}
					}
					
					@Override
					public void onBackGround() {
						
						WaterServiceImpl impl = new WaterServiceImpl(context);
						water = impl.findByTrace(StringUtils.fill(etContent.getText().toString(), "0", 6, true));
					}
				}).start();
				
			}
		});
		
	}

	@Override
	public BaseBean getBean() {
		return null;
	}
}
