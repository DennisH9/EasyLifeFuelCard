package com.newland.payment.mvc.service;

import com.newland.payment.mvc.model.ScriptResult;

/**
 * 脚本执行结果数据处理
 * @author linchunhui
 * @date 2015年5月12日 下午7:28:20
 *
 * 脚本执行结果数据只会存储一条，不会有多条
 */
public interface ScriptResultService {
	/**
	 * 取得脚本执行结果的数据
	 * <p>数据库中只会存储一条记录</p>
	 * @return
	 */
	public ScriptResult getScriptResult();
	
	
	/**
	 * 添加新的记录
	 * @param scriptResult
	 * @return 返回成功的记录数
	 * <p>成功会返回1，失败返回0</p>
	 * 
	 */
	public long addScriptResult(ScriptResult scriptResult) ;
	
	/**
	 * 删除记录
	 * @return 返回删除的条数
	 * 
	 * <p>因为只会有一条记录，所有删除就是删除这一条记录</p>
	 * <li>返回值=0时，表示没有记录被删除，或数据库中没有记录</li>
	 * <li>返回值=1时，表示删除成功</li>
	 */
	public int delete();
	
	/**
	 * 更新一个脚本
	 * @param water
	 * @return
	 */
	public int updateScriptResult(ScriptResult scriptResult);
}
