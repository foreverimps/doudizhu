package com.zj.platform.gamecenter.constant;

/**
 * socket的类型
 */
public enum SocketTypeEnum {

	WEB("web"),

	CLIENT("client");

	private String code;

	private SocketTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
