package com.newland.payment.mvc.dao.impl;

import android.content.Context;

import com.newland.base.dao.BaseDao;
import com.newland.payment.mvc.dao.ScriptResultDao;
import com.newland.payment.mvc.model.ScriptResult;

public class ScriptResultDaoImpl extends BaseDao<ScriptResult> implements ScriptResultDao {

	public ScriptResultDaoImpl(Context context) {
		super(context);
	}

	@Override
	public int deleteAll() {
		return super.delete("", new String[]{});
	}
	
}
