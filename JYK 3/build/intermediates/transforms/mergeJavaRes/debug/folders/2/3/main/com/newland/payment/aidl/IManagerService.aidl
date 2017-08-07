package com.newland.payment.aidl;

interface IManagerService {
    /**
     * 初始化
     * true 成功，false 失败
     */
	boolean reset();

    /**
     * 激活
     * true 成功，false 失败
     */
    boolean activate();

}
