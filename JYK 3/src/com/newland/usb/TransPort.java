package com.newland.usb;

import java.io.IOException;

/**
 * 文件交互接口
 * @author zhengbin.zheng
 */
public interface TransPort {
	
	/**
	 * 打开端口
	 */
	public boolean Open();
	
	/**
	 * 	 * 写入字节流
	 * @param inbuf - 将要写入的buf
	 * @param start - 起始位置
	 * @param length- buf长度
	 * @param timeout-超时时间
	 * @return -1 连接未打开 >0 数据传输长度
	 * @throws IOException
	 */
	public int Write(byte[] inbuf, int start, int length, int timeout)throws IOException;
	
	
	/**
	 *  从端口中读出字节流
	 * @param rebuf   - 缓冲字节数组
	 * @param timeout - 超时时间
	 * @return
	 * @throws IOException
	 */
	public int  Read(byte[] rebuf, int nLen, int timeout)throws IOException;

	/**
	 * 端口中存在的数据长度
	 * @return
	 */
	public int Available();
	
	/**
	 * 接口数据清理
	 */
	public void Clear();
	
	/**
	 * 关闭数据连接
	 * @throws IOException
	 */
	public void Close()throws IOException;
}
