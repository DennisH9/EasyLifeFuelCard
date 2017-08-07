package com.newland.payment.mvc.model;

import com.newland.base.dao.ann.Column;
import com.newland.base.dao.ann.Table;


/**
 * 操作员（包括四类操作员）
 * @author CB
 * @time 2015-4-23 上午9:42:16
 */
@Table(name = "T_USER")
public class User {

	@Column(name = "ID", primaryKey = true)
	private Long id;
	/** 操作员号 */
	@Column(name = "USER_NO")
	private String userNo;
	
	/** 密码 */
	@Column(name = "PASSWORD")
	private String password;
	
	/** 操作员类别：88-厂商管理员，00-主管，99-系统管理员，01-一般操作员 */
	@Column(name = "USER_TYPE")
	private Integer userType;
	
	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}
}
