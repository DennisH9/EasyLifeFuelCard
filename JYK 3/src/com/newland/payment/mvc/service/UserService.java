package com.newland.payment.mvc.service;

import java.util.List;

import com.newland.payment.mvc.DuplicatedUserException;
import com.newland.payment.mvc.model.User;


public interface UserService {
	/**
	 * 获取密码长度，用于动态设置输密框可输入最大长度
	 * @param userNo 操作员号（除厂商操作员外）
	 * @return 正确值[4, 8]，0表示该操作员不存在
	 */
	public int getPasswdLenByUserNo(String userNo);
	
	/**
	 * 添加一般操作员
	 * @return
	 */
	public long addUser(User user) throws DuplicatedUserException;
	
	
	/**
	 * 修改操作员密码，除厂商操作员外
	 * @param userNo 操作员账号 01 - 98，其它无效的账号，
	 * 参考 系统管理密码{@link updateManagerPassword} 以及主管{@link updateMasterPassword}
	 * @param oldPassword 密码长度限制为4位
	 * @param newPassword 密码长度限制为4位
	 * @return 返回更新结果
	 * <li>0-表示更新失败</li>
	 * <li>1-表示更新成功</li>
	 */
	public int updateUserPassword(String userNo, String oldPassword, String newPassword);
	
	/**
	 * 修改系统管理员密码，默认账号为99
	 * @param oldPassword 旧密码，长度必需要为8位
	 * @param newPassword 新密码，长度必需为8位
	 * @return 返回更新结果
	 * <li>0-表示更新失败</li>
	 * <li>1-表示更新成功</li>
	 */
	public int updateManagerPassword(String oldPassword, String newPassword);
	
	
	/**
	 * 修改主管密码，默认账号为00
	 * @param oldPassword 旧密码，长度必需要为6位
	 * @param newPassword 新密码，长度必需为6位
	 * @return 返回更新结果
	 * <li>0-表示更新失败</li>
	 * <li>1-表示更新成功</li>
	 */
	public int updateMasterPassword(String oldPassword, String newPassword);
	
	/**
	 * 删除指定一般操作员
	 * @param userNo 操作员账号，只能是 01 - 98的，其它无效
	 * @return
	 */
	public int deleteUser(String userNo);
	
	/**
	 * 登录校验
	 * @param userNo 操作员号
	 * @param password 密码
	 * @return -1-用户不存在， 0-密码错误，1-一般操作员登陆，2-主管登陆，3-系统管理员登陆，4-产商管理员登陆
	 */
	public int checkLogin(String userNo, String password);
	
	/**
	 * 查找所有一般操作员
	 * @return 返回所有一般操作员
	 */
	public List<User> findAllUsers();
	
}
