package com.zj.platform.gamecenter.constant;

/**
 * 牌的类型
 */
public enum CardTypeEnum {

	BAO_ZI(20, "豹子"),

	SHUN_JIN(16, "顺金"),

	JIN_HUA(12, "金花"),

	SHUN_ZI(8, "顺子"),

	DOUBLE(4, "对子"),

	SINGLE(1, "单张");

	private int code;

	private String description;

	private CardTypeEnum(int code, String description) {
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
		for (CardTypeEnum cardTypeEnum : CardTypeEnum.values()) {
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
