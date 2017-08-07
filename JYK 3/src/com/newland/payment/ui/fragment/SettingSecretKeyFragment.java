package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.base.CommonThread;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.listener.StringValueChangeListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.payment.ui.view.SettingEditView;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 终端密钥管理界面
 *
 * @author CB
 * @date 2015-5-14 
 * @time 下午10:08:27
 */
public class SettingSecretKeyFragment extends BaseSettingFragment{
	
	/** 主密钥索引 */
//	@ViewInject(R.id.sev_setting_secret_key_main_index)
	SettingEditView sevMainIndex;
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_secret_key_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		
		addCheckView(sevMainIndex);
		
		setTitle(R.string.setting_secret_key);
		
		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());
		sevMainIndex.setKeyboardNumber(keyboardNumber);
		
		sevMainIndex.setTitle(R.string.setting_secret_key_main_index);
		
		sevMainIndex.setMinLength(1);
		sevMainIndex.setParamData(1,ParamsConst.PARAMS_KEY_MAIN_KEY_INDEX);
		sevMainIndex.setCheckSuccessListener(new StringValueChangeListener() {
			
			@Override
			public void onChange(final String value) {
				new CommonThread(new ThreadCallBack(){

					@Override
					public void onBackGround() {
						if (StringUtils.isDigital(value)) {
							ParamsUtils.setTMKIndex(Integer.valueOf(value));
						}
						ParamsUtils.setBoolean(ParamsTrans.PARAMS_FLAG_SIGN, false);
					}

					@Override
					public void onMain() {
						
					}
					
				}).start();
			}
		});
	}

//	@OnClick({R.id.txt_setting_secret_key_head,
//		R.id.txt_setting_secret_key_des,R.id.setting_load_key})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_setting_secret_key_head) {//手输密钥
			activity.switchContent(new SettingSecretKeyHeadFragment());

		} else if (i == R.id.txt_setting_secret_key_des) {//设置Des算法
			activity.switchContent(new SettingDesFragment());

		} else if (i == R.id.setting_load_key) {//母pos导入密钥
			activity.switchContent(new MPOSLoadKeyFragment());

		}
	}

	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		
	}

}
