package com.newland.payment.mvc.dao.impl;

import java.util.List;

import android.content.Context;

import com.newland.base.dao.BaseDao;
import com.newland.payment.mvc.dao.WaterDao;
import com.newland.payment.mvc.model.Water;

/**
 * 流水处理DAO
 * @author linchunhui
 * @date 2015年5月12日 下午5:42:40
 *
 */
public class WaterDaoImpl extends BaseDao<Water> implements WaterDao {

	public WaterDaoImpl(Context context) {
		super(context);
	}

	@Override
	public List<Water> findAll() {
		return super.findAll();
	}
	
	@Override
	public Water findLastWater() {
		List<Water> list = super.query(" id desc", "1");
		return list.size() > 0 ? list.get(0) : null;
	}
	
	@Override
	public Water findById(long id) {
		return super.findById(id);
	}
	
	
	@Override
	public List<Water> findByColumnName(String columnName, Object value) {
		return super.query(columnName, new String[]{value.toString()}, null);
	}

	@Override
	public int deleteAll() {
		return super.delete("", new String[]{});
	}
	
	@Override
	public int deleteById(long id) {
		return super.delete(id);
	}

	@Override
	public int getCount() {
		return super.getRowCount();
	}

	@Override
	public List<Water> findByPage(int pageNo, int pageSize) {
		if (pageSize < 1) {
			pageSize = 10;
		}
		if (pageNo < 1) {
			pageNo = 1;
		}
		return super.query(new Water(), " id desc", pageSize, pageNo);
	}
	
	@Override
	public List<Water> findByTransTypeAndTransStatus(int transType, int transStatus){
		List<Water> list = super.query(
				"TRANS_TYPE=? and TRANS_STATUS=?",
				new String[] { String.valueOf(transType),
						String.valueOf(transStatus) }, null);
		return list;
	}
	
	@Override
	public Water findByTraceNoAndTime(String traceNo, String time) {
		List<Water> list = super.query(
				"TRACE=? and TIME=?",
				new String[] {traceNo, time}, null);
		return (list!=null&&!list.isEmpty())?list.get(0):null;
	}

}
