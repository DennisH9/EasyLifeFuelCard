package com.newland.payment.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.newland.base.CommonThread;
import com.newland.base.util.MessageUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.ui.listener.AbstractKeyBoardListener;
import com.newland.payment.ui.listener.StringValueChangeListener;
import com.newland.payment.ui.view.CommonInputDialog;
import com.newland.payment.ui.view.KeyboardNumber;
import com.newland.payment.ui.view.SettingChooseView;
import com.newland.payment.ui.view.SettingEditView;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 打印测试设置
 *
 * @author CB
 * @date 2015-5-20 
 * @time 上午9:41:26
 */
public class SettingPrintSettingFragment extends BaseSettingFragment{

	/** 签购单抬头 */
//	@ViewInject(R.id.scv_print_sign_order)
	SettingChooseView scvPrintSignOrder;
	/** 打印张数 */
//	@ViewInject(R.id.sev_print_paper_num)
	SettingEditView sevPrintPaperNum;
	/** 服务热线 */
//	@ViewInject(R.id.sev_print_service_hotline)
	SettingEditView sevPrintServiceHotline;
	/** 键盘 */
//	@ViewInject(R.id.key_board_number)
	KeyboardNumber keyboardNumber;

	private CommonInputDialog dialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_print_setting_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {

		addCheckView(sevPrintPaperNum);
		addCheckView(sevPrintServiceHotline);
		
		setTitle(R.string.setting_print);

		keyboardNumber.setKeyBoardListener(new AbstractKeyBoardListener());
		
		sevPrintPaperNum.setParamData(R.string.print_paper_num, keyboardNumber, ParamsConst.PARAMS_KEY_BASE_PRTCOUNT);
		sevPrintPaperNum.setMinLength(1);
		sevPrintPaperNum.setMaxLength(1);
		sevPrintPaperNum.setMinValue(1);
		sevPrintPaperNum.setMaxValue(3);
		
		scvPrintSignOrder.setParamData(R.string.print_sign_order, 
				R.string.common_chinese, "0",
				R.string.common_logo, "1",
				ParamsConst.PARAMS_KEY_BASE_PRINTTITLEMODE);
	
		new CommonThread(new ThreadCallBack(){
			String name;
			@Override
			public void onBackGround() {
				name = ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_PRINTTITLECN, "银联POS");
			}
			@Override
			public void onMain() {
				if (!StringUtils.isEmpty(name)) {
					scvPrintSignOrder.setContent(name);
				}
			}
		}).start();
		
		scvPrintSignOrder.setValueChangeListener(new StringValueChangeListener() {
			
			@Override
			public void onChange(String value) {
				if ("0".equals(value)) {
					new CommonThread(new ThreadCallBack() {
						String name;

						@Override
						public void onBackGround() {
							name = ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_PRINTTITLECN, "");
						}

						@Override
						public void onMain() {
							dialog = MessageUtils.showInputDialog(
									context,
									R.string.input_sign_order_title,
									name, 
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											
											
											
											new CommonThread(
													new ThreadCallBack() {
														@Override
														public void onBackGround() {
															ParamsUtils.setString(ParamsConst.PARAMS_KEY_BASE_PRINTTITLECN,	
																			dialog.getContent());
														}

														@Override
														public void onMain() {
															
															scvPrintSignOrder.setContent(dialog.getContent());
														}

													}).start();
										}
									});
							dialog.setMaxCharacterNum(20);
							dialog.setSingleLine();
						}

					}).start();
				} else {
					new CommonThread(new ThreadCallBack() {
						@Override
						public void onBackGround() {
							ParamsUtils
									.setString(
											ParamsConst.PARAMS_KEY_BASE_PRINTTITLECN,
											"");
						}

						@Override
						public void onMain() {
						}

					}).start();
				}
			}
		});


		sevPrintServiceHotline.setParamData(R.string.print_service_hotline, keyboardNumber, ParamsConst.PARAMS_KEY_BASE_HOTLINE);
		sevPrintServiceHotline.setMaxLength(20);

	}
	
//	@OnClick({R.id.txt_setting_print_other})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_setting_print_other) {//其它设置
			activity.switchContent(new SettingPrintOtherFragment());

		}
	}

	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		
	}
	@Override
	public void onFragmentHide() {
		scvPrintSignOrder.dismissDialog();
		super.onFragmentHide();
	}
}
