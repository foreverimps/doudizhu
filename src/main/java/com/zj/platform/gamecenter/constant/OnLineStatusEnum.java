package com.zj.platform.gamecenter.constant;

public enum OnLineStatusEnum {

	OFF_LINE(0, "离线"),

	ONLINE(1, "在线"),

	ONLINE_HIDE(2, "隐身"),

	;

	private int code;

	private String description;

	private OnLineStatusEnum(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
