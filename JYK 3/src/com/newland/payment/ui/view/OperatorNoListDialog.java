package com.newland.payment.ui.view;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.newland.payment.R;

public class OperatorNoListDialog extends Dialog{
	
	private ListView operatorList ;
	ArrayList<String> mList;
	public OperatorNoListDialog(Context context,int threm,ArrayList<String> list) {
		super(context,threm);
		this.mList = list;
	}
	public OperatorNoListDialog(Context context,ArrayList<String> list) {
		super(context);
		this.mList = list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operator_no_list_dialog);
		
		operatorList = (ListView)findViewById(R.id.lv_operator_list);

        final ArrayList<String> todoItems=mList;
        final ArrayAdapter<String> aa;
        aa=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,todoItems);
        operatorList.setAdapter(aa);

	}
}
