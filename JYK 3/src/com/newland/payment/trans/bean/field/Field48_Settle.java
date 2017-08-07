package com.newland.payment.trans.bean.field;

import com.newland.payment.common.TransConst.SettlementTableTypeConst;
import com.newland.payment.mvc.service.SettlementService;
import com.newland.payment.mvc.service.impl.SettlementServiceImpl;
import com.newland.payment.ui.activity.App;

/**内外卡结算金额统计*/
public class Field48_Settle {

	/**借记笔数(内卡)*/
	private int debitNum_N;
	/**借记金额(内卡)*/
	private long debitAmount_N;
	/**贷记笔数(内卡)*/
	private int creditNum_N;
	/**贷记金额(内卡)*/
	private long creditAmount_N;
	/** 对账应答代码(内卡)*/
	private String settleCode_N;
	
	/**借记笔数(外卡)*/
	private int debitNum_W;
	/**借记金额(外卡)*/
	private long debitAmount_W;
	/**贷记笔数(外卡)*/
	private int creditNum_W;
	/**贷记金额(外卡)*/
	private long creditAmount_W;
	/** 对账应答代码(外卡)*/
	private String settleCode_W;
	
	public Field48_Settle(){
		SettlementService settleService = new SettlementServiceImpl(App.getInstance().getApplicationContext());

		debitNum_N = settleService.getSettleNum_NK(SettlementTableTypeConst.SALE)
			     + settleService.getSettleNum_NK(SettlementTableTypeConst.AUTH_SALE)
			     + settleService.getSettleNum_NK(SettlementTableTypeConst.OFFLINE)
			     + settleService.getSettleNum_NK(SettlementTableTypeConst.AUTH_SALE_OFF)
			     + settleService.getSettleNum_NK(SettlementTableTypeConst.EC_SALE)
			     + settleService.getSettleNum_NK(SettlementTableTypeConst.NOT_BIN_ECLOAD)
			     + settleService.getSettleNum_NK(SettlementTableTypeConst.VOID_CASH_ECLOAD);

		debitAmount_N = settleService.getSettleAmount_NK(SettlementTableTypeConst.SALE)
			     + settleService.getSettleAmount_NK(SettlementTableTypeConst.AUTH_SALE)
			     + settleService.getSettleAmount_NK(SettlementTableTypeConst.OFFLINE)
			     + settleService.getSettleAmount_NK(SettlementTableTypeConst.AUTH_SALE_OFF)
			     + settleService.getSettleAmount_NK(SettlementTableTypeConst.EC_SALE)
			     + settleService.getSettleAmount_NK(SettlementTableTypeConst.NOT_BIN_ECLOAD)
			     + settleService.getSettleAmount_NK(SettlementTableTypeConst.VOID_CASH_ECLOAD);
		
		
		creditNum_N = settleService.getSettleNum_NK(SettlementTableTypeConst.VOID_SALE)
			     + settleService.getSettleNum_NK(SettlementTableTypeConst.VOID_AUTH_SALE)
			     + settleService.getSettleNum_NK(SettlementTableTypeConst.REFUND)
			     + settleService.getSettleNum_NK(SettlementTableTypeConst.EMV_REFUND)
			     + settleService.getSettleNum_NK(SettlementTableTypeConst.MAG_CASH_LOAD)
			     + settleService.getSettleNum_NK(SettlementTableTypeConst.CASH_ECLOAD);
		
		
		creditAmount_N = settleService.getSettleAmount_NK(SettlementTableTypeConst.VOID_SALE)
			     + settleService.getSettleAmount_NK(SettlementTableTypeConst.VOID_AUTH_SALE)
			     + settleService.getSettleAmount_NK(SettlementTableTypeConst.REFUND)
			     + settleService.getSettleAmount_NK(SettlementTableTypeConst.EMV_REFUND)
			     + settleService.getSettleAmount_NK(SettlementTableTypeConst.MAG_CASH_LOAD)
			     + settleService.getSettleAmount_NK(SettlementTableTypeConst.CASH_ECLOAD);
		
		
		
		debitNum_W = settleService.getSettleNum_WK(SettlementTableTypeConst.SALE)
			     + settleService.getSettleNum_WK(SettlementTableTypeConst.AUTH_SALE)
			     + settleService.getSettleNum_WK(SettlementTableTypeConst.OFFLINE)
			     + settleService.getSettleNum_WK(SettlementTableTypeConst.AUTH_SALE_OFF)
			     + settleService.getSettleNum_WK(SettlementTableTypeConst.EC_SALE)
			     + settleService.getSettleNum_WK(SettlementTableTypeConst.NOT_BIN_ECLOAD)
			     + settleService.getSettleNum_WK(SettlementTableTypeConst.VOID_CASH_ECLOAD);

		
		debitAmount_W = settleService.getSettleAmount_WK(SettlementTableTypeConst.SALE)
			     + settleService.getSettleAmount_WK(SettlementTableTypeConst.AUTH_SALE)
			     + settleService.getSettleAmount_WK(SettlementTableTypeConst.OFFLINE)
			     + settleService.getSettleAmount_WK(SettlementTableTypeConst.AUTH_SALE_OFF)
			     + settleService.getSettleAmount_WK(SettlementTableTypeConst.EC_SALE)
			     + settleService.getSettleAmount_WK(SettlementTableTypeConst.NOT_BIN_ECLOAD)
			     + settleService.getSettleAmount_WK(SettlementTableTypeConst.VOID_CASH_ECLOAD);
	
		creditNum_W = settleService.getSettleNum_WK(SettlementTableTypeConst.VOID_SALE)
			     + settleService.getSettleNum_WK(SettlementTableTypeConst.VOID_AUTH_SALE)
			     + settleService.getSettleNum_WK(SettlementTableTypeConst.REFUND)
			     + settleService.getSettleNum_WK(SettlementTableTypeConst.EMV_REFUND)
			     + settleService.getSettleNum_WK(SettlementTableTypeConst.MAG_CASH_LOAD)
			     + settleService.getSettleNum_WK(SettlementTableTypeConst.CASH_ECLOAD);	

		
		creditAmount_W = settleService.getSettleAmount_WK(SettlementTableTypeConst.VOID_SALE)
			     + settleService.getSettleAmount_WK(SettlementTableTypeConst.VOID_AUTH_SALE)
			     + settleService.getSettleAmount_WK(SettlementTableTypeConst.REFUND)
			     + settleService.getSettleAmount_WK(SettlementTableTypeConst.EMV_REFUND)
			     + settleService.getSettleAmount_WK(SettlementTableTypeConst.MAG_CASH_LOAD)
			     + settleService.getSettleAmount_WK(SettlementTableTypeConst.CASH_ECLOAD);
	
		settleCode_N = "0";
		settleCode_W = "0";
		
		
	}

	public int getDebitNum_N() {
		return debitNum_N;
	}

	public void setDebitNum_N(int debitNum_N) {
		this.debitNum_N = debitNum_N;
	}

	public long getDebitAmount_N() {
		return debitAmount_N;
	}

	public void setDebitAmount_N(long debitAmount_N) {
		this.debitAmount_N = debitAmount_N;
	}

	public int getCreditNum_N() {
		return creditNum_N;
	}

	public void setCreditNum_N(int creditNum_N) {
		this.creditNum_N = creditNum_N;
	}

	public long getCreditAmount_N() {
		return creditAmount_N;
	}

	public void setCreditAmount_N(long creditAmount_N) {
		this.creditAmount_N = creditAmount_N;
	}

	public int getDebitNum_W() {
		return debitNum_W;
	}

	public void setDebitNum_W(int debitNum_W) {
		this.debitNum_W = debitNum_W;
	}

	public long getDebitAmount_W() {
		return debitAmount_W;
	}

	public void setDebitAmount_W(long debitAmount_W) {
		this.debitAmount_W = debitAmount_W;
	}

	public int getCreditNum_W() {
		return creditNum_W;
	}

	public void setCreditNum_W(int creditNum_W) {
		this.creditNum_W = creditNum_W;
	}

	public long getCreditAmount_W() {
		return creditAmount_W;
	}

	public void setCreditAmount_W(long creditAmount_W) {
		this.creditAmount_W = creditAmount_W;
	}
	
	public String getString(){
		return String.format("%012d", debitAmount_N) 
				 + String.format("%03d", debitNum_N)
				 + String.format("%012d", creditAmount_N) 
				 + String.format("%03d", creditNum_N)
				 + settleCode_N;
//				 + String.format("%012d", debitAmount_W)
//				 + String.format("%03d", debitNum_W)
//				 + String.format("%012d", creditAmount_W)
//				 + String.format("%03d", creditNum_W)
//				 + settleCode_W;
	}
	
	public long getTotalAmount(){
		return debitAmount_N + creditAmount_N + debitAmount_W + creditAmount_W;
	}
}
