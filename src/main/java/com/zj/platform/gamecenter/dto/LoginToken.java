package com.zj.platform.gamecenter.dto;

import java.io.Serializable;

import com.zj.platform.gamecenter.entity.User;

/**
 * 用户的登录信息
 */
public class LoginToken implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
