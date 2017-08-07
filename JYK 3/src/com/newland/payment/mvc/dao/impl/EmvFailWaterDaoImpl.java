package com.newland.payment.mvc.dao.impl;

import android.content.Context;

import com.newland.base.dao.BaseDao;
import com.newland.payment.mvc.dao.EmvFailWaterDao;
import com.newland.payment.mvc.model.EmvFailWater;

public class EmvFailWaterDaoImpl extends BaseDao<EmvFailWater> implements EmvFailWaterDao {

	public EmvFailWaterDaoImpl(Context context) {
		super(context);
	}


	@Override
	public int deleteAll() {
		return super.delete("", new String[]{});
	}

	@Override
	public EmvFailWater findById(long id){
		return super.findById(id);
	}


	@Override
	public int getCount() {
		return super.getRowCount();
	}

}
