package com.newland.payment.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.newland.payment.R;
import com.newland.payment.ui.adapter.MenuSelectAdapter;
import com.newland.payment.ui.listener.OnListDialogClickListener;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 通用列表选择dialog
 * 
 * @author CB
 * @date 2015-5-19
 * @time 下午7:48:22
 */
public class CommonListDialog extends Dialog {
	
	/** 列表 */
//	@ViewInject(R.id.lv_select)
	private ListView lv;
	/** 标题 */
//	@ViewInject(R.id.txt_title)
	private TextView txtTitle;
	/** 取消按钮 */
//	@ViewInject(R.id.txt_cancel)
	private TextView txtCancel;
	/** 确定按钮 */
//	@ViewInject(R.id.txt_sure)
	private TextView txtSure;
	
	private View view;
	private OnListDialogClickListener listenerSure;
	private View.OnClickListener listenerCancel;
	private MenuSelectAdapter menuSelectAdapter;
	
	public CommonListDialog(Context context, int title, String[] items,
			OnListDialogClickListener listenerSure){
		this(context, context.getString(title), items, 0, null, listenerSure, null);
	}

	public CommonListDialog(Context context, String title, String[] items,
			int defaultSelected,
			AdapterView.OnItemClickListener itemSelectListener,
			OnListDialogClickListener listenerSure,
			View.OnClickListener listenerCancel) {
		super(context, R.style.common_full_dialog);
		view = View.inflate(context, R.layout.common_select_view, null);
		ViewUtils.inject(this, view);
		if(!StringUtils.isEmpty(title)){
			txtTitle.setText(title);
		}
		menuSelectAdapter = new MenuSelectAdapter(context, StringUtils.stringsToList(items));
		menuSelectAdapter.setSelfOnItemClickListener(itemSelectListener);
		
		this.listenerSure = listenerSure;
		this.listenerCancel = listenerCancel;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view);
		initView();
		initData();
		initEvent();
	}
	
	private void initView() {
		lv = (ListView) findViewById(R.id.lv_select);
		txtTitle = (TextView) findViewById(R.id.txt_title);
		txtCancel = (TextView) findViewById(R.id.txt_cancel);
		txtSure = (TextView) findViewById(R.id.txt_sure);
	}

	private void initData() {

		setCancelable(false);
		// APP 获取 HOME keyEvent的设定关键代码。
		getWindow().addFlags(3);
		getWindow().addFlags(5);
//		if(VERSION.SDK_INT > VERSION_CODES.JELLY_BEAN_MR1) {
//			getWindow().addFlags(3);
//		} else {
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_HOME_KEY_EVENT);
//		}
		lv.setAdapter(menuSelectAdapter);
		
	}

	private void initEvent() {
		
		//屏蔽HOME
		setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dg, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_HOME) {
					return true;
				} 
				return false;
			}
		});
		//确定按钮
		txtSure.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				if (listenerSure != null) {
					listenerSure.onClick(menuSelectAdapter.getCheckPosition());
				}
			}
		});
		//取消按钮
		txtCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				if (listenerCancel != null) {
					listenerCancel.onClick(v);
				}
			}
		});
	}
	
	/**
	 * 更新选中项
	 * @param position
	 */
	public void setCheck(int position) {
		menuSelectAdapter.setCheck(position);
	}
}
