package com.newland.payment.mvc.service.impl;

import android.content.Context;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.mvc.DuplicatedReverseWaterException;
import com.newland.payment.mvc.dao.ReverseWaterDao;
import com.newland.payment.mvc.dao.impl.ReverseWaterDaoImpl;
import com.newland.payment.mvc.model.ReverseWater;
import com.newland.payment.mvc.service.ReverseWaterService;
import com.newland.pos.sdk.bean.MessageTipBean;

/**
 * 冲正流水处理
 * @author linchunhui
 * @date 2015年5月12日 下午5:48:01
 *
 */
public class ReverseWaterServiceImpl implements ReverseWaterService {

	private ReverseWaterDao reverseWaterDao;
	
	public ReverseWaterServiceImpl(Context context) {
		reverseWaterDao = new ReverseWaterDaoImpl(context);
	}
	
	
	@Override
	public ReverseWater getReverseWater()  {
		return reverseWaterDao.get();
	}

	@Override
	public long add(ReverseWater reverseWater) throws DuplicatedReverseWaterException {
		if (reverseWaterDao.get() != null) {
			throw new DuplicatedReverseWaterException("已存在冲正流水，不能添加");
		}
		return reverseWaterDao.insert(reverseWater);
	}

	@Override
	public int delete() {
		
		ReverseWater rw = reverseWaterDao.get();
		if (rw == null) {
			return 0;
		}
		
		return reverseWaterDao.deleteAll();
	}


	@Override
	public int changeField39Result(String field39Value) {
		ReverseWater rw = reverseWaterDao.get();
		
		if (rw == null) {
			return 0;
		}
		rw.setResponse(field39Value);
		return this.reverseWaterDao.update(rw);
	}
	
	@Override
	public int changeField55(String field55){
		ReverseWater rw = reverseWaterDao.get();
		
		if (rw == null) {
			return 0;
		}
		rw.setField55(field55);
		return this.reverseWaterDao.update(rw);
	}

	@Override
	public int changeReverseWater(String field39, String field55, String field61) {
		ReverseWater rw = reverseWaterDao.get();
		
		if (rw == null) {
			return 0;
		}
		
		if (field39 != null){
			rw.setResponse(field39);
		}
		
		if (field55 != null){
			rw.setField55(field55);
		}
		
		if (field61 != null){
			rw.setField61(field61);
		}
		return this.reverseWaterDao.update(rw);
	}
}
