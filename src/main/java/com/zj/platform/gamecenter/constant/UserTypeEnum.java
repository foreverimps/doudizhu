package com.zj.platform.gamecenter.constant;

public enum UserTypeEnum {

	NORMAL(0, "正常"),

	VIP_1(1, "VIP_1"),

	VIP_2(2, "VIP_2"),

	VIP_3(3, "VIP_3"),

	VIP_4(4, "VIP_4"),

	VIP_5(5, "VIP_5"),

	VIP_6(6, "VIP_6"),

	VIP_7(7, "VIP_7"),

	VIP_8(8, "VIP_8"),

	VIP_9(9, "VIP_9");

	private int code;

	private String description;

	private UserTypeEnum(int code, String description) {
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
		for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
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
