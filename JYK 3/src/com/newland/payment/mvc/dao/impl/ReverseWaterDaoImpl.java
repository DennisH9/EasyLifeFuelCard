package com.newland.payment.mvc.dao.impl;

import java.util.List;

import android.content.Context;

import com.newland.base.dao.BaseDao;
import com.newland.payment.mvc.dao.ReverseWaterDao;
import com.newland.payment.mvc.model.ReverseWater;

/**
 * 冲正流水处理DAO
 * @author linchunhui
 * @date 2015年5月12日 下午5:50:19
 *
 */
public class ReverseWaterDaoImpl extends BaseDao<ReverseWater> implements ReverseWaterDao {

	public ReverseWaterDaoImpl(Context context) {
		super(context);
	}

	@Override
	public ReverseWater get() {
		List<ReverseWater> l = super.findAll();
		return l.size() > 0 ? l.get(0) : null;
	}

	@Override
	public int deleteAll() {
		return super.delete("", new String[]{});
	}
}
