package com.zj.platform.gamecenter.constant;

public enum ProductTypeEnum {

	RECHARGE(0, "充值商品"),

	EXCHANGE(1, "兑换商品"),

	GIFT(2, "礼包"),

	PROP(3, "道具");

	private int code;

	private String description;

	private ProductTypeEnum(int code, String description) {
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
		for (ProductTypeEnum userTypeEnum : ProductTypeEnum.values()) {
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
