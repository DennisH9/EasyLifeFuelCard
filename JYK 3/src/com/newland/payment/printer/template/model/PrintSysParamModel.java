package com.newland.payment.printer.template.model;

/**
 * 打印系统参数模板
 * @author lst
 * @date 20150519 
 */
public class PrintSysParamModel {
	/**是否打印交易控制参数*/
	private String ISPRINTTRANS;
	/**消费 */
	private String ISSALE;
	/**消费撤销*/
	private String ISVOIDSALE;
	/**退货*/
	private String ISREFUND;
	/**余额查询*/
	private String ISBALANCE;
	/**预授权*/
	private String ISPREAUTH;
	/**预授权撤销*/
	private String ISVOIDPREAUTH;
	/**预授权完成请求*/
	private String ISAUTHSALE;
	/**预授权完成通知*/
	private String ISAUTHSALEOFF;
	/**预授权完成撤销*/
	private String ISVOIDAUTHSALE;
	/**离线结算*/
	private String ISOFFLINE;
	/**结算调整*/
	private String ISREJUST;
	/**接触电子现金消费*/
	private String ISECSALE;
	/**快速支付（非接）*/
	private String ISECFASTSALE;
	/**指定账户圈存*/
	private String ISECLOAD;
	/**非指定账户圈存*/
	private String NOTBINDECLOAD;
	/**电子现金现金充值*/
	private String ISECLOADCASH;
	/**电子现金充值撤销*/
	private String ISECVOIDLOADCASH;
	/**电子现金脱机退货*/
	private String ISECOFFREFUND;
	/**电子钱包消费*/
	private String ISEPSALE;
	/**分期付款消费*/
	private String ISINSTALL;
	/**分期付款消费撤销*/
	private String ISVOIDINSTALL;
	/**联盟积分消费*/
	private String BONUS_ALLIANCE;
	/**发卡行积分消费*/
	private String BONUS_IIS_SALE;
	/**联盟积分消费撤销*/
	private String VOIDBONUS_ALL;
	/**发卡行消费撤销*/
	private String VOID_BONUS_IIS;
	/**联盟积分查询*/
	private String ALLIANCE_BALANCE;
	/**联盟积分退货*/
	private String ALLIANCE_REFUND;
	/**手机消费*/
	private String ISPHONESALE;
	/**手机消费撤销*/
	private String ISVOIDPHONESALE;
	/**手机芯片退货*/
	private String ISREFUNDPHONE;
	/**手机芯片预授权*/
	private String ISPHONEPREAUTH;
	/**手机预授权撤销*/
	private String ISPHONEVOIDPRE;
	/**手机预授权完成请求*/
	private String ISPHONEAUTH;
	/**手机预授权完成通知*/
	private String ISPHONEVOIDAUTH;
	/**手机预授权完成撤销*/
	private String ISPHONEBALANCE;
	/**手机芯片余额查询*/
	private String ISAPPOINT;
	/**预约消费*/
	private String ISVOIDAPPOINT;
	/**预约消费撤销*/
	private String ISORDERSALE;
	/**订购消费撤销*/
	private String ISORDERVOIDSALE;
	/**订购退货*/
	private String ISREFUNDORDER;
	/**订购预授权*/
	private String ISORDERPREAUTH;
	/**订购预授权撤销*/
	private String ISORDERVOIDPRE;
	/**订购预授权完成请求*/
	private String ISORDERAUTHSALE;
	/**订购预授权完成通知*/
	private String ISORDERAUTHOFF;
	/**订购预授权完成撤销*/
	private String ISORDERVOIDAUTH;
	/**磁条卡现金充值*/
	private String ISSTRIPECASHLOAD;
	/**磁条卡账户充值*/
	private String ISSTRIPELOAD;
	/**结算是否自动签退*/
	private String AUTOLOGOUT;
	/**结算是否打印明细*/
	private String PRNWATERREC;
	/**离线上送方式*/
	private String OFFLINESENDTYPE;
	/**离线上送笔数*/
	private String OFFLINESENDCOUNT;
	/** 离线重发次数 */
	private String OFFLINESENDTIME;
	/**是否输入主管密码*/
	private String ADMINPWD;
	/**是否允许手输卡号*/
	private String CARDINPUT;
	/**默认刷卡交易*/
	private String DEFAULTTRANSTYPE;
	/**退货最大限额*/
	private String MAXREFUNDAMT;
	
	
	
	/**是否打印系统控制参数*/
	private String ISPRINTSYSPARAM;
	
	/**当前交易凭证号*/
	private String TRACENO;
	/**当前交易批次号*/
	private String BATCHNO;
	/**是否打印中文收单行*/
    private String PNTCHACQUIRER;
	/**是否打印中文发卡行*/
	private String PNTCHCARDSCHEME;
	/** 套打签购单样式*/
	private String PNTTYPE;
	/** 热敏打印联数*/
	private String PRINTPAGE;
	/**签购单是否打印英文*/
	private String TICKETWITHEN;
	/**冲正重发次数*/
	private String REVERSALNUM;
	/**最大交易笔数*/
	private String MAXTRANSCOUNT;
	/**小费比例*/
	private String TIPRATE;
	/** 打印字体*/
	private String PNTFONTSIZE;
	/** 是否打印负号*/
	private String PRINTMINUS;
	/** 是否打印所有交易*/
	private String PNTALLTRANS;
	/**收单行名称*/
	private String UNKNOWBANK;
	
	

	/**是否打印刷卡输密控制参数*/
	private String ISPRINTINPUTPIN;
	
	/**消费撤销是否刷卡 &&分期付款撤销是否刷卡*/
	private String SALEVOIDSTRIP;
	/**预授权完成撤销是否刷卡*/
	 private String AUTHSALEVOIDSTRIP;
	/**消费撤销是否输密&&分期付款撤销是否输密*/
	 private String VOIDPIN;
	/** 预授权撤销是否输密*/
	 private String PREAUTHVOIDPIN;
	/**预授权完成撤销是否输密*/
	 private String AUTHSALEVOIDPIN;
	/**授权完成联机是否输入密码*/
	 private String AUTHSALEPIN;
	 
	 
	 /**是否打印密钥控制参数*/
		private String ISPRINTKEY;
	 
		/**主密钥索引号*/
		 private String MAINKEYNO;
		/**主密钥算法设置*/
		 private String ENCYPTMODE;
	 
	 

	 /**是否打印通讯控制参数*/
		private String ISPRINTCOMM;
		
		/**通讯方式*/
		 private String COMMTYPE;
		/**电话1*/
		 private String TELNO1;
		/**电话2*/
		 private String TELNO2;
		/**电话3*/
		 private String TELNO3;
		/**管理电话*/
		 private String MANAGETELNO;
		/**GPRS APN1*/
		 private String APN1;
		/**GPRS APN2*/
		 private String APN2;
		/**是否长链接*/
		 private String COMMMODE;
		/**本机IP地址*/
		private String IPADDR;
		/**子网掩码*/
		private String MASK;
		/**网关*/
		private String GATE;
		/**SSID*/
		 private String WIFISSID;
		/**加密方式*/
		 private String WIFIMODE;
		/**DNS*/
		 private String DNSIP;
		/**主机1域名*/
		 private String DOMAIN1;
		/**主机1端口*/
		 private String DNSPORT1;
		/**主机2域名*/
		 private String DOMAIN2;
		/**主机2端口*/
		 private String DNSPORT2;
		/**主机IP1地址*/
		 private String IP1;
		/**主机IP1端口号*/
		 private String PORT1;
		/**主机IP2地址*/
		 private String IP2;
		/**主机IP2端口号*/
		 private String PORT2;
		/**用户名*/
		 private String USERNAME;
		/**用户密码*/
		 private String USERPWD;
		/**呼叫中心号码*/
		 private String WIRELESSDIALNUN;
		/**TPDU*/
		 private String TPDU;
		/**拨号次数*/
		 private String REDIALNUM;
		/**交易超时时间*/
		 private String TIMEOUT;
		/**预拨号是否允许*/
		 private String ISPREDIAL;
	
		
	
	
	
	
	
	
	
	
	
	public String getISSALE() {
		return ISSALE;
	}
	public void setISSALE(String iSSALE) {
		ISSALE = iSSALE;
	}
	public String getISVOIDSALE() {
		return ISVOIDSALE;
	}
	public void setISVOIDSALE(String iSVOIDSALE) {
		ISVOIDSALE = iSVOIDSALE;
	}
	public String getISREFUND() {
		return ISREFUND;
	}
	public void setISREFUND(String iSREFUND) {
		ISREFUND = iSREFUND;
	}
	public String getISBALANCE() {
		return ISBALANCE;
	}
	public void setISBALANCE(String iSBALANCE) {
		ISBALANCE = iSBALANCE;
	}
	public String getISPREAUTH() {
		return ISPREAUTH;
	}
	public void setISPREAUTH(String iSPREAUTH) {
		ISPREAUTH = iSPREAUTH;
	}
	public String getISVOIDPREAUTH() {
		return ISVOIDPREAUTH;
	}
	public void setISVOIDPREAUTH(String iSVOIDPREAUTH) {
		ISVOIDPREAUTH = iSVOIDPREAUTH;
	}
	public String getISAUTHSALE() {
		return ISAUTHSALE;
	}
	public void setISAUTHSALE(String iSAUTHSALE) {
		ISAUTHSALE = iSAUTHSALE;
	}
	public String getISAUTHSALEOFF() {
		return ISAUTHSALEOFF;
	}
	public void setISAUTHSALEOFF(String iSAUTHSALEOFF) {
		ISAUTHSALEOFF = iSAUTHSALEOFF;
	}
	public String getISVOIDAUTHSALE() {
		return ISVOIDAUTHSALE;
	}
	public void setISVOIDAUTHSALE(String iSVOIDAUTHSALE) {
		ISVOIDAUTHSALE = iSVOIDAUTHSALE;
	}
	public String getISOFFLINE() {
		return ISOFFLINE;
	}
	public void setISOFFLINE(String iSOFFLINE) {
		ISOFFLINE = iSOFFLINE;
	}
	public String getISREJUST() {
		return ISREJUST;
	}
	public void setISREJUST(String iSREJUST) {
		ISREJUST = iSREJUST;
	}
	public String getISECSALE() {
		return ISECSALE;
	}
	public void setISECSALE(String iSECSALE) {
		ISECSALE = iSECSALE;
	}
	public String getISECFASTSALE() {
		return ISECFASTSALE;
	}
	public void setISECFASTSALE(String iSECFASTSALE) {
		ISECFASTSALE = iSECFASTSALE;
	}
	public String getISECLOAD() {
		return ISECLOAD;
	}
	public void setISECLOAD(String iSECLOAD) {
		ISECLOAD = iSECLOAD;
	}
	public String getNOTBINDECLOAD() {
		return NOTBINDECLOAD;
	}
	public void setNOTBINDECLOAD(String nOTBINDECLOAD) {
		NOTBINDECLOAD = nOTBINDECLOAD;
	}
	public String getISECLOADCASH() {
		return ISECLOADCASH;
	}
	public void setISECLOADCASH(String iSECLOADCASH) {
		ISECLOADCASH = iSECLOADCASH;
	}
	public String getISECVOIDLOADCASH() {
		return ISECVOIDLOADCASH;
	}
	public void setISECVOIDLOADCASH(String iSECVOIDLOADCASH) {
		ISECVOIDLOADCASH = iSECVOIDLOADCASH;
	}
	public String getISECOFFREFUND() {
		return ISECOFFREFUND;
	}
	public void setISECOFFREFUND(String iSECOFFREFUND) {
		ISECOFFREFUND = iSECOFFREFUND;
	}
	public String getISEPSALE() {
		return ISEPSALE;
	}
	public void setISEPSALE(String iSEPSALE) {
		ISEPSALE = iSEPSALE;
	}
	public String getISINSTALL() {
		return ISINSTALL;
	}
	public void setISINSTALL(String iSINSTALL) {
		ISINSTALL = iSINSTALL;
	}
	public String getISVOIDINSTALL() {
		return ISVOIDINSTALL;
	}
	public void setISVOIDINSTALL(String iSVOIDINSTALL) {
		ISVOIDINSTALL = iSVOIDINSTALL;
	}
	public String getBONUS_ALLIANCE() {
		return BONUS_ALLIANCE;
	}
	public void setBONUS_ALLIANCE(String bONUS_ALLIANCE) {
		BONUS_ALLIANCE = bONUS_ALLIANCE;
	}
	public String getBONUS_IIS_SALE() {
		return BONUS_IIS_SALE;
	}
	public void setBONUS_IIS_SALE(String bONUS_IIS_SALE) {
		BONUS_IIS_SALE = bONUS_IIS_SALE;
	}
	public String getVOIDBONUS_ALL() {
		return VOIDBONUS_ALL;
	}
	public void setVOIDBONUS_ALL(String vOIDBONUS_ALL) {
		VOIDBONUS_ALL = vOIDBONUS_ALL;
	}
	public String getVOID_BONUS_IIS() {
		return VOID_BONUS_IIS;
	}
	public void setVOID_BONUS_IIS(String vOID_BONUS_IIS) {
		VOID_BONUS_IIS = vOID_BONUS_IIS;
	}
	public String getALLIANCE_BALANCE() {
		return ALLIANCE_BALANCE;
	}
	public void setALLIANCE_BALANCE(String aLLIANCE_BALANCE) {
		ALLIANCE_BALANCE = aLLIANCE_BALANCE;
	}
	public String getALLIANCE_REFUND() {
		return ALLIANCE_REFUND;
	}
	public void setALLIANCE_REFUND(String aLLIANCE_REFUND) {
		ALLIANCE_REFUND = aLLIANCE_REFUND;
	}
	public String getISPHONESALE() {
		return ISPHONESALE;
	}
	public void setISPHONESALE(String iSPHONESALE) {
		ISPHONESALE = iSPHONESALE;
	}
	public String getISVOIDPHONESALE() {
		return ISVOIDPHONESALE;
	}
	public void setISVOIDPHONESALE(String iSVOIDPHONESALE) {
		ISVOIDPHONESALE = iSVOIDPHONESALE;
	}
	public String getISREFUNDPHONE() {
		return ISREFUNDPHONE;
	}
	public void setISREFUNDPHONE(String iSREFUNDPHONE) {
		ISREFUNDPHONE = iSREFUNDPHONE;
	}
	public String getISPHONEPREAUTH() {
		return ISPHONEPREAUTH;
	}
	public void setISPHONEPREAUTH(String iSPHONEPREAUTH) {
		ISPHONEPREAUTH = iSPHONEPREAUTH;
	}
	public String getISPHONEVOIDPRE() {
		return ISPHONEVOIDPRE;
	}
	public void setISPHONEVOIDPRE(String iSPHONEVOIDPRE) {
		ISPHONEVOIDPRE = iSPHONEVOIDPRE;
	}
	public String getISPHONEAUTH() {
		return ISPHONEAUTH;
	}
	public void setISPHONEAUTH(String iSPHONEAUTH) {
		ISPHONEAUTH = iSPHONEAUTH;
	}
	public String getISPHONEVOIDAUTH() {
		return ISPHONEVOIDAUTH;
	}
	public void setISPHONEVOIDAUTH(String iSPHONEVOIDAUTH) {
		ISPHONEVOIDAUTH = iSPHONEVOIDAUTH;
	}
	public String getISPHONEBALANCE() {
		return ISPHONEBALANCE;
	}
	public void setISPHONEBALANCE(String iSPHONEBALANCE) {
		ISPHONEBALANCE = iSPHONEBALANCE;
	}
	public String getISAPPOINT() {
		return ISAPPOINT;
	}
	public void setISAPPOINT(String iSAPPOINT) {
		ISAPPOINT = iSAPPOINT;
	}
	public String getISVOIDAPPOINT() {
		return ISVOIDAPPOINT;
	}
	public void setISVOIDAPPOINT(String iSVOIDAPPOINT) {
		ISVOIDAPPOINT = iSVOIDAPPOINT;
	}
	public String getISORDERSALE() {
		return ISORDERSALE;
	}
	public void setISORDERSALE(String iSORDERSALE) {
		ISORDERSALE = iSORDERSALE;
	}
	public String getISORDERVOIDSALE() {
		return ISORDERVOIDSALE;
	}
	public void setISORDERVOIDSALE(String iSORDERVOIDSALE) {
		ISORDERVOIDSALE = iSORDERVOIDSALE;
	}
	public String getISREFUNDORDER() {
		return ISREFUNDORDER;
	}
	public void setISREFUNDORDER(String iSREFUNDORDER) {
		ISREFUNDORDER = iSREFUNDORDER;
	}
	public String getISORDERPREAUTH() {
		return ISORDERPREAUTH;
	}
	public void setISORDERPREAUTH(String iSORDERPREAUTH) {
		ISORDERPREAUTH = iSORDERPREAUTH;
	}
	public String getISORDERVOIDPRE() {
		return ISORDERVOIDPRE;
	}
	public void setISORDERVOIDPRE(String iSORDERVOIDPRE) {
		ISORDERVOIDPRE = iSORDERVOIDPRE;
	}
	public String getISORDERAUTHSALE() {
		return ISORDERAUTHSALE;
	}
	public void setISORDERAUTHSALE(String iSORDERAUTHSALE) {
		ISORDERAUTHSALE = iSORDERAUTHSALE;
	}
	public String getISORDERAUTHOFF() {
		return ISORDERAUTHOFF;
	}
	public void setISORDERAUTHOFF(String iSORDERAUTHOFF) {
		ISORDERAUTHOFF = iSORDERAUTHOFF;
	}
	public String getISORDERVOIDAUTH() {
		return ISORDERVOIDAUTH;
	}
	public void setISORDERVOIDAUTH(String iSORDERVOIDAUTH) {
		ISORDERVOIDAUTH = iSORDERVOIDAUTH;
	}
	public String getISSTRIPECASHLOAD() {
		return ISSTRIPECASHLOAD;
	}
	public void setISSTRIPECASHLOAD(String iSSTRIPECASHLOAD) {
		ISSTRIPECASHLOAD = iSSTRIPECASHLOAD;
	}
	public String getAUTOLOGOUT() {
		return AUTOLOGOUT;
	}
	public void setAUTOLOGOUT(String aUTOLOGOUT) {
		AUTOLOGOUT = aUTOLOGOUT;
	}
	public String getPRNWATERREC() {
		return PRNWATERREC;
	}
	public void setPRNWATERREC(String pRNWATERREC) {
		PRNWATERREC = pRNWATERREC;
	}
	public String getOFFLINESENDTYPE() {
		return OFFLINESENDTYPE;
	}
	public void setOFFLINESENDTYPE(String oFFLINESENDTYPE) {
		OFFLINESENDTYPE = oFFLINESENDTYPE;
	}
	public String getOFFLINESENDCOUNT() {
		return OFFLINESENDCOUNT;
	}
	public void setOFFLINESENDCOUNT(String oFFLINESENDCOUNT) {
		OFFLINESENDCOUNT = oFFLINESENDCOUNT;
	}
	public String getADMINPWD() {
		return ADMINPWD;
	}
	public void setADMINPWD(String aDMINPWD) {
		ADMINPWD = aDMINPWD;
	}
	public String getCARDINPUT() {
		return CARDINPUT;
	}
	public void setCARDINPUT(String cARDINPUT) {
		CARDINPUT = cARDINPUT;
	}
	public String getDEFAULTTRANSTYPE() {
		return DEFAULTTRANSTYPE;
	}
	public void setDEFAULTTRANSTYPE(String dEFAULTTRANSTYPE) {
		DEFAULTTRANSTYPE = dEFAULTTRANSTYPE;
	}
	public String getMAXREFUNDAMT() {
		return MAXREFUNDAMT;
	}
	public void setMAXREFUNDAMT(String mAXREFUNDAMT) {
		MAXREFUNDAMT = mAXREFUNDAMT;
	}
	public String getISPRINTTRANS() {
		return ISPRINTTRANS;
	}
	public void setISPRINTTRANS(String iSPRINTTRANS) {
		ISPRINTTRANS = iSPRINTTRANS;
	}
	public String getISPRINTSYSPARAM() {
		return ISPRINTSYSPARAM;
	}
	public void setISPRINTSYSPARAM(String iSPRINTSYSPARAM) {
		ISPRINTSYSPARAM = iSPRINTSYSPARAM;
	}
	public String getTRACENO() {
		return TRACENO;
	}
	public void setTRACENO(String tRACENO) {
		TRACENO = tRACENO;
	}
	public String getBATCHNO() {
		return BATCHNO;
	}
	public void setBATCHNO(String bATCHNO) {
		BATCHNO = bATCHNO;
	}
	public String getPNTCHACQUIRER() {
		return PNTCHACQUIRER;
	}
	public void setPNTCHACQUIRER(String pNTCHACQUIRER) {
		PNTCHACQUIRER = pNTCHACQUIRER;
	}
	public String getPNTCHCARDSCHEME() {
		return PNTCHCARDSCHEME;
	}
	public void setPNTCHCARDSCHEME(String pNTCHCARDSCHEME) {
		PNTCHCARDSCHEME = pNTCHCARDSCHEME;
	}
	public String getPNTTYPE() {
		return PNTTYPE;
	}
	public void setPNTTYPE(String pNTTYPE) {
		PNTTYPE = pNTTYPE;
	}
	public String getPRINTPAGE() {
		return PRINTPAGE;
	}
	public void setPRINTPAGE(String pRINTPAGE) {
		PRINTPAGE = pRINTPAGE;
	}
	public String getTICKETWITHEN() {
		return TICKETWITHEN;
	}
	public void setTICKETWITHEN(String tICKETWITHEN) {
		TICKETWITHEN = tICKETWITHEN;
	}
	public String getREVERSALNUM() {
		return REVERSALNUM;
	}
	public void setREVERSALNUM(String rEVERSALNUM) {
		REVERSALNUM = rEVERSALNUM;
	}
	public String getMAXTRANSCOUNT() {
		return MAXTRANSCOUNT;
	}
	public void setMAXTRANSCOUNT(String mAXTRANSCOUNT) {
		MAXTRANSCOUNT = mAXTRANSCOUNT;
	}
	public String getTIPRATE() {
		return TIPRATE;
	}
	public void setTIPRATE(String tIPRATE) {
		TIPRATE = tIPRATE;
	}
	public String getPNTFONTSIZE() {
		return PNTFONTSIZE;
	}
	public void setPNTFONTSIZE(String pNTFONTSIZE) {
		PNTFONTSIZE = pNTFONTSIZE;
	}
	public String getPRINTMINUS() {
		return PRINTMINUS;
	}
	public void setPRINTMINUS(String pRINTMINUS) {
		PRINTMINUS = pRINTMINUS;
	}
	public String getPNTALLTRANS() {
		return PNTALLTRANS;
	}
	public void setPNTALLTRANS(String pNTALLTRANS) {
		PNTALLTRANS = pNTALLTRANS;
	}
	public String getUNKNOWBANK() {
		return UNKNOWBANK;
	}
	public void setUNKNOWBANK(String uNKNOWBANK) {
		UNKNOWBANK = uNKNOWBANK;
	}
	public String getISPRINTINPUTPIN() {
		return ISPRINTINPUTPIN;
	}
	public void setISPRINTINPUTPIN(String iSPRINTINPUTPIN) {
		ISPRINTINPUTPIN = iSPRINTINPUTPIN;
	}
	public String getSALEVOIDSTRIP() {
		return SALEVOIDSTRIP;
	}
	public void setSALEVOIDSTRIP(String sALEVOIDSTRIP) {
		SALEVOIDSTRIP = sALEVOIDSTRIP;
	}
	public String getAUTHSALEVOIDSTRIP() {
		return AUTHSALEVOIDSTRIP;
	}
	public void setAUTHSALEVOIDSTRIP(String aUTHSALEVOIDSTRIP) {
		AUTHSALEVOIDSTRIP = aUTHSALEVOIDSTRIP;
	}
	public String getVOIDPIN() {
		return VOIDPIN;
	}
	public void setVOIDPIN(String vOIDPIN) {
		VOIDPIN = vOIDPIN;
	}
	public String getPREAUTHVOIDPIN() {
		return PREAUTHVOIDPIN;
	}
	public void setPREAUTHVOIDPIN(String pREAUTHVOIDPIN) {
		PREAUTHVOIDPIN = pREAUTHVOIDPIN;
	}
	public String getAUTHSALEVOIDPIN() {
		return AUTHSALEVOIDPIN;
	}
	public void setAUTHSALEVOIDPIN(String aUTHSALEVOIDPIN) {
		AUTHSALEVOIDPIN = aUTHSALEVOIDPIN;
	}
	public String getAUTHSALEPIN() {
		return AUTHSALEPIN;
	}
	public void setAUTHSALEPIN(String aUTHSALEPIN) {
		AUTHSALEPIN = aUTHSALEPIN;
	}
	public String getISPRINTKEY() {
		return ISPRINTKEY;
	}
	public void setISPRINTKEY(String iSPRINTKEY) {
		ISPRINTKEY = iSPRINTKEY;
	}
	public String getMAINKEYNO() {
		return MAINKEYNO;
	}
	public void setMAINKEYNO(String mAINKEYNO) {
		MAINKEYNO = mAINKEYNO;
	}
	public String getENCYPTMODE() {
		return ENCYPTMODE;
	}
	public void setENCYPTMODE(String eNCYPTMODE) {
		ENCYPTMODE = eNCYPTMODE;
	}
	public String getISPRINTCOMM() {
		return ISPRINTCOMM;
	}
	public void setISPRINTCOMM(String iSPRINTCOMM) {
		ISPRINTCOMM = iSPRINTCOMM;
	}
	public String getCOMMTYPE() {
		return COMMTYPE;
	}
	public void setCOMMTYPE(String cOMMTYPE) {
		COMMTYPE = cOMMTYPE;
	}
	public String getTELNO1() {
		return TELNO1;
	}
	public void setTELNO1(String tELNO1) {
		TELNO1 = tELNO1;
	}
	public String getTELNO2() {
		return TELNO2;
	}
	public void setTELNO2(String tELNO2) {
		TELNO2 = tELNO2;
	}
	public String getTELNO3() {
		return TELNO3;
	}
	public void setTELNO3(String tELNO3) {
		TELNO3 = tELNO3;
	}
	public String getMANAGETELNO() {
		return MANAGETELNO;
	}
	public void setMANAGETELNO(String mANAGETELNO) {
		MANAGETELNO = mANAGETELNO;
	}
	public String getAPN1() {
		return APN1;
	}
	public void setAPN1(String aPN1) {
		APN1 = aPN1;
	}
	public String getAPN2() {
		return APN2;
	}
	public void setAPN2(String aPN2) {
		APN2 = aPN2;
	}
	public String getCOMMMODE() {
		return COMMMODE;
	}
	public void setCOMMMODE(String cOMMMODE) {
		COMMMODE = cOMMMODE;
	}
	public String getIPADDR() {
		return IPADDR;
	}
	public void setIPADDR(String iPADDR) {
		IPADDR = iPADDR;
	}
	public String getMASK() {
		return MASK;
	}
	public void setMASK(String mASK) {
		MASK = mASK;
	}
	public String getGATE() {
		return GATE;
	}
	public void setGATE(String gATE) {
		GATE = gATE;
	}
	public String getWIFISSID() {
		return WIFISSID;
	}
	public void setWIFISSID(String wIFISSID) {
		WIFISSID = wIFISSID;
	}
	public String getWIFIMODE() {
		return WIFIMODE;
	}
	public void setWIFIMODE(String wIFIMODE) {
		WIFIMODE = wIFIMODE;
	}
	public String getDNSIP() {
		return DNSIP;
	}
	public void setDNSIP(String dNSIP) {
		DNSIP = dNSIP;
	}
	public String getDOMAIN1() {
		return DOMAIN1;
	}
	public void setDOMAIN1(String dOMAIN1) {
		DOMAIN1 = dOMAIN1;
	}
	public String getDNSPORT1() {
		return DNSPORT1;
	}
	public void setDNSPORT1(String dNSPORT1) {
		DNSPORT1 = dNSPORT1;
	}
	public String getDOMAIN2() {
		return DOMAIN2;
	}
	public void setDOMAIN2(String dOMAIN2) {
		DOMAIN2 = dOMAIN2;
	}
	public String getDNSPORT2() {
		return DNSPORT2;
	}
	public void setDNSPORT2(String dNSPORT2) {
		DNSPORT2 = dNSPORT2;
	}
	public String getIP1() {
		return IP1;
	}
	public void setIP1(String iP1) {
		IP1 = iP1;
	}
	public String getPORT1() {
		return PORT1;
	}
	public void setPORT1(String pORT1) {
		PORT1 = pORT1;
	}
	public String getIP2() {
		return IP2;
	}
	public void setIP2(String iP2) {
		IP2 = iP2;
	}
	public String getPORT2() {
		return PORT2;
	}
	public void setPORT2(String pORT2) {
		PORT2 = pORT2;
	}
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	public String getUSERPWD() {
		return USERPWD;
	}
	public void setUSERPWD(String uSERPWD) {
		USERPWD = uSERPWD;
	}
	public String getWIRELESSDIALNUN() {
		return WIRELESSDIALNUN;
	}
	public void setWIRELESSDIALNUN(String wIRELESSDIALNUN) {
		WIRELESSDIALNUN = wIRELESSDIALNUN;
	}
	public String getTPDU() {
		return TPDU;
	}
	public void setTPDU(String tPDU) {
		TPDU = tPDU;
	}
	public String getREDIALNUM() {
		return REDIALNUM;
	}
	public void setREDIALNUM(String rEDIALNUM) {
		REDIALNUM = rEDIALNUM;
	}
	public String getTIMEOUT() {
		return TIMEOUT;
	}
	public void setTIMEOUT(String tIMEOUT) {
		TIMEOUT = tIMEOUT;
	}
	public String getISPREDIAL() {
		return ISPREDIAL;
	}
	public void setISPREDIAL(String iSPREDIAL) {
		ISPREDIAL = iSPREDIAL;
	}
	public String getISSTRIPELOAD() {
		return ISSTRIPELOAD;
	}
	public void setISSTRIPELOAD(String iSSTRIPELOAD) {
		ISSTRIPELOAD = iSSTRIPELOAD;
	}
	public String getOFFLINESENDTIME() {
		return OFFLINESENDTIME;
	}
	public void setOFFLINESENDTIME(String oFFLINESENDTIME) {
		OFFLINESENDTIME = oFFLINESENDTIME;
	}
}
