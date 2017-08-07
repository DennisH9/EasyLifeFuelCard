package com.newland.payment.mvc.service.impl;

import android.content.Context;

import com.lidroid.xutils.exception.DbException;
import com.newland.payment.mvc.dao.impl.BlackCardDaoImpl;
import com.newland.payment.mvc.dao.impl.EmvFailWaterDaoImpl;
import com.newland.payment.mvc.dao.impl.ReverseWaterDaoImpl;
import com.newland.payment.mvc.dao.impl.ScriptResultDaoImpl;
import com.newland.payment.mvc.dao.impl.SettlementDaoImpl;
import com.newland.payment.mvc.dao.impl.UserDaoImpl;
import com.newland.payment.mvc.dao.impl.WaterDaoImpl;
import com.newland.payment.mvc.service.CommonDBService;

public class CommonDBServiceImpl implements CommonDBService {

	private ReverseWaterDaoImpl reverseWaterDao;
	private WaterDaoImpl waterDao;
	private ScriptResultDaoImpl scriptResultDao;
	private UserDaoImpl userDao;
	private SettlementDaoImpl settlementDao;
	private EmvFailWaterDaoImpl emvFailWaterDao;
	private BlackCardDaoImpl blackCardDao;
	
	public CommonDBServiceImpl(Context context){
		this.userDao = new UserDaoImpl(context);
		this.waterDao = new WaterDaoImpl(context);
		this.scriptResultDao = new ScriptResultDaoImpl(context);
		this.reverseWaterDao = new ReverseWaterDaoImpl(context);
		this.settlementDao = new SettlementDaoImpl(context);
		this.emvFailWaterDao = new EmvFailWaterDaoImpl(context);
		this.blackCardDao = new BlackCardDaoImpl(context);
	}
	
	@Override
	public void recreateDatabase() {
		try {
			userDao.dropTable();
			waterDao.dropTable();
			scriptResultDao.dropTable();
			reverseWaterDao.dropTable();
			settlementDao.dropTable();
			emvFailWaterDao.dropTable();
			blackCardDao.dropTable();
			
			userDao.createTableIfNotExist();
			waterDao.createTableIfNotExist();
			scriptResultDao.createTableIfNotExist();
			reverseWaterDao.createTableIfNotExist();
			settlementDao.createTableIfNotExist();
			emvFailWaterDao.createTableIfNotExist();
			blackCardDao.createTableIfNotExist();
			
			userDao.initUserData();
			settlementDao.initSettlementData();
			
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	@Override
	public void cleanWater(CleanType type) {
		try {
			switch (type) {
			case WATER:
				waterDao.deleteAll();
				waterDao.revertSeq();
				break;

			case REVERSE_WATER:
				reverseWaterDao.deleteAll();
				reverseWaterDao.revertSeq();
				break;

			case SCRIPT_RESULT:
				scriptResultDao.deleteAll();
				scriptResultDao.revertSeq();
				break;
				
			case SETTLEMENT:
				settlementDao.deleteAll();
				settlementDao.revertSeq();
				settlementDao.initSettlementData();
				break;
				
			case EMV_FAIL_WATER:
				emvFailWaterDao.deleteAll();
				emvFailWaterDao.revertSeq();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
