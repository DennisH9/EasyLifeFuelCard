package com.newland.payment.trans.bean.field;

import com.newland.pos.sdk.emv.EmvTLV;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.TLVUtils;

/**
 * pboc交易需打印的参数
 * @author lst
 *
 */
public class Field_55_PrintData {
	
	
	/**
	 *@see EmvStandardReference#ISSUER_AUTHENTICATION_DATA
	 *发卡行应用数据 iad (9f10)
	 */
	private byte[] iad;
		
	/**
	 *	aip(tag=0x82)
	 *	(tag=0x82)
	 */
	private byte[] aip;
	/**
	 *	TC
	 * tag=0x9F26
	 */
	private byte[] TC;
	/**
	 *	termCap
	 *  tag=0x9F33
	 */
	private byte[] termCap;
	
	/**
	 *	arqc
	 *	tag=0x9F26
	 */
	private byte[] arqc;
	/**
	 *	cvm
	 *	tag=0x9F34
	 */
	private byte[] cvm;
	/**
	 *	tvr
	 *	tag=0x95
	 */
	private byte[] tvr;
	/**
	 *	unpr
	 *	tag=0x9F37
	 */
	private byte[] unpr;
	/**
	 *	19 应用标识符（AID，Tag 4F）
	 *	tag=0x4F
	 */
	private byte[] aid;
	/**
	 *	tsi
	 *	tag=0x9B
	 */
	private byte[] tsi;

	/**
	 * 交易计数器atc
	 * @see EmvStandardReference#APP_TRANSACTION_COUNTER
	 * tag=0x9F36
	 */
	private byte[] atc;
	
	/**
	 *	appname
	 *	tag=0x9F12
	 */
	private byte[] appName;
	
	/**
	 *	applable
	 *	tag=0x50
	 */
	private byte[] appLable;
	
	/**
	 *	signalFlag
	 *	tag=0xDF42
	 */
	private byte[] signalFlag;
	
	/**
	 * 卡产品标识
	 * @param data
	 * tag=0x9F63
	 */
	private byte[] cardProductId;
	
	public Field_55_PrintData(byte[] data) {
		 if (null == data) {
			return;
		}
		 EmvTLV[] list = TLVUtils.getTLVList(data);
		 aip = TLVUtils.getValueFromTLVlist(0x82, list);
		 termCap = TLVUtils.getValueFromTLVlist(0x9F33, list);
		 arqc = TLVUtils.getValueFromTLVlist(0x9F26, list);
		 cvm = TLVUtils.getValueFromTLVlist(0x9F34, list);
		 tvr = TLVUtils.getValueFromTLVlist(0x95, list);
		 unpr = TLVUtils.getValueFromTLVlist(0x9F37, list);
		 appName = TLVUtils.getValueFromTLVlist(0x9F12, list);
		 appLable = TLVUtils.getValueFromTLVlist(0x50, list);
		 signalFlag = TLVUtils.getValueFromTLVlist(0xDF42, list);
		 tsi = TLVUtils.getValueFromTLVlist(0x9B, list);
		 atc = TLVUtils.getValueFromTLVlist(0x9F36, list);
		 LoggerUtils.d("~~~~~~~~~~~~~~~~~~atc是---》"+BytesUtils.bytesToHex(atc));
		 aid = TLVUtils.getValueFromTLVlist(0x4F, list);
		 TC = TLVUtils.getValueFromTLVlist(0x9F26, list);
		 iad = TLVUtils.getValueFromTLVlist(0x9f10, list);
		 cardProductId = TLVUtils.getValueFromTLVlist(0x9F63, list);
	}

	public String getIad() {
		if (iad != null) {
			return BytesUtils.bytesToHex(iad);

		}
		return "";
	}

	public byte[] getAppname() {
		if (appName != null) {
			return appName;

		}
		return new byte[]{};
	}

	public void setAppname(byte[] appname) {
		this.appName = appname;
	}

	public byte[] getApplable() {
		if (appLable != null) {
			return appLable;

		}
		return new byte[]{};
	}

	public void setApplable(byte[] applable) {
		this.appLable = applable;
	}

	public String getSignalFlag() {
		if (signalFlag != null) {
			return BytesUtils.bytesToHex(signalFlag);

		}
		return "";
	}

	public void setSignalFlag(byte[] signalFlag) {
		this.signalFlag = signalFlag;
	}

	public void setIad(byte[] iad) {
		this.iad = iad;
	}

	public String getTsi() {
		if (tsi != null) {
			return BytesUtils.bytesToHex(tsi);
		}
		return "";
	}

	public void setTsi(byte[] tsi) {
		this.tsi = tsi;
	}

	public String getAip() {
		if (aip != null) {
			return BytesUtils.bytesToHex(aip);
		}
		return "";
	}

	public void setAip(byte[] aip) {
		this.aip = aip;
	}

	public String getTermCap() {
		if (termCap != null) {
			return BytesUtils.bytesToHex(termCap);
		}
		return "";
	}

	public void setTermCap(byte[] termCap) {
		this.termCap = termCap;
	}

	public String getArqc() {
		if (arqc != null) {
			return BytesUtils.bytesToHex(arqc);
		}
		return "";
	}

	public void setArqc(byte[] arqc) {
		this.arqc = arqc;
	}

	public String getCvm() {
		if (cvm != null) {
			return BytesUtils.bytesToHex(cvm);
		}
		
		return "";
	}

	public void setCvm(byte[] cvm) {
		this.cvm = cvm;
	}

	public String getTvr() {
		if (tvr != null) {
			return BytesUtils.bytesToHex(tvr);

		}
		return "";
	}

	public void setTvr(byte[] tvr) {
		this.tvr = tvr;
	}

	public String getUnpr() {
		if (unpr != null) {
			return BytesUtils.bytesToHex(unpr);

		}
		return "";
	}

	public void setUnpr(byte[] unpr) {
		this.unpr = unpr;
	}

	public String getAid() {
		if (aid != null) {
			return BytesUtils.bytesToHex(aid);

		}
		return "";
	}

	public void setAid(byte[] aid) {
		this.aid = aid;
	}



	public String getAtc() {
		if (atc != null) {
			return BytesUtils.bytesToHex(atc);

		}
		return "";
	}

	public void setAtc(byte[] atc) {
		this.atc = atc;
	}

	public String getTC() {
		if (TC != null) {
			return BytesUtils.bytesToHex(TC);

		}
		return "";
	}

	public void setTC(byte[] tC) {
		TC = tC;
	}

	public String getCardProductId() {
		if (cardProductId != null) {
			return BytesUtils.bytesToHex(cardProductId);

		}
		return "";
	}

	public void setCardProductId(byte[] cardProductId) {
		this.cardProductId = cardProductId;
	}
	
	
}
