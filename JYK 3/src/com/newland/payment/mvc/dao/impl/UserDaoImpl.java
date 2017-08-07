package com.newland.payment.mvc.dao.impl;

import java.util.List;

import android.content.Context;

import com.newland.base.dao.BaseDao;
import com.newland.payment.mvc.dao.UserDao;
import com.newland.payment.mvc.model.User;

public class UserDaoImpl extends BaseDao<User> implements UserDao{

	public UserDaoImpl(Context context) {
		super(context);
	}	

	@Override
	public int deleteByUserNo(String userNo) {
		return super.delete("user_no", userNo);
	}

	@Override
	public User findByUserNoAndUserType(String userNo, int userType) {
		List<User> l = super.query("user_type=? and user_no = ?", new String[] {String.valueOf(userType), userNo}, null);
		return l.size() > 0 ? l.get(0) : null;
	}

	@Override
	public List<User> findByUserType(int userType) {
		return super.query("user_type=?", new String[] {String.valueOf(userType)}, null);
	}
	
	@Override
	public int deleteAll() {
		return super.delete("", new String[]{});
	}
	
	public void initUserData(){
		//初始化默认操作员
		execSql("INSERT INTO T_USER(user_no,password,user_type) VALUES('99','20100322','4')");
		execSql("INSERT INTO T_USER(user_no,password,user_type) VALUES('00','123456','2')");
		execSql("INSERT INTO T_USER(user_no,password,user_type) VALUES('99','00000000','3')");
		execSql("INSERT INTO T_USER(user_no,password,user_type) VALUES('01','0000','1')");
		execSql("INSERT INTO T_USER(user_no,password,user_type) VALUES('02','0000','1')");
		execSql("INSERT INTO T_USER(user_no,password,user_type) VALUES('03','0000','1')");
		execSql("INSERT INTO T_USER(user_no,password,user_type) VALUES('04','0000','1')");
		execSql("INSERT INTO T_USER(user_no,password,user_type) VALUES('05','0000','1')");
	}
}
