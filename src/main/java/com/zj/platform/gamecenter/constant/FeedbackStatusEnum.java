package com.zj.platform.gamecenter.constant;

public enum FeedbackStatusEnum {

	WAIT_PROCESS(1, "待处理"),

	HAS_PROCESS(2, "已处理");

	private int code;

	private String description;

	private FeedbackStatusEnum(int code, String description) {
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
