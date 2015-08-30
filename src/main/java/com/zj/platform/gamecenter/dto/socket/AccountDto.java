package com.zj.platform.gamecenter.dto.socket;

public class AccountDto {

	private String account;// 账号

	private String password;// 密码

	private String checkCode;// 验证码

	private String mac;// 机器码

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getMac() {
	    return mac;
    }

	public void setMac(String mac) {
	    this.mac = mac;
    }

}
