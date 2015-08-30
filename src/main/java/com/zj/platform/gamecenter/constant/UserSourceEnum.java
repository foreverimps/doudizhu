package com.zj.platform.gamecenter.constant;

public enum UserSourceEnum {

	S_360(360, "360"),

	S_ROBOT(0, "机器人"),

	S_MYSELF(1, "自有用户");

	private int code;

	private String description;

	private UserSourceEnum(int code, String description) {
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

	public static String getDescription(int code) {
		for (UserSourceEnum userTypeEnum : UserSourceEnum.values()) {
			if (code == userTypeEnum.getCode()) {
				return userTypeEnum.getDescription();
			}
		}
		return null;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
