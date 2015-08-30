package com.zj.platform.gamecenter.constant;

public enum GameTypeEnum {

	THREE_CARDS(1, "诈金花"),

	DOU_DI_ZHU(2, "斗地主"),

	;

	private GameTypeEnum(int code, String description) {
		this.code = code;
		this.description = description;
	}

	private int code;

	private String description;

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
