package com.zj.platform.gamecenter.constant;

/**
 * 玩家状态
 */
public enum PlayerStatusEnum {

	WAIT(0, "等待"),

	READY(1, "准备"),

	GAMEING(2, "游戏中"),

	OUT(3, "出局");

	private int code;

	private String description;

	private PlayerStatusEnum(int code, String description) {
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
