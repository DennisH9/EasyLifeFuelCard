package com.newland.payment.mvc.dao.impl;

import java.util.List;

import android.content.Context;

import com.newland.base.dao.BaseDao;
import com.newland.payment.mvc.dao.SettlementDao;
import com.newland.payment.mvc.model.Settlement;

public class SettlementDaoImpl extends BaseDao<Settlement> implements SettlementDao {

	public SettlementDaoImpl(Context context) {
		super(context);
	}

	@Override
	public Settlement findSettlementByTypeAndOrganization(String type,
			int organization) {
		List<Settlement> l = super.query("ORGANIZATION=? and TYPE = ?", new String[] {String.valueOf(organization), type}, null);
		return l.size() > 0 ? l.get(0) : null;
	}

	@Override
	public int update(Settlement settlement) {
		return super.update(settlement);
	}

	@Override
	public int deleteAll() {
		return super.delete("", new String[]{});
	}
	
	public void initSettlementData(){
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('sale','0','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('void_sale','0','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('auth_sale','0','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('auth_sale_off','0','0','0')");
		
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('void_auth_sale','0','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('refund','0','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('offline','0','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('adjust','0','0','0')");
		
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('ec_sale','0','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('emv_refund','0','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('cash_ecload','0','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('void_cash_ecload','0','0','0')");
		
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('not_bin_ecload','0','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('mag_cash_load','0','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('sale','1','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('void_sale','1','0','0')");
		
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('auth_sale','1','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('auth_sale_off','1','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('void_auth_sale','1','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('refund','1','0','0')");
		
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('offline','1','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('adjust','1','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('ec_sale','1','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('emv_refund','1','0','0')");
		
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('cash_ecload','1','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('void_cash_ecload','1','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('not_bin_ecload','1','0','0')");
		execSql("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('mag_cash_load','1','0','0')");
	}
}
