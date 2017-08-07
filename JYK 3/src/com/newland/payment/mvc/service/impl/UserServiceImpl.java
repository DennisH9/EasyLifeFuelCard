package com.newland.payment.mvc.service.impl;

import java.util.List;

import android.content.Context;

import com.newland.payment.common.Const;
import com.newland.payment.mvc.DuplicatedUserException;
import com.newland.payment.mvc.dao.UserDao;
import com.newland.payment.mvc.dao.impl.UserDaoImpl;
import com.newland.payment.mvc.model.User;
import com.newland.payment.mvc.service.UserService;
import com.newland.pos.sdk.util.StringUtils;

public class UserServiceImpl implements UserService{

	private UserDao userDao;
	
	public UserServiceImpl(Context context){
		userDao = new UserDaoImpl(context);
	}
	
	@Override
	public int getPasswdLenByUserNo(String userNo) {
		if (userNo == null || !StringUtils.isDigital(userNo)) {
			return 4;
		}
		switch(Integer.parseInt(userNo)) {
		case 99:
			return 8;
		case 00:
			return 6;
		default:
			return 4;
		}
	}


	@Override
	public long addUser(User user) throws DuplicatedUserException {
		if (user == null 
				|| StringUtils.isEmpty(user.getUserNo()) 
				|| StringUtils.isEmpty(user.getPassword())
				|| !isUserNo(user.getUserNo())){
			return 0;
		}
		
		user.setUserType(Const.UserType.USER);
		
		if (userDao.findByUserNoAndUserType(user.getUserNo(), Const.UserType.USER) != null){
			throw new DuplicatedUserException("该操作员已存在，不能重复添加");
		}
		
		return userDao.insert(user);
	}

	@Override
	public int deleteUser(String userNo) {
		if (StringUtils.isEmpty(userNo) || !isUserNo(userNo)) {
			return 0;
		}
		
		User user = userDao.findByUserNoAndUserType(userNo, Const.UserType.USER);
		
		if (user!=null && Const.UserType.USER == user.getUserType()) {
			return userDao.deleteByUserNo(user.getUserNo());
		}
		return 0;
	}

	@Override
	public int checkLogin(String userNo, String password) {
		if (StringUtils.isEmpty(userNo) || StringUtils.isEmpty(password)) {
			return 0;
		}
		User user = null;
		if (userNo.equals("99")) {
			user = this.userDao.findByUserNoAndUserType(userNo, Const.UserType.SYSTEM_NAMAGER);
			if (user == null || !user.getPassword().equals(password)) {
				user = this.userDao.findByUserNoAndUserType(userNo, Const.UserType.PRODUCER);
			} 
			
		} else if (userNo.equals("00")) {
			user = this.userDao.findByUserNoAndUserType(userNo, Const.UserType.DIRECTOR);
		} else {
			user = this.userDao.findByUserNoAndUserType(userNo, Const.UserType.USER);
		}

		if (user == null) {
			return -1;
		} else if (!user.getPassword().equals(password)) {
			return 0;
		}
		
		return user.getUserType();
	}

	@Override
	public int updateManagerPassword(String oldPassword, String newPassword) {
		if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
			return 0;
		}
		
		if (oldPassword.length() != 8) {
			return 0;
		}
		
		if (newPassword.length() != 8) {
			return 0;
		}
		
		User user = userDao.findByUserNoAndUserType("99", Const.UserType.SYSTEM_NAMAGER);
		
		if (user == null) {
			return 0;
		}
		if (user.getPassword().equals(oldPassword)) {
			user.setPassword(newPassword);
			return this.userDao.update(user);
			
		}
		return 0;
	}

	@Override
	public int updateMasterPassword(String oldPassword, String newPassword) {
		if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
			return 0;
		}
		
		if (oldPassword.length() != 6) {
			return 0;
		}
		
		if (newPassword.length() != 6) {
			return 0;
		}
		 
		User user = userDao.findByUserNoAndUserType("00", Const.UserType.DIRECTOR);
		
		if (user == null) {
			return 0;
		}
		if (user.getPassword().equals(oldPassword)) {
			user.setPassword(newPassword);
			return this.userDao.update(user);
			
		}
		return 0;
	}

	@Override
	public int updateUserPassword(String userNo, String oldPassword,
			String newPassword) {
		if (StringUtils.isEmpty(userNo) || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
			return 0;
		}
		if (!isUserNo(userNo)) {
			return 0;
		}
		
		if (oldPassword.length() != 4) {
			return 0;
		}
		
		if (newPassword.length() != 4) {
			return 0;
		}
		User user = userDao.findByUserNoAndUserType(userNo, Const.UserType.USER);
		
		if (user == null) {
			return 0;
		}
		if (user.getPassword().equals(oldPassword)) {
			user.setPassword(newPassword);
			return this.userDao.update(user);
			
		}
		return 0;
	}
	
	private boolean isUserNo(String userNo) {
		int i = Integer.parseInt(userNo);
		return i > 0 || i < 99;
	}

	@Override
	public List<User> findAllUsers() {
		return userDao.findByUserType(Const.UserType.USER);
	}

}
