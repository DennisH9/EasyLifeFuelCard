package com.newland.payment.mvc.service;

import com.newland.payment.mvc.DuplicatedReverseWaterException;
import com.newland.payment.mvc.model.ReverseWater;

/**
 * 冲正流水处理
 * @author linchunhui
 * @date 2015年5月12日 下午5:43:28
 *
 */
public interface ReverseWaterService {
	/**
	 * 取冲正流水
	 * @return
	 */
	public ReverseWater getReverseWater();
	
	/**
	 * 添加新的冲正流水
	 * <p>如果冲正流水已经存在，则不允许添加，需要删除旧的冲正流水后再添加</p>
	 * @param reverseWater 冲正流水
	 * @return
	 */
	public long add(ReverseWater reverseWater) throws DuplicatedReverseWaterException;
	
	
	/**
	 * 删除冲正流水
	 * @return <li>0-数据库中没有冲正流水</li>
	 * <li> 大于0表示被删除的记录数</li>
	 */
	public int delete();
	
	/**
	 * 修改冲正39域的值
	 * @return
	 */
	public int changeField39Result(String field39Value);
	
	/**
	 * 修改55域内容
	 * @param field55
	 * @return
	 */
	public int changeField55(String field55);
	/**
	 * 修改39域，55域，61域内容,传null时不更新
	 * @param field39
	 * @param field55
	 * @param field61
	 * @return
	 */
	public int changeReverseWater(String field39,  String field55, String field61);
}
