package com.newland.payment.trans.impl;

import com.newland.base.util.ParamsUtils;
import com.newland.emv.EmvAppModule;
import com.newland.emv.EmvApplication;
import com.newland.payment.common.Const;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.ReadcardType;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.trans.bean.ThirdInvokeBean;
import com.newland.pos.sdk.bean.CardBean;
import com.newland.pos.sdk.common.TransConst.StepResult;
import com.newland.pos.sdk.emv.EmvCoreOperator;
import com.newland.pos.sdk.emv.EmvModule;
import com.newland.pos.sdk.security.SecurityModule;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.PublicLibJNIService;
import com.newland.pos.sdk.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class payQuery  extends AbstractBaseTrans {
	
	private CardBean cardBean = null;
	/**
	 * 0x9F26
	 */
	private String arqcTLV = null;
	
	private EmvApplication emvApp;
	
	private EmvAppModule emvAppModule;
	private  Map<String, String> cardMSG;
	
	private boolean isEmv = false;
	
	private boolean isNoPsw = false;
	
	private EmvModule emvModule;

	private CommonBean<PubBean> commonBean;
	
	private ThirdInvokeBean thirdInvokeBean = null;
	
	private class Step {
		public final static int TRANS_START = 1;
		public final static int INPUT_AMOUNT = 2;
		public final static int SWIP_CARD = 3;
		public final static int EMV_INIT_RF = 4;
		public final static int EMV_INIT = 5;
		public final static int EMV_PROCESS = 6;
		public final static int INPUT_PIN = 7;
		public final static int PRE_ONLINE_PROCESS = 8;
		public final static int PACK_AND_COMM = 9;
		public final static int EMV_COMPLETE = 10;
		public final static int UNPACK_MSG = 11;
		public final static int SHOW_CARDINFO = 12;
		public final static int TRANS_RESULT = TransConst.STEP_FINAL;
	}
	
	public payQuery(){
	}
	
	public payQuery(CardBean bean){
		this.cardBean = bean;
	}

	public payQuery(CommonBean<PubBean> commonBean){
		this.commonBean = commonBean;
	}

	public payQuery(boolean isEmv){
		this.isEmv = isEmv;
	}

	public payQuery(ThirdInvokeBean thirdInvokeBean) {
		// TODO Auto-generated constructor stub
	}

	//检查状态开关： 是否签到、是否通讯初始化、是否IC卡参数和公钥下载、结算标志（批上送）
	protected void checkPower(){
		super.checkPower();
	}
	
	@Override
	protected int init() {
		int res = super.init();
        this.pubBean = commonBean.getValue();
        //设置交易类型,交易属性
		pubBean.setTransType(TransType.TRANS_SALE);
		pubBean.setTransName(getTitle(pubBean.getTransType()));
		if (cardBean != null) {
			stepProvider.syncCardBean(pubBean, cardBean);
		}
		return res;
	}
	
	
	@Override
	protected void release() {
		cardBean = null;
		if (emvAppModule!=null) {
			emvAppModule.emvSuspend(0);
			emvAppModule.emvRFSuspend(0);
			emvAppModule = null;
		}
		if(commonBean != null) {
			goOnStep(commonBean);
		} else {
			super.release();
		}
//		super.release();
	}
	
	@AnnStep(stepIndex = Step.TRANS_START)
	public int step_transStart(){
		SecurityModule sm = SecurityModule.getInstance();
		sm.setCurrentMainKeyIndex(ParamsUtils.getTMkIndex()+1);
		try{
			String workKey = pubBean.getMacKey();
			if(workKey==null){
				workKey = "1111111111111111";
			}
			String TMK = "1111111111111111";//任意设置一个主密钥，用于加密传入的工作秘钥。
			sm.loadMainKey(ParamsUtils.getTMkIndex()+1, TMK);
			String MAK_DES = sm.softdes(TMK,workKey);
			String makCheck = sm.softdes(workKey, "0000000000000000");
			LoggerUtils.d("hjh   makCheck"+makCheck);
			sm.loadWorkKey(Const.WorkKeyType.MAC, MAK_DES,makCheck);
		}catch(Exception e){
			e.printStackTrace();
			commonBean.setResult(false);
			commonBean.setStepResult(StepResult.FAIL);
			return Step.TRANS_RESULT;
		}
		return Step.SWIP_CARD;
	}
	
	@AnnStep(stepIndex = Step.INPUT_AMOUNT)
	public int step_InputAmount() {
		boolean result = stepProvider.inputAmount(pubBean);
		if(!result){
			return FINISH;
		}
		return Step.SWIP_CARD;
	}
	
	@AnnStep(stepIndex = Step.SWIP_CARD)
	public int step_SwipCard() {
		if (cardBean == null){
			boolean result = false;
			int inputMode = ReadcardType.RFCARD;
			result = stepProvider.swipCard(pubBean, inputMode);
			if(!result){
				return FINISH;
			}
		}
		return Step.EMV_INIT_RF;
	}

	@AnnStep(stepIndex = Step.EMV_INIT_RF)
	public int step_EmvInitRf(){
		emvApp = new EmvApplication(this);
		emvModule = emvApp.getEmvAppModule();
		
//		boolean result = stepProvider.emvSimpleProcess(pubBean, emvApp);
//		if(!result){
//			return FINISH;
//		}
		boolean result = readSocialCard(pubBean);
		LoggerUtils.e("333 step_SwipCard  002:"+result);
		if (!result) {
			showToast("加油卡读取信息失败");
			commonBean.setResult(false);
			commonBean.setStepResult(StepResult.FAIL);
			return Step.TRANS_RESULT;
		} else {
			showToast("加油卡读取信息成功");
		}

		return Step.SHOW_CARDINFO;
		
	}

	@AnnStep(stepIndex = Step.SHOW_CARDINFO)
	public int step_show_cardinfo() {
		CommonBean <PubBean> commonBean = new CommonBean<>();
		commonBean.setValue(pubBean);
		LoggerUtils.d("hjh   SHOW_CARDINFO ");
		if(!showConsumeInfo(commonBean).getResult()){
			return FINISH;
		}
		return Step.PRE_ONLINE_PROCESS;
	}
	
	/**脚本上送、冲正*/
	@AnnStep(stepIndex = Step.PRE_ONLINE_PROCESS)
	public int step_PreOnlineProcess() {
		boolean result = stepProvider.preOnlineProcess(pubBean);
		if (!result) {
			commonBean.setResult(false);
			commonBean.setStepResult(StepResult.FAIL);
			return FINISH;
		}
		return Step.PACK_AND_COMM;
	}
	
	
	@AnnStep(stepIndex = Step.PACK_AND_COMM)
	public int step_PackAndComm() {
		
		initPubBean();
//		012210001001012|11111111111|00000001|010009|170316000004|0001|00000000001|2017-03-16 20:02:10|REQ_OIL_BINDING|1234512345123451
		SimpleDateFormat  sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat  sDateFormat2 = new SimpleDateFormat("yyMMdd");
		String date = sDateFormat.format(new java.util.Date());   
		String transDate = sDateFormat2.format(new java.util.Date());   
		StringBuffer sb = new StringBuffer();
		sb.append(pubBean.getGasNo());//		油站编号
		sb.append("|");
		sb.append(pubBean.getRefuelerNo());//		加油员编号
		sb.append("|");
		sb.append(pubBean.getPosNo());//		POS编号
		sb.append("|");
		sb.append("010001");//		交易代码
		sb.append("|");
		sb.append(transDate+pubBean.getTraceNo());//		交易流水号
		sb.append("|");//		油品号
		sb.append(pubBean.getOilType());
		sb.append("|");
		sb.append(pubBean.getOilTypeNo());//		油品编号
		sb.append("|");
		sb.append(StringUtils.paddingString(pubBean.getLiter()+"",12,"0",0));//		加油升数
		sb.append("|");
		sb.append(StringUtils.paddingString(pubBean.getAmount()+"",12,"0",0));//		加油重量
		sb.append("|");
		sb.append(StringUtils.paddingString(pubBean.getAmount()+"",12,"0",0));//		原加油总金额
		sb.append("|");
		sb.append(StringUtils.paddingString(pubBean.getAmount()+"",12,"0",0));//		加油总金额
		sb.append("|");
		sb.append(getCardMSG());//      加油卡信息
		sb.append("|");
		sb.append(date); //		交易日期时间
//		sb.append("0000000000000000");//		MAC
		int resCode = 0;
		resCode = dealPackAndComm2(true, sb.toString(), true);
		if(resCode != STEP_CONTINUE){
			//连接出错
			if (STEP_FINISH == resCode){
				return Step.TRANS_RESULT;
			}
			return Step.TRANS_RESULT;
		}
		return Step.UNPACK_MSG;
	}
	
	
	private String getCardMSG() {
		StringBuffer sb = new StringBuffer();
		sb.append(cardMSG.get("5A"));//卡号
		sb.append(",");
		sb.append(Integer.parseInt(cardMSG.get("BF03")));//卡类别
		sb.append(",");
		sb.append(Integer.parseInt(cardMSG.get("BF04")));//等级
		return sb.toString();
	}
	private String getCardMSGALL() {
		StringBuffer sb = new StringBuffer();
		sb.append(cardMSG.get("5A"));//卡号
		sb.append(",");
		sb.append(Integer.parseInt(cardMSG.get("BF03")));//卡类别
		sb.append(",");
		sb.append(Integer.parseInt(cardMSG.get("BF04")));//等级
		sb.append(",");
		sb.append(cardMSG.get("BF01"));//有效期
		sb.append(",");
		sb.append(cardMSG.get("BF02"));//计量单位
		sb.append(",");
		sb.append(cardMSG.get("BF05"));//员工卡标志
		sb.append(",");
		sb.append(cardMSG.get("BF06"));//属性标志
		sb.append(",");
		sb.append(cardMSG.get("BF07"));//小额账户
		sb.append(",");
		sb.append(cardMSG.get("BF08"));//受理范围限制标志
		sb.append(",");
		sb.append(cardMSG.get("BF09"));//限用车辆
		sb.append(",");
		sb.append(cardMSG.get("BF10"));//限制油品
		sb.append(",");
		sb.append(cardMSG.get("BF20"));//备用1
		sb.append(",");
		sb.append(cardMSG.get("BF21"));//备用2
		return sb.toString();
	}

	
	/** 追加流水*/
	@AnnStep(stepIndex = Step.UNPACK_MSG)
	public int step_AppendWater(){
		// 对业务处理结果进行判断
		String[] fieldList = null;
		String resp = communicationBean.getData();
		String mac = null;
		commonBean.setResult(false);
		commonBean.setStepResult(StepResult.FAIL);
		try {
            String strgbk = new String(BytesUtils.hexStringToBytes(resp), 0, resp.length()/2, "UTF-8");
            LoggerUtils.d("hjh    strgbk: "+  strgbk  + "     BytesUtils.hexStringToBytes(resp)" +BytesUtils.hexStringToBytes(resp));
            resp = StringUtils.strToHex(strgbk);
            LoggerUtils.d("hjh    resp: "+  resp  );
			mac = replaceMac2(resp.substring(8,resp.length()-34));  // 8字节MAC + 1个分割符'|'
			resp = StringUtils.hexToStr(resp);
			LoggerUtils.d("hjh    mac: "+  mac  + "   " +resp.substring(resp.length()-16,resp.length()));
			if(!mac.equals(resp.substring(resp.length()-16,resp.length()))){
				commonBean.setContent("MAC校验失败");
				return Step.TRANS_RESULT;
			}
			fieldList = resp.split("\\|");
			for(int i = 0; i < fieldList.length; i++){
				LoggerUtils.d("field["+i+"]" + " = " + fieldList[i]);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LoggerUtils.d("hjh   fieldList[12]" +fieldList[12]+"        "+(fieldList[12]!=null && fieldList[12].equals("00")));
		if(fieldList[12]!=null && fieldList[12].equals("00")){
//			field[0] = 015510001001012
//			field[1] = 11111111111
//			field[2] = 00000001
//			field[3] = 010001
//			field[4] = 170320000054
//			field[5] = N
//			field[6] =
//			field[7] = 000000000000
//			field[8] = 000000000000
//			field[9] = 000000030000
//			field[10] = 000000
//			field[11] = 2017-03-20 13:01:30
//			field[12] = 00
//			field[13] = 浜ゆ槗鎴愬姛
//			field[14] = ABCDEFGHABCDEFGH
			pubBean.setBack_cardInfo(getCardMSGALL());
			pubBean.setBack_isDiscount(fieldList[5]);
			pubBean.setBack_discountType(fieldList[6]);
			pubBean.setBack_discountRate(fieldList[7]);
			pubBean.setBack_bonus(fieldList[10]);
//			pubBean.setAmount(Long.parseLong(fieldList[9]));
			pubBean.setDiscountAmount(Long.parseLong(fieldList[8]));
			pubBean.setPay(true);
		}else{
			try{
				commonBean.setContent(fieldList[13]);
			}catch (Exception e){
				return Step.TRANS_RESULT;
			}
			return Step.TRANS_RESULT;
		}
		commonBean.setValue(pubBean);
		LoggerUtils.d("hjh   SHOW_CARDINFO ");
		if(!showConsumeInfo(commonBean).getResult()){
			return FINISH;
		}
		commonBean.setResult(true);
		commonBean.setStepResult(StepResult.SUCCESS);
		return Step.TRANS_RESULT;
	}
	
	/**结果显示、打印处理*/
	@AnnStep(stepIndex = Step.TRANS_RESULT)
	public int step_TransResult(){
		if(!commonBean.getResult()){
			transResultBean.setIsSucess(false);
			if(StringUtils.isEmpty(commonBean.getContent())){
				transResultBean.setContent("优惠查询失败");
			}else{
				transResultBean.setContent(commonBean.getContent());
			}
			transResultBean = showUITransResult(transResultBean);
		}
		return FINISH;
	}
	
	
	public boolean readSocialCard(PubBean pubBean){
		
		EmvAppModule emvAppModule = EmvAppModule.getInstance();
		EmvCoreOperator emvOperator = new EmvCoreOperator(emvAppModule);
		byte[] inbuf = new byte[100];
		byte[] outbuf = new byte[600];
		String data;
		String pid = "";// 证件号码
		String sw = "";
		String temp = "";
		int ret = 0;
		
		byte[] res = new byte[256];
		int[] pnLen = new int [1];
		ret = PublicLibJNIService.jnicardpowerup(res, pnLen);
		LoggerUtils.d("加油卡上电 :ret :"+ret);
		LoggerUtils.d("outbuf.length0 :"+outbuf.length);
		if (ret != 0) {
			PublicLibJNIService.jnicardpowerdown();
			return false;
		}

//		System.arraycopy(new byte[]{0x00,(byte)0xA4,0x00,0x00,0x02,(byte)0x06,(byte)0x01,(byte)0xFF}, 0, inbuf, 0, 8);
		System.arraycopy(new byte[]{0x00,(byte)0xA4,0x04,0x00,0x08,(byte)0xA0,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x33,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x00}, 0, inbuf, 0, 14);
		ret = emvOperator.emv_icc_rw(0, inbuf, inbuf.length, outbuf, outbuf.length);
		if(ret <=0) {
			LoggerUtils.d("加油卡选择目录失败 :ret :"+ret);	
			PublicLibJNIService.jnicardpowerdown();
			showToast("加油卡选择目录失败");
			return false;
		}
		data = BytesUtils.bytesToHex(outbuf,2,ret-2);
		LoggerUtils.d("加油卡选择目 data "+data);
		sw = data.substring(data.length()-4);
		if (!sw.equals("9000")) {
			LoggerUtils.d("PublicLibJNIService.jnicardpowerdown() :ret1 :"+ret);
			PublicLibJNIService.jnicardpowerdown();
			return false;
//				continue;
		}
		data = data.substring(0, data.length()-4);
		temp += data;

		System.arraycopy(new byte[]{0x00,(byte)0xB2,0x01,0x34,0x00}, 0, inbuf, 0, 5);
		for(int RecNo=1;RecNo<=2;RecNo++) {
			inbuf[2] = (byte) RecNo;
			ret = emvOperator.emv_icc_rw(0, inbuf, 8, outbuf, outbuf.length);
			if(ret <= 2) {
				LoggerUtils.d("PublicLibJNIService.jnicardpowerdown() :ret0 :"+ret);
				PublicLibJNIService.jnicardpowerdown();
				showToast("加油卡读取数据失败");
				return false;
			}
			replace(outbuf, (byte)0xA0, (byte)0xBF);
			data = BytesUtils.bytesToHex(outbuf,2,ret-2);
			sw = data.substring(data.length()-4);
			if (!sw.equals("9000")) {
				LoggerUtils.d("PublicLibJNIService.jnicardpowerdown() :ret1 :"+ret);
				PublicLibJNIService.jnicardpowerdown();
				return false;
			}
			data = data.substring(0, data.length()-4);
			temp += data;
		}

		System.arraycopy(new byte[]{0x00,(byte)0xB2,0x01,0x14,0x00}, 0, inbuf, 0, 5);
		ret = emvOperator.emv_icc_rw(0, inbuf, 5, outbuf, outbuf.length);
		if(ret <= 2) {
			LoggerUtils.d("PublicLibJNIService.jnicardpowerdown() :ret0 :"+ret);
			PublicLibJNIService.jnicardpowerdown();
			showToast("加油卡读取数据失败");
			return false;
		}
		data = BytesUtils.bytesToHex(outbuf,2,ret-2);
		sw = data.substring(data.length()-4);
		if (!sw.equals("9000")) {
			LoggerUtils.d("PublicLibJNIService.jnicardpowerdown() :ret1 :"+ret);
			PublicLibJNIService.jnicardpowerdown();
			return false;
		}
		data = data.substring(0, data.length()-4);
		temp += data;
		
		
		LoggerUtils.d("PublicLibJNIService.jnicardpowerdown() :ret3 :"+ret);
		LoggerUtils.d("data  ret  :"+temp);
		PublicLibJNIService.jnicardpowerdown();
		cardMSG = getTlvMap(temp);
		pubBean.setCardInfo(cardMSG);
		return true;
	}
	private byte[] replace(byte[] data,byte a,byte b){
		for(int i = 0; i < data.length; i++){
			if(data[i] == a){
				data[i] = b;
			}
		}
		return data;
	}
}
