package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.payment.ui.view.SettingEditView;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.security.SecurityModule;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 手输密钥界面
 *
 * @author CB
 * @date 2015-5-14 
 * @time 下午10:36:24
 */
public class SettingSecretKeyHeadFragment extends BaseSettingFragment{

	/** 密钥索引 */
//	@ViewInject(R.id.sev_setting_secret_key_index)
	SettingEditView sevSettingSecretKeyIndex;
	/** 密钥 */
//	@ViewInject(R.id.sev_setting_secret_key)
	SettingEditView sevSettingSecretKey;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;
	/** 导入按键*/
//	@ViewInject(R.id.txt_submit)
	TextView txtSubmit;
	/** 是否导入成功*/
	private boolean importMainkey = false;

	/** 密钥长度 */
	private int size;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_secret_key_head_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
		setTitle(R.string.setting_secret_key_head);
		
		sevSettingSecretKeyIndex.setTitle(R.string.setting_secret_key_head_index);
		sevSettingSecretKey.setTitle(R.string.setting_secret_key_head);
		
		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());
		
		sevSettingSecretKeyIndex.setMinLength(1);
		sevSettingSecretKeyIndex.setMaxLength(1);
		sevSettingSecretKeyIndex.setKeyboardNumber(keyboardNumber);
		sevSettingSecretKeyIndex.setContent("0");

		sevSettingSecretKey.setParamData(ParamsConst.PARAMS_KEY_HEAD_MAIN_KEY, false);

		size = ParamsUtils
		.getInt(ParamsConst.PARAMS_KEY_ENCRYPT_MODE) == 0 ? 16 : 32;
		sevSettingSecretKey.setMaxLength(size);
		sevSettingSecretKey.setMinLength(size);
		sevSettingSecretKey.showTextSize();
		sevSettingSecretKey.setDigits("ABCDEF0123456789");
		
	}

//	@OnClick(R.id.txt_submit)
	@Override
	protected void initClickEvent(View view) {
		//快速点击会有问题???
		//导入
		if (sevSettingSecretKeyIndex.getValue().equals("")) {

			ToastUtils.show(context, R.string.error_input_main_key_index);
		} else if (sevSettingSecretKey.getValue().length() != size) {
			
			ToastUtils.show(context, getString(R.string.error_main_key_length, size + ""));
			
		} else {
			txtSubmit.setEnabled(false);

			sevSettingSecretKey.hideSystemKeyboard();
			MainActivity.getInstance().showProgress();
			new CommonThread(new ThreadCallBack() {
				
				@Override
				public void onMain() {
					txtSubmit.setEnabled(true);

					MainActivity.getInstance().hideProgress();
					
					
					if(importMainkey){
						ToastUtils.show(context, R.string.common_input_main_key_success);
					} else {
						ToastUtils.show(context, R.string.common_input_main_key_fail);
					}
				}
				
				@Override
				public void onBackGround() {
					importMainkey = false;
					if (ParamsUtils.getTMkIndex() == Integer.valueOf(sevSettingSecretKeyIndex.getValue())) {
						ParamsUtils.setBoolean(ParamsTrans.PARAMS_FLAG_SIGN, false);
					}
					try{
						SecurityModule module = SecurityModule.getInstance();
						LoggerUtils.d("手输索引："+sevSettingSecretKeyIndex.getValue());
						LoggerUtils.d("手输主密钥："+sevSettingSecretKey.getValue());
						module.loadMainKey(Integer.valueOf(sevSettingSecretKeyIndex.getValue()),
								sevSettingSecretKey.getValue());
						importMainkey = true;
					}catch(Exception e){
						importMainkey = false;
						e.printStackTrace();
					}
				}
			}).start();
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
