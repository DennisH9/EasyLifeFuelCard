package com.newland.payment.ui.view;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.newland.payment.R;

/**
 * 数字键盘
 * Created by ZhengWx
 * on 17/1/23 16:49
 */

public class NumberKeyboardView extends LinearLayout implements View.OnClickListener {
    private EditText editText;

    public NumberKeyboardView(Context context) {
        this(context, null);
    }

    public NumberKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_number_keyboard, this, true);
        initView(view);
    }

    private void initView(View parent) {
        parent.findViewById(R.id.tv_num_0).setOnClickListener(this);
        parent.findViewById(R.id.tv_num_1).setOnClickListener(this);
        parent.findViewById(R.id.tv_num_2).setOnClickListener(this);
        parent.findViewById(R.id.tv_num_3).setOnClickListener(this);
        parent.findViewById(R.id.tv_num_4).setOnClickListener(this);
        parent.findViewById(R.id.tv_num_5).setOnClickListener(this);
        parent.findViewById(R.id.tv_num_6).setOnClickListener(this);
        parent.findViewById(R.id.tv_num_7).setOnClickListener(this);
        parent.findViewById(R.id.tv_num_8).setOnClickListener(this);
        parent.findViewById(R.id.tv_num_9).setOnClickListener(this);
        parent.findViewById(R.id.tv_num_0).setOnClickListener(this);
        parent.findViewById(R.id.iv_delete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_delete) {
            if (editText != null) {
                int index = editText.getSelectionStart();
                if (index > 0) {
                    Editable editable = editText.getText();
                    editable.delete(index-1, index);
                }
            }
        } else {
            String num = "";
            if (id == R.id.tv_num_0) {
                num = "0";
            } else if (id == R.id.tv_num_1) {
                num = "1";
            } else if (id == R.id.tv_num_2) {
                num = "2";
            } else if (id == R.id.tv_num_3) {
                num = "3";
            } else if (id == R.id.tv_num_4) {
                num = "4";
            } else if (id == R.id.tv_num_5) {
                num = "5";
            } else if (id == R.id.tv_num_6) {
                num = "6";
            } else if (id == R.id.tv_num_7) {
                num = "7";
            } else if (id == R.id.tv_num_8) {
                num = "8";
            } else if (id == R.id.tv_num_9) {
                num = "9";
            }

            if (editText != null) {
                editText.append(num);
            }
        }
    }

    public void setInputEdittext(EditText edittext) {
        this.editText = edittext;
    }

}
