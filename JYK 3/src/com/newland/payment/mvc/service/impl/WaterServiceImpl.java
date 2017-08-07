package com.newland.payment.mvc.service.impl;

import java.util.List;

import android.content.Context;

import com.newland.payment.mvc.DuplicatedTraceException;
import com.newland.payment.mvc.dao.WaterDao;
import com.newland.payment.mvc.dao.impl.WaterDaoImpl;
import com.newland.payment.mvc.model.Water;
import com.newland.payment.mvc.service.CommonDBService.CleanType;
import com.newland.payment.mvc.service.WaterService;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 流水处理
 * @author linchunhui
 * @date 2015年5月12日 下午5:43:08
 *
 */
public class WaterServiceImpl implements WaterService {
	
	private Context context;
	private WaterDao waterDao;

	public WaterServiceImpl(Context context) {
		this.context = context;
		this.waterDao = new WaterDaoImpl(context);
	}

	@Override
	public long addWater(Water water) throws DuplicatedTraceException {
		
		if (water == null || water.getTrace() == null) {
			throw new RuntimeException("添加流水失败：null参数");
		}
		
		// 判断是否有重复的流水号
		if (this.findByTrace(water.getTrace()) != null) {
			throw new DuplicatedTraceException("重复的流水号，添加失败:" + water.getTrace());
		}
		
		long rows = 0;
		if ((rows = this.waterDao.insert(water)) < 1) {
			throw new RuntimeException("添加流水失败");
		}
		
		return rows;
	}
	

	@Override
	public int deleteById(long id) {
		return this.waterDao.deleteById(id);
	}

	@Override
	public int deleteByTrace(String trace) {
		Water water = this.findByTrace(trace);
		if (water == null) {
			return 0;
		}
		return this.deleteById(water.getId());
	}
	
	@Override
	public Water findByTrace(String trace) {
		if (trace == null) {
			return null;
		}
		List<Water> list = this.waterDao.findByColumnName("trace=?", trace);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	@Override
	public Water findByReferNum(String referNum) {
		if (referNum == null) {
			return null;
		}
		List<Water> list = this.waterDao.findByColumnName("REFER_NUM=?", referNum);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	@Override
	public Water findLastWater(){
		return this.waterDao.findLastWater();
	}

	@Override
	public int getWaterCount() {
		return this.waterDao.getCount();
	}

	@Override
	public List<Water> findByPage(int pageNo, int pageSize) {
		return this.waterDao.findByPage(pageNo, pageSize);
	}

	@Override
	public List<Water> findAllLikeTrace(String trace) {
		return null;
	}

	@Override
	public int updateWater(Water water) {
		if(water == null){
			return 0;
		}
		if(water.getId() == null){
			Water tmp = findByTrace(water.getTrace());
			if(tmp == null){
				LoggerUtils.e("water is not exist trace no:[" + water.getTrace() + "]");
				return -1;
			}
			water.setId(tmp.getId());
		}
		return this.waterDao.update(water);
	}

	
	/**
	 * 根据交易类型和交易状态获取流水
	 * @param transType
	 * @param transStatus
	 * @return
	 */
	@Override
	public List<Water> findByTransTypeAndTransStatus(int transType, int transStatus){
		return this.waterDao.findByTransTypeAndTransStatus(transType, transStatus);
	}

	/**
	 * 根据索引查找数据库中流水
	 */
	@Override
	public Water findById(long id) {
		return this.waterDao.findById(id);
	}

	@Override
	public void clearWater() {
		CommonDBServiceImpl commonDBServiceImpl = new CommonDBServiceImpl(context);
		commonDBServiceImpl.cleanWater(CleanType.WATER);
	}

	@Override
	public void findAll() {
		this.waterDao.findAll();
		
	}
	
	@Override
	public Water findByTrace(String traceNo, String time) {
		return this.waterDao.findByTraceNoAndTime(traceNo, time);
	}

	@Override
	public Water findByOutOrderNo(String outOrderNo) {
		if (outOrderNo == null) {
			return null;
		}
		List<Water> list = this.waterDao.findByColumnName("OUTORDERNO=?", outOrderNo);
		return list.size() > 0 ? list.get(0) : null;
	}
}
