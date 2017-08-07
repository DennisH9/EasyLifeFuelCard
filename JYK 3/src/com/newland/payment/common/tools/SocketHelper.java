package com.newland.payment.common.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.openssl.PEMReader;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.common.Const.FileConst;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.ui.activity.App;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

import android.content.res.AssetManager;

/**
 * 连接服务端的通信
 * cxy
 * 时间：2014.08
 */
public class SocketHelper {
	
	private static SocketHelper socketHelper;
	private static final long connectTime = 60000;
	private long costTime = 0;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private boolean commMode;
	
	private String ip;
	private int port;
	private int timeOut;
	
	private SocketAddress address;
	
	private SocketListener listener;
	
	private SocketHelper(String ip, int port, int timeout, SocketListener listener){
		this.ip = ip;
		this.port = port;
		this.timeOut = timeout;
		this.listener = listener;
	}
	
	/**
	 * <p>如果是长连接的，需要在调用以下，进行预连接</p>
	 * <p>如果没有调用此方法，也不会影响连接交易</p>
	 * 
	 */
	public void initSocket() {
		if (commMode) {
			connect();
		}
	}
	
	public static SocketHelper getSocketHelper(String ip, int port, int timeout, SocketListener listener){
		if (socketHelper == null
				|| socketHelper.ip == null
				|| !socketHelper.ip.equals(ip) 
				|| socketHelper.port!=port 
				|| socketHelper.timeOut != timeout
				|| System.currentTimeMillis() >= (socketHelper.costTime + connectTime)) {
			if (socketHelper != null && System.currentTimeMillis() >= (socketHelper.costTime + connectTime)) {
				LoggerUtils.i("Long connection timeout!" + ((System.currentTimeMillis() - (socketHelper.costTime + connectTime)) / 1000) + "s");
				socketHelper.release(false);
			}
			socketHelper = new SocketHelper(ip, port, timeout, listener);
		}
		if(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE) == 0){
			socketHelper.commMode = ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_CDMA_MODE, false);
		}else if(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE) == 1){
			socketHelper.commMode = ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_GPRS_MODE, false);
		}else{
			socketHelper.commMode = ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_WIFI_MODE, false);
		}
		LoggerUtils.i("连接方式:" + (socketHelper.commMode ? "长连接" : "短连接"));
		return socketHelper;
	}
	
	private Socket createSocket(){
		//创建Socket实例
		Socket socket = null;
		SSLContext sslContext = null;
		boolean isSSL = ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_USE_HTTP);
		LoggerUtils.d("111 is SSL:" + isSSL);
		if (isSSL) {
			try {

				// 建立空BKS,android只能用BKS(BouncyCastle密库)，一般java应用参数传JKS(java自带密库)
				KeyStore ksKeys = KeyStore.getInstance("BKS");
				ksKeys.load(null, null);

				// 读入客户端证书
				AssetManager assetManager = App.getInstance().getAssets();
				InputStream inputStream = assetManager.open(FileConst.PEM_CERT);
				PEMReader cacertfile = new PEMReader(new InputStreamReader(inputStream));
				X509Certificate cacert = (X509Certificate) cacertfile.readObject();
				cacertfile.close();
				
				// 导入根证书作为trustedEntry
				KeyStore.TrustedCertificateEntry trustedEntry = new KeyStore.TrustedCertificateEntry(
						cacert);
				ksKeys.setEntry("ca_root", trustedEntry, null);
				TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");// 密钥管理器,一般java应用传SunX509
				tmf.init(ksKeys);

				// 构建SSLContext，此处传入参数为TLS，也可以为SSL
				sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, tmf.getTrustManagers(), null);// 第一个参数是授权的密钥管理器，用来授权验证。
																	// TrustManager[]第二个是被授权的证书管理器，用来验证服务器端的证书。

				SSLSocketFactory factory = sslContext.getSocketFactory();
				socket = (SSLSocket) factory.createSocket();
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else{
			socket = new Socket();
		}
		return socket;
	
	}

	/**
	 * 连接服务器
	 * @return 返回连接结果
	 * <li>true - 连接成功</li>
	 * <li>false - 连接失败</li>
	 */
	private boolean connect() {
		if (!isConnected()) {
			return connectSocketHost();
		}
		return true;
	}
	
	/**
	 * 判断socket是否已经连接
	 * @return
	 */
	private boolean isConnected() {
		if (socket == null 
				|| !socket.isConnected()
				|| input == null
				|| output == null) {
			return false;
		}
//		try {
//			socket.sendUrgentData(0xFF);
//		} catch (IOException ie) {
//			LoggerUtils.e("Server had bean shut down the socket!");
//			return false;
//		}
		return true;
	}
	
	/**
	 * 连接服务器，真实创建连接实体
	 * @return
	 */
	private boolean connectSocketHost() {	
		LoggerUtils.i("Ready connect server:" + ip + ":" + port);
		try {
			//获取SocketAddress对象
			socket = createSocket();
			address = new InetSocketAddress(ip, port);
			//设置连接超时并连接服务端
			socket.connect(address, timeOut * 1000);
			socket.setKeepAlive(true);
			if (socket.isConnected()) {
				//连接成功，获取socket的输入输出流
				input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				LoggerUtils.i("Connected server succ!");
				initLongConnectTimer();
				return true;
			} else {
				throw new Exception("Connect Server error! Check server IP and Port! - 2");
			}
		} catch (Exception e) {
			LoggerUtils.e("Connect Server error! Check server IP and Port! - 1");
			e.printStackTrace();
			socket = null;
			input = null;
			output = null;
		}
		return false;
	}
	
	private void initLongConnectTimer() {
		if (commMode) {
			costTime = System.currentTimeMillis();
		}
	}
	
	/**
	 * 接收数据
	 * @return 返回已经收到的数据
	 * @throws Exception
	 */
	public byte[] receive() {
		LoggerUtils.e("Ready receive data!");
		initLongConnectTimer();
		try {
			byte[] headBuf = new byte[2];
			int retLen = input.read(headBuf);
			if (retLen != 2) {
				LoggerUtils.e("error: The length of received data is:" + retLen);
				throw new SocketException("error: The length of received data is:" + retLen);
			}else {
				LoggerUtils.e("true: The length of received data is:" + retLen);
			}
			int msgLen = BytesUtils.getShort(headBuf);
			LoggerUtils.d("lxb getinputLen:" + msgLen);
			byte[] msg = new byte[msgLen+2]; // 存放接收报文
			System.arraycopy(headBuf, 0, msg, 0, 2);
			
			if(input == null){
				LoggerUtils.d("lxb getinput == null");
			}
			while(true){
			    /**相隔一段时间后 缓冲区内的可读数据没变的话说明客户端已经写入完成 退出循环 一次性将剩余数据取出*/
				if(input == null){
					LoggerUtils.d("lxb getinputavailable == null");
				}
				
			    if(input.available() == msgLen) break;
			    Thread.sleep(50);//try代码省略 sleep时间可以按需要调整
			}

			
			byte[] buffer = new byte[input.available()];
			retLen = input.read(buffer);

			if (retLen != msgLen) {
				throw new Exception("Received data length error! [" + retLen + "," + msgLen + "]");
			}
			System.arraycopy(buffer, 0, msg, 2, buffer.length);
			LoggerUtils.i("Received data length：" + msg.length);
//			LoggerUtils.i("接收的报文内容："+BytesUtils.bcdToString(msg));
			return msg;			
		} catch (Exception e) {
			LoggerUtils.e("Receive data error!");
			e.printStackTrace();
			release();
		}
		closeComm();
		return null;
	}

	/**
	 * 接收数据
	 * @return 返回已经收到的数据
	 * @throws Exception
	 */
	public byte[] receive2() {
		LoggerUtils.e("Ready receive data!");
		initLongConnectTimer();
		try {
			byte[] headBuf = new byte[4];
			int retLen = input.read(headBuf);
			if (retLen != 4) {
				LoggerUtils.e("error: The length of received data is:" + retLen);
				throw new SocketException("error: The length of received data is:" + retLen);
			}else {
				LoggerUtils.e("true: The length of received data is:" + retLen);
			}
			int msgLen = Integer.parseInt(StringUtils.hexToStr(BytesUtils.bytesToHex(headBuf)));
			LoggerUtils.d("hjh   receive data  :"+BytesUtils.bytesToHex(headBuf));
			LoggerUtils.d("lxb getinputLen:" + msgLen);
			byte[] msg = new byte[msgLen+4]; // 存放接收报文
			System.arraycopy(headBuf, 0, msg, 0, 4);

			if(input == null){
				LoggerUtils.d("lxb getinput == null");
			}
			while(true){
				/**相隔一段时间后 缓冲区内的可读数据没变的话说明客户端已经写入完成 退出循环 一次性将剩余数据取出*/
				if(input == null){
					LoggerUtils.d("lxb getinputavailable == null");
				}

				if(input.available() == msgLen) break;
				Thread.sleep(50);//try代码省略 sleep时间可以按需要调整
			}


			byte[] buffer = new byte[input.available()];
			retLen = input.read(buffer);

			if (retLen != msgLen) {
				throw new Exception("Received data length error! [" + retLen + "," + msgLen + "]");
			}
			System.arraycopy(buffer, 0, msg, 4, buffer.length);
			LoggerUtils.i("Received data length：" + msg.length);
//			LoggerUtils.i("接收的报文内容："+BytesUtils.bcdToString(msg));
			return msg;
		} catch (Exception e) {
			LoggerUtils.e("Receive data error!");
			e.printStackTrace();
			release();
		}
		closeComm();
		return null;
	}
	/**
	 * 接收数据
	 * @return 返回已经收到的数据
	 * @throws Exception
	 */
	public byte[] receiveSLL() {
		LoggerUtils.e("Ready receiveSLL data!");
		initLongConnectTimer();
		try {
			
			//TODO HTTP头 传统没做处理只做偏移 
			String httpHead1 = input.readLine();
			String httpHead2 = input.readLine();
			String httpHead3 = input.readLine();
			String httpHead4 = input.readLine();
			String httpHead5 = input.readLine();
			String httpHead6 = input.readLine();
			String httpHead7 = input.readLine();
			String httpHead8 = input.readLine();   //	\r\d
			
			LoggerUtils.d("1111:" + httpHead1);
			LoggerUtils.d("2222:" + httpHead2);
			LoggerUtils.d("3333:" + httpHead3);
			LoggerUtils.d("4444:" + httpHead4);
			LoggerUtils.d("5555:" + httpHead5);
			LoggerUtils.d("6666:" + httpHead6);
			LoggerUtils.d("7777:" + httpHead7);
			
			
			byte[] headBuf = new byte[2];
			int retLen = input.read(headBuf);
			if (retLen != 2) {
				LoggerUtils.e("error: The length of received data is:" + retLen);
				throw new SocketException("error: The length of received data is:" + retLen);
			}else {
				LoggerUtils.e("true: The length of received data is:" + retLen);
			}
			int msgLen = BytesUtils.getShort(headBuf);
			LoggerUtils.d("lxb getinputLen:" + msgLen);
			byte[] msg = new byte[msgLen+2]; // 存放接收报文
			System.arraycopy(headBuf, 0, msg, 0, 2);
			
			if(input == null){
				LoggerUtils.d("lxb getinput == null");
			}
			while(true){
			    /**相隔一段时间后 缓冲区内的可读数据没变的话说明客户端已经写入完成 退出循环 一次性将剩余数据取出*/
				if(input == null){
					LoggerUtils.d("lxb getinputavailable == null");
				}
				
			    if(input.available() == msgLen) break;
			    Thread.sleep(50);//try代码省略 sleep时间可以按需要调整
			}

			
			byte[] buffer = new byte[input.available()];
			retLen = input.read(buffer);

			if (retLen != msgLen) {
				throw new Exception("Received data length error! [" + retLen + "," + msgLen + "]");
			}
			System.arraycopy(buffer, 0, msg, 2, buffer.length);
			LoggerUtils.i("Received data length：" + msg.length);
//			LoggerUtils.i("接收的报文内容："+BytesUtils.bcdToString(msg));
			return msg;			
			
			
			
			
//			byte[] headBufTemp = new byte[2000];
//			int retLenTemp = input.read(headBufTemp);
//			
//			//截取长度186
//			LoggerUtils.d("lxb getheadBufTemp111:" + BytesUtils.bcdToString(headBufTemp));
//			LoggerUtils.d("lxb getheadBufTemp222:" + new String(headBufTemp, "GB2312"));
////			if (retLenTemp != 2) {
////				throw new SocketException("error: The length of received data is:" + retLenTemp);
////			}else {
////				LoggerUtils.e("true: The length of received data is:" + retLenTemp);
////			}
//			
//			//截取报文
//			byte[] headBuf = new byte[2];
//	        System.arraycopy(headBufTemp, 187, headBuf, 0, 2);
//	        LoggerUtils.d("lxb getISO8583Len:" + BytesUtils.bcdToString(headBuf));
//	        
//			int msgLen = BytesUtils.getShort(headBuf);
//			LoggerUtils.d("lxb getinputLen:" + msgLen);
//			byte[] msg = new byte[msgLen+2]; // 存放接收报文
//			//报文长度
//			System.arraycopy(headBuf, 0, msg, 0, 2);
//			//报文体
//			System.arraycopy(headBufTemp, 187+2, msg, 2, msgLen);
//			
//			LoggerUtils.i("Received data length：" + msg.length);
//			LoggerUtils.d("lxb getheadBufTemp444:" + BytesUtils.bcdToString(msg));
////			LoggerUtils.i("接收的报文内容："+BytesUtils.bcdToString(msg));
//			return msg;		
			
			
		} catch (Exception e) {
			LoggerUtils.e("Receive data error!");
			e.printStackTrace();
			release();
		}
		closeComm();
		return null;
	}
	
	/**
	 * 发送数据
	 * @param data 数据
	 * @throws Exception
	 */
	public boolean send(byte[] data) {
		LoggerUtils.e("Ready send data!");
		if (!connect()) {
			LoggerUtils.e("Connect server fail!");
			return false;
		}
		LoggerUtils.i("发送的报文内容111："+BytesUtils.bcdToString(data));	
		initLongConnectTimer();
		try {
			listener.overSendData(true);
			ByteArrayOutputStream ostream = new ByteArrayOutputStream();
			ostream.write(data);
			byte[] sendData = ostream.toByteArray();
			ostream.close();
			output.write(sendData);
			output.flush();
			LoggerUtils.i("Sended data length："+sendData.length);
			LoggerUtils.i("发送的报文内容222："+BytesUtils.bcdToString(sendData));			
			return true;
		} catch (IOException ioe) {
			LoggerUtils.e("发送数据失败");
			ioe.printStackTrace();
			release();
		}
		return false;
	}
	/**
	 * 发送数据
	 * @param data 数据
	 * @throws Exception
	 */
	public boolean sendSLL(byte[] data) {
		LoggerUtils.e("Ready sendSLL data!");
		if (!connect()) {
			LoggerUtils.e("Connect server fail!");
			return false;
		}
		
		String ip;
		int port;
		if(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE) == 0){
			ip = ParamsUtils.getString(ParamsConst.PARAMS_KEY_CDMA_SERVERIP1);
			port = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_CDMA_PORT1, 8080);			
		}else if(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE) == 1){
			ip = ParamsUtils.getString(ParamsConst.PARAMS_KEY_GPRS_SERVERIP1);
			port = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_GPRS_PORT1, 8080);			
		}else{
			ip = ParamsUtils.getString(ParamsConst.PARAMS_KEY_WIFI_SERVERIP1);
			port = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_WIFI_PORT1, 8080);					
		}
		
		byte[] dataLen =new byte[2];
		dataLen[0] = data[0];
		dataLen[1] = data[1];
		int msgLen = BytesUtils.getShort(dataLen) + 2;
		
		LoggerUtils.i("发送的报文内容111："+BytesUtils.bcdToString(data));
		byte[] httpHead1 = "POST /unp/webtrans/WPOS HTTP/1.1\r\n".getBytes();
		byte[] httpHead2 = ("HOST:" + ip + ":" + port + "\r\n").getBytes();
		byte[] httpHead3 = ("User-Agent:" + "Donjin Http 0.1" + "\r\n").getBytes();
		byte[] httpHead4 = ("Cache-Control:" + "no-cache" + "\r\n").getBytes();
		byte[] httpHead5 = ("Content-Type:" + "x-ISO-TPDU/x-auth" + "\r\n").getBytes();
		byte[] httpHead6 = ("Accept:" + "*/*" + "\r\n").getBytes();
		byte[] httpHead7 = ("Content-Length:" + msgLen + "\r\n\r\n").getBytes();
		
//		LoggerUtils.i("发送的httpHead1111："+BytesUtils.bcdToString(httpHead1));
//		LoggerUtils.i("发送的httpHead222："+BytesUtils.bcdToString(httpHead2));
//		LoggerUtils.i("发送的httpHead333："+BytesUtils.bcdToString(httpHead3));
//		LoggerUtils.i("发送的httpHead444："+BytesUtils.bcdToString(httpHead4));
//		LoggerUtils.i("发送的httpHead555："+BytesUtils.bcdToString(httpHead5));
//		LoggerUtils.i("发送的httpHead666："+BytesUtils.bcdToString(httpHead6));
//		LoggerUtils.i("发送的httpHead777："+BytesUtils.bcdToString(httpHead7));
		
		byte[] httpData = mergeByteArray( httpHead1,httpHead2,httpHead3,httpHead4,
				httpHead5, httpHead6, httpHead7, data);
		LoggerUtils.i("发送的报文内容head111："+BytesUtils.bcdToString(httpData));
		
		initLongConnectTimer();
		try {
			listener.overSendData(true);
			ByteArrayOutputStream ostream = new ByteArrayOutputStream();
			ostream.write(httpData);
			byte[] sendData = ostream.toByteArray();
			ostream.close();
			output.write(sendData);
			output.flush();
			LoggerUtils.i("Sended data length："+sendData.length);
			LoggerUtils.i("发送的报文内容222："+BytesUtils.bcdToString(sendData));			
			return true;
		} catch (IOException ioe) {
			LoggerUtils.e("发送数据失败");
			ioe.printStackTrace();
			release();
		}
		return false;
	}
	
	/**
	 * 强制释放所有资源
	 * @throws Exception
	 */
	public boolean release() {
		return release(true);
	}
	
	private boolean release(boolean isReConnect) {
		LoggerUtils.e("release socket");
		try {
			if (input != null) {
				input.close();
				input = null;
			}
			if (output != null) {
				output.close();
				output = null;
			}
			if (socket != null && socket.isConnected()) socket.close();
			return true;
		} catch (Exception e) {
//			e.printStackTrace();
		} finally {
			if (isReConnect && commMode) {
				connectSocketHost();
			}
		}
		return false;
	}

	/**
	 * 关闭连接（只有短连接时才会生效）
	 */
	public void closeComm(){
		if (!commMode) {
			release();
		}
	}
	
	public boolean isCommMode() {
		return commMode;
	}

	public void setCommMode(boolean commMode) {
		this.commMode = commMode;
	}
	
	public byte[] mergeByteArray(byte[]... args){
		int arrayLen = 0;
		int copiedLen = 0;
		for(byte[] array : args){
			arrayLen += array.length;
		}
		byte[] exchangeData = new byte[arrayLen];
		for(byte[] arrayCopy:args){
			System.arraycopy(arrayCopy,0,exchangeData,copiedLen,arrayCopy.length);
			copiedLen += arrayCopy.length;
		}

		return exchangeData;
	}

	
}
