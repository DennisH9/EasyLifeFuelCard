package com.newland.payment.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newland.base.util.DisplayUtils;
import com.newland.base.util.InputUtils;
import com.newland.payment.R;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 通用输入dialog
 * 
 * @author CB
 * @date 2015-5-19
 * @time 下午7:48:22
 */
public class CommonInputDialog extends Dialog {

	private View view;
	private LinearLayout llMain;
	private TextView txtTitle;
	private EditText etContent;
	private TextView txtCancel;
	private TextView txtSure;
	private TextView txtMiddle;
	private ImageView ivLine;
	private ImageView ivLineMiddle;

	private Context context;

	public CommonInputDialog(Context context) {
		super(context, R.style.common_full_dialog);
		this.context = context;

		view = View.inflate(context, R.layout.common_input_dialog_view, null);
		llMain = (LinearLayout) view.findViewById(R.id.ll_main);
		txtTitle = (TextView) view.findViewById(R.id.txt_title);
		etContent = (EditText) view.findViewById(R.id.et_content);
		txtCancel = (TextView) view.findViewById(R.id.txt_cancel);
		txtSure = (TextView) view.findViewById(R.id.txt_sure);
		txtMiddle = (TextView) view.findViewById(R.id.txt_middle);
		ivLine = (ImageView) view.findViewById(R.id.iv_line);
		ivLineMiddle = (ImageView) view.findViewById(R.id.iv_line_middle);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view);
		
		//关闭软键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		setCancelable(false);
		// APP 获取 HOME keyEvent的设定关键代码。
		getWindow().addFlags(3);
		getWindow().addFlags(5);
//		if(VERSION.SDK_INT > VERSION_CODES.JELLY_BEAN_MR1) {
//			getWindow().addFlags(3);
//		} else {
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_HOME_KEY_EVENT);
//		}
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
		
		setOnShowListener(new OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				InputUtils.showKeyboard(etContent);
			}
		});

	}

	/**
	 * 取消按钮监听器
	 * 
	 * @param resCancelText
	 * @param listenerCancel
	 */
	public void setCancelListener(int resCancelText,
			final View.OnClickListener listenerCancel) {

		if (listenerCancel == null) {
			ivLine.setVisibility(View.GONE);
			txtCancel.setVisibility(View.GONE);
		} else {
			ivLine.setVisibility(View.VISIBLE);
			txtCancel.setVisibility(View.VISIBLE);
			txtCancel.setText(resCancelText);
			txtCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					listenerCancel.onClick(v);
				}
			});
		}
	}

	/**
	 * 中间按钮监听器
	 * 
	 * @param resMiddelText
	 * @param listenerMiddle
	 */
	public void setMiddleListener(int resMiddelText,
			final View.OnClickListener listenerMiddle) {

		if (listenerMiddle == null) {
			ivLineMiddle.setVisibility(View.GONE);
			txtMiddle.setVisibility(View.GONE);
		} else {
			ivLineMiddle.setVisibility(View.VISIBLE);
			txtMiddle.setVisibility(View.VISIBLE);
			txtMiddle.setText(resMiddelText);
			txtMiddle.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					listenerMiddle.onClick(v);
				}
			});
		}
	}

	/**
	 * 确定按钮监听器
	 * 
	 * @param resSureText
	 * @param listenerSure
	 */
	public void setSureListener(int resSureText,
			final View.OnClickListener listenerSure) {

		if (listenerSure == null) {
			ivLine.setVisibility(View.GONE);
			txtSure.setVisibility(View.GONE);
		} else {
			ivLine.setVisibility(View.VISIBLE);
			txtSure.setVisibility(View.VISIBLE);
			txtSure.setText(resSureText);
			txtSure.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					listenerSure.onClick(v);
				}
			});
		}
	}

	public void setTitle(int resTitle) {
		if (resTitle != -1) {
			txtTitle.setText(resTitle);
		}
	}

	public void setTitle(String title) {
		if (title != null) {
			txtTitle.setText(title);
		}
	}

	public void setContent(String content) {
		if (content != null) {
			etContent.setText(content);
		}

	}

	public void setContent(int resContent) {
		if (resContent != -1) {
			etContent.setText(resContent);
		}

	}

	public String getContent() {
		return etContent.getText().toString();
	}
	
	/**
	 * 设置dialog的宽度
	 * 
	 * @param resDp
	 *            资源文件中dp的源
	 */
	public void setWidth(int resDp) {
		FrameLayout.LayoutParams params = (LayoutParams) llMain
				.getLayoutParams();
		params.width = DisplayUtils.getDimensPx(context, resDp);
	}
	
	/**
	 * 设置最大字符
	 * @param maxCharacter
	 */
	public void setMaxCharacterNum(final int maxCharacter) {
		InputFilter filter = new InputFilter() {
			
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				
				LoggerUtils.i("source:"+source+",start:"+start+",end:"+end+",dest:"+dest
						+",dstart:"+dstart+",dend:"+dend);
				
				if (getCharacterNum(source.toString()+dest+"") <= maxCharacter) {
					return null;
				} else {
					return "";
				}
				
			}
		};
		etContent.setFilters(new InputFilter[]{filter});
	}
	
	/**
	 * 设置单行
	 */
	public void setSingleLine() {
		etContent.setSingleLine();
	}
	
	/**
     * @description 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
     * @param content
     * @return
     */
    private int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }
    }

    /**
    * @description 返回字符串里中文字或者全角字符的个数
    * @param s
    * @return
    */
    private int getChineseNum(String s) {
        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

}
