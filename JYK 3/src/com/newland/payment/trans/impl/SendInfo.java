package com.newland.payment.trans.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransType;
import com.newland.payment.trans.AbstractBaseTrans;
import com.newland.payment.trans.ann.AnnStep;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.CommunicationBean;
import com.newland.payment.trans.bean.InputInfoBean;
import com.newland.payment.trans.bean.PubBean;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.bean.MessageTipBean;
import com.newland.pos.sdk.device.Device;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.TimeUtils;
import android.annotation.SuppressLint;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Toast;

/**
 * 交易上送
 */
public class SendInfo extends AbstractBaseTrans {

	private CommonBean<PubBean> commonBean;
	private CommunicationBean communicationBean;
	private int maxTimes = 3;
	private int failTimes = 0;	// 失败次数
	private int connectTimes = 0; // 连接失败次数
	String requstJSON = null;
	
	public SendInfo(CommonBean<PubBean> commonBean){
		this.commonBean = commonBean;
	}
	
	@Override
	protected void checkPower() {
		super.checkPower();
		checkCardExsit = false;
		checkSettlementStatus = false;
		checkSingIn = false;
		checkWaterCount = false;
		transcationManagerFlag = false; // 不开启事务
	}

	@Override
	protected int init() {
		int res = super.init();
		if (commonBean != null){
			pubBean = commonBean.getValue();
			commonBean.setResult(false);
		}
		return res;
	}
	
	@Override
	protected void release() {
		if (commonBean != null){
			goOnStep(commonBean);
		} else {
			super.release();
		}
	}
	
	@AnnStep(stepIndex = 1)
	public int step_Reversal() throws JSONException {
		
		InputInfoBean infoBean = new InputInfoBean();
		infoBean.setTitle(pubBean.getTransName());
		infoBean.setContent(getText(R.string.common_pls_input) + "手机号码");
		infoBean.setShortContent("手机号码");
		infoBean.setMode(InputInfoBean.INPUT_MODE_NUMBER);
		infoBean.setMaxLen(11);
		infoBean.setMinLen(11);
		infoBean.setEmptyFlag(true);
		infoBean = showUIInputInfo(infoBean);
		
		switch(infoBean.getStepResult()){
		case SUCCESS:
			//设置原参考号
			pubBean.setMobileNo(infoBean.getResult());
			break;
			
		case BACK:
		case TIME_OUT:
		case FAIL:
		default:
			break;
		}
		
		LoggerUtils.d("begin SendInfo");
		connectTimes = 0;
		while(failTimes <= maxTimes){

			JSONObject jsonObject=new JSONObject();  
	        jsonObject.put("bankcard", pubBean.getPan().trim());  
	        jsonObject.put("gjzj", String.format("%d", pubBean.getAmount()));  
	        jsonObject.put("id", "000001");  
	        jsonObject.put("jysj", TimeUtils.getCurrentYear()+pubBean.getDate()+pubBean.getTime());  
	        jsonObject.put("poscode", Device.getSN().substring(0,12));  
	        jsonObject.put("shopid", ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_MERCHANTID));  
	        jsonObject.put("trantype", pubBean.getTransType() == TransType.TRANS_SALE ? "03":"04");  
	        jsonObject.put("usertel", pubBean.getMobileNo());  
	        
	        requstJSON = jsonObject.toString();  
	        LoggerUtils.d("jSON字符串" + requstJSON);  
	        
			int resCode = packAndCommHttp();
			LoggerUtils.e("packAndComm resCode = "+resCode);
			
			switch (resCode){
			case STEP_FINISH: //连接失败,不计入冲正次数
				connectTimes++;
				if (connectTimes > maxTimes){
					
					MessageTipBean messageTipBean = new MessageTipBean();
					messageTipBean.setCancelable(false);
					messageTipBean.setContent("\t\t\t\t\t交易上送失败\r\n请凭 pos签购单人工处理");
					messageTipBean.setTitle("提示");
					showUIMessageTip(messageTipBean);
					
					commonBean.setResult(false);
					return FINISH;
				}else{
					continue;
				}
			case STEP_CONTINUE: //成功
				LoggerUtils.d("冲正->packAndComm,成功！");
				break;
			default: //失败
				
				continue;
			}
			
			break;
		}
		
		commonBean.setResult(true);
		return FINISH;
	}
	
	@SuppressWarnings("incomplete-switch")
	private int packAndCommHttp(){
		if(httpUrlConnection()){
			return STEP_CONTINUE;
		} else{
			return STEP_FINISH;
		}
		
	}
	
	
	@SuppressLint("ShowToast")
	private boolean httpUrlConnection(){
	    try{
	     String pathUrl = "http://pos.huiji51.com/api/values";
	     //建立连接
	     URL url=new URL(pathUrl);
	     HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
	     
	     ////设置连接属性
	     httpConn.setDoOutput(true);//使用 URL 连接进行输出
	     httpConn.setDoInput(true);//使用 URL 连接进行输入
	     httpConn.setUseCaches(false);//忽略缓存
	     httpConn.setRequestMethod("POST");//设置URL请求方法
	     
	     String requestString = requstJSON;
	     
	     //设置请求属性
	    //获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
	          byte[] requestStringBytes = requestString.getBytes("utf-8");
	          httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
	          httpConn.setRequestProperty("Content-Type", "application/json");
	          httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
	          httpConn.setRequestProperty("Charset", "UTF-8");
	          
	          //建立输出流，并写入数据
	          OutputStream outputStream = httpConn.getOutputStream();
	          outputStream.write(requestStringBytes);
	          outputStream.close();
	         //获得响应状态
	          int responseCode = httpConn.getResponseCode();
	          if(HttpURLConnection.HTTP_OK == responseCode){//连接成功
	        	  //当正确响应时处理数据
	        	  StringBuffer sb = new StringBuffer();
	              String readLine;
	              BufferedReader responseReader;
	             //处理响应流，必须与服务器响应流输出的编码一致
	              responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
	              while ((readLine = responseReader.readLine()) != null) {
	               sb.append(readLine).append("\n");
	              }
	              responseReader.close();
	              LoggerUtils.d("lxb getRes:" +  sb.toString());
	              
	              JSONObject jsonObject=new JSONObject(sb.toString()); 
	              String msg = jsonObject.getString("msg");
	              LoggerUtils.d("lxb msg:" +  jsonObject.getString("msg"));
	              if(msg == null || !msg.equals("成功")){
	            	 ToastUtils.showOnUIThread("上送失败");
	            	 return false;
	              } 
	          } else {
	        	  ToastUtils.showOnUIThread("上送失败");
	        	  return false;
	          }
	          
	    }catch(Exception ex){
	     ex.printStackTrace();
	    }
	    ToastUtils.showOnUIThread("上送成功");
	    return true;
	}
	
}
