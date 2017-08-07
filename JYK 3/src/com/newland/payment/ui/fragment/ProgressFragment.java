package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.newland.payment.R;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 进度条界面
 * @author chenkh
 * @date 2015-5-17
 * @time 上午10:06:33
 *
 */
@SuppressLint({ "InflateParams", "ValidFragment" })
public class ProgressFragment extends BaseFragment {
	
	private Dialog progressCountDialog;
	private TextView tvTips;
	private View rootView;
	private String msg;
	
	private ProgressFragment(){
		super(0);
	}
	
	public static ProgressFragment newInstance(String tipMessage) {
		ProgressFragment fragment = new ProgressFragment();
		fragment.msg = tipMessage;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.empty_view,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		showCountProgress(msg);
	}

	@Override
	protected void initClickEvent(View view) {
		
	}

	private void initCountProgress() {
		if (null == progressCountDialog) {
			progressCountDialog = new Dialog(activity, R.style.progress_dialog);
			progressCountDialog.setCancelable(false);
			rootView = LayoutInflater.from(activity).inflate(
					R.layout.progress_contain_cutdown_text_tip, null);
			tvTips = (TextView) rootView.findViewById(R.id.tv_dialog_tips);

			progressCountDialog.setContentView(rootView);
			// APP 获取 HOME keyEvent的设定关键代码。
			progressCountDialog.getWindow().addFlags(3);
			progressCountDialog.getWindow().addFlags(5);
//			if(VERSION.SDK_INT > VERSION_CODES.JELLY_BEAN_MR1) {
//				progressCountDialog.getWindow().addFlags(3);
//			} else {
//				progressCountDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_HOME_KEY_EVENT);
//			}
			
			progressCountDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dg, int keyCode,
						KeyEvent event) {
					return false;
				}
			});
		}
	}

	private void showCountProgress(final String msg) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (activity.isFinishing()) {
					return;
				}
				if (null == progressCountDialog) {
					initCountProgress();
				}
				if (progressCountDialog != null
						&& !progressCountDialog.isShowing()) {
					if (!StringUtils.isEmpty(msg))
						tvTips.setText(msg);
					progressCountDialog.show();
				} else if (null != progressCountDialog
						&& progressCountDialog.isShowing()) {
					if (!StringUtils.isEmpty(msg))
						tvTips.setText(msg);
				}
			}
		});

	}

	private void dismissDialog(){
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (null != progressCountDialog
						&& progressCountDialog.isShowing()) {
					progressCountDialog.dismiss();
					progressCountDialog = null;
				}
			}
		});
	}

	@Override
	protected void initEvent() {
		
	}
	
	@Override
	public void onFragmentHide() {
		dismissDialog();
		super.onFragmentHide();
	}

	@Override
	protected BaseBean getBean() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
