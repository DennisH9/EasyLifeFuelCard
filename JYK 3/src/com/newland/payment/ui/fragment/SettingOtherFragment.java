package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newland.base.CommonThread;
import com.newland.base.util.MessageUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.base.util.TransUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.service.CommonDBService;
import com.newland.payment.mvc.service.CommonDBService.CleanType;
import com.newland.payment.mvc.service.ReverseWaterService;
import com.newland.payment.mvc.service.impl.CommonDBServiceImpl;
import com.newland.payment.mvc.service.impl.ReverseWaterServiceImpl;
import com.newland.payment.ui.view.SettingSwitchView;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 其它功能设置界面
 *
 * @author CB
 * @date 2015-5-15 
 * @time 下午4:21:11
 */
@SuppressLint("ValidFragment")
public class SettingOtherFragment extends BaseSettingFragment{
	
	/** 下载功能 */
//	@ViewInject(R.id.txt_setting_other_down_load)
	TextView txtSettingOtherDownLoad;
	/** 电子签名设置 */
//	@ViewInject(R.id.txt_setting_other_elecsign)
	TextView txtSettingOtherElecsign;
	/** LBS定位 */
//	@ViewInject(R.id.ssv_setting_other_lbs)
	SettingSwitchView ssvSettingOtherLbs;
	/** 插卡开关 */
//	@ViewInject(R.id.ssv_setting_insert_card)
	SettingSwitchView ssvSettingInsertCard;
	/** 二次清分开关 */
//	@ViewInject(R.id.ssv_setting_use_distri_bution)
	SettingSwitchView ssvSettingUseDustri;

	public SettingOtherFragment(long outTime) {
		super(outTime);
	}
	public SettingOtherFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_other_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	@Override
	protected void initData() {
		setTitle(R.string.setting_other);
		ssvSettingOtherLbs.setParamData(R.string.setting_other_lbs, ParamsConst.PARAMS_KEY_IS_SUPPORT_LBS);
		ssvSettingInsertCard.setParamData(R.string.rfcard_first, ParamsConst.PARAM_IS_HIDING_SWING);
		ssvSettingUseDustri.setParamData(R.string.is_use_distri_bution, ParamsConst.PARAMS_IS_USE_DISTRI_BUTION);
	}

//	@OnClick({R.id.txt_setting_other_down_load,
//		R.id.txt_setting_other_elecsign,
//		R.id.txt_setting_clear_rush_water,
//		R.id.txt_setting_clear_trans_water,
//		R.id.txt_clear_script_execution_result_of_the_water,
//		R.id.txt_clear_settlement_marks,
//		R.id.txt_clear_request_processing})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_setting_other_down_load) {//下载功能
			activity.switchContent(new SettingOtherDownloadFragment());

		} else if (i == R.id.txt_setting_other_elecsign) {//电子签名设置
			activity.switchContent(new SettingOtherElecSignFragment());

		} else if (i == R.id.txt_setting_clear_rush_water) {//清冲正流水
			activity.commonDialog = MessageUtils.showCommonDialog(context,
					R.string.setting_clear_rush_water, false, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							// 清冲正流水
							new CommonThread(new ThreadCallBack() {
								@Override
								public void onMain() {
									ToastUtils
											.show(context,
													R.string.success_setting_clear_rush_water);
								}

								@Override
								public void onBackGround() {
									ReverseWaterService reverseWaterService = new ReverseWaterServiceImpl(
											context);
									reverseWaterService.delete();
								}
							}).start();

						}
					});

		} else if (i == R.id.txt_setting_clear_trans_water) {//清交易流水
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.setting_clear_trans_water, false, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 清交易流水
							new CommonThread(new ThreadCallBack() {
								@Override
								public void onMain() {
									ToastUtils.show(context, R.string.success_setting_clear_trans_water);
								}

								@Override
								public void onBackGround() {

									TransUtils.clearWater(context);
								}
							}).start();
						}
					});

		} else if (i == R.id.txt_clear_script_execution_result_of_the_water) {//清脚本执行结果流水
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.clear_script_execution_result_of_the_water, false, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {


							new CommonThread(new ThreadCallBack() {
								@Override
								public void onMain() {
									ToastUtils.show(context, R.string.success_clear_script_execution_result_of_the_water);
								}

								@Override
								public void onBackGround() {
									CommonDBService service = new CommonDBServiceImpl(
											context);
									service.cleanWater(CleanType.REVERSE_WATER);
								}
							}).start();

						}
					});

		} else if (i == R.id.txt_clear_settlement_marks) {//清结算标志
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.clear_settlement_marks, false, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							new CommonThread(new ThreadCallBack() {
								int result;

								@Override
								public void onMain() {
									if (result == 1) {
										ToastUtils.show(context, R.string.success_clear_settlement_marks);
									}
								}

								@Override
								public void onBackGround() {
									result = ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_SETTLT_HALT, false);
								}
							}).start();

						}
					});

		} else if (i == R.id.txt_clear_request_processing) {//清要求处理标志
			activity.commonDialog = MessageUtils.showCommonDialog(context, R.string.clear_request_processing, false, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							new CommonThread(new ThreadCallBack() {
								int result = 1;

								@Override
								public void onMain() {
									if (result == 1) {
										ToastUtils.show(context, R.string.success_clear_dispose_marks);
									}
								}

								@Override
								public void onBackGround() {
									String currentKey = ParamsTrans.PARAMS_IS_AID_DOWN;
									result = result | ParamsUtils.setBoolean(currentKey, false);

									currentKey = ParamsTrans.PARAMS_IS_CAPK_DOWN;
									result = result | ParamsUtils.setBoolean(currentKey, false);

									currentKey = ParamsTrans.PARAMS_IS_BALACKLIST_DOWN;
									result = result | ParamsUtils.setBoolean(currentKey, false);

									currentKey = ParamsTrans.PARAMS_IS_TMS_PARAM_DOWN;
									result = result | ParamsUtils.setBoolean(currentKey, false);

									currentKey = ParamsTrans.PARAMS_IS_PARAM_DOWN;
									result = result | ParamsUtils.setBoolean(currentKey, false);

									currentKey = ParamsTrans.PARAMS_IS_STATUS_SEND;
									result = result | ParamsUtils.setBoolean(currentKey, false);

									currentKey = ParamsTrans.PARAMS_IS_RESIGN;
									result = result | ParamsUtils.setBoolean(currentKey, false);
								}
							}).start();

						}
					});

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
