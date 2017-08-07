package com.newland.payment.mvc.dao;

import java.util.List;

import com.newland.payment.mvc.model.ScriptResult;


/**
 * 脚本执行结果
 * @author linchunhui
 * @date 2015年5月12日 下午7:33:25
 *
 */
public interface ScriptResultDao {
	
	public long insert(ScriptResult scriptResult);
	
	public int update(ScriptResult scriptResult);
	
	public List<ScriptResult> findAll();
	
	public int delete(long id);
	
	/**
	 * 删除所有记录
	 * @return
	 */
	public int deleteAll();
	
	/**
	 * 还原表自增长序列号
	 */
	public void revertSeq();
}
