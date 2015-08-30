package com.zj.platform.gamecenter.constant;

public enum UserMoneyOptTypeEnum {

	REG(1, "注册");

	private int code;

	private String description;

	private UserMoneyOptTypeEnum(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static String getDescription(int code) {
		for (ResultEnum result : ResultEnum.values()) {
			if (code == result.getCode()) {
				return result.getDescription();
			}
		}
		return null;
	}
}
