package com.newland.payment.mvc.dao;

import java.util.List;

import com.newland.payment.mvc.model.User;

/**
 * 流水处理DAO
 * @author linchunhui
 * @date 2015年5月12日 下午5:42:54
 *
 */
public interface UserDao {

	public User findByUserNoAndUserType(String userNo, int userType);
	
	public List<User> findByUserType(int userType);
	
	/** 增加 */
	public long insert(User user);
	
	/** 更新密码 */
	public int update(User user);
	
	/** 通过操作员号查找操作员删除 */
	public int deleteByUserNo(String userNo);
	
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
