package com.zj.platform.gamecenter.constant;

/**
 * 房间的类型
 */
public enum RoomTypeEnum {

	NORMAL(1, "标准"),

	FU_HAO(2, "富豪"),

	TU_HAO(3, "土豪");

	private int code;

	private String description;

	private RoomTypeEnum(int code, String description) {
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
		for (RoomTypeEnum cardTypeEnum : RoomTypeEnum.values()) {
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
