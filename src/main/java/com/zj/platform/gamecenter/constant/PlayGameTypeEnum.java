package com.zj.platform.gamecenter.constant;

/**
 * 游戏操作类型
 */
public enum PlayGameTypeEnum {

	IN(5, "进入"),

	SIT(6, "占座"),

	STAND(7, "离座"),

	READY(10, "准备"),

	COUNT_DOWN_BEFORE_BUREAU(11, "开局前倒计时"),

	SEND_CARDS(15, "发牌"),

	LOOK(20, "看牌"),

	CALL(25, "跟注"),

	CUR_CHANGE(26, "当前玩家变换"),

	ROUND_START(27, "新一轮开始"),

	ADD_CALL(30, "加注"),

	CALL_OVER(35, "跟到底"),

	COMPARE(37, "比较"),

	GIVE_UP(40, "弃牌"),

	OUT(45, "退出"),

	CHANGE_PLACE(50, "换座位"),

	GAME_OVER(55, "游戏结束"),

	GAME_WIN(60, "赢一盘"),

	GAME_LOST(70, "输一盘"),

	VIEW(75, "旁观");

	private int code;

	private String description;

	private PlayGameTypeEnum(int code, String description) {
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
		for (PlayGameTypeEnum socketConstant : PlayGameTypeEnum.values()) {
			if (code == socketConstant.getCode()) {
				return socketConstant.getDescription();
			}
		}
		return null;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
