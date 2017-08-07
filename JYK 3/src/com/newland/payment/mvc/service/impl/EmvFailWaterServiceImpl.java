package com.newland.payment.mvc.service.impl;

import java.util.List;

import android.content.Context;

import com.newland.payment.mvc.dao.EmvFailWaterDao;
import com.newland.payment.mvc.dao.impl.EmvFailWaterDaoImpl;
import com.newland.payment.mvc.model.EmvFailWater;
import com.newland.payment.mvc.service.EmvFailWaterService;
import com.newland.payment.mvc.service.CommonDBService.CleanType;

public class EmvFailWaterServiceImpl implements EmvFailWaterService{

	private Context context;
	private EmvFailWaterDao emvFailWaterDao;
	
	public EmvFailWaterServiceImpl(Context context){
		this.context = context;
		this.emvFailWaterDao = new EmvFailWaterDaoImpl(context);
	}
	
	@Override
	public long addEmvFailWater(EmvFailWater emvFailWater) {
		long rows = 0;
		if ((rows = this.emvFailWaterDao.insert(emvFailWater)) < 1) {
			throw new RuntimeException("添加流水失败");
		}
		return rows;
	}

	@Override
	public int updateEmvFailWater(EmvFailWater emvFailWater) {
		return this.emvFailWaterDao.update(emvFailWater);
	}

	@Override
	public EmvFailWater findEmvFailWater(long id) {
		return this.emvFailWaterDao.findById(id);
	}

	@Override
	public void clearEmvFailWater() {
		CommonDBServiceImpl commonDBServiceImpl = new CommonDBServiceImpl(context);
		commonDBServiceImpl.cleanWater(CleanType.EMV_FAIL_WATER);		
	}

	@Override
	public int getCount() {
		return this.emvFailWaterDao.getCount();
	}

	@Override
	public List<EmvFailWater> findAll() {
		return this.emvFailWaterDao.findAll();
	}

}
