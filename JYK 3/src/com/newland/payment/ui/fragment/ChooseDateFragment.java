package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.payment.R;
import com.newland.payment.common.Const;
import com.newland.payment.common.Const.DateWheelType;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.ui.listener.OnDateEnterListener;
import com.newland.payment.ui.view.wheelview.DateWheelView2;
import com.newland.payment.ui.view.wheelview.OnDateWheelViewChangeListener;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

import java.text.SimpleDateFormat;

/**
 * 日期选择界面
 *
 * @author CB
 * @time 2015-4-21 下午2:44:33
 */
@SuppressLint({ "SimpleDateFormat", "ValidFragment", "InflateParams" })
public class ChooseDateFragment extends BaseFragment{

	/** 有效日期 */
	public static final int TYPE_INDATE = 1;
	/** 交易日期 */
	public static final int TYPE_TRANSACTION = 2;

	/** 自定义日期控件 */
//	@ViewInject(R.id.date_wheel_view)
	private DateWheelView2 dateWheelView;
	/** 输入框 */
//	@ViewInject(R.id.edit_text)
	private EditText editText;
	/**  */
//	@ViewInject(R.id.iv_cancel)
	private ImageView ivCancel;
	/** 标题 */
//	@ViewInject(R.id.txt_title)
	private TextView txtTitle;
	/** 标题附件文字 */
//	@ViewInject(R.id.txt_title_except)
	private TextView txtTitleExcept;
	
	private SimpleDateFormat format;
	private String strFormat;
	private OnDateEnterListener onDateEnterListener;
	private int dateWheelType;
	private CommonBean<String> bean;
	
	public ChooseDateFragment (CommonBean<String> bean, int dateWheelType, OnDateEnterListener onDateEnterListener) {
		
		super(bean.getTimeOut());
		this.bean = bean;
		this.dateWheelType = dateWheelType;
		this.onDateEnterListener = onDateEnterListener;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.choose_date_fragment,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		
		setTitle(bean.getTitle());
		
		txtTitle.setText(bean.getContent());
		txtTitleExcept.setVisibility(bean.isEmptyFlag() ? View.VISIBLE : View.GONE);
		
		if ((dateWheelType & Const.DateWheelType.YEAR) == 0) {
			dateWheelView.setIsShowYear(false);
		} 
		if ((dateWheelType & Const.DateWheelType.MOUTH) == 0) {
			dateWheelView.setIsShowMouth(false);
		}
		if ((dateWheelType & Const.DateWheelType.DAY) == 0) {
			dateWheelView.setIsShowDay(false);
		}
		
		strFormat = "";
		
		if ((dateWheelType & Const.DateWheelType.YEAR) == DateWheelType.YEAR) {
			strFormat += "yy";
		} 
		if ((dateWheelType & Const.DateWheelType.MOUTH) == DateWheelType.MOUTH) {
			strFormat += strFormat.equals("") ? "MM" : "-MM";
		} 
		if ((dateWheelType & Const.DateWheelType.DAY) == DateWheelType.DAY) {
			strFormat += strFormat.equals("") ? "dd" : "-dd";
		}
		format = new SimpleDateFormat(strFormat);
		
		editText.setInputType(InputType.TYPE_NULL);
		
		
	}
	
//	@OnClick(R.id.button)
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.iv_cancel) {
			editText.setText("");

		} else if (i == R.id.button) {
			if (StringUtils.isEmpty(editText.getText().toString())
					&& !bean.isEmptyFlag()) {
				editText.setText(format.format(dateWheelView.getDate()));
			} else {

				if (onDateEnterListener != null) {
					onDateEnterListener.onEnter(StringUtils.isEmpty(editText
							.getText().toString()) ? null : dateWheelView
							.getDate());
				}
			}


		}
	}

	@Override
	protected void initEvent() {
		dateWheelView.setNowDate();
		dateWheelView.addOnDateWheelViewChangeListener(new OnDateWheelViewChangeListener() {
			
			@Override
			public void onChanged(long oldValue, long newValue) {
				LoggerUtils.i("dateWheelView:"+format.format(newValue));
				editText.setText(format.format(newValue));
			}
		});
		ivCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editText.setText("");
			}
		});
	}

	@Override
	public BaseBean getBean() {
		return bean;
	}
}
