package com.newland.payment.mvc.service.impl;

import java.util.List;

import android.content.Context;

import com.newland.payment.mvc.dao.ScriptResultDao;
import com.newland.payment.mvc.dao.impl.ScriptResultDaoImpl;
import com.newland.payment.mvc.model.ScriptResult;
import com.newland.payment.mvc.service.ScriptResultService;

/**
 * 脚本执行结果
 * @author linchunhui
 * @date 2015年5月12日 下午7:32:43
 *
 */
public class ScriptResultServiceImpl implements ScriptResultService {

	private ScriptResultDao scriptResultDao;
	
	
	public ScriptResultServiceImpl(Context context) {
		this.scriptResultDao = new ScriptResultDaoImpl(context);
	}
	
	@Override
	public ScriptResult getScriptResult() {
		List<ScriptResult> l = this.scriptResultDao.findAll();
		return l.size() > 0 ? l.get(0) : null;
	}

	@Override
	public long addScriptResult(ScriptResult scriptResult) {
//		判断是否有记录，如果有记录，则不允许添加
		this.delete();
		
		return this.scriptResultDao.insert(scriptResult);
		
	}

	@Override
	public int delete() {
		ScriptResult sr = this.getScriptResult();
		if (sr == null) {
			return 0;
		}
		return this.scriptResultDao.delete(sr.getId());
	}
	
	
	@Override
	public int updateScriptResult(ScriptResult scriptResult){
		return this.scriptResultDao.update(scriptResult);
	}

}
