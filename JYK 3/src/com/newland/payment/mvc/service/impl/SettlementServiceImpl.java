package com.newland.payment.mvc.service.impl;

import android.content.Context;

import com.newland.payment.mvc.dao.SettlementDao;
import com.newland.payment.mvc.dao.impl.SettlementDaoImpl;
import com.newland.payment.mvc.model.Settlement;
import com.newland.payment.mvc.service.SettlementService;
import com.newland.payment.mvc.service.CommonDBService.CleanType;

public class SettlementServiceImpl implements SettlementService {

	private Context context;
	private SettlementDao settlementDao;
	
	public SettlementServiceImpl(Context context){
		this.context = context;
		this.settlementDao = new SettlementDaoImpl(context);
	}

	/**
	 * 通过类型查找该类型的内卡笔数
	 * @param type 结算类型
	 * @return
	 */
	@Override
	public int getSettleNum_NK(String type) {
		Settlement settlement = settlementDao.findSettlementByTypeAndOrganization(type, 0);
		return settlement!=null ? settlement.getNum() : -1;
	}

	/**
	 * 通过类型查找该类型的内卡金额
	 * @param type 结算类型
	 * @return
	 */
	@Override
	public long getSettleAmount_NK(String type) {
		Settlement settlement = settlementDao.findSettlementByTypeAndOrganization(type, 0);
		return settlement!=null ? settlement.getAmount() : -1;
	}


	/**
	 * 通过类型查找该类型的外卡笔数
	 * @param type 结算类型
	 * @return
	 */
	@Override
	public int getSettleNum_WK(String type) {
		Settlement settlement = settlementDao.findSettlementByTypeAndOrganization(type, 1);
		return settlement!=null ? settlement.getNum() : -1;
	}

	/**
	 * 通过类型查找该类型的外卡金额
	 * @param type 结算类型
	 * @return
	 */
	@Override
	public long getSettleAmount_WK(String type) {
		Settlement settlement = settlementDao.findSettlementByTypeAndOrganization(type, 1);
		return settlement!=null ? settlement.getAmount() : -1;
	}

	/**
	 * 增加内卡指定结算类型笔数
	 * @return
	 */
	@Override
	public int addSettleNum_NK(String type, int num) {
		Settlement settlement = settlementDao.findSettlementByTypeAndOrganization(type, 0);
		if (settlement!=null) {
			settlement.setNum(settlement.getNum()+num);
			settlementDao.update(settlement);
			return 1;
		}
		return -1;
	}

	/**
	 * 增加内卡指定结算类型金额
	 * @return
	 */
	@Override
	public int addSettleAmount_NK(String type, long amount) {
		Settlement settlement = settlementDao.findSettlementByTypeAndOrganization(type, 0);
		if (settlement!=null) {
			settlement.setAmount(settlement.getAmount()+amount);
			settlementDao.update(settlement);
			return 1;
		}
		return -1;
	}

	/**
	 * 增加外卡指定结算类型笔数
	 * @return
	 */
	@Override
	public int addSettleNum_WK(String type, int num) {
		Settlement settlement = settlementDao.findSettlementByTypeAndOrganization(type, 1);
		if (settlement!=null) {
			settlement.setNum(settlement.getNum()+num);
			settlementDao.update(settlement);
			return 1;
		}
		return -1;
	}

	/**
	 * 增加外卡指定结算类型金额
	 * @return
	 */
	@Override
	public int addSettleAmount_WK(String type, long amount) {
		Settlement settlement = settlementDao.findSettlementByTypeAndOrganization(type, 1);
		if (settlement!=null) {
			settlement.setAmount(settlement.getAmount()+amount);
			settlementDao.update(settlement);
			return 1;
		}
		return -1;
	}

	/**
	 * 重置结算数据表
	 * @return
	 */
	@Override
	public void clearSettlement() {
		CommonDBServiceImpl commonDBServiceImpl = new CommonDBServiceImpl(context);
		commonDBServiceImpl.cleanWater(CleanType.SETTLEMENT);
	}
		
	
}
