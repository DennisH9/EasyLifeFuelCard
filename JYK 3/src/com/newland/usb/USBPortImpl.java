package com.newland.usb;

import java.io.IOException;

import com.newland.pos.sdk.util.LoggerUtils;

import android.content.Context;
import android.newland.AnalogSerialManager;

public class USBPortImpl implements TransPort{
	
	private AnalogSerialManager analogSerial;
	
	public USBPortImpl(Context content)
	{
		analogSerial = (AnalogSerialManager)content.getSystemService("AnalogSerial_service");
		//analogSerial = new AnalogSerialManager(ia);
	}

	@Override
	public boolean Open() {
		// TODO Auto-generated method stub
		int ret = analogSerial.open();
		LoggerUtils.d("串口打开结果：" + ret);
		if(ret <= -1){
			return false;
		}
		analogSerial.setconfig(115200, 0, "8N1NN".getBytes());
		return true;
	}

	@Override
	public int Write(byte[] inbuf, int start, int length, int timeout)
			throws IOException {
		analogSerial.write(inbuf, length, timeout);
		return 0;
	}

	@Override
	public int Read(byte[] rebuf, int nLen,int timeout) throws IOException {
		// TODO Auto-generated method stub
		return analogSerial.read(rebuf, nLen, timeout);
	}

	@Override
	public int Available() {
		// TODO Auto-generated method stub
		
		return 0;
	}

	@Override
	public void Clear() {
		byte[] flushbuf = new byte[1024];
		while(analogSerial.read(flushbuf, flushbuf.length, 0)==flushbuf.length){}
	}

	@Override
	public void Close() throws IOException {
		// TODO Auto-generated method stub
		analogSerial.close();
		
		System.out.println("Close");
	}
}
