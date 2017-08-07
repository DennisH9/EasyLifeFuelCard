package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.payment.ui.view.SettingEditView;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 设置重发次数界面  
 * @author lst
 *
 */
public class SettingResendTimesFragment extends BaseSettingFragment {
	/**离线重发次数  */
//	@ViewInject(R.id.edit_view1)
	SettingEditView editView1;
	/**冲正重发次数 */
//	@ViewInject(R.id.edit_view2)
	SettingEditView editView2;
	/**签名重发次数  */
//	@ViewInject(R.id.edit_view3)
	SettingEditView editView3;
	/**TMS重发次数  */
//	@ViewInject(R.id.edit_view4)
	SettingEditView editView4;
//	@ViewInject(R.id.setting_keyboeard_number)
	KeyboardNumber keyboardNumber;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mFragmentView = inflater.inflate(R.layout.setting_resend_times_fragment,
				null);

		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {

		addCheckView(editView1);
		addCheckView(editView2);
		addCheckView(editView3);
		addCheckView(editView4);
		
		setTitle(R.string.setting_trans_retry_no);
		
		/**离线重发次数  */
		editView1.setTitle(R.string.offsend_resend_times);
		/**冲正重发次数 */
		editView2.setTitle(R.string.reversal_resend_times);
		/**签名重发次数  */
		editView3.setTitle(R.string.sign_resend_times);
		/**TMS重发次数  */
		editView4.setTitle(R.string.tms_resend_times);
		
		editView1.setParamData(ParamsConst.PARAMS_KEY_OFFSEND_RESEND_TIMES);
		editView2.setParamData(ParamsConst.PARAMS_KEY_REVERSAL_RESEEND_TIMES);
		editView3.setParamData(ParamsConst.PARAMS_KEY_SIGN_RESEND_TIMES);
		editView4.setParamData(ParamsConst.PARAMS_KEY_TMS_RESEND_TIMES);
		
		editView1.setKeyboardNumber(keyboardNumber);
		editView1.setMaxValue(9);
		editView1.setMinValue(1);
		editView1.setMaxLength(1);
		
		editView2.setKeyboardNumber(keyboardNumber);
		editView2.setMaxValue(3);
		editView2.setMinValue(1);
		editView2.setMaxLength(1);
		
		editView3.setKeyboardNumber(keyboardNumber);
		editView3.setMinValue(0);
		editView3.setMaxLength(1);
		
		editView4.setKeyboardNumber(keyboardNumber);
		editView4.setMinValue(0);
		editView4.setMaxLength(2);
		
		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());
				
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

	}

}
