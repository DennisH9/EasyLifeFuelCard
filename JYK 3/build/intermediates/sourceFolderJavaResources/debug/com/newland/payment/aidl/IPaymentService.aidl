package com.newland.payment.aidl;
import com.newland.payment.aidl.SimpleWater;
interface IPaymentService {     
	
     /**
     * 设置参数
     * @param key 参数key
     * @param value 参数值
     */
    boolean setParam(String key, String value);
    
     /**
     * 获取参数
     * @param key 参数key
     * @return 返回打印张数
     */
    String getParam(String key);
    
    /**
     * 重打印
     * @param traceNo 流水号
     * @param transTime 交易时间(yyyyMMddHHmmss)
     */
    int rePrint(String traceNo, String transTime);
    
    /**
     * 查询流水详情
     * @param water 		返回流水详情
     * @param voucherNo 	流水号
     * @param transTime 交易时间(yyyyMMddHHmmss)
     * @return -1：voucherNo为空;0：查询失败，water为空;1：查询成功，water不为空
     */
    int findWater(inout SimpleWater water, String voucherNo, String transTime);
    
    /**
	* 重打印结算单
    */
    int rePrintSettle();
    
      /**
	* 通过外部订单号查询流水详情
	* @param water	返回流水详情
	* @param outOrderNo 	外部订单号
	* @return -1：outOrderNo为空;0：查询失败，water为空;1：查询成功，water不为空
	*/
	int findWaterByOutOrderNo(inout SimpleWater water, String outOrderNo);
    
}   