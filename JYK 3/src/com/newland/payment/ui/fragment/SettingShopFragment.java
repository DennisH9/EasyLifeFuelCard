package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.emv.EmvAppModule;
import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.listener.StringValueChangeListener;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.payment.ui.view.SettingEditView;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.emv.EmvModule;

/**
 * 商户参数设置
 *
 * @author CB
 * @date 2015-5-13 
 * @time 上午11:19:28
 */
public class SettingShopFragment extends BaseSettingFragment{

	/** 商户号设置 */
//	@ViewInject(R.id.setting_shop_no)
	private SettingEditView sevNo;
	/** 终端号设置 */
//	@ViewInject(R.id.setting_shop_terminal)
	private SettingEditView sevTerminal;
	/** 商户中文名称 */
//	@ViewInject(R.id.setting_shop_chinese_name)
	private SettingEditView sevChineseName;
	/** 商户英文名称 */
//	@ViewInject(R.id.setting_shop_english_name)
	private SettingEditView sevEnglishName;
	/** 应用显示名称 */
//	@ViewInject(R.id.setting_shop_app_name)
	private SettingEditView sevAppName;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;
	EmvModule emvModule = EmvAppModule.getInstance();
	boolean result = false;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_shop_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
		setTitle(R.string.setting_shop);

		addCheckView(sevNo);
		addCheckView(sevTerminal);
		addCheckView(sevChineseName);
		addCheckView(sevEnglishName);
		addCheckView(sevAppName);
		
		//初始化控件属性
		sevNo.setTitle(R.string.setting_shop_no);
		sevTerminal.setTitle(R.string.setting_shop_terminal);
		sevChineseName.setTitle(R.string.setting_shop_chinese_name);
		sevEnglishName.setTitle(R.string.setting_shop_english_name);
		sevAppName.setTitle(R.string.setting_shop_app_name);
		//初始化值
		sevTerminal.setDigits(Const.DIGITS.LOWERCASE+Const.DIGITS.UPPERCASE+Const.DIGITS.NUMBER);
		sevTerminal.setSafePassword();
		sevNo.setSafePassword();
		sevNo.setMinLength(15);
		sevNo.setParamData(15, ParamsConst.PARAMS_KEY_BASE_MERCHANTID);
		sevNo.setCheckSuccessListener(new StringValueChangeListener() {
			
			@Override
			public void onChange(final String value) {
				MainActivity.getInstance().addQueueTask(
				new ThreadCallBack(){
					boolean result = false;
					@Override
					public void onBackGround() {
						ParamsUtils.setShopId(value);
						result = emvModule.emvSetMerchantId(value);
					}

					@Override
					public void onMain() {
						if (!result){
							ToastUtils.showOnUIThread("EMV设置商户号失败");
						}
					}
					
				});
			}
		});
		
		sevTerminal.setMinLength(8);
		sevTerminal.setParamData(8, ParamsConst.PARAMS_KEY_BASE_POSID);
		sevTerminal.setCheckSuccessListener(new StringValueChangeListener() {
			
			@Override
			public void onChange(final String value) {
				MainActivity.getInstance().addQueueTask(new ThreadCallBack(){
					boolean result = false;
					@Override
					public void onBackGround() {
						ParamsUtils.setPosId(value);
						result = emvModule.emvSetTerminalId(value);				
					}

					@Override
					public void onMain() {
						if (!result){
							ToastUtils.showOnUIThread("EMV设置终端号失败");
						}	
					}
					
				});
			}
		});
		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());
		sevChineseName.setParamData(ParamsConst.PARAMS_KEY_BASE_MERCHANTNAME); 
		sevChineseName.setCheckSuccessListener(new StringValueChangeListener() {
			
			@Override
			public void onChange(final String value) {
				MainActivity.getInstance().addQueueTask(new ThreadCallBack() {
					boolean result = false;
					@Override
					public void onMain() {
						if (!result){
							ToastUtils.showOnUIThread("EMV设置商户名称失败");
						}
					}
					
					@Override
					public void onBackGround() {
						result = emvModule.emvSetMerchantName(value);	

					}
				});
			}
		});
		sevEnglishName.setParamData(20,ParamsConst.PARAMS_KEY_BASE_MERCHANTNAMEEN); 
		sevAppName.setParamData(14,ParamsConst.PARAMS_KEY_BASE_APPDISPNAME); 

		sevChineseName.setMaxCharacter(40);
		
		sevEnglishName.setDigits(Const.DIGITS.LOWERCASE+Const.DIGITS.UPPERCASE);
		
		sevNo.setWaterEnabled(R.string.has_water_please_first_settlement);
		sevTerminal.setWaterEnabled(R.string.has_water_please_first_settlement);
		
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
