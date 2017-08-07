package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.newland.base.util.MessageUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.base.util.ViewUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.trans.bean.SignatureBean;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.view.HandWriteView;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.BitmapUtils;
import com.newland.pos.sdk.util.JBigConvert;
import com.newland.pos.sdk.util.LoggerUtils;

import java.io.File;

/**
 * 签名
 * 
 * @time 2015-4-17 下午4:23:40
 */
@SuppressLint({ "InflateParams", "ValidFragment" })
public class SignatureFragment extends BaseFragment {

	/** 签名view */
//	@ViewInject(R.id.signature_view)
	HandWriteView signatureView;
	/** 取消*/
//	@ViewInject(R.id.signature_cancel)
	TextView btnCancel;
	/** 擦除*/
//	@ViewInject(R.id.signature_reset)
	Button btnReset;
	/** 确认*/
//	@ViewInject(R.id.signature_confirm)
	Button btnConfirm;
	
	private static final String SIGNATURE_JBIG_DIR = App.SIGNATURE_JBIG_DIR;
	
	private static final String SIGNATURE_BMP_DIR = App.SIGNATURE_BMP_DIR;
	
	private static final String JBIG_END = "";
	
	private static final String PNG_END = "";
	
	private static final int MAX_LEN = com.newland.payment.common.Const.SIGNATURE_MAX_LEN;
	
	private SignatureBean bean;
	
	private String jbigFileName = "";
	
	private String pngFileName = "";
	
	/**重签次数*/
	private int resetTimes;
	/**是否确认电子签名*/
	private boolean needConfrim;
	
	public SignatureFragment(long timeOut) {
		super(timeOut);
	}
	
	public static SignatureFragment newInstance(SignatureBean signatureBean) {
		SignatureFragment fragment = new SignatureFragment(signatureBean.getTimeOut());
		fragment.bean = signatureBean;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.signatrue_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
		jbigFileName = SIGNATURE_JBIG_DIR + bean.getTraceCode() + JBIG_END;
		pngFileName = SIGNATURE_BMP_DIR + bean.getTraceCode() + PNG_END;
		
		resetTimes = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_ELEC_RESIGN_TIMES);
		needConfrim = ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_CONFIRM_ELECSIGN);
		

		if (resetTimes < 1) {
			btnReset.setEnabled(false);
		}
		
		File jbigDir = new File(SIGNATURE_JBIG_DIR);
		if (jbigDir.exists() && !jbigDir.isDirectory()) {
			jbigDir.delete();
		}
		if (!jbigDir.exists()) {
			jbigDir.mkdirs();
		}
		
		File bmpDir = new File(SIGNATURE_BMP_DIR);
		if (bmpDir.exists() && !bmpDir.isDirectory()) {
			bmpDir.delete();
		}
		if (!bmpDir.exists()) {
			bmpDir.mkdirs();
		}
		
		signatureView.setSignatureCode(bean.getSignatureCode());
	}

	@Override
	protected void initClickEvent(View view) {

	}

	private Dialog progressBarDialog = null;
	private TextView tvTips;
	private View view;
	
	@Override
	protected void initEvent() {

		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//
		LoggerUtils.d("111 签名点击确认按钮");		
				if(ViewUtils.isFastClick()){
					return;
				}
				
				if(!signatureView.isValid()){
					LoggerUtils.d("signatureView data is not valid");
					ToastUtils.show(activity, getString(R.string.signature_not_valid));
					return;
				}
				//
				if (needConfrim) {
					activity.commonDialog = MessageUtils.showCommonDialog(context,
							R.string.signature_confirm_use,
							new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									saveSignature();
								}
							}, new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									onFail();
								}
							});
				} else {
					saveSignature();
				}
				
				
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				LoggerUtils.d("cancel click");
				onBack();
			}
		});
		
		btnReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				LoggerUtils.d("reset click");
				if (resetTimes > 0) {

					resetTimes--;
					signatureView.clear();
					btnReset.setEnabled(true);
				} else {
					btnReset.setEnabled(false);
				}
			}
		});
		
		signatureView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				activity.resetProgress();
				return false;
			}
		});
		
	}
	
	
	private void saveSignature(){
		//
		LoggerUtils.d("111 saveSignature->1");
		progressBarDialog = new Dialog(context, R.style.progress_dialog);
		progressBarDialog.setCancelable(false);

		view = LayoutInflater.from(context).inflate(
				R.layout.progress_contain_cutdown_text_tip, null);
		tvTips = (TextView) view.findViewById(R.id.tv_dialog_tips);

		tvTips.setText(R.string.signature_saveing);
		progressBarDialog.setContentView(view);
		
		progressBarDialog.show();
		new Thread(new Runnable() {
			public void run() {
				File bmpFile = new File(pngFileName);
				File jbigFile = new File(jbigFileName);
				try {
					Bitmap srcBitmap = signatureView.getCachebBitmap();
					Bitmap bmpBitmap = srcBitmap;//压缩图片代码：Bitmap.createScaledBitmap(srcBitmap, 128, 64, true);
					BitmapUtils.saveBmp1(bmpBitmap, pngFileName);
					JBigConvert.convertToJBIG(pngFileName, jbigFileName);
					
					if (jbigFile.length() < MAX_LEN) {
						LoggerUtils.d("signature save jbig file success :"
								+ jbigFile.length());
						bean.setPngFileName(pngFileName);
						bean.setJbigFileName(jbigFileName);
						onSucess();
					} else {
						LoggerUtils.d("signature save jbig file fail : "
								+ jbigFile.length());
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ToastUtils.show(activity, getString(R.string.signature_data_too_big));
							}
						});
						
						onFail();
						jbigFile.delete();
						bmpFile.delete();
					}
				} catch (Exception e) {
					e.printStackTrace();
					if(bmpFile != null && bmpFile.exists()){
						bmpFile.delete();
					}
					if(jbigFile != null && jbigFile.exists()){
						jbigFile.delete();
					}
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ToastUtils.show(activity, getString(R.string.signature_error));
						}
					});
					onFail();
				} finally{
					activity.runOnUiThread(new Runnable() {
						public void run() {
							progressBarDialog.dismiss();
						}
					});
				}
			}
		}).start();
	}
	@Override
	public BaseBean getBean() {
		return this.bean;
	}

	@Override
	public void onFragmentHide() {
		super.onFragmentHide();
		dismissTipDialog();
	}
	
	private Dialog tipDialog = null;
	private void dismissTipDialog(){
		if(tipDialog != null){
			tipDialog.dismiss();
			tipDialog = null;
		}
	}
	
	@Override
	protected boolean doClickBackEvent() {
		tipDialog = MessageUtils.showCommonDialog(
				activity, 
				activity.getResources().getString(R.string.signature_cancel_tip_msg), 
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// 确认
						dismissTipDialog();
						onBack();
					}
				}, 
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// 取消
						dismissTipDialog();
					}
				});
		return false;
	}
	
	@Override
	protected boolean doClickHomeEvent() {
		return doClickBackEvent();
	}
}
