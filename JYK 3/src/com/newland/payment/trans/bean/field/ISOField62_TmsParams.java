package com.newland.payment.trans.bean.field;

import java.io.UnsupportedEncodingException;
import com.newland.payment.trans.manage.ParamsHelper;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 62域用法八,TMS参数传递
 *
 * @author LCZ
 * @date 2015-6-18 
 * @time 上午01:38:20
 */
public class ISOField62_TmsParams{

	/** 26.平台标识码 */
	private String platformId;
	/** 27.下载内容标志*/
	private String downContextSign;
	/** 28.下载任务标识 */
	private String task;
	/** 29.限制日期 */
	private String limitDate;
	/** 30.应用版本号 */
	private String appVersion;
	/** 31.POS下载时机标志*/
	private String downOccasion;
	/** 32.三个交易电话号码之1*/
	private String telephone1;
	/** 33.三个交易电话号码之2 */
	private String telephone2;
	/** 34.TMS1的IP1和端口号1*/
	private String ipAndPort1;
	/** 35.TMS1的IP1和端口号2*/
	private String ipAndPort2;
	/** 36.TMS的GPRS参数*/
	private String gprsParam;
	
	/** 37.TMS的CDMA接入方式 */
	private String cdmaParam;
	/** 38.下载任务校验码*/
	private String checkCode;
	/** 39.自动重拨间隔时间 */
	private String redialTime;
	/** 40.任务提示信息 */
	private String taskInfo;
	/** 41.TPDU*/
	private String tpdu;
	/** 42.下载开始日期时间 */
	private String downStartDate;
	/** 43.下载结束日期时间*/
	private String downEndDate;
	
	public ISOField62_TmsParams(String data) throws UnsupportedEncodingException{
		LoggerUtils.d("ISOField62_Params：" + data);
		
		ParamsHelper params = new ParamsHelper(data);
		platformId = params.getData(8); 
		downContextSign = params.getData(2); 
		task = params.getData(4); 
		limitDate = params.getData(8);
		if("00".equals(downContextSign)) {
			appVersion = params.getData(6); 
		} else if("01".equals(downContextSign)) {
			int len = Integer.parseInt(params.getData(1));
			appVersion = params.getData(len); 
		}	
		downOccasion = params.getData(4); 
		telephone1 = params.getData(20); 
		telephone2 = params.getData(20); 
		ipAndPort1 = params.getData(30); 
		ipAndPort2 = params.getData(30); 
		gprsParam = params.getData(60); 
		cdmaParam = params.getData(60); 
		checkCode = params.getData(32); 
		redialTime = params.getData(4); 
		taskInfo = params.getData(30); 
		tpdu = params.getData(10); 
		downStartDate = params.getData(12); 
		downEndDate = params.getData(12); 
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getDownContextSign() {
		return downContextSign;
	}

	public void setDownContextSign(String downContextSign) {
		this.downContextSign = downContextSign;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getLimitDate() {
		return limitDate;
	}

	public void setLimitDate(String limitDate) {
		this.limitDate = limitDate;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getDownOccasion() {
		return downOccasion;
	}

	public void setDownOccasion(String downOccasion) {
		this.downOccasion = downOccasion;
	}

	public String getTelephone1() {
		return telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	public String getIpAndPort1() {
		return ipAndPort1;
	}

	public void setIpAndPort1(String ipAndPort1) {
		this.ipAndPort1 = ipAndPort1;
	}

	public String getIpAndPort2() {
		return ipAndPort2;
	}

	public void setIpAndPort2(String ipAndPort2) {
		this.ipAndPort2 = ipAndPort2;
	}

	public String getGprsParam() {
		return gprsParam;
	}

	public void setGprsParam(String gprsParam) {
		this.gprsParam = gprsParam;
	}

	public String getCdmaParam() {
		return cdmaParam;
	}

	public void setCdmaParam(String cdmaParam) {
		this.cdmaParam = cdmaParam;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getRedialTime() {
		return redialTime;
	}

	public void setRedialTime(String redialTime) {
		this.redialTime = redialTime;
	}

	public String getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(String taskInfo) {
		this.taskInfo = taskInfo;
	}

	public String getTpdu() {
		return tpdu;
	}

	public void setTpdu(String tpdu) {
		this.tpdu = tpdu;
	}

	public String getDownStartDate() {
		return downStartDate;
	}

	public void setDownStartDate(String downStartDate) {
		this.downStartDate = downStartDate;
	}

	public String getDownEndDate() {
		return downEndDate;
	}

	public void setDownEndDate(String downEndDate) {
		this.downEndDate = downEndDate;
	}
}
