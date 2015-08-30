package com.zj.platform.gamecenter.constant;

public enum StatusEnum {

	VALID(1, "有效"),

	INVALID(0, "无效");

	private int code;

	private String description;

	private StatusEnum(int code, String description) {
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

	public String getDescription(int code) {
		for (PlayerStatusEnum cardTypeEnum : PlayerStatusEnum.values()) {
			if (code == cardTypeEnum.getCode()) {
				return cardTypeEnum.getDescription();
			}
		}
		return null;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
