package com.zj.platform.gamecenter.constant;

public enum SocketTaskStatusEnum {

	WAIT_PROCESS(0, "待处理"),

	LOCK(1, "锁定中"),

	IN_PROCESS(2, "处理中"),

	FINISH(3, "完成");

	private int code;

	private String description;

	private SocketTaskStatusEnum(int code, String description) {
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

	public void setDescription(String description) {
		this.description = description;
	}

	public static String getDescription(String code) {
		for (SocketTaskStatusEnum statusEnum : SocketTaskStatusEnum.values()) {
			if (code.equals(statusEnum.getCode())) {
				return statusEnum.getDescription();
			}
		}
		return null;
	}
}
