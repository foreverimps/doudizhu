package com.zj.platform.gamecenter.constant;

/**
 * 牌的花色
 */
public enum CardColorEnum {

	BLACK(1, "黑桃"),

	RED(2, "红桃"),

	PLUM(3, "梅花"),

	BOX(4, "方块");

	private int code;

	private String description;

	private CardColorEnum(int code, String description) {
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
		for (CardColorEnum cardTypeEnum : CardColorEnum.values()) {
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
