package com.newland.payment.trans.bean;

import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 通讯发送接收对象
 * @author chenkh
 * @date 2015-5-11
 * @time 下午4:11:37
 *
 */
@SuppressWarnings("serial")
public class CommunicationBean extends BaseBean {

	/** 在完整报文数据前，加https数据在头部，并计算长度 */
	private String https;
	
	/** TPDU */
	private String tpdu;
	
	/** 报文头 */
	private String head;
	
	/** 8583报文 (String),发送前该域为组包器组出来的8583包，后台应答之后替换为应答8583报文*/
	private String data;

	
	/** 
	 * 界面显示的文字信息 
	 * 数组长度为3，若设置数组不等于3个即使用默认值
	 */
	private String[] content;
	
	/** 
	 * 发送的数据
	 * (包含：2byte len1+https头+2byte len2+tpdu+报文头+8583报文体) 
	 */
	private byte[] request;
	
	/** 
	 * 接收的数据 
	 * (包含：2byte len1+https头+2byte len2+tpdu+报文头+8583报文体) 
	 */
	@SuppressWarnings("unused")
	private byte[] response;
	/**
	 * 是否使用ip3 MPOS下载
	 */
	private boolean mposDwnm;
	
	private CommunicationFailReason reason;

	public boolean getMposDwnm() {
		return mposDwnm;
	}

	public void setMposDwnm(boolean mposDwnm1) {
		this.mposDwnm = mposDwnm1;
	}
	
	public String getHttps() {
		return https;
	}

	public void setHttps(String https) {
		this.https = https;
	}

	public String getTpdu() {
		return tpdu;
	}

	public void setTpdu(String tpdu) {
		this.tpdu = tpdu;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	/**
	 * 输出的8583报文
	 * @return
	 */
	public String getData() {
		return data;
	}

	/**
	 * 输入的8583报文
	 * @param date
	 */
	public void setData(String data) {
		this.data = data;
	}

	public String[] getContent() {
		return content;
	}

	public void setContent(String[] content) {
		this.content = content;
	}

	public byte[] getRequest() {
		request = null;
		byte[] b_data = null;
		byte[] b_head = null;
		byte[] b_tpdu = null;
		byte[] b_https = null;
		if(!StringUtils.isEmpty(data)) {
			//例如0x30 0x30 - >00
			b_data = BytesUtils.hexToBytes(data);
		} 
		if (!StringUtils.isEmpty(head)) {
			b_head = BytesUtils.hexToBytes(head);
		}
		if (!StringUtils.isEmpty(tpdu)) {
			b_tpdu = BytesUtils.hexToBytes(tpdu);
		}
		if (!StringUtils.isEmpty(https)) {
			LoggerUtils.d("https = "+https);
			b_https = https.getBytes();
		}
		int dataLen = (b_data == null?0:b_data.length); 
		int headLen = (b_head == null?0:b_head.length);  
		int tpduLen = (b_tpdu == null?0:b_tpdu.length);
		/** 报文长度占2字节 */
		int len = 2;
		/** 报文长度 */
		int res2Len = dataLen+headLen+tpduLen;
		/** res2格式：2字节 报文长度 + tpduLen字节 TPDU + headLen字节 Head + dateLen字节 8583体*/
		byte[] res2 = new byte[res2Len+len+2];
//		System.arraycopy(BytesUtils.intToBytes(2,2), 0, res2, 0, 2);//加上报文长度的长度 
//		LoggerUtils.d("req   "+ BytesUtils.bytesToHex(res2));
		System.arraycopy(BytesUtils.intToBytes(res2Len,2), 0, res2,0, len);
		LoggerUtils.d("req   "+ BytesUtils.bytesToHex(res2));
	
		if (b_tpdu!=null)
			System.arraycopy(b_tpdu, 0, res2, len, tpduLen);
		if (b_head!=null)
			System.arraycopy(b_head, 0, res2, len+tpduLen, headLen);
		if (b_data!=null)
			System.arraycopy(b_data, 0, res2, len+tpduLen+headLen, dataLen);
		
		if (b_https!=null) {
			int httpsLen = b_https.length;
			int res1Len = httpsLen + res2.length;
			/** res2格式：2字节 全包长度 + httpsLen字节 数据 + res2.length字节 res2数据*/
			byte[] res1 = new byte[res1Len+len];
			System.arraycopy(BytesUtils.intToBytes(res1Len, 2), 0, res1, 0, len);
			System.arraycopy(b_https, 0, res1, len, httpsLen);
			System.arraycopy(res2, 0, res1, len+httpsLen, res2.length);
			
			request = new byte[res1.length];
			System.arraycopy(res1, 0, request, 0, res1.length);
		} else {
			request = new byte[res2.length];
			System.arraycopy(res2, 0, request, 0, res2.length);
		}
		LoggerUtils.d("request = " +BytesUtils.bytesToHex(request));
		return request;
	}

	public void setResponse(byte[] response) {
		byte[] res = null;
		if (response==null) {
			setData(null);
			LoggerUtils.e("返回数据为空");
			return;
		}
		if(!StringUtils.isEmpty(https)) {
			/** 有https头的情况 */
			String s_response = BytesUtils.bytesToHex(response);
			LoggerUtils.d("解析完整报文："+s_response);
			String flag = BytesUtils.bytesToHex("\r\n".getBytes());
			String[] data = s_response.split(flag);
			int num = data.length;
			if (num == 9) {
				int len = data[num-1].length();
				res = new byte[len];
				System.arraycopy(data[num-1], 0, res, 0, len);
			} else {
				setData(null);
				LoggerUtils.e("返回数据中“回车符换行符”个数不足8个，解析报文失败");
				return;
			}
		} else {
			res = new byte[response.length];
			System.arraycopy(response, 0, res, 0, res.length);
		}
		/** 开始解析 长度+tpdu+报文头+8583体*/
		byte[] b_len = new byte[2];
		System.arraycopy(res, 0, b_len, 0, b_len.length);
		int len = BytesUtils.bytesToIntWhereByteLengthEquals2(b_len);
		if (len == res.length - 2 && len!=0) {
			byte[] b_head = null;
			byte[] b_tpdu = null;
			if (!StringUtils.isEmpty(head)) {
				b_head = BytesUtils.hexToBytes(head);
			}
			if (!StringUtils.isEmpty(tpdu)) {
				b_tpdu = BytesUtils.hexToBytes(tpdu);
			}
			int tpduLen = (b_tpdu == null?0:b_tpdu.length);
			int headLen = (b_head == null?0:b_head.length);  
			System.arraycopy(res, b_len.length, b_tpdu, 0, tpduLen);
			System.arraycopy(res, b_len.length+tpduLen, b_head, 0, headLen);
			/** 返回报文头 */
			LoggerUtils.d("返回的报文头："+BytesUtils.bytesToHex(b_head));
			setHead(BytesUtils.bytesToHex(b_head));
			
			int dataLen = len - tpduLen - headLen;
			byte[] data = new byte[dataLen];
			System.arraycopy(res, b_len.length+tpduLen+headLen, data, 0, data.length);
			LoggerUtils.d("返回的8583体："+BytesUtils.bytesToHex(data));
			setData(BytesUtils.bytesToHex(data));
//			setResData(data);
		} else {
			setData(null);
			LoggerUtils.e("返回数据中报文头长度校验出错");
			return;
		}
		
	}
	
	public CommunicationFailReason getReason() {
		return reason;
	}

	public void setReason(CommunicationFailReason reason) {
		this.reason = reason;
	}

	/**
	 * 通讯失败原因
	 * @author chenkh
	 * @date 2015-5-26
	 * @time 上午12:21:24
	 *
	 */
	public enum CommunicationFailReason {
		/**
		 * 连接失败
		 */
		CONNECT_FAIL,
		
		/**
		 * 发送失败
		 */
		SEND_FAIL,
		
		/**
		 * 接收失败
		 */
		RECEIVE_FAIL
	}
}
