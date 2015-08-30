package com.zj.platform.gamecenter.constant;

/**
 * 筹码的类型
 */
public enum NoteTypeEnum {

	TYPE_1_1(100),

	TYPE_1_3(300),

	TYPE_1_5(500),

	TYPE_1_8(800),

	TYPE_1_10(1000),

	TYPE_2_1(1000),

	TYPE_2_3(3000),

	TYPE_2_5(5000),

	TYPE_2_8(8000),

	TYPE_2_10(10000),

	TYPE_3_1(10000),

	TYPE_3_3(30000),

	TYPE_3_5(50000),

	TYPE_3_8(80000),

	TYPE_3_10(100000);

	private int code;

	private NoteTypeEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
